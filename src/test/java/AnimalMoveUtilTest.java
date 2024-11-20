import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.Point;
import simulation.animal.Animal;
import simulation.animal.herbivore.Caterpillar;
import simulation.animal.herbivore.Herbivore;
import simulation.util.AnimalMoveUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static simulation.animal.Animal.Direction.*;

@Slf4j
public class AnimalMoveUtilTest {
    static class NotHungryAnimal extends Herbivore {
        public NotHungryAnimal(Field field, int x, int y) {
            super(field, x, y);
        }

        @Override
        public int speed() {
            return 1;
        }

        @Override
        public double neededFoodWeight() {
            return 0;
        }

        @Override
        public Animal createNewInstance(Field field, int x, int y) {
            return new NotHungryAnimal(field, x, y);
        }

        @Override
        public double initialWeight() {
            return 1;
        }

        @Override
        public int maxQuantity() {
            return 2_140_000_000;
        }

        @Override
        public double loseWeight() {
            return weight;
        }
    }

    private Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @Test
    void testAnimalShouldMoveRandomly() {
        Animal animal = new NotHungryAnimal(field, 0, 0);
        for (int i = 0; i < 25_000; i++) {
            AnimalMoveUtil.randomMove(field, animal);
        }

        log.debug(animal.toString());
        log.info(field.toString());
    }

    @Test
    void testAnimalShouldMoveRandomlyNotUpAndNotLeft() {
        Animal animal = new NotHungryAnimal(field, 0, 0);
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
        Animal animal = new NotHungryAnimal(field, 0, height);
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
        Animal animal = new NotHungryAnimal(field, width, height);
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
        Animal animal = new NotHungryAnimal(field, width, 0);
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
        AnimalMoveUtil.randomMove(field, animal);
        assertEquals(new Point(0, 0), field.getEntityPoint(animal));
        assertEquals(new Point(0, 0), animal.getPoint());
    }
}
