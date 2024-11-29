package concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import simulation.Field;
import simulation.concurrent.*;
import simulation.entity.Entity;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Deer;
import simulation.entity.animal.herbivore.Rabbit;
import simulation.entity.animal.predator.Bear;
import simulation.entity.animal.predator.Wolf;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ConcurrentSimulationTest {
    private static final Marker IMPORTANT_MARKER = MarkerFactory.getMarker("IMPORTANT");

    @DisplayName("1 sup | 1 work")
    @Test
    public void testOneSup() throws InterruptedException {
        Field field = new Field();
        int n = 1;
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(n);

        EntityWorker worker = new EntityWorker(field, queue, (x) -> {
            Animal animal = x.toAnimal();
            log.debug("Got an animal: {}", animal);

            assertNotNull(animal);
            assertTrue(animal.isAlive());

            animal.move(Animal.Direction.UP);
        },
                (x) -> x.loopCounter.get() < n);

        EntitySupplier supplier = new EntitySupplier(field, queue,
                () -> new Deer(field, 5, 5),
                (x) -> x.loopCounter.get() < n);

        Thread workerThread = new Thread(worker, "worker");
        Thread supplierThread = new Thread(supplier, "supplier");
        workerThread.start();
        supplierThread.start();

        workerThread.join();
        supplierThread.join();

        Entity entity = field.getEntityGroup(5, 4).getEntityList().get(0);

        assertNotNull(entity);
        assertTrue(entity.isAlive());
        assertEquals(270.0, entity.getWeight());
        assertEquals(Deer.class, entity.getClass());

        assertEquals(1, field.getEntities().size());
        assertEquals(1, field.getEntityGroup(5, 4).size());
    }

    @DisplayName("3 sup | 1 work")
    @Test
    public void testThreeSup() throws InterruptedException {
        Field field = new Field();
        int n = 3;
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(n);

        EntityWorker worker = new EntityWorker(field, queue, (x) -> {
            Animal animal = x.toAnimal();
            log.debug("Got an animal: {}", animal);

            assertNotNull(animal);
            assertTrue(animal.isAlive());

            animal.move(Animal.Direction.UP);
        },
                (x) -> x.loopCounter.get() < n);

        EntitySupplier deerSupplier = new EntitySupplier(field, queue,
                () -> new Deer(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier wolfSupplier = new EntitySupplier(field, queue,
                () -> new Wolf(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier rabbitSupplier = new EntitySupplier(field, queue,
                () -> new Rabbit(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        Thread workerThread = new Thread(worker, "worker");
        Thread deerSupplierThread = new Thread(deerSupplier, "deerSupplier");
        Thread wolfSupplierThread = new Thread(wolfSupplier, "wolfSupplier");
        Thread rabbitSupplierThread = new Thread(rabbitSupplier, "rabbitSupplier");

        workerThread.start();
        deerSupplierThread.start();
        wolfSupplierThread.start();
        rabbitSupplierThread.start();

        workerThread.join();
        deerSupplierThread.join();
        wolfSupplierThread.join();
        rabbitSupplierThread.join();

        Field.EntityGroup entityGroup = field.getEntityGroup(5, 4);
        log.info("Final entityGroup: {}", entityGroup);
        entityGroup.iterate((x) -> log.info(x.toString()));

        assertNotNull(entityGroup);
        assertTrue(entityGroup.size() > 0);
    }

    @DisplayName("3 sup | 3 work")
    @Test
    public void testThreeSupThreeWork() throws InterruptedException {
        Field field = new Field();
        int n = 3;
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(n);

        EntityWorker worker = new EntityWorker(field, queue, (x) -> {
            Animal animal = x.toAnimal();
            log.debug("Got an animal: {}", animal);

            assertNotNull(animal);
            assertTrue(animal.isAlive());

            animal.move(Animal.Direction.UP);
        },
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier deerSupplier = new EntitySupplier(field, queue,
                () -> new Deer(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier wolfSupplier = new EntitySupplier(field, queue,
                () -> new Wolf(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier rabbitSupplier = new EntitySupplier(field, queue,
                () -> new Rabbit(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        ExecutorService executor = Executors.newFixedThreadPool(6);

        executor.submit(worker);
        executor.submit(worker);
        executor.submit(worker);
        executor.submit(deerSupplier);
        executor.submit(wolfSupplier);
        executor.submit(rabbitSupplier);

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            log.warn("Work didn't finished in expected time");
        }

        Field.EntityGroup entityGroup = field.getEntityGroup(5, 4);
        log.info("Final entityGroup: {}", entityGroup);
        entityGroup.iterate((x) -> log.info(x.toString()));

        assertNotNull(entityGroup);
        assertTrue(entityGroup.size() > 0);
    }

    @DisplayName("4 sup | 4 work")
    @Test
    public void testFourSupFourWork() throws InterruptedException {
        Field field = new Field();
        int n = 4;
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(n);

        EntityWorker worker = new EntityWorker(field, queue, (x) -> {
            Animal animal = x.toAnimal();
            log.debug("Got an animal: {}", animal);

            assertNotNull(animal);
            assertTrue(animal.isAlive());

            animal.move(Animal.Direction.UP);
        },
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier deerSupplier = new EntitySupplier(field, queue,
                () -> new Deer(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier wolfSupplier = new EntitySupplier(field, queue,
                () -> new Wolf(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier rabbitSupplier = new EntitySupplier(field, queue,
                () -> new Rabbit(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        ExecutorService executor = Executors.newFixedThreadPool(6);

        executor.submit(worker);
        executor.submit(worker);
        executor.submit(worker);
        executor.submit(deerSupplier);
        executor.submit(wolfSupplier);
        executor.submit(rabbitSupplier);

        executor.shutdown();
        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
            log.warn("Work didn't finished in expected time");
        }

        Field.EntityGroup entityGroup = field.getEntityGroup(5, 4);
        log.info("Final entityGroup: {}", entityGroup);
        entityGroup.iterate((x) -> log.info(x.toString()));

        assertNotNull(entityGroup);
        assertTrue(entityGroup.size() > 0);
    }

    @DisplayName("Concurrent simulation (1 work | 1 sup)")
    @Test
    public static void testSimulationOneWorkOneSup() {
        log.info("Started concurrent simulation");

        Field field = new Field();
        BlockingQueue<Entity> queue = ConcurrentConfig.getBlockingQueue();

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        int nWorkers = 1, nSuppliers = 1;

        Semaphore semaphore = new Semaphore(nWorkers);

        for (int i = 0; i < nWorkers; i++) {
            executorService.submit(new RandomMoveEntityWorker(field, queue, semaphore));
        }

        for (int i = 0; i < nSuppliers; i++) {
            executorService.submit(new InfiniteEntitySupplier(field, queue, semaphore));
        }

        Runnable fieldChecker = () -> {
            while (true) {
                log.info(IMPORTANT_MARKER, field.toString());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        executorService.submit(fieldChecker);

        executorService.shutdown();

        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        log.info("Ended concurrent simulation");
    }

    @DisplayName("Vision test")
    @Test
    public void testVision() throws InterruptedException {
        Field field = new Field();
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(10);

        EntitySupplier supplier = new EntitySupplier(field, queue,
                () -> new Bear(field, 10, 10),
                (x) -> true);

        Runnable worker = () -> {
            int size = field.getEntityGroup(0, 0).size();
        };
    }
}
