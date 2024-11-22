package simulation;

import lombok.extern.slf4j.Slf4j;
import simulation.Field.EntityGroup;
import simulation.entity.Entity;
import simulation.entity.animal.Animal;
import simulation.exception.AnimalSpeedLimitExceededException;
import simulation.exception.DeadEntityException;
import simulation.util.AnimalMoveUtil;
import simulation.util.EntityUtil;
import simulation.util.Validations;

import java.util.Set;

@Slf4j
public class Simulation {
    public void start() {
        int entitiesNumber = 10000;
        while (true) {
            Field field = new Field();
            EntityUtil.addEntities(field, entitiesNumber);
            System.out.println(field.shortToString());
            var entities = field.getEntities();
            for (Entity entity : entities) {
                tryToMove(field, entity.toAnimal());
            }
            System.out.println(field.shortToString());
            System.out.println("\n\n--------------------------");
        }

    }

    public void tryToMove(Field field, Animal animal) {
        if (animal == null) {
            return;
        }

        try {
            AnimalMoveUtil.randomMove(field, animal);
            checkCounters(field, animal);
        } catch (DeadEntityException | AnimalSpeedLimitExceededException ignored) {

        }
    }

    public void checkCounters(Field field, Animal animal) {
        var entities = field.getEntities();
        int animalCnt = 0;
        int plantCnt = 0;

        for (Entity entity : entities) {
            if (entity.isAnimal()) {
                animalCnt++;
            } else if (entity.isPlant()) {
                plantCnt++;
            }
        }



        if (animalCnt != field.animalCnt() || plantCnt != field.plantCnt()) {
            log.warn("Field values: Animals {} vs Plants {}", field.animalCnt(), field.plantCnt());
            log.warn("Calculated values: Animals {} vs Plants {}", animalCnt, plantCnt);

            log.error("NOT CHECK");

            EntityGroup entityGroup = field.getEntityGroup(animal.getPoint());


            throw new RuntimeException();
        }
    }
}
