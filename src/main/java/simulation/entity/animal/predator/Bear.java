package simulation.entity.animal.predator;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.*;

import static simulation.entity.EntityConfig.addChance;

public class Bear extends Predator {
    public Bear() {
    }

    public Bear(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 500;
    }

    @Override
    public int maxQuantity() {
        return 5;
    }

    @Override
    protected void populateChancesMap() {
        addChance(getClass(), Boa.class, 0.8);
        addChance(getClass(), Horse.class, 0.4);
        addChance(getClass(), Deer.class, 0.8);
        addChance(getClass(), Rabbit.class, 0.8);
        addChance(getClass(), Mouse.class, 0.9);
        addChance(getClass(), Goat.class, 0.7);
        addChance(getClass(), Sheep.class, 0.7);
        addChance(getClass(), Boar.class, 0.5);
        addChance(getClass(), Buffalo.class, 0.2);
        addChance(getClass(), Duck.class, 0.1);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public double neededFoodWeight() {
        return 80;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Bear(field, x, y);
    }
}
