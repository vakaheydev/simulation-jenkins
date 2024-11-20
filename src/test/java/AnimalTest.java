import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.entity.Point;
import simulation.entity.animal.Animal;
import simulation.entity.animal.Animal.Direction;
import simulation.entity.animal.predator.Eagle;
import simulation.entity.animal.predator.Wolf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static simulation.entity.animal.Animal.Direction.*;

@Slf4j
public class AnimalTest {
    private Field field;

    static class NotHungryAnimal extends Eagle {
        public NotHungryAnimal(Field field, int x, int y) {
            super(field, x, y);
        }

        @Override
        public double loseWeight() {
            return this.weight;
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

    public void assertMoves(Animal animal, Direction... directions) {
        for (var direction : directions) {
            assertMove(animal, direction);
        }
    }

    public void assertMove(Animal animal, Direction direction) {
        Point expectedPoint = getDirectionPoint(animal.getPoint(), direction);
        animal.move(direction);
        assertEquals(expectedPoint, animal.getPoint());
        assertEquals(expectedPoint, field.getEntityPoint(animal));
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
