package it.unibo.wildenc.mvc.model.weaponary.weapons;

import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ConcreteProjectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of a generic {@link Weapon}. This will be used as a 
 * schematic to model all {@link Weapons}s each with different characteristics.
 */
public class GenericWeapon implements Weapon {

    private static final double BURST_DELAY = 0.2;

    private WeaponStats weaponStats;
    private int level = 0;
    private double timeSinceLastAtk = Double.MAX_VALUE;
    private final String weaponName;
    private int currentBullet = 0;
    Function<ProjectileStats, List<AttackContext>> attackInfoGenerator;

    public GenericWeapon(
        final double cooldown,
        final ProjectileStats pStats,
        final BiConsumer<Integer, WeaponStats> upgradeLogics,
        final Function<ProjectileStats, List<AttackContext>> attackInfoGenerator,
        final int initialBurst,
        final String weaponName
    ) {
        this.weaponStats = new WeaponStats(
            cooldown, pStats, initialBurst, upgradeLogics
        );
        this.attackInfoGenerator = attackInfoGenerator;
        this.weaponName = weaponName;
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public Set<Projectile> attack(final double deltaTime) {
        this.timeSinceLastAtk += deltaTime;
        if(canBurst()) {
            if(!isInCooldown()) {
                currentBullet = 0;
            }
            currentBullet++;
            timeSinceLastAtk = 0;
            return generateProjectiles(this.attackInfoGenerator.apply(this.weaponStats.pStats()));
        }
        return Set.of();
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public void upgrade() {
        this.weaponStats.upgradeLogics().accept(this.level, this.weaponStats);
    }
    
    // This method is used for testing purposes only.
    public WeaponStats getStats() {
        return this.weaponStats;
    }

    private boolean isInCooldown() {
        return timeSinceLastAtk < this.weaponStats.weaponCooldown();
    }

    private boolean canBurst() {
        return !isInCooldown() ? true :
            (currentBullet < this.weaponStats.burstSize() && timeSinceLastAtk >= BURST_DELAY);
    }

    private Set<Projectile> generateProjectiles(List<AttackContext> contexts) {
        return contexts.stream()
            .map(e -> new ConcreteProjectile(e, this.weaponStats.pStats()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return this.weaponName;
    }
}
