package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import simulation.Field;
import simulation.entity.Entity;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class ConcurrentSimulation {
    private static final Marker IMPORTANT_MARKER = MarkerFactory.getMarker("IMPORTANT");
    public static void main(String[] args) {
        start();
    }
    public static void start() {
        Field field = new Field();

        log.info(IMPORTANT_MARKER, "Started concurrent simulation");

        BlockingQueue<Entity> queue = ConcurrentConfig.getBlockingQueue();

        Thread worker = new Thread(new EntityWorker(field, queue), "worker_I");
        Thread worker2 = new Thread(new EntityWorker(field, queue), "worker_II");
        Thread worker3 = new Thread(new EntityWorker(field, queue), "worker_III");
        worker.start();
        worker2.start();
        worker3.start();

        Thread supplier = new Thread(new EntitySupplier(field, queue), "supplier");
        supplier.start();

        while (true) {
            log.info(IMPORTANT_MARKER, field.toString());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

//        try {
//            worker.join();
//            worker2.join();
//            worker3.join();
//            supplier.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

//        log.info("Ended concurrent simulation");
    }
}
