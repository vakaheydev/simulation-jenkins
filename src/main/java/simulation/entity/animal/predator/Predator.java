package simulation.entity.animal.predator;

import simulation.Field;
import simulation.entity.animal.Animal;

public abstract class Predator extends Animal {
    public Predator() {
    }

    public Predator(Field field, int x, int y) {
        super(field, x, y);
    }
}
