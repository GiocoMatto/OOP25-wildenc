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
    public PlayerImpl(final Vector2dc startPos, final double hitbox, final double speed, final int maxHealth) {
        // inizializzazione con valori iniziali
        super(startPos, hitbox, speed, maxHealth, new LinkedHashSet<>());
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
        this.expToNextLevel = this.level * BASE_EXP_STEP;

        //aumenta vita massima al level up quando le armi sono maxate
        //final double newMaxHP = this.getMaxHealth() + 20.0;
        //this.setMaxHealth(newMaxHP);

        //aumento velocità al level up quando le armi sono maxate
        //final double newSpeed = this.getSpeed() * 1.03;
        //this.setSpeed(newSpeed);

        System.out.println("LEVEL UP, level: " + level);
    }
    
    /**
     * Method for exp bar in the View
     * @return necessary exp to complete the level
     */
    public int getExpToNextLevel() {
        return this.expToNextLevel;
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

        while(this.experience >= this.expToNextLevel) {
            //il player ottiene tanta exp e sale di più livelli in una botta
            this.experience = this.experience - this.expToNextLevel; //l'eccesso rimane per il prossimo libello
            levelUp();
        }
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