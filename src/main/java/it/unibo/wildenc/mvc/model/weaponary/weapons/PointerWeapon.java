package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;

/**
 * Specialization of GenericWeapon that allows changes to the position to hit.
 * This was made for weapons that can change their position dynamically.
 */
public class PointerWeapon extends GenericWeapon {

    /**
     * Constructor for the class.
     * 
     * @param weaponName the name of the weapon.
     * @param initialCooldown the initial cooldown of the weapon.
     * @param initialBurst the initial quantity of Projectiles in a burst.
     * @param initialProjAtOnce the initial quantity of Projectile shot in one attack.
     * @param initialPosToHit the position which the weapon is aiming at, initially.
     * @param pStats the statistics of the Projectile this weapon will shoot.
     * @param upgradeLogics the logics of upgrading for this weapon.
     * @param attackInfoGenerator a {@link Function} specifing how the Projectiles should be shot.
     */
    public PointerWeapon(
        final String weaponName, 
        final double initialCooldown, 
        final int initialBurst, 
        final int initialProjAtOnce,
        final Supplier<Vector2dc> initialPosToHit,
        final ProjectileStats pStats,
        final BiConsumer<Integer, WeaponStats> upgradeLogics,
        final Function<WeaponStats, List<AttackContext>> attackInfoGenerator
    ) {
        super(
            weaponName, 
            initialCooldown, 
            initialBurst, 
            initialProjAtOnce, 
            initialPosToHit,
            pStats, 
            upgradeLogics, 
            attackInfoGenerator
        );
    }

    /**
     * Sets a new position which the Weapon is aiming to.
     * 
     * @param newPosToHit the new position aimed by the weapon.
     */
    public void setPosToHit(final Supplier<Vector2dc> newPosToHit) {
        this.getStats().setPosToHit(newPosToHit);
    }
}
