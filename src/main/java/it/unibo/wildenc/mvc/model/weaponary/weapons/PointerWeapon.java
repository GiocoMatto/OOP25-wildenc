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
     * {@inheritDoc}
     */
    public void setPosToHit(final Supplier<Vector2dc> newPosToHit) {
        this.weaponStats.setPosToHit(newPosToHit);
    }
}
