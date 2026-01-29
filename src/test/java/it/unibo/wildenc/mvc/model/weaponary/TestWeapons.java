package it.unibo.wildenc.mvc.model.weaponary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.joml.Vector2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.Weapon.WeaponStats;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.Projectile;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

public class TestWeapons {

    private static final Vector2d FORTYFIVE_DEG_VERSOR = new Vector2d(1,1).normalize();
    private WeaponFactory weaponMaker = new WeaponFactory();
    private Weapon currentWeapon;
    private List<Projectile> generatedProjectiles;

    @BeforeEach
    public void setup() {
        this.currentWeapon = weaponMaker.getDefaultWeapon(10.0, 10.0, 2.0, 1.0, 10.0, 1);
        generatedProjectiles = new ArrayList<>();
    }

    @Test
    public void testWeaponCreation() {
        WeaponStats currentWeaponStats = currentWeapon.getStats();
        assertTrue(currentWeaponStats.burstSize() == 1);
        assertTrue(currentWeaponStats.weaponCooldown() == 10.0);
        ProjectileStats currentWeaponProjStats = currentWeaponStats.pStats();
        assertTrue("BasicProj".equals(currentWeaponProjStats.getID()));
        assertTrue(currentWeaponProjStats.getStatValue(ProjStatType.DAMAGE) == 10.0);
        assertTrue(currentWeaponProjStats.getStatValue(ProjStatType.HITBOX) == 2.0);
        assertTrue(currentWeaponProjStats.getStatValue(ProjStatType.VELOCITY) == 1.0);
        assertTrue(currentWeaponProjStats.getTTL() == 10.0);
    }

    @Test
    public void testAttack() {
        generatedProjectiles.addAll(this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0, 0), FORTYFIVE_DEG_VERSOR, Optional.empty()))
        ));
        assertTrue(!generatedProjectiles.isEmpty());
        assertTrue(generatedProjectiles.getFirst().getPosition().equals(new Vector2d(0.0, 0.0)));
        assertEquals(FORTYFIVE_DEG_VERSOR.x(), generatedProjectiles.getFirst().getDirection().x(), 1E-6, "");
        assertTrue(generatedProjectiles.getFirst().isAlive());
        assertTrue("BasicProj".equals(generatedProjectiles.getFirst().getID()));
    }

    @Test
    public void testMovement() {
        generatedProjectiles.addAll(this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty()))
        ));
        assertTrue(!generatedProjectiles.isEmpty());
        final double expectedValue = Math.cos(Math.toRadians(45));
        final Projectile generatedProjectile = generatedProjectiles.getFirst();
        generatedProjectile.updatePosition(1);
        assertTrue(generatedProjectile.getPosition().distance(new Vector2d(expectedValue, expectedValue)) < 1E-6);
        generatedProjectile.updatePosition(1);
        assertTrue(generatedProjectile.getPosition().distance(new Vector2d(2 * expectedValue, 2 * expectedValue)) < 1E-6);
        generatedProjectile.updatePosition(1);
        assertTrue(generatedProjectile.getPosition().distance(new Vector2d(3 * expectedValue, 3 * expectedValue)) < 1E-6);
        generatedProjectile.updatePosition(10);
        assertTrue(generatedProjectile.getPosition().distance(new Vector2d(13 * expectedValue, 13 * expectedValue)) < 1E-6);
    }

    @Test
    public void testBarrage() throws InterruptedException {
        // Creating first Projectile
        Set<Projectile> generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty()))
        );
        // Projectile exists!
        assertTrue(!generatedProj.isEmpty());

        // Waiting 100ms and trying to shoot again.
        Thread.sleep(100);
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty()))
        );
        // Nope, this time is not present.
        assertFalse(!generatedProj.isEmpty());

        // After 200ms, the 2nd projectile of the burst appears!
        Thread.sleep(100);
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty()))
        );
        assertTrue(!generatedProj.isEmpty());

        // Waiting another 300ms for not a projectile to appear.
        Thread.sleep(300);
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty()))
        );
        assertFalse(!generatedProj.isEmpty());

        // In the end, after 1.2s (1s cd + 200ms of barrage...) a new Projectile appears!
        Thread.sleep(700);
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty()))
        );
        assertTrue(!generatedProj.isEmpty());
    }
}
