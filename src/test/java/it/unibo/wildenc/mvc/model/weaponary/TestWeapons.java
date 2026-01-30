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

    private static final double TEST_COOLDOWN = 1.0;
    private static final double TEST_DMG = 10.0;
    private static final double TEST_RADIUS = 2.0;
    private static final double TEST_VELOCITY = 1.0;
    private static final double TEST_TTL = 10.0;
    private static final int TEST_BURST = 2;
    
    private WeaponFactory weaponMaker = new WeaponFactory();
    private Weapon currentWeapon;
    private List<Projectile> generatedProjectiles;

    @BeforeEach
    public void setup() {
        // TODO: make this a real entity.
        this.currentWeapon = weaponMaker.getDefaultWeapon(
            TEST_COOLDOWN,
            TEST_DMG,
            TEST_RADIUS,
            TEST_VELOCITY,
            TEST_TTL,
            TEST_BURST,
            null
        );
        generatedProjectiles = new ArrayList<>();
    }

    @Test
    public void testWeaponCreation() {
        WeaponStats currentWeaponStats = currentWeapon.getStats();
        assertTrue(currentWeaponStats.burstSize() == TEST_BURST);
        assertTrue(currentWeaponStats.weaponCooldown() == TEST_COOLDOWN);
        ProjectileStats currentWeaponProjStats = currentWeaponStats.pStats();
        assertTrue("BasicProj".equals(currentWeaponProjStats.getID()));
        assertTrue(currentWeaponProjStats.getStatValue(ProjStatType.DAMAGE) == TEST_DMG);
        assertTrue(currentWeaponProjStats.getStatValue(ProjStatType.HITBOX) == TEST_RADIUS);
        assertTrue(currentWeaponProjStats.getStatValue(ProjStatType.VELOCITY) == TEST_VELOCITY);
        assertTrue(currentWeaponProjStats.getTTL() == TEST_TTL);
    }

    @Test
    public void testAttack() {
        generatedProjectiles.addAll(this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0, 0), FORTYFIVE_DEG_VERSOR, Optional.empty())),
            0
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
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty())),
            0
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
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty())),
            0
        );
        // Projectile exists!
        assertTrue(!generatedProj.isEmpty());
        // Waiting 100ms and trying to shoot again.
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty())),
            0.1
        );
        // Nope, this time is not present.
        assertFalse(!generatedProj.isEmpty());

        // After 200ms, the 2nd projectile of the burst appears!
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty())),
            0.1
        );
        assertTrue(!generatedProj.isEmpty());

        // Waiting another 300ms for not a projectile to appear.
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty())),
            0.3
        );
        assertFalse(!generatedProj.isEmpty());

        // In the end, after 1.2s (1s cd + 200ms of barrage...) a new Projectile appears!
        generatedProj = this.currentWeapon.attack(
            List.of(new AttackContext(new Vector2d(0.0, 0.0), FORTYFIVE_DEG_VERSOR, Optional.empty())),
            0.9
        );
        assertTrue(!generatedProj.isEmpty());
    }
}
