package it.unibo.wildenc.mvc.model.player;

import java.util.LinkedHashSet;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.entities.AbstractEntity;

/**
 * Implementation of the Player entity
 */
public class PlayerImpl extends AbstractEntity implements Player {

    // private final Vector2d inputDirection = new Vector2d(0, 0); //ultima direzione richiesta dall'utente
    private int experience;
    private int level;
    private int money;

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
        this.money = 0;
    }

    @Override
    protected Vector2dc alterDirection() { 
        //il plauyer risponde all'input salvato in inputDirection.
        return getDirection();
    }

    @Override
    public boolean canTakeDamage() {
        //il giocatore non è mai invulnerabile
        return false; 
    }

    public void setDirection(final Vector2dc direction) {
        //aggioro vetotre che alterDirection() legge al prossimo update
        super.setDirection(direction);
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
        return "player:player"; // FIXME: Add ID field to player and rename this to "player:" + this.playerid
    }

    @Override
    public boolean canLevelUp() {
        return false;
    }

    @Override
    public void addExp(final int amount) {
        this.experience = this.experience + amount;
    }

    @Override
    public void addMoney(int amount) {
        this.money = this.money + amount;
    }

    @Override
    public int getMoney() {
        return this.money;
    }

    @Override
    public void heal(int amount) {
        double newHealth = Math.min(this.getMaxHealth(), this.getCurrentHealth() + amount);
        this.setHealth(newHealth);
    }

}