package simulation.entity;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.animal.EntityType;

import java.util.Objects;

@Slf4j
public abstract class Entity {
    // <--- Protected ---> \\
    protected double weight;
    protected Field field;
    protected Point point;

    // <--- Private ---> \\
    private Integer hashCode;
    private Integer hashId;

    public Entity() {
        weight = initialWeight();
    }

    public Entity(Field field, int x, int y) {
        this();
        this.field = field;
        this.point = new Point(x, y);
        field.addEntity(x, y, this);
    }

    public boolean isAnimal() {
        return getEntityType().equals(EntityType.ANIMAL);
    }

    public boolean isPlant() {
        return getEntityType().equals(EntityType.PLANT);
    }

    public void die() {
        log.debug("{} dies", this);
        field.removeEntity(this);
    }

    public abstract double initialWeight();

    public abstract int maxQuantity();

    public abstract EntityType getEntityType();

    public boolean isAlive() {
        return field.getEntityPoint(this) != null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Entity entity = (Entity) object;
        return Objects.equals(field, entity.field) && Objects.equals(point, entity.point) && Objects.equals(hashId, entity.hashId);
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            hashId = 31 * 17 + field.getEntityGroup(point).entityCnt(getClass());
            hashCode = Objects.hash(this.getClass().getSimpleName(), this.point, this.hashId);
        }

        return hashCode;
    }

    @Override
    public String toString() {
        return String.format("%s@%d[%.2f kg] in %s", getClass().getSimpleName(), hashCode % 100, weight, point);
    }

    public double getWeight() {
        return weight;
    }
    public Point getPoint() {
        return point;
    }
}
