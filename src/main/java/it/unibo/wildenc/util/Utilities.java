package it.unibo.wildenc.util;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.dataloaders.StatLoader;

/**
 * Class containing utilities for the project.
 */
public final class Utilities {
    private static final Random RAND = new Random();

    private Utilities() { }

    /**
     * Picks a random value in a {@link List} of values.
     * 
     * @param <T> the type of the values
     * @param values the {@link List} of values
     * @return a random value picked from the list.
     */
    public static <T> T pickRandom(final List<T> values) {
        return values.get(RAND.nextInt(values.size()));
    }

    /**
     * Gets a random enemy name, loaded with {@link StatLoader}, based
     * off a specified rarity.
     * 
     * @param rarity the rarity of the enemy to be picked.
     * @return a {@link String} containing the name of the picked enemy.
     */
    public static String randomNameForRarity(final String rarity) {
        return pickRandom(StatLoader.getInstance().getAllEnemyData().stream()
            .filter(e -> e.rarity().equals(rarity.toLowerCase(Locale.ITALIAN)))
            .map(e -> e.entityName())
            .toList()).toLowerCase();
    }

    /**
     * Utility for normalizing {@link Vector2d}s. This was made to avoid normalizing
     * (0, 0) vectors to (NaN, NaN).
     * 
     * @param toConvert the {@link Vector2dc} to be normalized. 
     * @return the normalized vector.
     */
    public static Vector2dc normalizeVector(final Vector2dc toConvert) {
        final var norm = new Vector2d(toConvert).normalize();
        return norm.isFinite() ? norm : new Vector2d(0, 0);
    }

    public static String capitalize(final String toCap) {
        return toCap.substring(0, 1).toUpperCase() + toCap.substring(1);
    }
}
