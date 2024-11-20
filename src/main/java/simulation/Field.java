package simulation;

import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.exception.TooMuchEntitiesException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Field {
    public class EntityGroup {
        private final Set<Entity> entities;
        private final Map<Class<? extends Entity>, Integer> entititesCntMap;

        public EntityGroup() {
            entities = ConcurrentHashMap.newKeySet();
            entititesCntMap = new HashMap<>();
        }

        public void addEntity(Entity entity) {
            entities.add(entity);
            entititesCntMap.merge(entity.getClass(), 1, Integer::sum);
        }

        public void removeEntity(Entity entity) {
            entities.remove(entity);
            entititesCntMap.merge(entity.getClass(), 1, (oldValue, subtrahend) -> oldValue - subtrahend);
        }

        public final Set<Entity> getEntities() {
            return entities;
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
        checkPos(x, y);
        checkMaxQuantity(entity, x, y);
        increaseCounters(entity);
        field[y][x].addEntity(entity);
        entityPointMap.put(entity, new Point(x, y));
    }

    public void removeEntity(Entity entity) {
        Point point = entityPointMap.get(entity);
        decreaseCounters(entity);
        field[point.y()][point.x()].removeEntity(entity);
        entityPointMap.remove(entity);
    }

    private void increaseCounters(Entity entity) {
        if (!entityPointMap.containsKey(entity)) {
            if (entity.isPlant()) {
                plantCnt++;
            } else if (entity.isAnimal()) {
                animalCnt++;
            }
        }
    }

    public void decreaseCounters(Entity entity) {
        if (entityPointMap.containsKey(entity)) {
            if (entity.isPlant()) {
                plantCnt--;
            } else if (entity.isAnimal()) {
                animalCnt--;
            }
        }
    }

    public void moveEntity(int x, int y, Entity entity) {
        removeEntity(entity);
        addEntity(x, y, entity);
    }

    public EntityGroup getEntityGroup(int x, int y) {
        checkPos(x, y);
        return field[y][x];
    }

    public EntityGroup getEntityGroup(Point point) {
        checkPos(point.x(), point.y());
        return field[point.y()][point.x()];
    }

    public Point getEntityPoint(Entity entity) {
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
        if (field[y][x].entityCnt(entity.getClass()) == entity.maxQuantity()) {
            throw new TooMuchEntitiesException(entity.getClass());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n<----------------------->").append("\n");
        sb.append("\t  I   N   F   O").append("\n\n");
        sb.append(animalCnt).append(" animals").append("\n");
        sb.append(plantCnt).append(" plants").append("\n");
        sb.append("\n");

        for (var entry : entityPointMap.entrySet()) {
            sb.append(entry.getKey()).append("\n");
        }
        sb.append("\n<----------------------->").append("\n");
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
