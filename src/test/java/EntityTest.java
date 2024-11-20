import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.entity.Entity;
import simulation.Field;
import simulation.entity.Point;
import simulation.entity.animal.herbivore.Buffalo;
import simulation.entity.animal.herbivore.Caterpillar;
import simulation.entity.animal.herbivore.Duck;
import simulation.entity.animal.herbivore.Mouse;
import simulation.util.EntityUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static simulation.entity.animal.Animal.Direction.*;

public class EntityTest {
    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @DisplayName("Different hash codes")
    @Test
    public void testEntityShouldHaveDifferentHashCode() {
        EntityUtil.addEntities(field, new Point(0, 0), Duck.class, 199);
        EntityUtil.addEntities(field, new Point(0, 1), Mouse.class, 499);
        EntityUtil.addEntities(field, new Point(0, 2), Caterpillar.class, 999);

        checkEntityGroup(field.getEntityGroup(0, 0), Duck.class);
        checkEntityGroup(field.getEntityGroup(0, 1), Mouse.class);
        checkEntityGroup(field.getEntityGroup(0, 2), Caterpillar.class);
    }

    public void checkEntityGroup(Field.EntityGroup group, Class<? extends Entity> clazz) {
        List<Entity> entities = new ArrayList<>(group.getEntities());

        Entity entity = EntityUtil.newInstance(field, clazz);


        for (int i = 0; i < entities.size(); i++) {
            int hashCode = entities.get(i).hashCode();

            for (int j = 0; j < entities.size(); j++) {
                if (i == j) {
                    continue;
                }

                int otherHashCode = entities.get(j).hashCode();
                assertNotEquals(hashCode, otherHashCode);
            }
        }
    }

    @DisplayName("Equals hash codes")
    @Test
    public void shouldHaveSameHashCode() {
        Buffalo buffalo = new Buffalo(field, 5, 5);
        int hashCode = buffalo.hashCode();

        buffalo.move(UP);
        assertEquals(hashCode, buffalo.hashCode());

        buffalo.move(RIGHT);
        assertEquals(hashCode, buffalo.hashCode());

        buffalo.move(UP, DOWN);
        assertEquals(hashCode, buffalo.hashCode());

        buffalo.move(LEFT);
        assertEquals(hashCode, buffalo.hashCode());
    }
}
