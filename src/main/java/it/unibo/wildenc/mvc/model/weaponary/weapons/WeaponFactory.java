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

/**
 * Factory class for generating different kinds of weapon.
 */
public class WeaponFactory {

    private static final int EXAMPLE_MULTIPLIER = 5;

    /**
     * Class for getting a default weapon.
     * A default weapon is a weapon that shoots one or more projectiles in one
     * direction, and the projectile goes straight following that direction.
     * 
     * @param baseCooldown the initial cooldown of the weapon
     * @param baseDamage the base damage of the projectiles
     * @param hbRadius the hitbox radius of the projectiles
     * @param baseVelocity the velocity of the projectiles
     * @param baseTTL the time to live of the projectiles
     * @param baseProjAtOnce how many projectiles at once the weapon will shoot
     * @param baseBurst how many projectiles are in a single burst
     * @param ownedBy the owner of the weapon
     * @param posToHit the position which the projectiles have to follow
     * @return a {@link Weapon} with all the informations inserted.
     */
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
        return new PointerWeapon(
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
                    weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, level * EXAMPLE_MULTIPLIER);
                    weaponStats.getProjStats().setMultiplier(ProjStatType.VELOCITY, level);
                    weaponStats.getProjStats().setMultiplier(
                        ProjStatType.HITBOX, 
                        weaponStats.getProjStats().getStatValue(ProjStatType.HITBOX) + level
                    );
                    weaponStats.setBurstSize(level);
                },
                weaponStats -> {
                    final int pelletNumber = weaponStats.getProjectilesShotAtOnce();
                    final double totalArc = Math.toRadians(45);

                    final Vector2dc origin = weaponStats.getProjStats().getOwner().getPosition();
                    final double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);
                    final Vector2dc targetPos = weaponStats.getProjStats().getPositionToHit().get();

                    final Vector2d centralDirection = new Vector2d(targetPos).sub(origin).normalize();

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
        );
    }
}
