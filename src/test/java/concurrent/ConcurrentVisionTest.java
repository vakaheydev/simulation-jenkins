package concurrent;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.entity.Entity;
import simulation.util.AnimalMoveUtil;
import util.TestAnimal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
public class ConcurrentVisionTest {
    @DisplayName("Vision test")
    @Test
    public void testVision() {
        Field field = new Field();

        TestAnimal boa = new TestAnimal(field, 3, 3);

        Runnable pointChanger = () -> {
            while (true) {
                AnimalMoveUtil.randomMove(field, boa);
            }
        };

        Runnable worker = () -> {
            while (true) {
                List<Entity> entityList = field.getEntities();
                Entity entity = entityList.get(0);

                log.debug("Got an entity: {}", entity);

                assertEquals(1, entityList.size());
                assertEquals(entity.getPoint(), boa.getPoint());
                assertEquals(entity, boa);
            }
        };

        ExecutorService service = Executors.newFixedThreadPool(8);
        service.execute(pointChanger);
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            futures.add(service.submit(worker));
        }

        service.shutdown();

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof AssertionError) {
                    throw new RuntimeException("Assertion failed", e.getCause());
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
