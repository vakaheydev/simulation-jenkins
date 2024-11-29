package util;

import simulation.Field;
import simulation.entity.animal.Animal;

public class TestAnimal extends Animal {
    public TestAnimal(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 1;
    }

    @Override
    public int maxQuantity() {
        return 100_000;
    }

    @Override
    protected void populateChancesMap() {
    }

    @Override
    public int speed() {
        return 100_000;
    }

    @Override
    public double neededFoodWeight() {
        return 0;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new TestAnimal(field, x, y);
    }

    @Override
    public void loseWeight() {
    }
}
