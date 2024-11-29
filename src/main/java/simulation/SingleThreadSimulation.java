package simulation;

import lombok.extern.slf4j.Slf4j;
import simulation.entity.Entity;
import simulation.entity.animal.Animal;
import simulation.exception.AnimalSpeedLimitExceededException;
import simulation.exception.DeadEntityException;
import simulation.util.AnimalMoveUtil;
import simulation.util.EntityUtil;

@Slf4j
public class SingleThreadSimulation implements Simulation {
    @Override
    public void start() {
        Field field = new Field();
        int entitiesNumber = 100_000;
        EntityUtil.addEntities(field, entitiesNumber);
        System.out.println(field.shortToString());
        var entities = field.getEntities();
        for (Entity entity : entities) {
            tryToMove(field, entity.toAnimal());
        }
        System.out.println(field.shortToString());
        System.out.println("\n\n--------------------------");
    }

    public void tryToMove(Field field, Animal animal) {
        if (animal == null) {
            return;
        }

        try {
            AnimalMoveUtil.randomMove(field, animal);
        } catch (DeadEntityException | AnimalSpeedLimitExceededException ignored) {

        }
    }
}
