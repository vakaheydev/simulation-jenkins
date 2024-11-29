package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;
import simulation.util.EntityUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

@Slf4j
public class InfiniteEntitySupplier extends EntitySupplier {
    public InfiniteEntitySupplier(Field field, BlockingQueue<Entity> queue, Semaphore semaphore) {
        super(field, queue,
                () -> {

                    try {
                        log.trace("Semaphore permits before: {}", semaphore.availablePermits());
                        semaphore.acquire();
                        log.trace("Semaphore permits left: {}", semaphore.availablePermits());
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    Entity entity = EntityUtil.addEntity(field);

                    return entity;
                },
                supplier -> true);
    }
}
