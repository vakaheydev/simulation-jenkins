package simulation.util;

import simulation.animal.Animal;

import static simulation.animal.Animal.Direction;

public final class AnimalMoveUtil {
    private AnimalMoveUtil() {
    }

    public static void randomMove(Animal animal, int moveCnt) {
        checkSpeed(animal, moveCnt);

        for (int i = 0; i < moveCnt; i++) {
            randomMove(animal);
        }
    }

    public static void randomMove(Animal animal) {
        Direction rndDirection = PointUtil.getRandomDirection(animal.getPoint());
        animal.move(rndDirection);
    }

    private static void checkSpeed(Animal animal, int moveCnt) {
        if (animal.speed() < moveCnt) {
            throw new IllegalArgumentException(String.format("Specified moves amount[%d] is higher than animal " +
                    "speed[%d]", moveCnt, animal.speed()));
        }
    }
}
