package it.unibo.wildenc.mvc.model.weaponary.weapons;

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

public class ShurikenFactory implements WeaponFactory {

    private final int distanceFromPlayer;

    public ShurikenFactory(final int fromPlayer) {
        this.distanceFromPlayer = fromPlayer;
    }

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
        Supplier<Vector2dc> posToHit
    ) {
        return new GenericWeapon(
            weaponName, 
            baseCooldown, 
            baseBurst, 
            baseProjAtOnce,
            ownedBy::getPosition, 
            ProjectileStats.getBuilder()
                .damage(baseDamage)
                .velocity(baseVelocity)
                .id(weaponName)
                .owner(ownedBy)
                .ttl(baseTTL)
                .physics(
                    (dt, atkInfo) -> circularMovement(baseBurst, atkInfo, ownedBy, distanceFromPlayer)
                ).build(), 
                null,
                wStats -> circularSpawn(wStats)
        );
    }

    private static List<AttackContext> circularSpawn(final WeaponStats weaponStats) {
        final int pelletNumber = weaponStats.getProjectilesShotAtOnce();
        final List<AttackContext> projContext = new ArrayList<>();
        final double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);
        final double step = Math.toRadians(360) / pelletNumber; 
        for (int i = 0; i < pelletNumber; i++) {
            final double currentAngle = i * step;
            final Vector2d offsetDir = new Vector2d(Math.cos(currentAngle), Math.sin(currentAngle));

            projContext.add(new AttackContext(
                weaponStats.getProjStats().getOwner().getPosition(), 
                velocity, 
                () -> {
                    return new Vector2d(weaponStats.getProjStats().getOwner().getPosition()).add(offsetDir);
                }
            ).protectiveCopy());
        }
        return projContext;
    }

    private static Vector2d circularMovement(
        final double dt, 
        final AttackContext atkInfo, 
        Entity ownedBy,
        final double orbitRadius
    ) {
        Vector2dc center = ownedBy.getPosition();
        Vector2dc dir = atkInfo.getDirectionVersor();
        double currentRad = Math.atan2(dir.y(), dir.x());
        double nextRad = currentRad + (atkInfo.getVelocity() * dt);
        atkInfo.setDirection(Math.toDegrees(nextRad));
        double deltaAngleRad = atkInfo.getVelocity() * dt;
        double nextAngleRad = currentRad + deltaAngleRad;
        double nextAngleDeg = Math.toDegrees(nextAngleRad);
        atkInfo.setDirection(nextAngleDeg); 

        return new Vector2d(
            center.x() + orbitRadius * Math.cos(nextAngleRad),
            center.y() + orbitRadius * Math.sin(nextAngleRad)
        );
    }
}
