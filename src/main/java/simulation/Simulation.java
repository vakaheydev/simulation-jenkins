package simulation;

import lombok.extern.slf4j.Slf4j;
import simulation.entity.Entity;
import simulation.entity.animal.Animal;
import simulation.util.AnimalMoveUtil;
import simulation.util.EntityUtil;

import java.util.Set;

@Slf4j
public class Simulation {
    public void start() {
        Field field = new Field();
        EntityUtil.addEntities(field, 100_000);
        System.out.println(field);
        Set<Entity> entities = field.getEntities();
        for (Entity entity : entities) {
            if (entity instanceof Animal animal) {
                AnimalMoveUtil.randomMove(field, animal);
            }
        }
//        System.out.println(field.shortToString());
    }
}
