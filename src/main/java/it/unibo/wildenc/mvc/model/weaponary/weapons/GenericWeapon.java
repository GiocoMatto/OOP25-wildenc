package it.unibo.wildenc.mvc.model.weaponary.weapons;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.weaponary.ProjectileStats;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ConcreteProjectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Implementation of a generic {@link Weapon}. This will be used as a 
 * schematic to model all {@link Weapons}s each with different characteristics.
 */
public class GenericWeapon implements Weapon {

    private WeaponStats weaponStats;
    private int level = 0;
    private long timeAtLastAtk;
    private final String weaponName;

    public GenericWeapon(
        final double cooldown,
        final ProjectileStats pStats,
        final BiConsumer<Integer, WeaponStats> upgradeLogics,
        final String weaponName
    ) {
        this.weaponStats = new WeaponStats(
            cooldown, pStats, upgradeLogics
        );
        this.weaponName = weaponName;
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public Optional<Projectile> attack(
        Vector2d startingPoint,
        Vector2d atkDirection, 
        Optional<Supplier<Vector2d>> toFollow
    ) {
        final long timestamp = System.currentTimeMillis();
        if(!isInCooldown(timestamp)) {
            this.timeAtLastAtk = timestamp;
            return Optional.ofNullable(
                new ConcreteProjectile(this.weaponStats.pStats(), atkDirection, startingPoint, toFollow)
            );
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public void upgrade() {
        this.weaponStats.upgradeLogics().accept(this.level, this.weaponStats);
    }
    
    // TODO: Remove this method. This is used for testing purposes only.
    public WeaponStats getStats() {
        return this.weaponStats;
    }

    private boolean isInCooldown(final long timestamp) {
        return timestamp - timeAtLastAtk > this.weaponStats.weaponCooldown();
    }

    @Override
    public String getName() {
        return this.weaponName;
    }
}
