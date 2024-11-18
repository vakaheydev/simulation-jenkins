import simulation.Point;
import simulation.animal.Plant;
import simulation.animal.herbivore.Caterpillar;
import simulation.util.PointUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.Field;

public class PointUtilTest {
    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @Test
    void testPointUtilShouldGenerateRandomPoint() {
        Point point = PointUtil.getRandomPoint(field, Plant.class);
        System.out.println(point);
        Plant plant = new Plant(field, point.x(), point.y());

        Assertions.assertEquals(1, field.plantCnt());
        Assertions.assertEquals(1, field.getEntityGroup(point).entityCnt(Plant.class));
        Assertions.assertEquals(point, field.getEntityPoint(plant));
        System.out.println(field);
    }

    @Test
    void testPointUtilShouldGenerateRandomPoints() {
        for (int i = 0; i < 100_000; i++) {
            Point point = PointUtil.getRandomPoint(field, Plant.class);
            Caterpillar caterpillar = new Caterpillar(field, point.x(), point.y());
        }

        Assertions.assertEquals(100_000, field.entityCnt());
        Assertions.assertEquals(100_000, field.animalCnt());

        System.out.println(field);
    }
}
