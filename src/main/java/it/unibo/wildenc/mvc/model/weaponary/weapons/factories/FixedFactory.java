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

public class FixedFactory implements WeaponFactory {

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
        return new GenericWeapon(
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
            weaponStats -> basicSpawn(weaponStats)
        );
    }

    private List<AttackContext> basicSpawn(WeaponStats weaponStats) {
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

    private Vector2d straightMovement(Double dt, AttackContext atkInfo) {
        return new Vector2d(
            atkInfo.getLastPosition().x() + atkInfo.getVelocity() * dt * atkInfo.getDirectionVersor().x(),
            atkInfo.getLastPosition().y() + atkInfo.getVelocity() * dt * atkInfo.getDirectionVersor().y()
        );
    }
}
