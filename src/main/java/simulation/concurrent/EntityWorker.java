package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class EntityWorker extends AbstractEntityProcessor {
    protected final Consumer<Entity> consumer;
    protected final Predicate<EntityWorker> loopPredicate;

    public EntityWorker(Field field, BlockingQueue<Entity> queue, Consumer<Entity> consumer,
                        Predicate<EntityWorker> loopPredicate) {
        super(field, queue, 0);
        this.consumer = consumer;
        this.loopPredicate = loopPredicate;
    }

    @Override
    public void process() {
        log.debug("Worker started");

        log.trace("Loop counter: {}", loopCounter);

        Entity entity;

        try {
            entity = queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.debug("Got an entity: {}", entity);
        consumer.accept(entity);
    }

    @Override
    public boolean shouldContinue() {
        return loopPredicate.test(this);
    }
}
