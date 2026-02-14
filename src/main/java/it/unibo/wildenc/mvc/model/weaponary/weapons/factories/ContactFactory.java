package it.unibo.wildenc.mvc.model.weaponary.weapons.factories;

import java.util.List;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
import it.unibo.wildenc.mvc.model.weaponary.weapons.GenericWeapon;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

/**
 * Factory for Contact weapons. Contact weapons are an implementation
 * of {@link GenericWeapons} which generates a still projectile 
 * with hitbox the hitbox of the owner. This was planned as a
 * contact-damage weapon. 
 */
public class ContactFactory implements WeaponFactory {

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
        final Supplier<Vector2dc> posToHit) {
            return new GenericWeapon(
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
                (lvl, ws) -> { }, 
                weaponStats -> spawnOn(ownedBy.getPosition())
            );
    }

    private Vector2d still(final AttackContext atkInfo) {
        return new Vector2d(atkInfo.getLastPosition());
    }

    private List<AttackContext> spawnOn(final Vector2dc pos) {
        return List.of(
            new AttackContext(pos, 0, () -> pos)
        );
    }
}
