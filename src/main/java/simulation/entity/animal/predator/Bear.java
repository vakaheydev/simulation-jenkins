package simulation.entity.animal.predator;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.*;

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
        addChance(Boa.class, 0.8);
        addChance(Horse.class, 0.4);
        addChance(Deer.class, 0.8);
        addChance(Rabbit.class, 0.8);
        addChance(Mouse.class, 0.9);
        addChance(Goat.class, 0.7);
        addChance(Sheep.class, 0.7);
        addChance(Boar.class, 0.5);
        addChance(Buffalo.class, 0.2);
        addChance(Duck.class, 0.1);
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
