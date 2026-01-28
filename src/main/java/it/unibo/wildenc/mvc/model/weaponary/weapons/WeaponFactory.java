package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ConcreteProjectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;

public class WeaponFactory {
    public Weapon getDefaultWeapon() {
        return new GenericWeapon(
            1,
            new ProjectileStats(
                10, 
                2,
                1,
                10,
                "BasicProj",
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
                (atkInfos, projStats) -> Set.of(new ConcreteProjectile(projStats, atkInfos.getFirst().protectiveCopy())),
                2,
                "BasicWeapon"
        );        
    }
}

