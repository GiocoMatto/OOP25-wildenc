package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ConcreteProjectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;

public class WeaponFactory {
    
    public Weapon getDefaultWeapon(
        final double baseCooldown,
        final double baseDamage,
        final double hbRadius, 
        final double baseVelocity,
        final double baseTTL,
        final int baseBurst,
        final Entity ownedBy,
        final Supplier<Vector2d> posToHit 
    ) {
        return new GenericWeapon(
            baseCooldown,
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
                    weaponStats.pStats().setMultiplier(ProjStatType.DAMAGE, level * 5);
                    weaponStats.pStats().setMultiplier(ProjStatType.VELOCITY, level);
                    weaponStats.pStats().setMultiplier(ProjStatType.HITBOX, level);
                },
                projStats -> List.of(new AttackContext(projStats.getOwner().getPosition(), posToHit)),
                baseBurst,
                "BasicWeapon"
        );        
    }
}

