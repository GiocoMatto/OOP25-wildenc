package it.unibo.wildenc.mvc.model.weaponary.projectiles;

import java.util.function.BiFunction;
import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.Type;
import it.unibo.wildenc.mvc.model.weaponary.AttackMovementInfo;

/**
 * Implementation of a generic {@link Projectile}. This will be used 
 * as a schematic for modelling any projectile weapons can shoot.
 */
public class ConcreteProjectile implements Projectile {

    private ProjectileStats projStats;

    /**
     * Constructor for the class. This will initialize the statistics of the projectile.
     * @param dmg the damage of the projectile.
     * @param type the {@link Type} of the projectile.
     * @param hitboxRadius the hitbox's radius.
     * @param projID the identifier of the weapon that shot this projectile.
     * @param initialPosition the position where this projectile starts at.
     * @param movement an {@link AttackMovementInfo} containing all the informations
     *  regarding the projectile's movement.
     * @param func a {@link BiFunction} with the physics the projectile has to follow
     *  to move in the space.
     */
    public ConcreteProjectile(
        double dmg, Type type, double hitboxRadius, String projID, Vector2d initialPosition, 
        AttackMovementInfo movement, BiFunction<Vector2d, AttackMovementInfo, Vector2d> func
    ) {
        this.projStats = new ProjectileStats(
            dmg, movement, type, func, projID, hitboxRadius, initialPosition
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void move() {
        this.projStats.updatePosition(
            this.projStats.movingFunc().apply(
                this.projStats.currentPosition(), this.projStats.movementInfo()
            )
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2d getPosition() {
        return this.projStats.currentPosition();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamage() {
        return this.projStats.damage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return this.projStats.type();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getHitbox() {
        return this.projStats.hitboxRadius();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getID(){
        return this.projStats.id();
    }
}
