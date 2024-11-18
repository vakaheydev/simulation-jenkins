package simulation.util;

import simulation.Entity;
import simulation.Field;
import simulation.Point;
import simulation.animal.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static simulation.animal.Animal.Direction.*;
import static simulation.animal.Animal.Direction.RIGHT;

public final class PointUtil {
    private static final Random rnd = new Random();
    private PointUtil() {};
    public static Animal.Direction getRandomDirection(Point point) {
        List<Animal.Direction> possibleDirections = getDirections(point);

        int rndIdx = new Random().nextInt(possibleDirections.size());
        return possibleDirections.get(rndIdx);
    }

    public static List<Animal.Direction> getDirections(Point point) {
        List<Animal.Direction> result = new ArrayList<>();

        if (point.y() > 0) {
            result.add(UP);
        }

        if (point.y() < Field.FIELD_HEIGHT - 1) {
            result.add(DOWN);
        }

        if (point.x() > 0) {
            result.add(LEFT);
        }

        if (point.x() < Field.FIELD_WIDTH - 1) {
            result.add(RIGHT);
        }

        return result;
    }

    public static Point getRandomPoint(Field field, Class<? extends Entity> clazz) {
        int maxQuantity = EntityUtil.getMaxQuantity(field, clazz);

        Point point = new Point(getRandomX(), getRandomY());

        while (true) {
            Field.EntityGroup group = field.getEntityGroup(point);
            if (group.entityCnt(clazz) < maxQuantity) {
                return point;
            }
            point = new Point(getRandomX(), getRandomY());
        }
    }

    private static int getRandomX() {
        return rnd.nextInt(Field.FIELD_WIDTH);
    }

    private static int getRandomY() {
        return rnd.nextInt(Field.FIELD_HEIGHT);
    }
}
