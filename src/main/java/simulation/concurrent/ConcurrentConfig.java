package simulation.concurrent;

import simulation.entity.Entity;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConcurrentConfig {
    private ConcurrentConfig() {}
    private static final BlockingQueue<Entity> blockingQueue = new ArrayBlockingQueue<>(10);

    public static BlockingQueue<Entity> getBlockingQueue() {
        return blockingQueue;
    }
}
