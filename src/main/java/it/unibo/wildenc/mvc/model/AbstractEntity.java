package it.unibo.wildenc.mvc.model;

import it.unibo.wildenc.mvc.model.weaponary.weapons.Weapon;
import it.unibo.wildenc.mvc.model.mapobjects.AbstractMovable;
import java.util.Collections;
import java.util.List;

import org.joml.Vector2dc;

public abstract class AbstractEntity extends AbstractMovable implements Entity {

    private  final int health;
    private  List<Weapon> weapons;

    public AbstractEntity(
        final Vector2dc spawnPosition, 
        final double hitbox, 
        final double movementSpeedfinal,
        final int health, 
        final List<Weapon> weapons
    ) {
        super(spawnPosition, hitbox, movementSpeedfinal);
        this.health = health;
        this.weapons = weapons;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public List<Weapon> getWeapons() {
        return Collections.unmodifiableList(weapons);
    }

    @Override
    public boolean takeDamage(int dmg) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void updatePosition(final long deltaTime) {
        var vec = specificMovement();
        super.setDirection(vec);
        super.updatePosition(deltaTime);
    }
    
    /**
     * Specify the logic of movement of the enemy type.
     * @return If the enemy can move in the direction specified.
     */
    public abstract Vector2dc specificMovement();

}
