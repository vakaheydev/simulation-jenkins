package simulation.entity;

import java.util.HashMap;
import java.util.Map;

public class EntityConfig {
    private EntityConfig() {
    }

    private static final Map<Class<? extends Entity>, Map<Class<? extends Entity>, Double>> chances = new HashMap<>();

    public static void addChance(Class<? extends Entity> predatorClass, Class<? extends Entity> victimClass,
                                 double chance) {
        chances.computeIfAbsent(predatorClass, k -> new HashMap<>()).put(victimClass, chance);
    }

    public static double getChanceToEat(Class<? extends Entity> predatorClass, Class<? extends Entity> victimClass) {
        var chancesMap = chances.get(predatorClass);

        if (chancesMap == null) {
            throw new IllegalArgumentException(String.format("Specified predator %s has no chances",
                    predatorClass.getSimpleName()));
        }

        return chancesMap.getOrDefault(victimClass, -1.0);
    }
}
