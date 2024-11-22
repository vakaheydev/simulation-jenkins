package simulation.exception;

import simulation.entity.animal.Animal;

public class AnimalSpeedLimitExceededException extends RuntimeException {
    public AnimalSpeedLimitExceededException(Animal animal) {
        super(String.format("This animal[%s] can't move more than %d locations at " +
                "once", animal, animal.speed()));
    }
}
