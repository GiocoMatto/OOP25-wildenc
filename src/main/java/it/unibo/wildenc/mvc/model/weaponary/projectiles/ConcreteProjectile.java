package it.unibo.wildenc.mvc.model.weaponary.projectiles;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;

/**
 * Implementation of a generic {@link Projectile}. This will be used 
 * as a schematic for modelling any projectile weapons can shoot.
 */
public class ConcreteProjectile extends AbstractMovable implements Projectile {

    private static final double MS_TO_S = 1000.0;

    private ProjectileStats projStats;
    private AttackContext attackInformation;
    private long lastMovement = System.currentTimeMillis();

    /**
     * Constructor of the class.
     * @param pStats the stats of the generated Projectile, in form of a {@link ProjectileStats}
     * @param attackInfo the informations about the initial state of the projectile in form of
     *  a {@link AttackInfo}
     */
    public ConcreteProjectile(
        final AttackContext atkInfo,
        final ProjectileStats pStats
    ) {
        super(pStats.getOwner().getPosition(), pStats.getStatValue(ProjStatType.HITBOX), pStats.getStatValue(ProjStatType.VELOCITY));
        this.projStats = pStats;
        this.attackInformation = atkInfo;
    }

    /**
     * {@inheritDoc}
     * Specific method implementation for generalizing the movement of the Projectile.
     * When an attack is made, the Projectile is just being generated in a specific position
     * but it won't move yet. When an updatePosition is called on a Projectile, it will be moving
     * followng it's movement function, which is specified at the moment of the creation in its
     * ProjectileStats. After this, there are 2 main cases for the generation of the Projectile:
     *  - It could be moving according to physics' laws (for example, by going straight in a direction)
     *  - Else, it could lock its position to the position specified at followThis (for example giving the player an aura
     *      that moves with him)
     *  If the generated Projectile doesn't need to use angles, it's initial angle and it's angular velocity will be set to 0.
     */
    @Override
    public void updatePosition(final double deltaTime) {
        this.getWritablePosition().set(this.projStats.getMovementFunction().apply(
            deltaTime,
            attackInformation
        ));
        this.attackInformation.updateLastPosition(getPosition());
        lastMovement = System.currentTimeMillis();
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
    public String getID(){
        return this.projStats.getID();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAlive() {
        return (System.currentTimeMillis() - lastMovement) / MS_TO_S < this.projStats.getTTL();
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

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }
}

