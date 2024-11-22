package simulation.entity.animal;

import simulation.Field;
import simulation.entity.Entity;

public class Plant extends Entity {
    public Plant() {
    }

    public Plant(Field field, int x, int y) {
        super(field, x, y);
    }

    @Override
    public double initialWeight() {
        return 1;
    }

    @Override
    public int maxQuantity() {
        return 200;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.PLANT;
    }
}
