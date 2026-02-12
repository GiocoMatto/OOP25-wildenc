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
 * Factory for FixedWeapons. A FixedWeapon is a {@link GeneriWeapon} that shoots projectiles
 * directly to a specified point.
 */
public class FixedFactory implements WeaponFactory {

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
            posToHit,
            ProjectileStats.getBuilder()
                .damage(baseDamage)
                .physics(this::straightMovement)
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
            this::basicSpawn
        );
    }

    private List<AttackContext> basicSpawn(final WeaponStats weaponStats) {
        final List<AttackContext> toRet = new ArrayList<>();
        for (int i = 0; i < weaponStats.getProjectilesShotAtOnce(); i++) {
            toRet.add(new AttackContext(
                new Vector2d(weaponStats.getProjStats().getOwner().getPosition()), 
                weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY), 
                weaponStats.getPosToHit()
            ));
        }
        return toRet;
    }

    private Vector2d straightMovement(final double dt, final AttackContext atkInfo) {
        return new Vector2d(
            atkInfo.getLastPosition().x() + atkInfo.getVelocity() * dt * atkInfo.getDirectionVersor().x(),
            atkInfo.getLastPosition().y() + atkInfo.getVelocity() * dt * atkInfo.getDirectionVersor().y()
        );
    }
}
