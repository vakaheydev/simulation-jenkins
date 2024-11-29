package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import simulation.Field;
import simulation.Simulation;
import simulation.entity.Entity;

import java.util.concurrent.*;

@Slf4j
public class ConcurrentSimulation implements Simulation {
    private static final Marker IMPORTANT_MARKER = MarkerFactory.getMarker("IMPORTANT");

    @Override
    public void start() {
        log.info("Started concurrent simulation");

        Field field = new Field();
        BlockingQueue<Entity> queue = ConcurrentConfig.getBlockingQueue();


        ExecutorService es = Executors.newFixedThreadPool(8);
        CompletionService<Void> ecs = new ExecutorCompletionService<>(es);

        int nWorkers = 6, nSuppliers = 1;

        Semaphore semaphore = new Semaphore(nWorkers);

        for (int i = 0; i < nWorkers; i++) {
            ecs.submit(() -> {
                new RandomMoveEntityWorker(field, queue, semaphore).run();
                return null;
            });
        }

        for (int i = 0; i < nSuppliers; i++) {
            ecs.submit(() -> {
                new InfiniteEntitySupplier(field, queue, semaphore).run();
                return null;
            });
        }

        Runnable fieldChecker = () -> {
            while (true) {
                log.info(IMPORTANT_MARKER, field.shortToString());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        ecs.submit(() -> {
            fieldChecker.run();
            return null;
        });

        for (int i = 0; i < nWorkers + nSuppliers + 1; i++) {
            try {
                ecs.take().get();
            } catch (InterruptedException | ExecutionException e) {
                es.shutdownNow();
                throw new RuntimeException(e);
            }
        }

        log.info("Ended concurrent simulation");
    }
}
