package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;

import java.util.concurrent.BlockingQueue;

@Slf4j
public abstract class AbstractEntitySupplier implements Runnable {
    protected final Field field;
    protected final BlockingQueue<Entity> queue;

    public AbstractEntitySupplier(Field field, BlockingQueue<Entity> queue) {
        this.field = field;
        this.queue = queue;
    }

    @Override
    public void run() {
        log.debug("Supplier started");
        while (true) {
            Entity entity = supply();
            log.debug("Supplied entity: {}", entity);
            try {
                queue.put(entity);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public abstract Entity supply();
}
