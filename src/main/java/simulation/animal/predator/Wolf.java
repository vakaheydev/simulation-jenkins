package simulation.animal.predator;

import simulation.animal.Animal;
import simulation.Field;
import simulation.animal.herbivore.*;

public class Wolf extends Predator {
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
        addChance(Horse.class, 0.1);
        addChance(Deer.class, 0.15);
        addChance(Rabbit.class, 0.6);
        addChance(Mouse.class, 0.8);
        addChance(Goat.class, 0.6);
        addChance(Sheep.class, 0.7);
        addChance(Boar.class, 0.15);
        addChance(Buffalo.class, 0.1);
        addChance(Duck.class, 0.4);
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
