package simulation.entity.animal;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.exception.AnimalSpeedLimitExceededException;
import simulation.exception.DeadEntityException;
import simulation.exception.TooMuchEntitiesException;

import java.util.List;
import java.util.Random;

import static simulation.entity.EntityConfig.getChanceToEat;
import static simulation.util.PointUtil.getDirectionPoint;

@Slf4j
public abstract class Animal extends Entity {
    // <--- Public --->
    public enum Direction {
        RIGHT, LEFT, UP, DOWN
    }

    public Animal() {
    }

    public Animal(Field field, int x, int y) {
        super(field, x, y);
        populateChancesMap();
    }

    public double eat(Entity entity) {
        checkDeath();

        if (canEat(entity)) {
            double chance = getChanceToEat(getClass(), entity.getClass());
            if (randomEat(chance)) {
                log.info("{} eats {}", this, entity);
                double eatenWeight = entity.getWeight();
                weight += eatenWeight;
                entity.die();
                return eatenWeight;
            }
            return 0;
        }
        return 0;
    }

    private boolean randomEat(double chance) {
        Random rnd = new Random();

        return rnd.nextDouble() <= chance;
    }

    protected boolean canEat(Entity entity) {
        return getChanceToEat(getClass(), entity.getClass()) > 0 && entity.isAlive();
    }

    protected abstract void populateChancesMap();

    public void multiply(Entity entity) {
        checkDeath();
        if (this.point.equals(entity.getPoint())) {
            multiplyInternal(entity);
        }
    }

    private void multiplyInternal(Entity entity) {
        checkDeath();

        if (this.getClass().equals(entity.getClass())) {
            log.info("{} multiply with {}", this, entity);
            try {
                Animal newInstance = createNewInstance(this.field, point.x(), point.y());
                log.debug("{} was born", newInstance);
                if (hashCode() == newInstance.hashCode()) {
                    log.error("Equals hash code: {} and {}", this, newInstance);
                    log.error("Equals hash code: {} and {}", this.hashCode(), newInstance.hashCode());
                    throw new IllegalStateException();
                }
            } catch (TooMuchEntitiesException e) {
                log.error("{} wasn't added after multiplication because it is already too much of it", entity);
            }
        }
    }

    public void move(Direction... directions) {
        checkDeath();

        if (directions.length > speed()) {
            throw new AnimalSpeedLimitExceededException(this);
        }

        for (Direction direction : directions) {
            moveTo(direction);
            try {
                checkDeath();
            } catch (DeadEntityException ignored) {
                return;
            }
        }
    }

    private void moveTo(Direction direction) {
        Point directionPoint = getDirectionPoint(this.point, direction);
        int x = directionPoint.x();
        int y = directionPoint.y();

        List<Entity> entities = field.getEntityGroup(x, y).getEntityGroupSet();

        log.debug("{} moving {} to {}", this, direction, directionPoint);
        log.trace("EntityGroup in {}: {}", directionPoint, entities);

        field.moveEntity(point, new Point(x, y), this);
        this.point = new Point(x, y);

        actOnEntityList(entities);

        if (isAlive()) {
            log.trace("EntityGroup in {} after acting: {}", point, field.getEntityGroup(point).getEntityGroupSet());
            loseWeight();
        }
    }

    private void actOnEntityList(List<Entity> entities) {
        for (var other : entities) {
            this.multiply(other);
            this.eat(other);

            if (other instanceof Animal otherAnimal) {
                if (other.isAlive()) {
                    otherAnimal.eat(this);
                }
            }
        }

    }

    /**
     * By default, lose 10% of current weight
     */
    protected void loseWeight() {
        checkDeath();
        weight -= initialWeight() * 0.1;
    }

    public abstract int speed();

    public abstract double neededFoodWeight();

    public abstract Animal createNewInstance(Field field, int x, int y);

    @Override
    public EntityType getEntityType() {
        return EntityType.ANIMAL;
    }
}
