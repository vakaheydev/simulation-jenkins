package simulation.concurrent;

import simulation.Field;
import simulation.entity.Entity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractEntityProcessor implements Runnable {
    protected final Field field;
    protected final BlockingQueue<Entity> queue;
    public final AtomicInteger loopCounter;

    public AbstractEntityProcessor(Field field, BlockingQueue<Entity> queue, int initialLoopCntValue) {
        this.field = field;
        this.queue = queue;
        this.loopCounter = new AtomicInteger(initialLoopCntValue);
    }

    public abstract void process();
    public abstract boolean shouldContinue();

    @Override
    public void run() {
        while(shouldContinue()) {
            process();
            loopCounter.incrementAndGet();
        }
    }
}
