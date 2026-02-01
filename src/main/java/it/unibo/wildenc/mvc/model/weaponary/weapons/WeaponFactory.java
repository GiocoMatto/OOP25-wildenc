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

public class WeaponFactory {
    
    public Weapon getDefaultWeapon(
        final double baseCooldown,
        final double baseDamage,
        final double hbRadius, 
        final double baseVelocity,
        final double baseTTL,
        final int baseProjAtOnce,
        final int baseBurst,
        final Entity ownedBy,
        final Supplier<Vector2dc> posToHit 
    ) {
        return new GenericWeapon(
            "BasicWeapon",
            baseCooldown,
            baseBurst,
            baseProjAtOnce,
            new ProjectileStats(
                baseDamage, 
                hbRadius,
                baseVelocity,
                baseTTL,
                "BasicProj",
                ownedBy,
                posToHit,
                (dt, atkInfo) -> {
                    final Vector2dc start = atkInfo.getLastPosition();
                    return new Vector2d(
                        start.x() + dt * atkInfo.getVelocity() * atkInfo.getDirectionVersor().x(),
                        start.y() + dt * atkInfo.getVelocity() * atkInfo.getDirectionVersor().y()
                    );
                }),
                (level, weaponStats) -> {
                    weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, level * 5);
                    weaponStats.getProjStats().setMultiplier(ProjStatType.VELOCITY, level);
                    weaponStats.getProjStats().setMultiplier(
                        ProjStatType.HITBOX, 
                        weaponStats.getProjStats().getStatValue(ProjStatType.HITBOX) + level
                    );
                    weaponStats.setBurstSize(level);
                },
                weaponStats -> {
                    int pelletNumber = weaponStats.getProjectilesShotAtOnce();
                    double totalArc = Math.toRadians(45);
                    
                    Vector2dc origin = weaponStats.getProjStats().getOwner().getPosition();
                    double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);
                    Vector2dc targetPos = weaponStats.getProjStats().getPositionToHit().get();
                    
                    Vector2d centralDirection = new Vector2d(targetPos).sub(origin).normalize();

                    List<AttackContext> projContext = new ArrayList<>();

                    for (int i = 0; i < pelletNumber; i++) {
                        double currentAngle = (pelletNumber > 1) 
                            ? -(totalArc / 2.0) + (i * (totalArc / (pelletNumber - 1))) 
                            : 0;

                        double cos = Math.cos(currentAngle);
                        double sin = Math.sin(currentAngle);
                        
                        double rotatedX = centralDirection.x() * cos - centralDirection.y() * sin;
                        double rotatedY = centralDirection.x() * sin + centralDirection.y() * cos;
                        Vector2d rotatedDir = new Vector2d(rotatedX, rotatedY);
                        
                        Vector2d fakeTarget = new Vector2d(origin).add(rotatedDir);

                        projContext.add(new AttackContext(
                            origin, 
                            velocity, 
                            () -> fakeTarget
                        ));
                    }
                    return projContext;
                }
        );
    }
}