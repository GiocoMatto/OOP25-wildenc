package it.unibo.wildenc.mvc.model.enemies;

import java.util.List;
import org.joml.Vector2dc;
import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;
import it.unibo.wildenc.mvc.model.*;

public abstract class AbstractEnemy extends AbstractEntity implements Enemy {
    private final MapObject target;
    private final String name;
    
    /**
     * Create a General Enemy without the logic of attack.
     * @param health
     * @param weapons
     * @param name
     */
    public AbstractEnemy(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeedfinal,
        final int health, 
        final List<Weapon> weapons, 
        final String name,
        final MapObject target
    ) {
        super(spawnPosition, hitbox, movementSpeedfinal, health, weapons);
        this.name = name;
        this.target = target;
    }
    
    public String getName() {
        return this.name;
    }

    public MapObject getTarget() {
        return this.target;
    }
}
