package simulation.concurrent;

import lombok.extern.slf4j.Slf4j;
import simulation.Field;
import simulation.entity.Entity;
import simulation.util.EntityUtil;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class EntitySupplier extends AbstractEntitySupplier {
    public EntitySupplier(Field field, BlockingQueue<Entity> queue) {
        super(field, queue);
    }

    @Override
    public Entity supply() {
        List<Entity> entities;

        try {
            entities = field.getEntities();
        } catch (NullPointerException exception) {
            log.error("Error: null pointer");
            throw new IllegalStateException();
        }

        log.debug("entityList: {}", entities);

        int size = entities.size();

        if (size < 10) {
            Entity entity = EntityUtil.addEntity(field);
            return entity;
        }

        int idx = new Random().nextInt(size);
        return entities.get(idx);
    }
}
