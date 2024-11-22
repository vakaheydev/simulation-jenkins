import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.Field.EntityGroup;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.entity.animal.Animal;
import simulation.entity.animal.Animal.Direction;
import simulation.entity.animal.Plant;
import simulation.entity.animal.predator.Bear;
import simulation.entity.animal.predator.Eagle;
import simulation.entity.animal.predator.Wolf;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static simulation.entity.animal.Animal.Direction.*;

@Slf4j
public class AnimalTest {
    private Field field;

    static class NotHungryAnimal extends Eagle {
        public NotHungryAnimal(Field field, int x, int y) {
            super(field, x, y);
        }

        @Override
        public void loseWeight() {
        }
    }

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @DisplayName("Animal move")
    @Test
    public void shouldMove() {
        var animal = new NotHungryAnimal(field, 5, 5);
        assertMove(animal, UP);
        assertMove(animal, DOWN);
        assertMove(animal, LEFT);
        assertMove(animal, RIGHT);

        assertMoves(animal, UP, UP);
        assertMoves(animal, DOWN, DOWN);
        assertMoves(animal, LEFT, LEFT);
        assertMoves(animal, RIGHT, RIGHT);

        assertMoves(animal, UP, RIGHT);
        assertMoves(animal, UP, LEFT);
        assertMoves(animal, DOWN, LEFT);
        assertMoves(animal, DOWN, RIGHT);
        assertMoves(animal, RIGHT, DOWN);
        assertMoves(animal, RIGHT, UP);
        assertMoves(animal, LEFT, DOWN);
        assertMoves(animal, LEFT, UP);
    }

    @DisplayName("Animal multiply")
    @Test
    public void testAnimalsShouldMultiply() {
        Wolf wolf = new Wolf(field, 0, 0);
        Wolf wolf3 = new Wolf(field, 0, 0);

        assertFalse(wolf.hashCode() == wolf3.hashCode());

        Wolf wolf2 = new Wolf(field, 1, 0);

        log.info(String.valueOf(wolf.hashCode()));
        log.info(String.valueOf(wolf2.hashCode()));
        log.info(String.valueOf(wolf3.hashCode()));

        assertEquals(3, field.entityCnt());
        assertEquals(3, field.animalCnt());
        assertEquals(0, field.plantCnt());

        wolf2.move(LEFT);

        log.info(field.toString());

        assertEquals(5, field.entityCnt());
        assertEquals(5, field.animalCnt());
        assertEquals(0, field.plantCnt());
    }

    @DisplayName("Animal move and multiply")
    @Test
    public void animalShouldMoveAndMultiply() {
        Wolf wolf = new Wolf(field, 5, 5);
        Bear bear = new Bear(field, 5, 5);
        Bear bear2 = new Bear(field, 5, 5);
        Bear bear3 = new Bear(field, 5, 5);
        Plant plant = new Plant(field, 5, 5);

        Bear bear4 = new Bear(field, 5, 4);


        assertEquals(6, field.entityCnt());
        assertEquals(5, field.animalCnt());
        assertEquals(1, field.plantCnt());

        bear4.move(DOWN);

        assertEquals(7, field.entityCnt());
        assertEquals(6, field.animalCnt());
        assertEquals(1, field.plantCnt());

        Point point = new Point(5, 5);
        assertEquals(point, bear.getPoint());
        assertEquals(point, bear2.getPoint());
        assertEquals(point, bear3.getPoint());
        assertEquals(point, bear4.getPoint());
        assertEquals(point, wolf.getPoint());
        assertEquals(point, plant.getPoint());

        EntityGroup entityGroup = field.getEntityGroup(5, 5);

        assertEquals(7, entityGroup.size());
        assertEquals(5, entityGroup.entityCnt(Bear.class));
        assertEquals(1, entityGroup.entityCnt(Wolf.class));
        assertEquals(1, entityGroup.entityCnt(Plant.class));

        EntityTest.checkDifferentHashCodeOnEntityGroup(entityGroup);

        List<Entity> entities = entityGroup.getEntityGroupSet();
    }

    public void assertMoves(Animal animal, Direction... directions) {
        for (var direction : directions) {
            assertMove(animal, direction);
        }
    }

    public void assertMove(Animal animal, Direction direction) {
        Point expectedPoint = getDirectionPoint(animal.getPoint(), direction);
        animal.move(direction);
        assertEquals(expectedPoint, animal.getPoint());
    }

    private Point getDirectionPoint(Point curPoint, Direction direction) {
        int x = curPoint.x();
        int y = curPoint.y();
        return switch (direction) {
            case RIGHT -> new Point(x + 1, y);
            case LEFT -> new Point(x - 1, y);
            case UP -> new Point(x, y - 1);
            case DOWN -> new Point(x, y + 1);
        };
    }
}
