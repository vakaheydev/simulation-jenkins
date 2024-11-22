package simulation.entity.animal.predator;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Caterpillar;
import simulation.entity.animal.herbivore.Duck;
import simulation.entity.animal.herbivore.Mouse;
import simulation.entity.animal.herbivore.Rabbit;

import static simulation.entity.EntityConfig.addChance;

public class Fox extends Predator {
    public Fox() {
    }

    public Fox(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 8;
    }

    @Override
    public int maxQuantity() {
        return 30;
    }

    @Override
    protected void populateChancesMap() {
        addChance(getClass(), Rabbit.class, 0.7);
        addChance(getClass(), Mouse.class, 0.9);
        addChance(getClass(), Duck.class, 0.6);
        addChance(getClass(), Caterpillar.class, 0.4);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public double neededFoodWeight() {
        return 2;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Fox(field, x, y);
    }
}
