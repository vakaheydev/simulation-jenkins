package simulation.animal.herbivore;

import simulation.Field;
import simulation.animal.Animal;

public class Caterpillar extends Herbivore {
    public Caterpillar() {
    }

    public Caterpillar(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 0.01;
    }

    @Override
    public int maxQuantity() {
        return 1000;
    }

    @Override
    public int speed() {
        return 0;
    }

    @Override
    public void move(Direction... directions) {
    }

    @Override
    public double neededFoodWeight() {
        return 0;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Caterpillar(field, x, y);
    }
}
