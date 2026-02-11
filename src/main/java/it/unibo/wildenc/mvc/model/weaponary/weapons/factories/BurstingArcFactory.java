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

public class BurstingArcFactory implements WeaponFactory {

    @Override
    public Weapon createWeapon(
        String weaponName, 
        double baseCooldown, 
        double baseDamage, 
        double hbRadius,
        double baseVelocity, 
        double baseTTL, 
        int baseProjAtOnce, 
        int baseBurst, 
        Entity ownedBy,
        final boolean immortal,
        Supplier<Vector2dc> posToHit
    ) {
        return new PointerWeapon(
            weaponName,
            baseCooldown,
            baseBurst,
            baseProjAtOnce,
            posToHit,
            ProjectileStats.getBuilder()
                .damage(baseDamage)
                .physics((dt, atkInfo) -> straightMovement(dt, atkInfo))
                .radius(hbRadius)
                .velocity(baseVelocity)
                .ttl(baseTTL)
                .owner(ownedBy)
                .id(weaponName)
                .build(),
            (level, weaponStats) -> {
                weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, level);
                weaponStats.getProjStats().setMultiplier(ProjStatType.VELOCITY, level);
                weaponStats.getProjStats().setMultiplier(
                    ProjStatType.HITBOX, 
                    weaponStats.getProjStats().getStatValue(ProjStatType.HITBOX) + level
                );
                weaponStats.setBurstSize(level);
            },
            weaponStats -> arcSpawn(weaponStats)
        );
    }

    private static List<AttackContext> arcSpawn(final WeaponStats weaponStats) {
        final int pelletNumber = weaponStats.getProjectilesShotAtOnce();
        final double totalArc = Math.toRadians(45);

        final Vector2dc origin = weaponStats.getProjStats().getOwner().getPosition();
        final double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);
        final Vector2dc targetPos = weaponStats.getPosToHit().get();

        final Vector2d centralDirection = new Vector2d(Utilities.normalizeVector(new Vector2d(targetPos)));

        final List<AttackContext> projContext = new ArrayList<>();

        for (int i = 0; i < pelletNumber; i++) {
            final double currentAngle = (pelletNumber > 1) 
                ? - (totalArc / 2.0) + (i * (totalArc / (pelletNumber - 1))) 
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
