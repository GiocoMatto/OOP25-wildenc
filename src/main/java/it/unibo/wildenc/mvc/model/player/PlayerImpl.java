package it.unibo.wildenc.mvc.model.player;

import java.util.LinkedHashSet;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.entities.AbstractEntity;

/**
 * Implementation of the Player entity
 */
public class PlayerImpl extends AbstractEntity implements Player {

    private final Vector2d inputDirection = new Vector2d(0, 0); //ultima direzione richiesta dall'utente
    private int experience;
    private int level;

    /**
     * Creates a new Player.
     * * @param startPos  Starting position on the map
     * @param hitbox    Hitbox radius
     * @param speed     Movement speed
     * @param maxHealth Maximum health
     */
    public PlayerImpl(final Vector2dc startPos, final double hitbox, final double speed, final int maxHealth) {
        // inizializzazione con valori iniziali
        super(startPos, hitbox, speed, maxHealth, new LinkedHashSet<>());
        this.experience = 0;
        this.level = 1;
    }

    @Override
    protected Vector2dc alterDirection() { 
        //il plauyer risponde all'input salvato in inputDirection.
        return this.inputDirection;
    }

    @Override
    public boolean canTakeDamage() {
        //il giocatore non è mai invulnerabile
        return true; 
    }

    public void setDirection(final Vector2dc direction) {
        //aggioro vetotre che alterDirection() legge al prossimo update
        this.inputDirection.set(direction);
    }

    @Override
    public int getExp() {
        return this.experience;
    }

    @Override
    public void levelUp() {
        this.level++;
        // TODO: Implementare logica di aumento statistiche o scelta weapon
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getName'");
    }

    @Override
    public boolean canLevelUp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'canLevelUp'");
    }

}