package simulation.util;

import simulation.Entity;
import simulation.Field;
import simulation.Point;
import simulation.animal.Animal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static simulation.animal.Animal.Direction.*;

public final class PointUtil {
    private static final Random rnd = new Random();

    private PointUtil() {
    }

    ;

    public static Animal.Direction getRandomDirection(Field field, Point point) {
        List<Animal.Direction> possibleDirections = getDirections(field, point);

        int rndIdx = new Random().nextInt(possibleDirections.size());
        return possibleDirections.get(rndIdx);
    }

    public static List<Animal.Direction> getDirections(Field field, Point point) {
        List<Animal.Direction> result = new ArrayList<>();

        if (point.y() > 0) {
            result.add(UP);
        }

        if (point.y() < field.height - 1) {
            result.add(DOWN);
        }

        if (point.x() > 0) {
            result.add(LEFT);
        }

        if (point.x() < field.width - 1) {
            result.add(RIGHT);
        }

        return result;
    }

    public static Point getRandomPoint(Field field, Class<? extends Entity> clazz) {
        int maxQuantity = EntityUtil.getMaxQuantity(field, clazz);

        Point point = new Point(getRandomX(field), getRandomY(field));

        while (true) {
            Field.EntityGroup group = field.getEntityGroup(point);
            if (group.entityCnt(clazz) < maxQuantity) {
                return point;
            }
            point = new Point(getRandomX(field), getRandomY(field));
        }
    }

    private static int getRandomX(Field field) {
        return rnd.nextInt(field.width);
    }

    private static int getRandomY(Field field) {
        return rnd.nextInt(field.height);
    }
}
