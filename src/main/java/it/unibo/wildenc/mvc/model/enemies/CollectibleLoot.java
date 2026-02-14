package it.unibo.wildenc.mvc.model.enemies;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import it.unibo.wildenc.mvc.model.Collectible;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.map.objects.ExperienceGem;
import it.unibo.wildenc.mvc.model.map.objects.HealthPotion;
import it.unibo.wildenc.mvc.model.map.objects.MoneyCoin;

/**
 * Utility class for generating enemies loot drop.
 */
public final class CollectibleLoot {
    private static final Random RANDOM = new Random();
    private static final int VALUE_COLLECTIBLE = 34;
    private static final int RANGE_PROBABILITY = 100;

    private CollectibleLoot() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Generates an experience gem loot.
     * 
     * @return a function that creates an Experience gem at the enemy's position.
     */
    public static Function<MapObject, Optional<Collectible>> experienceLoot() {
        return e -> Optional.of(new ExperienceGem(e.getPosition(), VALUE_COLLECTIBLE));
    }

    /**
     * Generates a money coin loot.
     * 
     * @return a function that creates a money coin at the enemy's position.
     */
    public static Function<MapObject, Optional<Collectible>> coinLoot() {
        return e -> Optional.of(new MoneyCoin(e.getPosition(), VALUE_COLLECTIBLE));
    }

    /**
     * Generates a Health potion loot.
     * 
     * @return a function that creates a health potion at the enemy's position.
     */
    public static Function<MapObject, Optional<Collectible>> healthLoot() {
        return e -> Optional.of(new HealthPotion(e.getPosition(), VALUE_COLLECTIBLE));
    }

    /**
     * Wraps a loot function with a percentage chance of success.
     * 
     * @param loot the original loot function
     * @param percent probability of dropping the loot
     * @return a function that return the loot if the probability check passes
     */
    public static Function<MapObject, Optional<Collectible>> percentageLoot(
        final Function<MapObject, Optional<Collectible>> loot, 
        final double percent) {
        return hasPercentageHit(percent) ? loot : e -> Optional.empty();
    }

    private static boolean hasPercentageHit(final double percent) {
        return RANDOM.nextInt(RANGE_PROBABILITY) <= percent * RANGE_PROBABILITY;
    }

}
