package concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.concurrent.EntitySupplier;
import simulation.concurrent.EntityWorker;
import simulation.entity.Entity;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Deer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class ConcurrentSimulationTest {
    @DisplayName("One entity")
    @Test
    public void testOneEntity() throws InterruptedException {
        Field field = new Field();
        BlockingQueue<Entity> queue = new ArrayBlockingQueue<>(1);

        EntityWorker worker = new EntityWorker(field, queue, (x)  -> {
            Animal animal = x.toAnimal();
            log.debug("Got an animal: {}", animal);
            assertNotNull(animal);

            animal.move(Animal.Direction.UP);
        },
                (x) -> x.loopCounter.get() < 1);

        EntitySupplier supplier = new EntitySupplier(field, queue,
                () -> new Deer(field, 5, 5),
                (x) -> x.loopCounter.get() < 1);

        Thread workerThread = new Thread(worker, "worker");
        Thread supplierThread = new Thread(supplier, "supplier");
        workerThread.start();
        supplierThread.start();

        workerThread.join();
        supplierThread.join();
    }
}
