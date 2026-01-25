package it.unibo.wildenc.mvc.model.enemies;

import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;
import org.locationtech.jts.geom.*;

public class RangedEnemy extends AbstractEnemy {
    private static final int MIN_DISTANCE = 80;
    private static final int MAX_DISTANCE = 100;
    private final GeometryFactory gf = new GeometryFactory();

    public RangedEnemy(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeedfinal, 
        final int health,
        final Set<Weapon> weapons, 
        final String name,
        final MapObject target
    ) {
        super(
            spawnPosition, 
            hitbox, 
            movementSpeedfinal, 
            health, 
            weapons, 
            name,
            target
        );
    }

    @Override
    public Vector2dc alterDirection() {
        final var posTarget = getTarget().getPosition();
        final var minArea = gf.createPoint(
            new Coordinate(
                posTarget.x(), 
                posTarget.y()
            )
        ).buffer(MIN_DISTANCE, 64);
        final var maxArea = gf.createPoint(
            new Coordinate(
                posTarget.x(), 
                posTarget.y()
            )
        ).buffer(MAX_DISTANCE, 64);
        final var posEnemy = gf.createPoint(
            new Coordinate(
                this.getPosition().x(),
                this.getPosition().y()
            )
        );
        if (!maxArea.contains(posEnemy)) {
            return direction(getTarget().getPosition(), this.getPosition()).normalize();
        } else if (!minArea.contains(posEnemy)) {
            return new Vector2d(0 ,0);
        }
        return direction(this.getPosition(), getTarget().getPosition()).normalize();
    }

}
