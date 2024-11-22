package simulation.entity.animal.herbivore;

import simulation.Field;
import simulation.entity.animal.Animal;

import static simulation.entity.EntityConfig.addChance;

public class Boar extends Herbivore {

    public Boar() {
        super();
    }

    public Boar(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    protected void populateChancesMap() {
        super.populateChancesMap();
        addChance(getClass(), Caterpillar.class, 0.9);
    }

    @Override
    public double initialWeight() {
        return 400;
    }

    @Override
    public int maxQuantity() {
        return 50;
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public double neededFoodWeight() {
        return 50;
    }

    @Override
    public Animal createNewInstance(Field field, int x, int y) {
        return new Boar(field, x, y);
    }
}
