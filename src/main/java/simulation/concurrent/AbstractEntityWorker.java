package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;

import java.util.concurrent.BlockingQueue;

@Slf4j
public abstract class AbstractEntityWorker implements Runnable {
    protected final Field field;
    protected final BlockingQueue<Entity> queue;

    public AbstractEntityWorker(Field field, BlockingQueue<Entity> queue) {
        this.field = field;
        this.queue = queue;
    }

    @Override
    public void run() {
        log.debug("Worker started");
        while (true) {
            Entity entity = null;
            try {
                entity = queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("Got an entity: {}", entity);
            work(entity);
        }
    }

    public abstract void work(Entity entity);
}
