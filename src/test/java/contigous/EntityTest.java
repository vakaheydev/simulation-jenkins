package contigous;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.entity.animal.Plant;
import simulation.entity.animal.herbivore.*;
import simulation.exception.DeadEntityException;
import simulation.util.EntityUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
        EntityUtil.addEntities(field, new Point(12, 99), Caterpillar.class, 999);

        checkDifferentHashCodeOnEntityGroup(field.getEntityGroup(0, 0));
        checkDifferentHashCodeOnEntityGroup(field.getEntityGroup(0, 1));
        checkDifferentHashCodeOnEntityGroup(field.getEntityGroup(0, 2));
        checkDifferentHashCodeOnEntityGroup(field.getEntityGroup(12, 99));
    }

    @DisplayName("Different hash codes after multiplying")
    @Test
    public void testEntityShouldHaveDifferentHashCodeAfterMultiplying() {
        EntityUtil.addEntities(field, new Point(5, 5), Deer.class, 10);
        EntityUtil.addEntities(field, new Point(5, 4), Deer.class, 1);

        field.getEntityGroup(5, 4)
                .getEntityGroupSet()
                .get(0)
                .toAnimal()
                .move(UP);

        checkDifferentHashCodeOnEntityGroup(field.getEntityGroup(5, 5));
    }

    public static void checkDifferentHashCodeOnEntityGroup(Field.EntityGroup group) {
        List<Entity> entities = new ArrayList<>(group.getEntityGroupSet());

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
    public void testShouldHaveSameHashCode() {
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

    @DisplayName("Entity should die")
    @Test
    public void testEntityShouldDie() {
        Plant plant = new Plant(field, 15, 22);
        Deer deer = new Deer(field, 14, 22);
        deer.move(RIGHT);

        assertFalse(plant.isAlive());
        assertNull(plant.getPoint());

        assertTrue(deer.isAlive());
        assertNotNull(deer.getPoint());

        deer.die();

        assertThrows(DeadEntityException.class, plant::getWeight);
        assertThrows(DeadEntityException.class, deer::getWeight);
        assertThrows(DeadEntityException.class, () -> deer.move(RIGHT));
        assertThrows(DeadEntityException.class, () -> deer.eat(plant));
        assertThrows(DeadEntityException.class, () -> deer.multiplyWith(deer));
    }
}
