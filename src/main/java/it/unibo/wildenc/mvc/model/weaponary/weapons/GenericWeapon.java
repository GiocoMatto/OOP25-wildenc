package it.unibo.wildenc.mvc.model.weaponary.weapons;

import it.unibo.wildenc.mvc.model.Projectile;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ConcreteProjectile;
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

    private final String weaponName;
    Function<WeaponStats, List<AttackContext>> attackInfoGenerator;
    private WeaponStats weaponStats;
    private double timeSinceLastAtk = Double.MAX_VALUE;
    private int currentBullet = 0;

    public GenericWeapon(
        final String weaponName,
        final double initialCooldown,
        final int initialBurst,
        final int initialProjAtOnce,
        final ProjectileStats pStats,
        final BiConsumer<Integer, WeaponStats> upgradeLogics,
        final Function<WeaponStats, List<AttackContext>> attackInfoGenerator
    ) {
        this.weaponStats = new WeaponStats(
            initialCooldown,
            pStats,
            initialBurst,
            initialProjAtOnce,
            upgradeLogics
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
            return generateProjectiles(this.attackInfoGenerator.apply(this.weaponStats));
        }
        return Set.of();
    }

    /**
     * {@inheritDocs}
     */
    @Override
    public void upgrade() {
        this.weaponStats.levelUp();
    }
    
    // This method is used for testing purposes only.
    public WeaponStats getStats() {
        return this.weaponStats;
    }

    private boolean isInCooldown() {
        return timeSinceLastAtk < this.weaponStats.getCooldown();
    }

    private boolean canBurst() {
        return !isInCooldown() ? true :
            (currentBullet < this.weaponStats.getCurrentBurstSize() && timeSinceLastAtk >= BURST_DELAY);
    }

    private Set<Projectile> generateProjectiles(List<AttackContext> contexts) {
        return contexts.stream()
            .map(e -> new ConcreteProjectile(e, this.weaponStats.getProjStats()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return this.weaponName;
    }
}
