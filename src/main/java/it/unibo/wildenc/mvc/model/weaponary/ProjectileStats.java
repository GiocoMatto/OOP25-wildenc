package it.unibo.wildenc.mvc.model.weaponary;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

import org.joml.Vector2d;

public class ProjectileStats {

    private class Stat {
        private final String statName;
        private final double baseValue;
        private double currentMultiplier = 1.0;

        Stat(final String name, final double val) {
            this.statName = name;
            this.baseValue = val;
        }

        private String getName() {
            return this.statName;
        }

        private void setMult(final double newMult) {
            this.currentMultiplier = newMult;
        }

        private double getValue() {
            return this.baseValue * currentMultiplier;
        }
    }

    private final List<Stat> projStats = new ArrayList<>();
    private final double timeToLive;
    private final String projID;
    private final BiFunction<Vector2d, AttackMovementInfo, Vector2d> projMovementFunction;

    public ProjectileStats(
        final double baseDamage,
        final double baseRadius,
        final String id,
        final double baseVelocity,
        final double ttl,
        final BiFunction<Vector2d, AttackMovementInfo, Vector2d> moveFunc
    ) {
        projStats.add(new Stat("Damage", baseDamage));
        projStats.add(new Stat("Hitbox", baseRadius));
        projStats.add(new Stat("Velocity", baseVelocity));
        this.timeToLive = ttl;
        this.projID = id;
        this.projMovementFunction = moveFunc;
    }

    public double getStatValue(final String statName) {
        try {
            return projStats.stream()
            .filter(e -> e.getName().equals(statName))
            .findFirst().get().getValue();
        } catch (final NoSuchElementException statNotFound) {
            // TODO: Remove this System.out.println.
            System.out.println("The stat" + statName + " was not found." + statNotFound);
            return 0.0;
        }
    }

    public BiFunction<Vector2d, AttackMovementInfo, Vector2d> getMovementFunction() {
        return this.projMovementFunction;
    }

    public String getID() {
        return this.projID;
    }

    public double getTTL() {
        return this.timeToLive;
    }

    public void setMultiplier(final String statName, final double newMult) {
        try {
            projStats.stream()
            .filter(e -> e.getName().equals(statName))
            .findFirst().get().setMult(newMult);
        } catch (final NoSuchElementException statNotFound){
            // TODO: Remove this System.out.println.
            System.out.println("The stat" + statName + " was not found." + statNotFound);
        }
    }
}
