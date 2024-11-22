package simulation.exception;

import simulation.entity.Entity;

public class DeadEntityException extends RuntimeException {
    public DeadEntityException(Entity entity) {
        super(String.format("Entity (%s) is died", entity));
    }
}
