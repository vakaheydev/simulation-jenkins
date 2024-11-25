package concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.concurrent.EntitySupplier;
import simulation.concurrent.EntityWorker;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Deer;
import simulation.entity.animal.herbivore.Rabbit;
import simulation.entity.animal.predator.Bear;
import simulation.entity.animal.predator.Wolf;
import simulation.util.EntityUtil;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static simulation.util.EntityUtil.addEntities;
import static simulation.util.EntityUtil.getRandomClass;

@Slf4j
public class ConcurrentSimulationTest {
    @DisplayName("1 sup | 1 work")
    @Test
    public void testOneSup() throws InterruptedException {
        Field field = new Field();
        int n = 1;
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(n);

        EntityWorker worker = new EntityWorker(field, queue, (x)  -> {
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

        EntityWorker worker = new EntityWorker(field, queue, (x)  -> {
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

//        Entity entity = field.getEntityGroup(5, 4).getEntityList().get(0);
//
//        assertNotNull(entity);
//        assertTrue(entity.isAlive());
//        assertEquals(270.0, entity.getWeight());
//        assertEquals(Deer.class, entity.getClass());
//
//        assertEquals(1, field.getEntities().size());
//        assertEquals(1, field.getEntityGroup(5, 4).size());
    }

    @DisplayName("3 sup | 3 work")
    @Test
    public void testThreeSupThreeWork() throws InterruptedException {
        Field field = new Field();
        int n = 3;
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(n);

        EntityWorker worker = new EntityWorker(field, queue, (x)  -> {
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

//        Entity entity = field.getEntityGroup(5, 4).getEntityList().get(0);
//
//        assertNotNull(entity);
//        assertTrue(entity.isAlive());
//        assertEquals(270.0, entity.getWeight());
//        assertEquals(Deer.class, entity.getClass());
//
//        assertEquals(1, field.getEntities().size());
//        assertEquals(1, field.getEntityGroup(5, 4).size());
    }
}
