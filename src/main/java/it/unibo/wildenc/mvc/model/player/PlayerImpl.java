package it.unibo.wildenc.mvc.model.player;

import java.util.LinkedHashSet;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.entities.AbstractEntity;

/**
 * Implementation of the Player entity
 */
public class PlayerImpl extends AbstractEntity implements Player {

    private static final int BASE_EXP_STEP = 100; //costante per il calcolo

    // private final Vector2d inputDirection = new Vector2d(0, 0); //ultima direzione richiesta dall'utente
    private String playerName;
    private int experience;
    private int level;
    private int expToNextLevel;
    private int money;

    /**
     * Creates a new Player.
     * * @param startPos  Starting position on the map
     * @param hitbox    Hitbox radius
     * @param speed     Movement speed
     * @param maxHealth Maximum health
     */
    public PlayerImpl(
        final String name,
        final Vector2dc startPos, 
        final double hitbox, 
        final double speed, 
        final double maxHealth
    ) {
        // inizializzazione con valori iniziali
        super(startPos, hitbox, speed, maxHealth, new LinkedHashSet<>());
        this.playerName = name;
        this.experience = 0;
        this.level = 1;
        this.money = 0;
        this.expToNextLevel = this.level * BASE_EXP_STEP;
    }

    @Override
    protected Vector2dc alterDirection() { 
        //il plauyer risponde all'input salvato in inputDirection.
        return getDirection();
    }

    @Override
    public boolean canTakeDamage() {
        //il giocatore non è mai invulnerabile
        return true; 
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
        this.experience = this.experience - this.expToNextLevel; //l'eccesso rimane per il prossimo libello
        this.level++;
        this.expToNextLevel = this.level * BASE_EXP_STEP;

        //aumenta vita massima al level up quando le armi sono maxate
        final double newMaxHP = this.getMaxHealth() + 1.0;
        this.setMaxHealth(newMaxHP);

        //aumento velocità al level up quando le armi sono maxate
        final double newSpeed = this.getSpeed() * 1.005;
        this.setSpeed(newSpeed);

        System.out.println("LEVEL UP, level: " + level);
    }
    
    @Override
    public int getExpToNextLevel() {
        return this.expToNextLevel;
    }

    @Override
    public String getName() {
        return "player:" + this.playerName;
    }

    @Override
    public boolean canLevelUp() {
        return this.experience >= this.expToNextLevel;
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

    @Override
    public int getLevel() {
        return level;
    }

}