package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;
import simulation.entity.animal.Animal;
import simulation.util.AnimalMoveUtil;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class EntityWorker extends AbstractEntityWorker {
    public EntityWorker(Field field, BlockingQueue<Entity> queue) {
        super(field, queue);
    }

    @Override
    public void work(Entity entity) {
        if (!entity.isAlive()) {
            log.info("Entity[{}] is dead", entity);
            return;
        }

        if (entity.isAnimal()) {
            Animal animal = entity.toAnimal();
            if (animal.speed() > 0) {
                AnimalMoveUtil.randomMove(field, entity.toAnimal());
            }
        }
    }
}
