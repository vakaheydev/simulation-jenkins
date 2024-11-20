package simulation.animal.predator;

import simulation.Field;
import simulation.animal.Animal;

public abstract class Predator extends Animal {
    public Predator() {
    }

    public Predator(Field field, int x, int y) {
        super(field, x, y);
    }
}
