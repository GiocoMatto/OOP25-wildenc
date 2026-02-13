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
import it.unibo.wildenc.mvc.model.weaponary.weapons.GenericWeapon;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponStats;

/**
 * Factory class for a TyphoonWeapon. A TyphoonWeapon is an implementation
 * of a {@link GenericWeapon} which generates projectile which orbit around the
 * player, at a specified distance.
 */
public class TyphoonFactory implements WeaponFactory {

    private static final int LV_2 = 2;
    private static final int LV_5 = 5;

    private final double distanceFromPlayer;

    /**
     * Constructor for the class.
     * 
     * @param fromPlayer the offset between the orbitals and the player.
     */
    public TyphoonFactory(final double fromPlayer) {
        this.distanceFromPlayer = fromPlayer;
    }

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
                .immortal(immortal)
                .radius(hbRadius)
                .physics(
                    (dt, atkInfo) -> circularMovement(baseBurst, atkInfo, ownedBy, distanceFromPlayer)
                ).build(), 
                (lvl, weaponstats) -> {
                    weaponstats.getProjStats().setMultiplier(ProjStatType.DAMAGE, 1 + Math.log(lvl));
                    if (lvl % LV_2 == 0) {
                        weaponstats.setCooldown(
                            weaponstats.getCooldown() - lvl / 100
                        );
                    }
                    if (lvl % LV_5 == 0) {
                        weaponstats.increaseProjectilesShotAtOnce();
                    }
                },
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
        final Entity ownedBy,
        final double orbitRadius
    ) {
        final Vector2dc center = ownedBy.getPosition();
        final Vector2dc dir = atkInfo.getDirectionVersor();
        final double currentRad = Math.atan2(dir.y(), dir.x());
        final double nextRad = currentRad + (atkInfo.getVelocity() * dt);
        atkInfo.setDirection(Math.toDegrees(nextRad));
        final double deltaAngleRad = atkInfo.getVelocity() * dt;
        final double nextAngleRad = currentRad + deltaAngleRad;
        final double nextAngleDeg = Math.toDegrees(nextAngleRad);
        atkInfo.setDirection(nextAngleDeg); 

        return new Vector2d(
            center.x() + orbitRadius * Math.cos(nextAngleRad),
            center.y() + orbitRadius * Math.sin(nextAngleRad)
        );
    }
}
