package it.unibo.wildenc.mvc.model.enemies;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import org.joml.Vector2d;
import it.unibo.wildenc.mvc.model.Collectible;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.EnemyFactory;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.dataloaders.StatLoader;
import it.unibo.wildenc.mvc.model.dataloaders.StatLoader.LoadedEntityStats;
import it.unibo.wildenc.mvc.model.enemies.AbstractEnemy.AbstractEnemyField;
import it.unibo.wildenc.mvc.model.map.objects.ExperienceGem;
import it.unibo.wildenc.mvc.model.map.objects.HealthPotion;
import it.unibo.wildenc.mvc.model.map.objects.MoneyCoin;

/**
 * {@inheritDoc}.
 */
public class EnemyFactoryImpl implements EnemyFactory {
    private static final int VALUE_COLLECTIBLE = 34;
    private static final int RANGE_PROBABILITY = 100;
    private static final Random RANDOM = new Random();

    private final MapObject target;
    private final StatLoader statLoader = StatLoader.getInstance();

    /**
     * Create a Factory that associate the same target to all enemys.
     * 
     * @param target MapObject to attack.
     */
    public EnemyFactoryImpl(final MapObject target) {
        this.target = target;
    }

    private void addDefaultWeaponTo(final Enemy e) {
        e.addWeapon(statLoader.getWeaponFactoryForWeapon("enemyranged", e, () -> target.getPosition())); // NOPMD
        // Lambda cannot be written like that - false positive.
    }

    private void addMeleeWeaponTo(final Enemy e) {
        e.addWeapon(statLoader.getWeaponFactoryForWeapon("enemymelee", e, () -> e.getPosition())); // NOPMD
        // Lambda cannot be written like that - false positive.
    }

    private Function<MapObject, Optional<Collectible>> experienceLoot() {
        return e -> Optional.of(new ExperienceGem(e.getPosition(), VALUE_COLLECTIBLE));
    }

    private Function<MapObject, Optional<Collectible>> coinLoot() {
        return e -> Optional.of(new MoneyCoin(e.getPosition(), VALUE_COLLECTIBLE));
    }

    private Function<MapObject, Optional<Collectible>> healthLoot() {
        return e -> Optional.of(new HealthPotion(e.getPosition(), VALUE_COLLECTIBLE));
    }

    private Function<MapObject, Optional<Collectible>> percentageLoot(
        final Function<MapObject, 
        Optional<Collectible>> loot, 
        final double percent
    ) {
        return hasPercentageHit(percent) ? loot : e -> Optional.empty();
    }

    private boolean hasPercentageHit(final double percent) {
        return RANDOM.nextInt(RANGE_PROBABILITY) <= percent * RANGE_PROBABILITY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enemy closeRangeEnemy(final Vector2d spawnPosition, final int healt, final String name) {
        final LoadedEntityStats loadedEntityStats = loadEntityFromName(name);
        final Enemy e = new CloseRangeEnemy(new AbstractEnemyField(
            spawnPosition, 
            loadedEntityStats.hbRadius(), 
            loadedEntityStats.velocity(), 
            loadedEntityStats.maxHealth(), 
            name, 
            Optional.of(target),
            new HashSet<>(List.of(experienceLoot(), percentageLoot(coinLoot(), 0.1)))
        ));
        addMeleeWeaponTo(e);
        addDefaultWeaponTo(e);
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enemy closeRangeFastEnemy(final Vector2d spawnPosition, final int healt, final String name) {
        final LoadedEntityStats loadedEntityStats = loadEntityFromName(name);
        final Enemy e = new CloseRangeEnemy(new AbstractEnemyField(
            spawnPosition, 
            loadedEntityStats.hbRadius(), 
            2 * loadedEntityStats.velocity(), 
            loadedEntityStats.maxHealth(), 
            name, 
            Optional.of(target),
            new HashSet<>(List.of(
                experienceLoot(), 
                percentageLoot(coinLoot(), 0.2), 
                percentageLoot(healthLoot(), 0.4)
            ))
        ));
        addMeleeWeaponTo(e);
        addDefaultWeaponTo(e);
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enemy rangedEnemy(final Vector2d spawnPosition, final int healt, final String name) {
        final LoadedEntityStats loadedEntityStats = loadEntityFromName(name);
        final Enemy e = new RangedEnemy(new AbstractEnemyField(
            spawnPosition, 
            loadedEntityStats.hbRadius(), 
            loadedEntityStats.velocity(), 
            loadedEntityStats.maxHealth(), 
            name, 
            Optional.of(target),
            new HashSet<>(List.of(
                experienceLoot(), 
                percentageLoot(healthLoot(), 0.5)
            ))
        ));
        addMeleeWeaponTo(e);
        addDefaultWeaponTo(e);
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enemy rangedDoubleShotEnemy(final Vector2d spawnPosition, final int healt, final String name) {
        final LoadedEntityStats loadedEntityStats = loadEntityFromName(name);
        final Enemy e = new RangedEnemy(new AbstractEnemyField(
            spawnPosition, 
            loadedEntityStats.hbRadius(), 
            loadedEntityStats.velocity(), 
            loadedEntityStats.maxHealth(), 
            name, 
            Optional.of(target),
            new HashSet<>(List.of(
                experienceLoot(), 
                healthLoot()
            ))
        ));
        addDefaultWeaponTo(e);
        addDefaultWeaponTo(e);
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enemy roamingEnemy(final Vector2d spawnPosition, final int healt, final String name) {
        final LoadedEntityStats loadedEntityStats = loadEntityFromName(name);
        final Enemy e = new RoamingEnemy(new AbstractEnemyField(
            spawnPosition, 
            loadedEntityStats.hbRadius(), 
            loadedEntityStats.velocity(), 
            loadedEntityStats.maxHealth(), 
            name, 
            Optional.empty(), 
            new HashSet<>(List.of(
                experienceLoot(), 
                percentageLoot(healthLoot(), 0.05)
            ))
        ));
        addMeleeWeaponTo(e);
        addDefaultWeaponTo(e);
        return e;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enemy roamingLongLifeEnemy(final Vector2d spawnPosition, final int healt, final String name) {
        final LoadedEntityStats loadedEntityStats = loadEntityFromName(name);
        final Enemy e = new RoamingEnemy(new AbstractEnemyField(
            spawnPosition, 
            loadedEntityStats.hbRadius(), 
            loadedEntityStats.velocity(), 
            loadedEntityStats.maxHealth() + loadedEntityStats.maxHealth() / 2, 
            name, 
            Optional.empty(), 
            new HashSet<>(List.of(
                experienceLoot(), 
                percentageLoot(coinLoot(), 0.05),
                percentageLoot(healthLoot(), 0.1)
            ))
        ));
        addMeleeWeaponTo(e);
        addDefaultWeaponTo(e);
        return e;
    }

    private LoadedEntityStats loadEntityFromName(final String name) {
        return statLoader.getLoadedEnemyStats(name);
    }
}
