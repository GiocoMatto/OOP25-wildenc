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

public class ContactFactory implements WeaponFactory {

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
        boolean immortal,
        Supplier<Vector2dc> posToHit) {
            return new GenericWeapon(
                weaponName, 
                baseCooldown, 
                baseBurst, 
                baseProjAtOnce, 
                posToHit,
                ProjectileStats.getBuilder()
                .damage(baseDamage)
                    .physics(this::still)
                    .radius(hbRadius)
                    .velocity(0)
                    .ttl(baseTTL)
                    .owner(ownedBy)
                    .id(weaponName)
                    .immortal(immortal)
                    .build(),
                (lvl, ws) -> {}, 
                weaponStats -> spawnOn(ownedBy.getPosition())
            );
    }

    private Vector2d still(final double deltaTime, final AttackContext atkInfo) {
        return new Vector2d(atkInfo.getLastPosition());
    }

    private List<AttackContext> spawnOn(final Vector2dc pos) {
        return List.of(
            new AttackContext(pos, 0, () -> pos)
        );
    }
}
