package simulation.animal.herbivore;

import simulation.Field;
import simulation.animal.Animal;

public class Goat extends Herbivore {
    public Goat() {
    }

    public Goat(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 60;
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
        return 10;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Goat(field, x, y);
    }
}
