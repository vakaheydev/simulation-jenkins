package contigous;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.entity.Point;
import simulation.entity.animal.Plant;
import simulation.entity.animal.herbivore.Caterpillar;
import simulation.util.PointUtil;

@Slf4j
public class PointUtilTest {
    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @Test
    void testPointUtilShouldGenerateRandomPoint() {
        Point point = PointUtil.getRandomPoint(field, Plant.class);
        log.debug(point.toString());
        Plant plant = new Plant(field, point.x(), point.y());

        Assertions.assertEquals(1, field.plantCnt());
        Assertions.assertEquals(1, field.getEntityGroup(point).entityCnt(Plant.class));
        Assertions.assertEquals(point, plant.getPoint());

        log.info(field.toString());
    }

    @DisplayName("A lot of random points")
    @Test
    void testPointUtilShouldGenerateRandomPoints() {
        for (int i = 0; i < 10000; i++) {
            Point point = PointUtil.getRandomPoint(field, Plant.class);
            Caterpillar caterpillar = new Caterpillar(field, point.x(), point.y());
        }

        Assertions.assertEquals(10000, field.entityCnt());
        Assertions.assertEquals(10000, field.animalCnt());

        log.info(field.toString());
    }
}
