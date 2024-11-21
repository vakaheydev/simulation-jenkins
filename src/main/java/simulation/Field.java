package simulation;

import lombok.extern.slf4j.Slf4j;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.exception.TooMuchEntitiesException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class Field {
    public class EntityGroup {
        private final Set<Entity> entities;
        private final Map<Class<? extends Entity>, Integer> entititesCntMap;

        public EntityGroup() {
            entities = ConcurrentHashMap.newKeySet();
            entititesCntMap = new HashMap<>();
        }

        public EntityGroup(EntityGroup group) {
            entities = Set.copyOf(group.entities);
            entititesCntMap = Map.copyOf(group.entititesCntMap);
        }

        public void addEntity(Entity entity) {
            entities.add(entity);
            entititesCntMap.merge(entity.getClass(), 1, Integer::sum);
        }

        public void removeEntity(Entity entity) {
            entities.remove(entity);
            entititesCntMap.merge(entity.getClass(), 1, (oldValue, subtrahend) -> oldValue - subtrahend);
        }

        public final List<Entity> getEntities() {
            return List.copyOf(entities);
        }

        public int entityCnt(Class<? extends Entity> clazz) {
            return entititesCntMap.getOrDefault(clazz, 0);
        }

        public void iterate(Consumer<Entity> func) {
            for (var entity : entities) {
                func.accept(entity);
            }
        }

        public int size() {
            return entities.size();
        }

        @Override
        public String toString() {
            return "EntityGroup{" +
                    entities.size() +
                    " entities" +
                    '}';
        }
    }

    private final Map<Entity, Point> entityPointMap;
    private final EntityGroup[][] field;
    private int animalCnt = 0;
    private int plantCnt = 0;
    public final int height = 100;
    public final int width = 20;

    public Field() {
        entityPointMap = new HashMap<>();
        field = new EntityGroup[height][width];

        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new EntityGroup();
            }
        }
    }

    public void addEntity(int x, int y, Entity entity) {
        requireNonNull(entity);
        checkPos(x, y);
        try {
            checkMaxQuantity(entity, x, y);
        } catch (TooMuchEntitiesException e) {
            throw e;
        }

        increaseCounters(entity);
        field[y][x].addEntity(entity);
        entityPointMap.put(entity, new Point(x, y));
    }

    public void removeEntity(Entity entity) {
        requireNonNull(entity);
        Point point = entityPointMap.get(entity);
        decreaseCounters(entity);
        field[point.y()][point.x()].removeEntity(entity);
        entityPointMap.remove(entity);
    }

    private void increaseCounters(Entity entity) {
        requireNonNull(entity);
        if (!entityPointMap.containsKey(entity)) {
            if (entity.isPlant()) {
                plantCnt++;
            } else if (entity.isAnimal()) {
                animalCnt++;
            }
        }
    }

    public void decreaseCounters(Entity entity) {
        requireNonNull(entity);
        if (entityPointMap.containsKey(entity)) {
            if (entity.isPlant()) {
                plantCnt--;
            } else if (entity.isAnimal()) {
                animalCnt--;
            }
        }
    }

    public void moveEntity(Point from, Point to,  Entity entity) {
        requireNonNull(entity);
        removeEntity(entity);
        try {
            addEntity(to.x(), to.y(), entity);
        } catch (TooMuchEntitiesException e) {
            addEntity(from.x(), from.y(), entity);
            log.debug("{} wasn't added because it is already too much of it", entity);
        }
    }

    public EntityGroup getEntityGroup(int x, int y) {
        checkPos(x, y);
        return new EntityGroup(field[y][x]);
    }

    public Set<Entity> getEntities(int x, int y) {
        checkPos(x, y);
        return field[y][x].entities;
    }

    public EntityGroup getEntityGroup(Point point) {
        checkPos(point.x(), point.y());
        return field[point.y()][point.x()];
    }

    public Point getEntityPoint(Entity entity) {
        requireNonNull(entity);
        return entityPointMap.get(entity);
    }

    private void checkPos(int x, int y) {
        if (y < 0 || y > height - 1) {
            throw new IllegalArgumentException(String.format("x (%d) must be in bounds: [%d - %d]", x, 0,
                    height - 1));
        }
        if (x < 0 || x > width - 1) {
            throw new IllegalArgumentException(String.format("y (%d) must be in bounds: [%d - %d]", y, 0,
                    width - 1));
        }
    }

    private void checkMaxQuantity(Entity entity, int x, int y) {
        requireNonNull(entity);
        if (field[y][x].entityCnt(entity.getClass()) == entity.maxQuantity()) {
            throw new TooMuchEntitiesException(entity);
        }
    }

    public Set<Entity> getEntities() {
        return Set.copyOf(entityPointMap.keySet());
    }

    public static void requireNonNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Can't be null");
        }
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

        for (var entry : entityPointMap.entrySet()) {
            sb.append(entry.getKey()).append("\n");
        }
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
        return entityPointMap.size();
    }

    public int animalCnt() {
        return animalCnt;
    }

    public int plantCnt() {
        return plantCnt;
    }

    public void clear() {
        entityPointMap.clear();
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j].entities.clear();
                field[i][j].entititesCntMap.clear();
            }
        }
        animalCnt = 0;
        plantCnt = 0;
    }
}
