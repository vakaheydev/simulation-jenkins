package simulation.entity.animal.predator;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Duck;
import simulation.entity.animal.herbivore.Mouse;
import simulation.entity.animal.herbivore.Rabbit;

import static simulation.entity.EntityConfig.addChance;

public class Boa extends Predator {
    public Boa() {
    }

    public Boa(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 15;
    }

    @Override
    public int maxQuantity() {
        return 30;
    }

    @Override
    protected void populateChancesMap() {
        addChance(getClass(), Rabbit.class, 0.2);
        addChance(getClass(), Mouse.class, 0.4);
        addChance(getClass(), Duck.class, 0.1);
    }

    @Override
    public int speed() {
        return 1;
    }

    @Override
    public double neededFoodWeight() {
        return 3;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Boa(field, x, y);
    }
}
