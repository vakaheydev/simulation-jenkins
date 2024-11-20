package simulation.entity.animal.predator;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Duck;
import simulation.entity.animal.herbivore.Mouse;
import simulation.entity.animal.herbivore.Rabbit;

public class Eagle extends Predator {
    public Eagle() {
    }

    public Eagle(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 6;
    }

    @Override
    public int maxQuantity() {
        return 20;
    }

    @Override
    protected void populateChancesMap() {
        addChance(Fox.class, 0.1);
        addChance(Rabbit.class, 0.9);
        addChance(Mouse.class, 0.9);
        addChance(Duck.class, 0.8);
    }

    @Override
    public int speed() {
        return 3;
    }

    @Override
    public double neededFoodWeight() {
        return 1;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Eagle(field, x, y);
    }
}
