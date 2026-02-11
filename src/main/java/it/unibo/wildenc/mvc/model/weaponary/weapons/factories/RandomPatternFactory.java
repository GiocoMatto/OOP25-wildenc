package it.unibo.wildenc.mvc.model.weaponary.weapons.factories;

import java.util.List;
import java.util.Random;
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
import it.unibo.wildenc.util.Utilities;

public class RandomPatternFactory implements WeaponFactory{

    private static final int DS_X = 300;
    private static final int DS_Y = 130;

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
        return new GenericWeapon(
            weaponName,
            baseCooldown,
            baseBurst,
            baseProjAtOnce,
            posToHit,
            ProjectileStats.getBuilder()
                .damage(baseDamage)
                .physics((dt, atkInfo) -> still(dt, atkInfo))
                .radius(hbRadius)
                .velocity(baseVelocity)
                .ttl(baseTTL)
                .owner(ownedBy)
                .id(weaponName)
                .immortal(immortal)
                .build(),
            (level, weaponStats) -> {
                weaponStats.getProjStats().setMultiplier(ProjStatType.DAMAGE, (level / 10) + 1);
                weaponStats.getProjStats().setMultiplier(ProjStatType.HITBOX, (level / 10) + 1);
                if (level % 5 == 0) {
                    weaponStats.setBurstSize(
                        weaponStats.getCurrentBurstSize() + 1
                    );
                }
            },
            weaponStats -> randomSpawn(weaponStats)
        );
    }

    private Vector2d still(final double deltaTime, final AttackContext atkInfo) {
        return new Vector2d(atkInfo.getLastPosition());
    }

    private List<AttackContext> randomSpawn(final WeaponStats weaponStats) {
        final Vector2d randomDir = generateRandomPositionFrom(
            weaponStats.getProjStats()
                .getOwner()
                .getPosition()
        );
        return List.of(
            new AttackContext(
                randomDir,
                0, 
                () -> randomDir
            )
        );   
    }

    private Vector2d generateRandomPositionFrom(final Vector2dc initialPos) {
        final Random rand = new Random();
        final Vector2d origin = new Vector2d(initialPos);
        final Vector2d randomDir = 
            switch (rand.nextInt(2)) {
                case 1 -> new Vector2d(
                    Utilities.pickRandom(List.of(-DS_X, DS_X)), 
                    rand.nextInt(-DS_Y, DS_Y)
                );
                default -> new Vector2d(
                    rand.nextInt(-DS_X, DS_X), 
                    Utilities.pickRandom(List.of(-DS_Y, DS_Y))
                );
            };
        origin.add(randomDir);
        return origin;
    }
}
