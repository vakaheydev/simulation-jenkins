package simulation.entity.animal.predator;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.*;

import static simulation.entity.EntityConfig.addChance;

public class Wolf extends Predator {
    public Wolf() {
    }

    public Wolf(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 50;
    }

    @Override
    public int maxQuantity() {
        return 30;
    }

    @Override
    protected void populateChancesMap() {
        addChance(getClass(), Horse.class, 0.1);
        addChance(getClass(), Deer.class, 0.15);
        addChance(getClass(), Rabbit.class, 0.6);
        addChance(getClass(), Mouse.class, 0.8);
        addChance(getClass(), Goat.class, 0.6);
        addChance(getClass(), Sheep.class, 0.7);
        addChance(getClass(), Boar.class, 0.15);
        addChance(getClass(), Buffalo.class, 0.1);
        addChance(getClass(), Duck.class, 0.4);
    }

    @Override
    public int speed() {
        return 3;
    }

    @Override
    public double neededFoodWeight() {
        return 8;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Wolf(field, x, y);
    }
}
