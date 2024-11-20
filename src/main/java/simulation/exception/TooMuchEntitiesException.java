package simulation.exception;

import simulation.entity.Entity;

public class TooMuchEntitiesException extends RuntimeException {
    public TooMuchEntitiesException(Class<? extends Entity> entityClass) {
        super(String.format("There is already max amount of specified " +
                "entity[%s] on this location", entityClass.getSimpleName()));
    }
}
