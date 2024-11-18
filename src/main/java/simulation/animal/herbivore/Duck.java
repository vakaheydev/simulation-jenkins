package simulation.animal.herbivore;

import simulation.Field;
import simulation.animal.Animal;

public class Duck extends Herbivore {
    public Duck(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    protected void populateChancesMap() {
        super.populateChancesMap();
        addChance(Caterpillar.class, 1.0);
    }

    @Override
    public double initialWeight() {
        return 1;
    }

    @Override
    public int maxQuantity() {
        return 200;
    }

    @Override
    public int speed() {
        return 4;
    }

    @Override
    public double neededFoodWeight() {
        return 0.15;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Duck(field, x, y);
    }
}
