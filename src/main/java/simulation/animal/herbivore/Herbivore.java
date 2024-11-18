package simulation.animal.herbivore;

import simulation.animal.Plant;
import simulation.Field;
import simulation.animal.Animal;

public abstract class Herbivore extends Animal {
    public Herbivore(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    protected void populateChancesMap() {
        addChance(Plant.class, 1.0);
    }
}
