package simulation.entity;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.EntityType;
import simulation.entity.animal.Plant;
import simulation.exception.DeadEntityException;

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

    public Animal toAnimal() {
        if (isAnimal()) {
            return (Animal) this;
        }

        return null;
    }

    public Plant toPlant() {
        if (isPlant()) {
            return (Plant) this;
        }

        return null;
    }

    public void die() {
        if (!isAlive()) {
            log.error("Tried to kill already died animal");
            return;
        }

        log.debug("{} dies", this);
        field.removeEntity(this);
        this.point = null;
        this.field = null;
    }

    public abstract double initialWeight();

    public abstract int maxQuantity();

    public abstract EntityType getEntityType();

    public boolean isAlive() {
        return point != null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Entity entity = (Entity) object;
        return
                Objects.equals(field, entity.field) &&
                        Objects.equals(point, entity.point) &&
                        Objects.equals(weight, entity.weight) &&
                        Objects.equals(hashId, entity.hashId) &&
                        Objects.equals(hashCode, entity.hashCode);
    }

    @Override
    public int hashCode() {
        if (hashCode == null) {
            int entityCnt = field.getEntityGroup(point).entityCnt(getClass());
            hashId = 7919 * 101 + entityCnt;
            hashCode = Objects.hash(this.getClass().getSimpleName(), this.point, this.hashId, this.weight);
        }

        return hashCode;
    }

    @Override
    public String toString() {
        String strHashCode = (hashCode != null ? String.valueOf(hashCode % 1000) : "undefined");
        String strPoint = (isAlive() ? String.format("in %s", point.toString()) : "dead");
        return String.format("%s@%s[%.2f kg] %s", getClass().getSimpleName(), strHashCode, weight, strPoint);
    }

    public double getWeight() {
        checkDeath();
        return weight;
    }

    protected void checkDeath() {
        if (!isAlive()) {
            throw new DeadEntityException(this);
        }

        if (weight <= 0) {
            die();
            throw new DeadEntityException(this);
        }
    }

    public Point getPoint() {
        return point;
    }
}
