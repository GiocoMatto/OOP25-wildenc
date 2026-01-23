package it.unibo.wildenc.mvc.model.weaponary.weapons;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.Type;
import it.unibo.wildenc.mvc.model.weaponary.AttackMovementInfo;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ConcreteProjectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Implementation of a generic {@link Weapon}. This will be used as a 
 * schematic to model all {@link Weapons}s each with different characteristics.
 */
public class GenericWeapon implements Weapon {

    private WeaponStats weaponStats;
    private Function<Type, String> typeNameFunction;
    private int level = 0;
    private long timeAtLastAtk;

    public GenericWeapon(
        double cooldown, double dmg, double vel, Type type, String id,
        double hitboxRadius, BiFunction<Vector2d, AttackMovementInfo, Vector2d> movement, 
        Function<Type, String> nameFunc, BiConsumer<Integer, WeaponStats> upgradeLogics
    ) {
        this.weaponStats = new WeaponStats(
            cooldown, dmg, vel, type, id, hitboxRadius, movement, upgradeLogics, nameFunc
        );
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public Optional<Projectile> attack(Vector2d startingPoint, Vector2d atkDirection) {
        final long timestamp = System.currentTimeMillis();
        if(!isInCooldown(timestamp)) {
            this.timeAtLastAtk = timestamp;
            return Optional.ofNullable(
                    new ConcreteProjectile(
                    this.weaponStats.projDamage(),
                    this.weaponStats.projType(),
                    this.weaponStats.hbRadius(),
                    this.weaponStats.projID(),
                    startingPoint,
                    new AttackMovementInfo(atkDirection, this.weaponStats.projVelocity()),
                    this.weaponStats.moveFunction()
                )
            );
        } else {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public void upgrade() {
        this.weaponStats.upgradeLogics().accept(this.level, this.weaponStats);
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public String getName() {
        return this.typeNameFunction.apply(this.weaponStats.projType());
    }
    
    // TODO: Remove this method. This is used for testing purposes only.
    public WeaponStats getStats() {
        return this.weaponStats;
    }

    private boolean isInCooldown(final long timestamp) {
        return timestamp - timeAtLastAtk > this.weaponStats.weaponCooldown();
    }
}
