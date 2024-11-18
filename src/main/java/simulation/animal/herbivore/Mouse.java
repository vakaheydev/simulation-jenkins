package simulation.animal.herbivore;

import simulation.Field;
import simulation.animal.Animal;

public class Mouse extends Herbivore {
    public Mouse(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 0.05;
    }

    @Override
    public int maxQuantity() {
        return 500;
    }

    @Override
    public int speed() {
        return 1;
    }

    @Override
    public double neededFoodWeight() {
        return 0.01;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Mouse(field, x, y);
    }
}
