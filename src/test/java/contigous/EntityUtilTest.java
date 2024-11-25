package contigous;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulation.Field;
import simulation.entity.Point;
import simulation.entity.animal.Plant;
import simulation.entity.animal.predator.Bear;
import simulation.entity.animal.predator.Wolf;
import simulation.util.EntityUtil;

@Slf4j
public class EntityUtilTest {
    Field field;

    @BeforeEach
    void setUp() {
        field = new Field();
    }

    @Test
    void testFieldEntityGroupShouldCountEntities() {
        EntityUtil.addEntities(field, new Point(0, 0), Plant.class, 20);
        Field.EntityGroup entityGroup = field.getEntityGroup(0, 0);
        Assertions.assertEquals(20, entityGroup.entityCnt(Plant.class));
    }

    @Test
    void testFieldEntityGroupShouldCountEntities2() {
        EntityUtil.addEntities(field, new Point(0, 0), Plant.class, 91);
        EntityUtil.addEntities(field, new Point(0, 1), Wolf.class, 30);
        Field.EntityGroup entityGroup = field.getEntityGroup(0, 0);
        Field.EntityGroup entityGroup2 = field.getEntityGroup(0, 1);
        Assertions.assertEquals(91, entityGroup.entityCnt(Plant.class));
        Assertions.assertEquals(30, entityGroup2.entityCnt(Wolf.class));
    }

    @Test
    void testFieldEntityGroupShouldThrowIllegalArgumentEx() {
        Assertions.assertThrows(RuntimeException.class, () -> EntityUtil.addEntities(field,
                new Point(0, 0), Wolf.class, 31));
        Field.EntityGroup entityGroup = field.getEntityGroup(0, 0);
    }

    @Test
    void testShouldCreateEntitiesInRandomPoints() {
        EntityUtil.addEntities(field, Bear.class, 5);

        Assertions.assertEquals(5, field.entityCnt());
        Assertions.assertEquals(5, field.animalCnt());

        log.info(field.toString());
    }

    @Test
    void testShouldCreateRandomEntitiesInRandomPoints() {
        EntityUtil.addEntities(field, 1000);

        Assertions.assertEquals(1000, field.entityCnt());
        Assertions.assertEquals(field.entityCnt() - field.plantCnt(), field.animalCnt());
        Assertions.assertEquals(field.entityCnt() - field.animalCnt(), field.plantCnt());

        log.info(field.toString());
    }
}
