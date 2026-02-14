package it.unibo.wildenc.mvc.model.weaponary.weapons.factories;

import java.util.ArrayList;
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
 * Factory for BurstingArc weapons. A BurstingArc weapons is a type of {@link PointerWeapon}
 * which shoots one or multiple projectiles splitting an arc of 60 degrees from the position
 * of the pointer.
 */
public class BurstingArcFactory implements WeaponFactory {

    public static final int LV_5 = 5;
    public static final int LV_7 = 7;

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
                .physics(BurstingArcFactory::straightMovement)
                .radius(hbRadius)
                .velocity(baseVelocity)
                .ttl(baseTTL)
                .owner(ownedBy)
                .id(weaponName)
                .build(),
            (level, weaponStats) -> {
                weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, (double) (level + 1) / 2);
                weaponStats.getProjStats().setMultiplier(ProjStatType.VELOCITY, (double) (level + 1) / 2);
                if (level % LV_5 == 0) {
                    weaponStats.increaseProjectilesShotAtOnce();
                }
                if (level % LV_7 == 0) {
                    weaponStats.setBurstSize(
                        weaponStats.getCurrentBurstSize() + 1
                    );
                }
            },
            BurstingArcFactory::arcSpawn
        );
    }

    private static List<AttackContext> arcSpawn(final WeaponStats weaponStats) {
        final int pelletNumber = weaponStats.getProjectilesShotAtOnce();
        final double totalArc = Math.toRadians(45);

        final Vector2dc origin = weaponStats.getProjStats().getOwnerPosition();
        final double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);
        final Vector2dc targetPos = weaponStats.getPosToHit().get();

        final Vector2d centralDirection = new Vector2d(Utilities.normalizeVector(new Vector2d(targetPos)));

        final List<AttackContext> projContext = new ArrayList<>();

        for (int i = 0; i < pelletNumber; i++) {
            final double currentAngle = (pelletNumber > 1) 
                ? -(totalArc / 2.0) + (i * (totalArc / (pelletNumber - 1))) 
                : 0;

            final double cos = Math.cos(currentAngle);
            final double sin = Math.sin(currentAngle);

            final double rotatedX = centralDirection.x() * cos - centralDirection.y() * sin;
            final double rotatedY = centralDirection.x() * sin + centralDirection.y() * cos;
            final Vector2d rotatedDir = new Vector2d(rotatedX, rotatedY);

            final Vector2d fakeTarget = new Vector2d(origin).add(rotatedDir);

            projContext.add(new AttackContext(
                origin, 
                velocity, 
                () -> fakeTarget
            ));
        }
        return projContext;
    }

    private static Vector2d straightMovement(final double dt, final AttackContext atkInfo) {
        final Vector2dc start = atkInfo.getLastPosition();
        return new Vector2d(
            start.x() + dt * atkInfo.getVelocity() * atkInfo.getDirectionVersor().x(),
            start.y() + dt * atkInfo.getVelocity() * atkInfo.getDirectionVersor().y()
        );
    } 
}
