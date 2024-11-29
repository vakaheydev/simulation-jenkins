package simulation.util;

import simulation.Field;
import simulation.entity.Entity;
import simulation.exception.DeadEntityException;
import simulation.exception.TooMuchEntitiesException;
import simulation.exception.ValidationException;

public class ValidationUtil {
    private ValidationUtil() {
    }

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            throw new ValidationException("This obj can't be null");
        }
    }

    public static void checkEntityAlive(Entity entity) {
        if (!entity.isAlive()) {
            throw new DeadEntityException(entity);
        }
    }

    public static void checkEntity(Entity entity) {
        checkNotNull(entity);
        checkEntityAlive(entity);
        checkNotNull(entity.getPoint());
    }

    public static void checkPoint(Field field, int x, int y) {
        checkNotNull(field);
        int height = field.height;
        int width = field.width;

        if (y < 0 || y > height - 1) {
            throw new ValidationException(String.format("y (%d) must be in bounds: [%d - %d]", y, 0,
                    height - 1));
        }
        if (x < 0 || x > width - 1) {
            throw new ValidationException(String.format("x (%d) must be in bounds: [%d - %d]", x, 0,
                    width - 1));
        }
    }

    public static void checkEntitiesQuantity(Field field, Entity entity, int x, int y) {
        checkEntity(entity);
        if (field.getEntityGroup(x, y).entityCnt(entity.getClass()) == entity.maxQuantity()) {
            throw new TooMuchEntitiesException(entity);
        }
    }
}
