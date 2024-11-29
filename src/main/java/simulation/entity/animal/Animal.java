package simulation.entity.animal;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.exception.AnimalSpeedLimitExceededException;
import simulation.exception.DeadEntityException;
import simulation.exception.TooMuchEntitiesException;
import simulation.util.Validations;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static simulation.entity.EntityConfig.getChanceToEat;
import static simulation.util.PointUtil.getDirectionPoint;
import static simulation.util.Validations.checkEntity;

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

    protected abstract void populateChancesMap();

    public abstract int speed();

    public abstract double neededFoodWeight();

    public abstract Animal createNewInstance(Field field, int x, int y);

    public void eat(Entity entity) {
        checkDeath();
        if (canEat(entity)) {
            eatInternally(entity);
        }
    }

    private void eatInternally(Entity entity) {
        double chance = getChanceToEat(getClass(), entity.getClass());
        if (randomEat(chance)) {
            log.info("{} eats {}", this, entity);
            weight += entity.getWeight();
            entity.die();
        }
    }

    private boolean randomEat(double chance) {
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        return rnd.nextDouble() <= chance;
    }

    public boolean canEat(Entity entity) {
        return getChanceToEat(getClass(), entity.getClass()) > 0 && entity.isAlive();
    }

    public void multiplyWith(Animal entity) {
        checkDeath();
        checkEntity(entity);

        if (canMultiplyWith(entity)) {
            multiplyWithInternal(entity);
        }
    }

    private void multiplyWithInternal(Entity entity) {
        log.info("{} multiply with {}", this, entity);
        createChildWith(entity);
    }

    public boolean canMultiplyWith(Animal animal) {
        return getClass().equals(animal.getClass()) && point.equals(animal.getPoint());
    }

    private void createChildWith(Entity entity) {
        try {
            Animal newInstance = createNewInstance(this.field, point.x(), point.y());
            log.debug("{} was born", newInstance);
        } catch (TooMuchEntitiesException e) {
            log.warn("{} wasn't added after multiplication because it is already too much of it", entity);
        }
    }

    public void move(Direction... directions) {
        Validations.checkNotNull(directions);
        checkDeath();

        if (directions.length > speed()) {
            throw new AnimalSpeedLimitExceededException(this);
        }

        for (Direction direction : directions) {
            try {
                moveTo(direction);
            } catch (DeadEntityException ignored) {
                return;
            }
        }
    }

    private void moveTo(Direction direction) {
        synchronized (field) {
            checkDeath();
            Point directionPoint = getDirectionPoint(this.point, direction);
            int x = directionPoint.x();
            int y = directionPoint.y();

            List<Entity> entities = field.getEntityGroup(x, y).getEntityList();

            log.debug("{} moving {} to {}", this, direction, directionPoint);
            log.trace("EntityGroup in {}: {}", directionPoint, entities);

            field.moveEntity(point, new Point(x, y), this);

            this.point = new Point(x, y);

            actOnEntityList(entities);

            if (isAlive()) {
                loseWeight();
                log.trace("EntityGroup in {} after acting: {}", point, field.getEntityGroup(point).getEntityList());
            }

            checkDeath();
        }

    }

    private void actOnEntityList(List<Entity> entities) {
        for (var other : entities) {
            this.eat(other);

            if (other.isAnimal() && other.isAlive()) {
                Animal otherAnimal = other.toAnimal();
                this.multiplyWith(otherAnimal);
                otherAnimal.eat(this);
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

    @Override
    public EntityType getEntityType() {
        return EntityType.ANIMAL;
    }
}
