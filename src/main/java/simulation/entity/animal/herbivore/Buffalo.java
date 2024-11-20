package simulation.entity.animal.herbivore;

import simulation.Field;
import simulation.entity.animal.Animal;

public class Buffalo extends Herbivore {
    public Buffalo() {
    }

    public Buffalo(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 700;
    }

    @Override
    public int maxQuantity() {
        return 10;
    }

    @Override
    public int speed() {
        return 3;
    }

    @Override
    public double neededFoodWeight() {
        return 100;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Buffalo(field, x, y);
    }
}
