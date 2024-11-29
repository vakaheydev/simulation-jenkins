package simulation.util;

import simulation.Field;
import simulation.entity.animal.Animal;

import static simulation.entity.animal.Animal.Direction;

public final class AnimalMoveUtil {
    private AnimalMoveUtil() {
    }

    public static void randomMove(Field field, Animal animal, int moveCnt) {
        checkSpeed(animal, moveCnt);

        for (int i = 0; i < moveCnt; i++) {
            randomMove(field, animal);
        }
    }

    public static void randomMove(Field field, Animal animal) {
        ValidationUtil.checkEntity(animal);
        Direction rndDirection = PointUtil.getRandomDirection(field, animal.getPoint());
        animal.move(rndDirection);
    }

    private static void checkSpeed(Animal animal, int moveCnt) {
        if (animal.speed() < moveCnt) {
            throw new IllegalArgumentException(String.format("Specified moves amount[%d] is higher than animal " +
                    "speed[%d]", moveCnt, animal.speed()));
        }
    }
}
