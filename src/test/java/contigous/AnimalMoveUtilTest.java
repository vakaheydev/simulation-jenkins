package contigous;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.entity.Point;
import simulation.entity.animal.Animal;
import simulation.entity.animal.herbivore.Caterpillar;
import simulation.exception.AnimalSpeedLimitExceededException;
import simulation.util.AnimalMoveUtil;
import util.TestAnimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static simulation.entity.animal.Animal.Direction.*;

@Slf4j
public class AnimalMoveUtilTest {
    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @Test
    void testAnimalShouldMoveRandomly() {
        Animal animal = new TestAnimal(field, 0, 0);
        for (int i = 0; i < 25_000; i++) {
            AnimalMoveUtil.randomMove(field, animal);
        }

        log.debug(animal.toString());
        log.info(field.toString());
    }

    @Test
    void testAnimalShouldMoveRandomlyNotUpAndNotLeft() {
        Animal animal = new TestAnimal(field, 0, 0);
        for (int i = 0; i < 1000; i++) {
            AnimalMoveUtil.randomMove(field, animal);
            if (animal.getPoint().equals(new Point(0, 1))) {
                animal.move(UP);
            } else if (animal.getPoint().equals(new Point(1, 0))) {
                animal.move(LEFT);
            }
        }

        log.debug(animal.toString());
        log.info(field.toString());
    }

    @Test
    void testAnimalShouldMoveRandomlyNotLeftAndDown() {
        int height = field.height - 1;
        Animal animal = new TestAnimal(field, 0, height);
        for (int i = 0; i < 1000; i++) {
            AnimalMoveUtil.randomMove(field, animal);
            if (animal.getPoint().equals(new Point(0, height - 1))) {
                animal.move(DOWN);
            } else if (animal.getPoint().equals(new Point(1, height))) {
                animal.move(LEFT);
            }
        }

        log.debug(animal.toString());
        log.info(field.toString());
    }

    @Test
    void testAnimalShouldMoveRandomlyNotRightAndDown() {
        int height = field.height - 1;
        int width = field.width - 1;
        Animal animal = new TestAnimal(field, width, height);
        for (int i = 0; i < 1000; i++) {
            AnimalMoveUtil.randomMove(field, animal);
            if (animal.getPoint().equals(new Point(width, height - 1))) {
                animal.move(DOWN);
            } else if (animal.getPoint().equals(new Point(width - 1, height))) {
                animal.move(RIGHT);
            }
        }

        log.debug(animal.toString());
        log.info(field.toString());
    }

    @Test
    void testAnimalShouldMoveRandomlyNotUpAndRight() {
        int width = field.width - 1;
        Animal animal = new TestAnimal(field, width, 0);
        for (int i = 0; i < 1000; i++) {
            AnimalMoveUtil.randomMove(field, animal);
            if (animal.getPoint().equals(new Point(width, 1))) {
                animal.move(UP);
            } else if (animal.getPoint().equals(new Point(width - 1, 0))) {
                animal.move(RIGHT);
            }
        }

        log.debug(animal.toString());
        log.info(field.toString());
    }

    @Test
    void testAnimalWithZeroSpeedMove() {
        Animal animal = new Caterpillar(field, 0, 0);
        assertThrows(AnimalSpeedLimitExceededException.class, () -> AnimalMoveUtil.randomMove(field, animal));
    }
}
