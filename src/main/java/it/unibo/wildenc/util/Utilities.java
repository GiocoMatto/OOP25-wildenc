package it.unibo.wildenc.util;

import java.util.List;
import java.util.Random;

import org.joml.Vector2d;
import org.joml.Vector2dc;

public class Utilities {
    private static final Random RAND = new Random();

    public static <T> T pickRandom(final List<T> values) {
        return values.get(RAND.nextInt(values.size()));
    }

    public static Vector2dc normalizeVector(final Vector2dc toConvert) {
        var norm = new Vector2d(toConvert).normalize();
        return norm.isFinite() ? norm : new Vector2d(0, 0);
    }
}
