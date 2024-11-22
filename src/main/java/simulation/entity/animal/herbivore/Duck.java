package simulation.entity.animal.herbivore;

import simulation.Field;
import simulation.entity.animal.Animal;

import static simulation.entity.EntityConfig.addChance;

public class Duck extends Herbivore {
    public Duck() {
    }

    public Duck(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    protected void populateChancesMap() {
        super.populateChancesMap();
        addChance(getClass(), Caterpillar.class, 1.0);
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
