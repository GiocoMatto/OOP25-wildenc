package it.unibo.wildenc.Weaponary;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.FirstWeapon;
import it.unibo.wildenc.mvc.model.Projectile;
import it.unibo.wildenc.mvc.model.Type;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.AbstractWeapon.WeaponStats;

import org.joml.Vector2d;

public class TestWeapons {
    /**
     * Test for a FirstWeapon shooting basic Projectiles.
     */
    private Weapon firstWeaponTest;

    @BeforeEach
    public void initTest() {
        this.firstWeaponTest = new FirstWeapon(
            1, 1, 1, Type.WATER, 1,
            (sp, vel) -> new Vector2d(sp.x() + vel, sp.y() + vel)
        );
    }

    @Test
    public void testStatsCorrect() {
        final WeaponStats currentWeaponStats = firstWeaponTest.getStats();
        assertEquals("Bollaraggio", this.firstWeaponTest.getName());
        assertTrue(currentWeaponStats.projDamage() == 1.0);
        assertTrue(currentWeaponStats.projVelocity() == 1.0);
        assertTrue(currentWeaponStats.projType() == Type.WATER);
    }

    @Test
    public void testProjectileCreation() {
        final Projectile testProj = this.firstWeaponTest.attack(new Vector2d(0, 0));
        assertTrue(testProj.getClass().getSimpleName().equals("ConcreteProjectile"));
        assertTrue(testProj.getPosition().equals(new Vector2d(0, 0)));
        assertTrue(testProj.getType() == Type.WATER);
        assertTrue(testProj.getDamage() == 1.0);
    }

    @Test
    public void testProjectileMovement() {
        final Projectile testProj = this.firstWeaponTest.attack(new Vector2d(0, 0));
        assertTrue(testProj.getPosition().equals(new Vector2d(0, 0)));
        testProj.move();
        assertTrue(testProj.getPosition().equals(new Vector2d(1, 1)));
        testProj.move();
        assertFalse(testProj.getPosition().equals(new Vector2d(1, 1)));
        testProj.move();
        assertTrue(testProj.getPosition().equals(new Vector2d(3, 3)));
    }
}
