package it.unibo.wildenc.mvc.model.enemies;

import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Collectible;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.map.objects.ExperienceGem;
import it.unibo.wildenc.mvc.model.map.objects.HealthPotion;
import it.unibo.wildenc.mvc.model.map.objects.MoneyCoin;

public class CollectibleLoot {
    private static final int VALUE_COLLECTIBLE = 34;
    private static final int RANGE_PROBABILITY = 100;

    public static Function<MapObject, Optional<Collectible>> experienceLoot(final Vector2dc pos) {
        return e -> Optional.of(new ExperienceGem(e.getPosition(), VALUE_COLLECTIBLE));
    };

    public static Function<MapObject, Optional<Collectible>> coinLoot(final Vector2dc pos) {
        return e -> Optional.of(new MoneyCoin(e.getPosition(), VALUE_COLLECTIBLE));
    };

    public static Function<MapObject, Optional<Collectible>> healthLoot(final Vector2dc pos) {
        return e -> Optional.of(new HealthPotion(e.getPosition(), VALUE_COLLECTIBLE));
    };

    public static Function<MapObject, Optional<Collectible>> percentageLoot(final Function<MapObject, Optional<Collectible>> loot, double percent) {
        return hasPercentageHit(percent) ? loot : e -> Optional.empty();
    }

    private static boolean hasPercentageHit(double percent) {
        Random r = new Random();
        return r.nextInt(RANGE_PROBABILITY) <= percent * RANGE_PROBABILITY;
    }

}
