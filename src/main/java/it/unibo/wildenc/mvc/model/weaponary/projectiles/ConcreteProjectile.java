package it.unibo.wildenc.mvc.model.weaponary.projectiles;

import java.util.Optional;
import java.util.function.Supplier;

import org.joml.Vector2d;

import it.unibo.wildenc.mvc.model.map.objects.AbstractMovable;
import it.unibo.wildenc.mvc.model.weaponary.AttackMovementInfo;
import it.unibo.wildenc.mvc.model.weaponary.ProjectileStats;

/**
 * Implementation of a generic {@link Projectile}. This will be used 
 * as a schematic for modelling any projectile weapons can shoot.
 */
public class ConcreteProjectile extends AbstractMovable implements Projectile {

    private static final double MS_TO_S = 1000.0;

    private ProjectileStats projStats;
    private Vector2d movementDirection;
    private Optional<Supplier<Vector2d>> followThis;
    private long lastMovement = System.currentTimeMillis();

    /**
     * Constructor of the class.
     * @param pStats the statistics of the generated projectile
     * @param direction the direction the projectile has to follow
     * @param startPos the position where the projectile starts
     * @param toFollow a {@link Optional} containing a {@link Supplier} 
     *  of a position that the projectile has to follow
     */
    public ConcreteProjectile(
        final ProjectileStats pStats,
        final Vector2d direction,
        final Vector2d startPos,
        final Optional<Supplier<Vector2d>> toFollow
    ) {
        super(startPos, pStats.getStatValue("Hitbox"), pStats.getStatValue("Velocity"));
        this.movementDirection = direction;
        this.projStats = pStats;
        this.followThis = toFollow;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosition(final double deltaTime) {
        if(followThis.isPresent()) {
            this.getWritablePosition().set(followThis.get().get());
        }
        this.getWritablePosition().set(this.projStats.getMovementFunction().apply(
                new Vector2d(this.getWritablePosition()),
                new AttackMovementInfo(
                    movementDirection, deltaTime, this.projStats.getStatValue("Velocity")
                )
            )
        );
        lastMovement = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDamage() {
        return this.projStats.getStatValue("Damage");
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
}
