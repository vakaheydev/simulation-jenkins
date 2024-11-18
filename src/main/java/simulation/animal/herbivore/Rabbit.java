package simulation.animal.herbivore;

import simulation.Field;
import simulation.animal.Animal;

public class Rabbit extends Herbivore {
    public Rabbit(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 2;
    }

    @Override
    public int maxQuantity() {
        return 150;
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public double neededFoodWeight() {
        return 0.45;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Rabbit(field, x, y);
    }
}
