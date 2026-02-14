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
 * Class for SimplePointer weapons. Simple pointer weapons are weapons
 * that shoot one or more projectiles in the pointed direction.
 */
public class SimplePointerFactory implements WeaponFactory {

    private static final int LV_7 = 7;

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
                .physics(this::straightMovement)
                .radius(hbRadius)
                .velocity(baseVelocity)
                .ttl(baseTTL)
                .owner(ownedBy)
                .id(weaponName)
                .immortal(immortal)
                .build(),
            (level, weaponStats) -> {
                weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, 1 + ((double) level / 4));
                weaponStats.getProjStats().setMultiplier(ProjStatType.VELOCITY, ((double) level / 100) + 1);
                if (level % LV_7 == 0) {
                    weaponStats.setBurstSize(
                        weaponStats.getCurrentBurstSize() + 1
                    );
                }
            },
            SimplePointerFactory::singleSpawn
        );
    }

    private Vector2d straightMovement(final double dt, final AttackContext atkInfo) {
        final Vector2dc start = atkInfo.getLastPosition();
        return new Vector2d(
            start.x() + dt * atkInfo.getVelocity() * atkInfo.getDirectionVersor().x(),
            start.y() + dt * atkInfo.getVelocity() * atkInfo.getDirectionVersor().y()
        );
    }

    private static List<AttackContext> singleSpawn(final WeaponStats weaponStats) {
        final Vector2dc origin = weaponStats.getProjStats().getOwnerPosition();
        final double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);
        final Vector2dc targetPos = weaponStats.getPosToHit().get();

        final Vector2d dir = new Vector2d(Utilities.normalizeVector(new Vector2d(targetPos)));
        final Vector2d finalTarget = new Vector2d(origin).add(dir);

        return List.of(new AttackContext(
            origin, 
            velocity, 
            () -> finalTarget
        ));
    }
}
