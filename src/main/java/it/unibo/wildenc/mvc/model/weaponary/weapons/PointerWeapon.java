package it.unibo.wildenc.mvc.model.weaponary.weapons;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.model.Projectile;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ConcreteProjectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;

/**
 * Specialization of GenericWeapon that allows changes to the position to hit.
 * This was made for weapons that can change their position dynamically.
 */
public class PointerWeapon extends GenericWeapon {

    public PointerWeapon(
        String weaponName, 
        double initialCooldown, 
        int initialBurst, 
        int initialProjAtOnce,
        ProjectileStats pStats, 
        BiConsumer<Integer, WeaponStats> upgradeLogics,
        Function<WeaponStats, List<AttackContext>> attackInfoGenerator
    ) {
        super(weaponName, initialCooldown, initialBurst, initialProjAtOnce, pStats, upgradeLogics, attackInfoGenerator);
    }

    /**
     * {@inheritDoc}
     */
    public void setPosToHit(final Supplier<Vector2dc> newPosToHit) {
        this.getStats().setPosToHit(newPosToHit);
    }

    /**
     * Method for generating projectiles off an AttackContext list.
     * Note that in this case, the following position is calculated
     * based off the one which is updated in the weapon.
     */
    protected Set<Projectile> generateProjectiles(final List<AttackContext> contexts) {
    contexts.stream()
        .forEach(e -> e.setFollowing(this.getStats().getPosToHit()));
    return contexts.stream()
        .map(e -> new ConcreteProjectile(e, this.getStats().getProjStats()))
        .collect(Collectors.toSet());
    }
}
