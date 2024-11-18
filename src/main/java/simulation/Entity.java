package simulation;

import simulation.animal.EntityType;

import java.util.Objects;

public abstract class Entity {
    private final int hashId;
    public Entity(Field field, int x, int y) {
        this.field = field;
        this.point = new Point(x, y);
        this.hashId = 31 * 17 + field.getEntityGroup(x, y).entityCnt(getClass());
        field.addEntity(x, y, this);
        weight = initialWeight();
    }

    protected static double weight;

    protected Field field;
    protected Point point;

    public boolean isAnimal() {
        return getEntityType().equals(EntityType.ANIMAL);
    }

    public boolean isPlant() {
        return getEntityType().equals(EntityType.PLANT);
    }

    public void die() {
        System.out.println(this + " dies");
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
        return Objects.equals(field, entity.field) && Objects.equals(point, entity.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getSimpleName(), field, point, hashId);
    }

    @Override
    public String toString() {
        return String.format("%s[%.2f kg] in %s", getClass().getSimpleName(), weight, point);
    }

    public double getWeight() {
        return weight;
    }

    public Field getField() {
        return field;
    }

    public Point getPoint() {
        return point;
    }
}
