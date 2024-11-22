import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.entity.Entity;
import simulation.entity.Point;
import simulation.entity.animal.Animal;
import simulation.entity.animal.Plant;
import simulation.entity.animal.herbivore.*;
import simulation.entity.animal.predator.Bear;
import simulation.entity.animal.predator.Boa;
import simulation.entity.animal.predator.Fox;
import simulation.exception.AnimalSpeedLimitExceededException;
import simulation.exception.DeadEntityException;
import simulation.util.AnimalMoveUtil;
import simulation.util.EntityUtil;

import static org.junit.jupiter.api.Assertions.*;
import static simulation.entity.animal.Animal.Direction.*;

@Slf4j
public class SimulationTest {
    private Field field = new Field();

    @BeforeEach
    public void setup() {
        field.clear();
    }

    @Test
    @DisplayName("Duck multiply 1X | eat 2X | dies from hunger")
    public void testDuckAndCaterpillarShouldActCorrectly() {
        Duck duck = new Duck(field, 1, 2);
        Duck duck2 = new Duck(field, 2, 2);
        Caterpillar caterpillar = new Caterpillar(field, 1, 2);
        Caterpillar caterpillar2 = new Caterpillar(field, 5, 4);
        Caterpillar caterpillar3 = new Caterpillar(field, 5, 3);

        log.info("duck1: {}", duck.hashCode());
        log.info("duck2: {}", duck2.hashCode());

        assertNotEquals(duck.hashCode(), duck2.hashCode());

        log.info(field.toString());
        duck2.move(LEFT, RIGHT, DOWN, RIGHT);
        duck2.move(RIGHT, RIGHT, RIGHT);
        duck2.move(RIGHT, RIGHT, RIGHT, UP);

        log.info("--- End of simulation ---");
        log.info(field.toString());

        assertEquals(3, field.entityCnt());
        assertEquals(3, field.animalCnt());
        assertEquals(0, field.plantCnt());

        assertEquals(new Point(1, 2), duck.getPoint());
        assertEquals(new Point(5, 4), caterpillar2.getPoint());

        assertFalse(caterpillar3.isAlive());
        assertFalse(caterpillar.isAlive());
        assertFalse(duck2.isAlive());
        assertTrue(duck.isAlive());

        assertEquals(2, field.getEntityGroup(1, 2).size());
        assertEquals(1, field.getEntityGroup(5, 4).size());
        assertEquals(0, field.plantCnt());
    }

    @DisplayName("Herbivore should eat all plants in location")
    @Test
    public void testHerbivoreShouldEatAllPlants() {
        EntityUtil.addEntities(field, new Point(0, 0), Plant.class, 200);
        Buffalo buffalo = new Buffalo(field, 1, 0);

        assertEquals(201, field.entityCnt());
        assertEquals(1, field.animalCnt());
        assertEquals(200, field.plantCnt());

        buffalo.move(LEFT);

        assertEquals(1, field.entityCnt());
        assertEquals(1, field.animalCnt());
        assertEquals(0, field.plantCnt());

        assertEquals(830.0, buffalo.getWeight());

        log.info(field.toString());
    }

    @DisplayName("Rabbit eats and multiply")
    @Test
    public void testRabbitShouldEatAndMultiply() {
        Rabbit rabbit = new Rabbit(field, 5, 5);
        Rabbit rabbit2 = new Rabbit(field, 6, 5);
        Plant plant = new Plant(field, 6, 5);
        Plant plant2 = new Plant(field, 6, 5);

        assertEquals(4, field.entityCnt());
        assertEquals(2, field.animalCnt());
        assertEquals(2, field.plantCnt());

        rabbit.move(RIGHT);

        assertEquals(3, field.entityCnt());
        assertEquals(3, field.animalCnt());
        assertEquals(0, field.plantCnt());

        log.info(field.toString());

    }

    @DisplayName("Predator should eat herbivore")
    @Test
    public void testPredatorShouldEatHerbivore() {
        Bear bear = new Bear(field, 0, 0);
        Mouse mouse = new Mouse(field, 0, 1);
        Mouse mouse2 = new Mouse(field, 0, 1);
        Mouse mouse3 = new Mouse(field, 0, 1);
        Mouse mouse4 = new Mouse(field, 0, 1);
        Mouse mouse5 = new Mouse(field, 0, 1);
        Mouse mouse6 = new Mouse(field, 0, 1);
        Mouse mouse7 = new Mouse(field, 0, 1);
        Mouse mouse8 = new Mouse(field, 0, 1);
        Mouse mouse9 = new Mouse(field, 0, 1);
        Mouse mouse10 = new Mouse(field, 0, 1);

        assertEquals(11, field.entityCnt());
        assertEquals(11, field.animalCnt());
        assertEquals(0, field.plantCnt());

        bear.move(DOWN);

        assertTrue(field.animalCnt() < 11);
        assertTrue(field.entityCnt() < 11);
        assertEquals(0, field.plantCnt());

        log.info(field.toString());
    }

    @DisplayName("Herbivore should eat plant")
    @Test
    public void testHerbivoreShouldEatPlant() {
        Plant plant = new Plant(field, 0, 0);
        Deer deer = new Deer(field, 1, 0);

        assertEquals(2, field.entityCnt());
        assertEquals(1, field.animalCnt());
        assertEquals(1, field.plantCnt());

        deer.move(LEFT);

        double initWeight = deer.initialWeight();

        assertEquals(initWeight - (initWeight * 0.1) + 1, deer.getWeight());

        assertEquals(1, field.entityCnt());
        assertEquals(1, field.animalCnt());
        assertEquals(0, field.plantCnt());

        log.info(field.toString());
    }

    @DisplayName("Animal shouldn't move further than it can")
    @Test
    public void testAnimalShouldnNotMoveMoreThanSpeed() {
        Fox fox = new Fox(field, 0, 0);

        assertEquals(1, field.entityCnt());
        assertEquals(1, field.animalCnt());
        assertEquals(0, field.plantCnt());

        assertThrows(AnimalSpeedLimitExceededException.class, () -> fox.move(
                UP, UP, RIGHT));
    }

    @DisplayName("Animal should die from hunger")
    @Test
    public void testAnimalShouldDieFromHunger() {
        Goat goat = new Goat(field, 5, 0);

        assertEquals(1, field.entityCnt());
        assertEquals(1, field.animalCnt());
        assertEquals(0, field.plantCnt());

        goat.move(LEFT);

        assertEquals(new Point(4, 0), goat.getPoint());

        goat.move(DOWN);

        assertEquals(new Point(4, 1), goat.getPoint());

        goat.move(UP);

        assertEquals(new Point(4, 0), goat.getPoint());

        goat.move(RIGHT, DOWN, DOWN);

        assertEquals(new Point(5, 2), goat.getPoint());

        goat.move(UP, DOWN);

        assertEquals(new Point(5, 2), goat.getPoint());

        goat.move(DOWN, UP);

        assertEquals(0, field.entityCnt());
        assertEquals(0, field.animalCnt());
        assertEquals(0, field.plantCnt());
        assertFalse(goat.isAlive());
    }

    @DisplayName("Animal should move")
    @Test
    public void testAnimalShouldMove() {
        Boa boa = new Boa(field, 0, 0);
        Goat goat = new Goat(field, 5, 0);

        assertEquals(2, field.entityCnt());
        assertEquals(2, field.animalCnt());
        assertEquals(0, field.plantCnt());

        goat.move(LEFT);

        assertEquals(new Point(4, 0), goat.getPoint());

        goat.move(DOWN);

        assertEquals(new Point(4, 1), goat.getPoint());

        goat.move(UP);

        assertEquals(new Point(4, 0), goat.getPoint());

        goat.move(RIGHT, DOWN, DOWN);

        assertEquals(new Point(5, 2), goat.getPoint());

        goat.move(UP, DOWN);

        assertEquals(new Point(5, 2), goat.getPoint());

        EntityUtil.addEntities(field, new Point(5, 2), Plant.class, 100);

        goat.move(DOWN, UP);

        assertEquals(new Point(5, 2), goat.getPoint());

        goat.move(LEFT, LEFT, LEFT);

        assertEquals(new Point(2, 2), goat.getPoint());

        goat.move(LEFT, RIGHT);

        assertEquals(new Point(2, 2), goat.getPoint());

        goat.move(RIGHT, LEFT);

        assertEquals(new Point(2, 2), goat.getPoint());

        goat.move(LEFT, LEFT);
        goat.move(UP, UP);

        assertEquals(new Point(0, 0), goat.getPoint());

        Field.EntityGroup group = field.getEntityGroup(0, 0);

        assertEquals(2, group.size());
    }

    @DisplayName("Stress test of simulation ")
    @Test
    public void start() {
        int entitiesNumber = 25000;
        int cnt = 3;
        while (cnt > 0) {
            Field field = new Field();
            EntityUtil.addEntities(field, entitiesNumber);
            System.out.println(field.shortToString());
            var entities = field.getEntities();
            for (Entity entity : entities) {
                tryToMove(field, entity.toAnimal());
            }
            System.out.println(field.shortToString());
            System.out.println("\n\n--------------------------");
            cnt--;
        }
    }

    public void tryToMove(Field field, Animal animal) {
        if (animal == null) {
            return;
        }

        try {
            AnimalMoveUtil.randomMove(field, animal);
            checkCounters(field);
        } catch (DeadEntityException | AnimalSpeedLimitExceededException ignored) {

        }
    }

    public void checkCounters(Field field) {
        var entities = field.getEntities();
        int animalCnt = 0;
        int plantCnt = 0;

        for (Entity entity : entities) {
            if (entity.isAnimal()) {
                animalCnt++;
            } else if (entity.isPlant()) {
                plantCnt++;
            }
        }

        assertEquals(animalCnt, field.animalCnt());
        assertEquals(plantCnt, field.plantCnt());
        assertEquals(plantCnt + animalCnt, field.entityCnt());
    }
}
