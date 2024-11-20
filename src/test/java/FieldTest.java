import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.Point;
import simulation.animal.Plant;
import simulation.animal.herbivore.Deer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static simulation.animal.Animal.Direction.UP;

public class FieldTest {
    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @Test
    void testShouldHaveEntitiesOnField() {
        Deer deer = new Deer(field, 0, 1);
        Plant plant = new Plant(field, 0, 0);

        assertEquals(new Point(0, 1), field.getEntityPoint(deer));
        assertEquals(new Point(0, 0), field.getEntityPoint(plant));

        deer.move(UP);

        assertEquals(new Point(0, 0), field.getEntityPoint(deer));
        assertNull(field.getEntityPoint(plant));
        assertEquals(1, field.entityCnt());
        assertEquals(1, field.animalCnt());
        assertEquals(0, field.plantCnt());
    }
}
