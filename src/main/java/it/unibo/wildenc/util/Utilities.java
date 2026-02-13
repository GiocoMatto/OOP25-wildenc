package it.unibo.wildenc.util;

import java.util.List;
import java.util.Random;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.dataloaders.StatLoader;

public class Utilities {
    private static final Random RAND = new Random();

    public static <T> T pickRandom(final List<T> values) {
        return values.get(RAND.nextInt(values.size()));
    }

    public static String randomNameForRarity(final String rarity) {
        return pickRandom(StatLoader.getInstance().getAllEnemyData().stream()
            .filter(e -> e.rarity().equals(rarity.toLowerCase()))
            .map(e -> e.entityName())
            .toList()).toLowerCase();
    }

    public static Vector2dc normalizeVector(final Vector2dc toConvert) {
        var norm = new Vector2d(toConvert).normalize();
        return norm.isFinite() ? norm : new Vector2d(0, 0);
    }

    public static String capitalize(final String toCap) {
        return toCap.substring(0, 1).toUpperCase() + toCap.substring(1);
    }
}
