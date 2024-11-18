package simulation.exception;

import simulation.Entity;

public class TooMuchEntitiesException extends RuntimeException {
    public TooMuchEntitiesException(int currentAmount, Class<? extends Entity> entityClass) {
        super(String.format("There is already max amount[%d] of specified " +
                "entity[%s} on this location", currentAmount, entityClass.getSimpleName()));
    }
}
