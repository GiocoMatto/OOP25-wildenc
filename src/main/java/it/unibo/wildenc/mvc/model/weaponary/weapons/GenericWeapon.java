package it.unibo.wildenc.mvc.model.weaponary.weapons;

import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * Implementation of a generic {@link Weapon}. This will be used as a 
 * schematic to model all {@link Weapons}s each with different characteristics.
 */
public class GenericWeapon implements Weapon {

    private static final double BURST_DELAY = 0.2;
    private static final double MS_TO_S = 1000.0;

    private WeaponStats weaponStats;
    private int level = 0;
    private double timeAtLastAtk;
    private final String weaponName;
    private int currentBullet;
    BiFunction<List<AttackContext>, ProjectileStats, Set<Projectile>> atkFunc;

    public GenericWeapon(
        final double cooldown,
        final ProjectileStats pStats,
        final BiConsumer<Integer, WeaponStats> upgradeLogics,
        final BiFunction<List<AttackContext>, ProjectileStats, Set<Projectile>> atkFunc,
        final int initialBurst,
        final String weaponName
    ) {
        this.weaponStats = new WeaponStats(
            cooldown, pStats, initialBurst, upgradeLogics
        );
        this.atkFunc = atkFunc;
        this.weaponName = weaponName;
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public Set<Projectile> attack(final List<AttackContext> atkInfo) {
        if(canBurst()) {
            if(!isInCooldown()) {
                currentBullet = 0;
            }
            currentBullet++;
            timeAtLastAtk = System.currentTimeMillis();
            return this.atkFunc.apply(atkInfo, this.weaponStats.pStats());
        }
        return Set.of();
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public void upgrade() {
        this.weaponStats.upgradeLogics().accept(this.level, this.weaponStats);
    }
    
    // This method is used for testing purposes only.
    public WeaponStats getStats() {
        return this.weaponStats;
    }

    private boolean isInCooldown() {
        return (System.currentTimeMillis() - timeAtLastAtk) / MS_TO_S < this.weaponStats.weaponCooldown();
    }

    private boolean canBurst() {
        return !isInCooldown() ? true :
            (currentBullet < this.weaponStats.burstSize() && (System.currentTimeMillis() - timeAtLastAtk) / MS_TO_S >= BURST_DELAY);
    }

    @Override
    public String getName() {
        return this.weaponName;
    }
}
