package simulation.animal.herbivore;

import simulation.Field;
import simulation.animal.Animal;

public class Horse extends Herbivore {
    public Horse() {
    }

    public Horse(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 400;
    }

    @Override
    public int maxQuantity() {
        return 20;
    }

    @Override
    public int speed() {
        return 4;
    }

    @Override
    public double neededFoodWeight() {
        return 60;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Horse(field, x, y);
    }
}
