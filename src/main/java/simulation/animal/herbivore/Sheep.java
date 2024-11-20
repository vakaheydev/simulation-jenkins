package simulation.animal.herbivore;

import simulation.Field;
import simulation.animal.Animal;

public class Sheep extends Herbivore {
    public Sheep() {
    }

    public Sheep(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 70;
    }

    @Override
    public int maxQuantity() {
        return 140;
    }

    @Override
    public int speed() {
        return 3;
    }

    @Override
    public double neededFoodWeight() {
        return 15;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Sheep(field, x, y);
    }
}
