package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;
import simulation.entity.animal.Animal;
import simulation.exception.DeadEntityException;
import simulation.util.AnimalMoveUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

@Slf4j
public class RandomMoveEntityWorker extends EntityWorker {
    public RandomMoveEntityWorker(Field field, BlockingQueue<Entity> queue, Semaphore semaphore) {
        super(field, queue, entity -> {
            if (entity.isAnimal()) {
                Animal animal = entity.toAnimal();
                while (animal.isAlive() && animal.speed() > 0) {
                    try {
                        AnimalMoveUtil.randomMove(field, animal);
                    } catch (DeadEntityException ex) {
                        log.error("Animal {}", animal);
                        break;
                    }
                }
            }


            semaphore.release();

        }, worker -> true);
    }
}
