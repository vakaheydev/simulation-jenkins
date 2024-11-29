package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
public class EntitySupplier extends AbstractEntityProcessor {
    protected final Supplier<Entity> supplier;
    protected final Predicate<EntitySupplier> loopPredicate;

    public EntitySupplier(Field field, BlockingQueue<Entity> queue, Supplier<Entity> supplier,
                          Predicate<EntitySupplier> loopPredicate) {
        super(field, queue, 0);
        this.supplier = supplier;
        this.loopPredicate = loopPredicate;
    }

    @Override
    public void process() {
        Entity entity = supplier.get();

        log.debug("Supplied entity: {}", entity);

        try {
            queue.put(entity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean shouldContinue() {
        return loopPredicate.test(this);
    }
}
