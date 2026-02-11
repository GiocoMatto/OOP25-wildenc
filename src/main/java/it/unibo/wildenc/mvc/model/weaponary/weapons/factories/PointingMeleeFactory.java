package it.unibo.wildenc.mvc.model.weaponary.weapons.factories;

import java.util.List;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;
import it.unibo.wildenc.mvc.model.weaponary.weapons.PointerWeapon;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponStats;
import it.unibo.wildenc.util.Utilities;

public class PointingMeleeFactory implements WeaponFactory {

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
        Supplier<Vector2dc> posToHit
    ) {
    return new PointerWeapon(
            weaponName,
            baseCooldown,
            baseBurst,
            baseProjAtOnce,
            posToHit,
            ProjectileStats.getBuilder()
                .damage(baseDamage)
                // FISICA: Usiamo una lambda che mantiene l'offset rispetto all'owner
                .physics((dt, atkInfo) -> still(dt, atkInfo))
                .radius(hbRadius)
                .velocity(0) // Melee di solito non ha velocità propria
                .ttl(baseTTL) // Usare un valore basso, es. 0.1 - 0.2
                .owner(ownedBy)
                .id(weaponName)
                .immortal(immortal)
                .build(),
            (level, weaponStats) -> {
                weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, level);
                // Per il melee, aumentare il livello spesso aumenta il raggio (range)
                weaponStats.getProjStats().setMultiplier(
                    ProjStatType.HITBOX, 
                    weaponStats.getProjStats().getStatValue(ProjStatType.HITBOX) + level * 2
                );
            },
            weaponStats -> meleeSpawn(weaponStats)
        );
    }

    private Vector2d still(final double deltaTime, final AttackContext atkInfo) {
        return new Vector2d(atkInfo.getLastPosition());
    }

    private static List<AttackContext> meleeSpawn(final WeaponStats weaponStats) {
        final Vector2dc origin = weaponStats.getProjStats().getOwner().getPosition();
        final double velocity = weaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY);
        
        final Vector2d direction = new Vector2d(
            Utilities.normalizeVector(weaponStats.getPosToHit().get())
        ).mul(60);

        final Vector2d finalTarget = new Vector2d(origin).add(direction);
        return List.of(new AttackContext(
            new Vector2d(finalTarget), 
            velocity, 
            () -> finalTarget
        ));
    }
}
