package it.unibo.wildenc.mvc.model.weaponary.projectiles;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Projectile;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;

/**
 * Implementation of a generic {@link Projectile}. This will be used 
 * as a schematic for modelling any projectile weapons can shoot.
 */
public class ConcreteProjectile extends AbstractMovable implements Projectile {
    
    private final ProjectileStats projStats;
    private final AttackContext attackInformation;
    private double timePassed;

    /**
     * Constructor of the class.
     * 
     * @param pStats the stats of the generated Projectile, in form of a {@link ProjectileStats}
     * @param atkInfo the informations about the initial state of the projectile in form of
     *      a {@link AttackContext}
     */
    public ConcreteProjectile(
        final AttackContext atkInfo,
        final ProjectileStats pStats
    ) {
        super(
            atkInfo.getLastPosition(), 
            pStats.getStatValue(ProjStatType.HITBOX),
            pStats.getStatValue(ProjStatType.VELOCITY)
        );
        this.projStats = pStats;
        this.timePassed = 0;
        this.attackInformation = atkInfo;
    }

    /**
     * {@inheritDoc}
     * Specific method implementation for generalizing the movement of the Projectile.
     * Uses the movement function of the projectile and its {@link AttackContext}.
     */
    @Override
    public void updatePosition(final double deltaTime) {
        this.getWritablePosition().set(this.projStats.getMovementFunction().apply(
            deltaTime,
            attackInformation
        ));
        this.attackInformation.updateLastPosition(getPosition());
        this.timePassed += deltaTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamage() {
        return this.projStats.getStatValue(ProjStatType.DAMAGE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "projectile:" + this.projStats.getID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return this.timePassed < this.projStats.getTTL();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vector2dc getDirection() {
        return this.attackInformation.getDirectionVersor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entity getOwner() {
        return this.projStats.getOwner();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isImmortal() {
        return this.projStats.isImmortal();
    }
}

