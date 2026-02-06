package it.unibo.wildenc.util;

import java.util.List;
import java.util.Random;

public class Utilities {
    private static final Random RAND = new Random();

    public static <T> T pickRandom(final List<T> values) {
        return values.get(RAND.nextInt(values.size()));
    }

}
