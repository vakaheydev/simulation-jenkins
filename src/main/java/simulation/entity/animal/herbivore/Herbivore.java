package simulation.entity.animal.herbivore;

import simulation.Field;
import simulation.entity.animal.Animal;
import simulation.entity.animal.Plant;

public abstract class Herbivore extends Animal {
    public Herbivore() {
    }

    public Herbivore(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    protected void populateChancesMap() {
        addChance(Plant.class, 1.0);
    }
}