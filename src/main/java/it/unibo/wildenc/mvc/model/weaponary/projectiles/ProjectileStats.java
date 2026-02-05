package it.unibo.wildenc.mvc.model.weaponary.projectiles;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;

/**
 * Class for managing the statistics for a Projectile. This is made for mantaining SRP and
 * making a Projectile fully customizable on declaration of a specific weapon.
 */
public class ProjectileStats {

    private final Set<ProjStat> projStats = new LinkedHashSet<>();
    private final double timeToLive;
    private final String projID;
    private final Entity projOwner;
    private final BiFunction<Double, AttackContext, Vector2d> projMovementFunction;
    private final Supplier<Vector2dc> positionToFollow;

    /**
     * Constructor for the class. This will be passed to a Projectile when it will be generated,
     * and it's used by the weapon to memorize which kind of projectile it can shoot.
     * 
     * @param baseDamage the base damage of the Projectile
     * @param baseRadius the base radius of the hitbox of the Projectile
     * @param baseVelocity the base movement velocity of the Projectile (could be angular in case of orbitals)
     * @param ttl the time of life of the Projectile, after which it's considered gone
     * @param id an identifier for the Projectile
     * @param ownedBy the {@link Entity} who generated this Projectile
     * @param toFollow a {@link Supplier} of {@link Vector2dc} which represent a 
     *      position which the Projectile has to follow
     * @param moveFunc the function that defines the Projectile's movement
     */
    public ProjectileStats(
        final double baseDamage,
        final double baseRadius,
        final double baseVelocity,
        final double ttl,
        final String id,
        final Entity ownedBy,
        final Supplier<Vector2dc> toFollow,
        final BiFunction<Double, AttackContext, Vector2d> moveFunc

    ) {
        projStats.add(new ProjStat(ProjStatType.DAMAGE, baseDamage));
        projStats.add(new ProjStat(ProjStatType.HITBOX, baseRadius));
        projStats.add(new ProjStat(ProjStatType.VELOCITY, baseVelocity));
        this.timeToLive = ttl;
        this.projID = id;
        this.projOwner = ownedBy;
        this.positionToFollow = toFollow;
        this.projMovementFunction = moveFunc;
    }

    /**
     * Getter method for returning a specific statistic of the Projectile,
     * such as DAMAGE, VELOCITY...
     * 
     * @param statType the statistic (specified via a {@link StatType}) which value is needed
     * @return the current value assumed by the statistic, or 0.0 if the stat wasn't found.
     */
    public double getStatValue(final ProjStatType statType) {
        try {
            return projStats.stream()
            .filter(e -> e.getType() == statType)
            .findFirst().get().getValue();
        } catch (final NoSuchElementException statNotFound) {
            return 0.0;
        }
    }

    /**
     * Getter method that returns the movement function of the projectile.
     * The projectile will start in an initial position, and the informations
     * used to move to a next position are passed in form of an {@link AttackMovementInfo}
     * 
     * @return a {@link BiFunction} representing the movement function of the Projectile
     */
    public BiFunction<Double, AttackContext, Vector2d> getMovementFunction() {
        return this.projMovementFunction;
    }

    /**
     * Getter method for returning the supplier that is currently giving
     * the direction for the projectile to follow.
     * 
     * @return a {@link Supplier} of {@link Vector2dc} which 
     *      contains the position which the projectile has to follow.
     */
    public Supplier<Vector2dc> getPositionToHit() {
        return this.positionToFollow;
    }

    /**
     * Getter method for the ID of the Projectile.
     * Every Projectile has an ID because it will be useful to differentiate
     * different kinds of projectiles.
     * 
     * @return a {@link String} containing the ID of the Projectile.
     */
    public String getID() {
        return this.projID;
    }

    /**
     * Getter method for the Time To Live of the projectile.
     * The Time To Live is a statistic that represents an interval of time
     * in which the Projectile is considered "alive". Once elapsed, the Projectile
     * should be considered "dead" and removed from the game.
     * 
     * @return the "Time To Live" of the projectile, in seconds. 
     */
    public double getTTL() {
        return this.timeToLive;
    }

    /**
     * Getter method for the owner of the projectile.
     * 
     * @return the {@link Entity} that generated this Projectile.
     */
    public Entity getOwner() {
        return this.projOwner;
    }

    /**
     * Setter method for changing the multiplier of a specific statistic
     * of the projectile. This will be highly used when upgrading a Weapon.
     * If the stat wasn't found, no change will be made.
     * 
     * @param statType the {@link ProjStat} which multiplier has to change
     * @param newMult the new multiplier
     */
    public void setMultiplier(final ProjStatType statType, final double newMult) {
        projStats.stream()
            .filter(e -> e.getType() == statType)
            .findFirst().get().setMult(newMult);
    }

    /**
     * Enum for memorizing different Stats type.
     * This was done to explicity differentiate every stat of a weapon.
     */
    public enum ProjStatType {
        DAMAGE("Damage"),
        VELOCITY("Velocity"),
        HITBOX("Hitbox Radius");

        private final String statName;

        ProjStatType(final String name) {
            this.statName = name;
        }

        /**
         * toString() method for ProjStatType.
         * 
         * @return the name of the corresponding ProjStatType.
         */
        @Override
        public String toString() {
            return this.statName;
        }
    }

    /**
     * Class for managing a specific statistic of the projectile.
     * Every statistic has a type, a base value, and a multiplier.
     * When a Weapon will be upgraded, the multiplier will change
     * according to the level of the weapon.
     * Ex. Assuming the formula lvl * baseMult as the upgrade formula for a Weapon,
     * if the base damage of a weapon is 10, at level 3 the effective damage will be 30.
     */
    private class ProjStat {
        private final ProjStatType statType;
        private final double baseValue;
        private double currentMultiplier = 1.0;

        /**
         * Constructor for the class.
         * 
         * @param type the type of stat to memorize.
         * @param val the base value for the specific stat.
         */
        ProjStat(final ProjStatType type, final double val) {
            this.statType = type;
            this.baseValue = val;
        }

        /**
         * Getter method for the stat type.
         * 
         * @return the memorized stat type.
         */
        private ProjStatType getType() {
            return this.statType;
        }

        /**
         * Setter method for setting a new multiplier for the stat.
         * 
         * @param newMult the new multiplier that will be set.
         */
        private void setMult(final double newMult) {
            this.currentMultiplier = newMult;
        }

        /**
         * Getter method that gives the effective value of the statistic.
         * 
         * @return the effective value of the statistic (base value times multiplier)
         */
        private double getValue() {
            return this.baseValue * currentMultiplier;
        }
    }
}
