package simulation;

import lombok.extern.slf4j.Slf4j;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.exception.TooMuchEntitiesException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static simulation.util.ValidationUtil.*;

@Slf4j
public class Field {
    // <--- Public --->
    public static class EntityGroup {
        // <--- Private --->
        private record Pair(int real, int hash) {
            public Pair increase() {
                return new Pair(real + 1, hash + 1);
            }

            public Pair decrease() {
                return new Pair(Math.max(0, real - 1), hash);
            }
        }

        private final Set<Entity> entityGroupSet;
        private final Map<Class<? extends Entity>, Pair> entitiesCntMap;

        public EntityGroup() {
            entityGroupSet = ConcurrentHashMap.newKeySet();
            entitiesCntMap = new ConcurrentHashMap<>();
        }

        public EntityGroup(EntityGroup group) {
            entityGroupSet = Set.copyOf(group.entityGroupSet);
            entitiesCntMap = Map.copyOf(group.entitiesCntMap);
        }

        public synchronized void addEntity(Entity entity) {
            entityGroupSet.add(entity);
            entitiesCntMap.compute(entity.getClass(), (k, v) -> {
                if (v == null) {
                    return new Pair(1, 1);
                } else {
                    return v.increase();
                }
            });
        }

        public synchronized void removeEntity(Entity entity) {
            entityGroupSet.remove(entity);
            entitiesCntMap.computeIfPresent(entity.getClass(), (k, v) -> v.decrease());
        }

        public final List<Entity> getEntityList() {
            return List.copyOf(entityGroupSet);
        }

        public int entityCnt(Class<? extends Entity> clazz) {
            Pair pair = entitiesCntMap.get(clazz);

            return pair == null ? 0 : pair.real;
        }

        public synchronized void iterate(Consumer<Entity> func) {
            for (var entity : entityGroupSet) {
                func.accept(entity);
            }
        }

        public int size() {
            return entityGroupSet.size();
        }

        @Override
        public String toString() {
            return "EntityGroup{" +
                    entityGroupSet.size() +
                    " entities" +
                    '}';
        }
    }

    public final int height = 100;
    public final int width = 20;

    // <--- Private --->

    private final EntityGroup[][] field;
    private final List<Entity> entityList;
    private final AtomicInteger animalCnt = new AtomicInteger(0);
    private final AtomicInteger plantCnt = new AtomicInteger(0);

    public Field() {
        field = new EntityGroup[height][width];
        entityList = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new EntityGroup();
            }
        }
    }

    public synchronized void addEntity(int x, int y, Entity entity) {
        addEntityInternal(x, y, entity);
        increaseCounters(entity);
    }

    public synchronized void removeEntity(Entity entity) {
        removeEntityInternal(entity);
        decreaseCounters(entity);
    }

    private void addEntityInternal(int x, int y, Entity entity) {
        checkEntity(entity);
        checkPoint(this, x, y);
        checkEntitiesQuantity(this, entity, x, y);


        entityList.add(entity);
        field[y][x].addEntity(entity);
    }

    private void removeEntityInternal(Entity entity) {
        checkEntity(entity);
        Point point = entity.getPoint();

        field[point.y()][point.x()].removeEntity(entity);
        entityList.remove(entity);
    }

    private void increaseCounters(Entity entity) {
        if (entity.isPlant()) {
            plantCnt.incrementAndGet();
        } else if (entity.isAnimal()) {
            animalCnt.incrementAndGet();
        }
    }

    private void decreaseCounters(Entity entity) {
        if (entity.isPlant()) {
            plantCnt.decrementAndGet();
        } else if (entity.isAnimal()) {
            animalCnt.decrementAndGet();
        }
    }

    public synchronized void moveEntity(Point from, Point to, Entity entity) {
        checkEntity(entity);
        removeEntityInternal(entity);
        try {
            addEntityInternal(to.x(), to.y(), entity);
        } catch (TooMuchEntitiesException e) {
            addEntityInternal(from.x(), from.y(), entity);
            log.debug("{} wasn't added because it is already too much of it", entity);
        }
    }

    public EntityGroup getEntityGroup(int x, int y) {
        checkPoint(this, x, y);
        return new EntityGroup(field[y][x]);
    }

    public EntityGroup getEntityGroup(Point point) {
        checkPoint(this, point.x(), point.y());
        return field[point.y()][point.x()];
    }

    public List<Entity> getEntities() {
        return List.copyOf(entityList);
    }

    public boolean containsEntity(Entity entity) {
        return entityList.contains(entity);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<----------------------->").append("\n");
        sb.append("\t  I   N   F   O").append("\n\n");
        sb.append(entityCnt()).append(" entities, including:").append("\n");
        sb.append(animalCnt).append(" animals").append("\n");
        sb.append(plantCnt).append(" plants").append("\n");
        sb.append("\n");

        entityList.forEach(x -> sb.append(x).append("\n"));

        sb.append("\n<----------------------->").append("\n");
        return sb.toString();
    }

    public String shortToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<----------------------->").append("\n");
        sb.append("Field").append("\n");
        sb.append(entityCnt()).append(" entities, including:").append("\n");
        sb.append(animalCnt).append(" animals").append("\n");
        sb.append(plantCnt).append(" plants").append("\n");
        sb.append("<----------------------->").append("\n");
        return sb.toString();
    }

    public int entityCnt() {
        return entityList.size();
    }

    public int animalCnt() {
        return animalCnt.get();
    }

    public int plantCnt() {
        return plantCnt.get();
    }

    public synchronized void clear() {
        entityList.clear();
        for (EntityGroup[] entityGroups : field) {
            for (EntityGroup entityGroup : entityGroups) {
                entityGroup.entityGroupSet.clear();
                entityGroup.entitiesCntMap.clear();
            }
        }
        animalCnt.set(0);
        plantCnt.set(0);
    }
}
