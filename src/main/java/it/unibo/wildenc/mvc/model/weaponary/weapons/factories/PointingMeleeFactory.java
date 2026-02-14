package it.unibo.wildenc.mvc.model.weaponary.weapons.factories;

import java.util.List;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;
import it.unibo.wildenc.mvc.model.weaponary.weapons.PointerWeapon;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponStats;
import it.unibo.wildenc.util.Utilities;

/**
 * Factory for PointingMelee weapons. PointingMelee weapons are an implementation
 * of {@link PointingWeapon} which generates a still projectile with a small offset
 * from who's generating it.
 */
public class PointingMeleeFactory implements WeaponFactory {

    private static final double INITIAL_FROM_PLAYER = 60;

    private double fromPlayer = INITIAL_FROM_PLAYER;

    /**
     * {@inheritDoc}
     */
    @Override
    public Weapon createWeapon(
        final String weaponName, 
        final double baseCooldown, 
        final double baseDamage, 
        final double hbRadius,
        final double baseVelocity, 
        final double baseTTL, 
        final int baseProjAtOnce, 
        final int baseBurst, 
        final Entity ownedBy, 
        final boolean immortal,
        final Supplier<Vector2dc> posToHit
    ) {
    return new PointerWeapon(
            weaponName,
            baseCooldown,
            baseBurst,
            baseProjAtOnce,
            posToHit,
            ProjectileStats.getBuilder()
                .damage(baseDamage)
                .physics((dt, atkInfo) -> still(atkInfo))
                .radius(hbRadius)
                .velocity(0)
                .ttl(baseTTL)
                .owner(ownedBy)
                .id(weaponName)
                .immortal(immortal)
                .build(),
            (level, weaponStats) -> {
                weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, level);
                weaponStats.getProjStats().setMultiplier(ProjStatType.HITBOX, 1 + ((double) level / 100));
                this.fromPlayer += (double) level / 10;
            },
            this::meleeSpawn
        );
    }

    private Vector2d still(final AttackContext atkInfo) {
        return new Vector2d(atkInfo.getLastPosition());
    }

    private List<AttackContext> meleeSpawn(final WeaponStats weaponStats) {
        final Vector2dc origin = weaponStats.getProjStats().getOwnerPosition();
        final double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);

        final Vector2d direction = new Vector2d(
            Utilities.normalizeVector(weaponStats.getPosToHit().get())
        ).mul(fromPlayer);

        final Vector2d finalTarget = new Vector2d(origin).add(direction);
        return List.of(new AttackContext(
            new Vector2d(finalTarget), 
            velocity, 
            () -> finalTarget
        ));
    }
}
