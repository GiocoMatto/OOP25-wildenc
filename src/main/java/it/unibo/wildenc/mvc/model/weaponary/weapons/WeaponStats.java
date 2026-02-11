package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;

/**
 * Class for managing the statistics of a Weapon, such as
 * cooldown, burst size, Projectiles shot at once...
 */
public class WeaponStats {
    private double weaponCooldown;
    private int burstSize;
    private int currentLevel = 1;
    private int projectilesAtOnce;
    private ProjectileStats pStats;
    private Supplier<Vector2dc> posToHit;
    private final BiConsumer<Integer, WeaponStats> upgradeLogics;

    /**
     * Constructor for the class.
     * 
     * @param initialCooldown the initial cooldown of the weapon
     * @param projStats the statistics of the Projectile(s) this weapon is shooting
     * @param initialBurst the initial number of Projectiles in a burst
     * @param initialProjQuantity the initial number of Projectiles shot at once
     * @param upLogics the logics for upgrading the weapon.
     */
    public WeaponStats(
        final double initialCooldown,
        final ProjectileStats projStats,
        final int initialBurst,
        final int initialProjQuantity,
        final Supplier<Vector2dc> initialPosToHit,
        final BiConsumer<Integer, WeaponStats> upLogics
    ) {
        this.weaponCooldown = initialCooldown;
        this.pStats = projStats;
        this.burstSize = initialBurst;
        this.projectilesAtOnce = initialProjQuantity;
        this.upgradeLogics = upLogics;
        this.posToHit = initialPosToHit;
    }

    /**
     * Getter method for the cooldown of the weapon.
     * 
     * @return the cooldown of the weapon.
     */
    public double getCooldown() {
        return this.weaponCooldown;
    }

    /**
     * Setter method for the cooldown of the weapon.
     * 
     * @param newCD the new cooldown of the weapon.
     */
    public void setCooldown(final double newCD) {
        this.weaponCooldown = newCD;
    }

    /**
     * Getter method for the current burst size.
     * 
     * @return the current burst size.
     */
    public int getCurrentBurstSize() {
        return this.burstSize;
    }
    
    /**
     * Setter method for the burst size.
     * 
     * @param newBurstSize the new size of the burst.
     */
    public void setBurstSize(final int newBurstSize) {
        this.burstSize = newBurstSize;
    }

    /**
     * Getter method for the statistic of the Projectile this weapon
     * is shooting.
     * 
     * @return the statistics of the projectile in form of {@link ProjectileStats}.
     */
    public ProjectileStats getProjStats() {
        return this.pStats;
    }

    /**
     * Getter method for the number of projectiles shot in one attack.
     * 
     * @return how many projectiles are shot at once.
     */
    public int getProjectilesShotAtOnce() {
        return this.projectilesAtOnce;
    }

    /**
     * Method for increasing the number of projectile shot 
     * in one attack by one unit.
     */
    public void increaseProjectilesShotAtOnce() {
        this.projectilesAtOnce++;
    }

    /**
     * Getter method for the current level of the weapon.
     * 
     * @return the weapon's current level.
     */
    public int getLevel() {
        return this.currentLevel;
    }

    /**
     * Method for levelling up the weapon. This will use the
     * upgradeLogics {@link BiConsumer} making the necessary
     * changes to the weapon as it is upgraded.
     */
    public void levelUp() {
        this.currentLevel++;
        this.upgradeLogics.accept(this.currentLevel, this);
    }

    /**
     * Getter method for getting the position the projectiles will hit.
     * 
     * @return the supplier for the position to hit.
     */
    public Supplier<Vector2dc> getPosToHit() {
        return this.posToHit;
    }

    /**
     * Method for setting a new position the projectiles will hit.
     * 
     * @param newPosToHit the new supplier for the position to hit.
     */
    protected void setPosToHit(final Supplier<Vector2dc> newPosToHit) {
        this.posToHit = newPosToHit;
    }
}
