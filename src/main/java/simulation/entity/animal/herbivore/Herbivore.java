package simulation.entity.animal.herbivore;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.Plant;

import static simulation.entity.EntityConfig.addChance;

public abstract class Herbivore extends Animal {
    public Herbivore() {
    }

    public Herbivore(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    protected void populateChancesMap() {
        addChance(getClass(), Plant.class, 1.0);
    }
}
