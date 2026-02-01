package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.function.BiConsumer;

import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;

public class WeaponStats {
    private double weaponCooldown;
    private int burstSize;
    private int currentLevel = 1;
    private int projectilesAtOnce;
    private final ProjectileStats pStats;
    private final BiConsumer<Integer, WeaponStats> upgradeLogics;

    public WeaponStats(
        final double initialCooldown,
        final ProjectileStats projStats,
        final int initialBurst,
        final int initialProjQuantity,
        final BiConsumer<Integer, WeaponStats> upLogics
    ) {
        this.weaponCooldown = initialCooldown;
        this.pStats = projStats;
        this.burstSize = initialBurst;
        this.projectilesAtOnce = initialProjQuantity;
        this.upgradeLogics = upLogics;
    }

    public double getCooldown() {
        return this.weaponCooldown;
    }

    public void setCooldown(final double newCD) {
        this.weaponCooldown = newCD;
    }

    public int getCurrentBurstSize() {
        return this.burstSize;
    }

    public void setBurstSize(final int newBurstSize) {
        this.burstSize = newBurstSize;
    }

    public ProjectileStats getProjStats() {
        return this.pStats;
    }

    public int getProjectilesShotAtOnce() {
        return this.projectilesAtOnce;
    }

    public void increaseProjectilesShotAtOnce() {
        this.projectilesAtOnce++;
    }

    public BiConsumer<Integer, WeaponStats> getUpgradeLogics() {
        return this.upgradeLogics;
    }

    public int getLevel() {
        return this.currentLevel;
    }

    public void levelUp() {
        this.currentLevel++;
        this.upgradeLogics.accept(this.currentLevel, this);
    }
}
