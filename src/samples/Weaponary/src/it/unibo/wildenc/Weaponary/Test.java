package it.unibo.wildenc.Weaponary;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;

import it.unibo.wildenc.Weaponary.AbstractWeapon.WeaponStats; // Make this another class.

public class Test {
    /**
     * Test for a FirstWeapon shooting basic Projectiles.
     */
    private Weapon firstWeaponTest;

    @org.junit.Before
    public void initTest() {
        this.firstWeaponTest = new FirstWeapon(
            1, 1, Type.WATER,
            (sp, vel) -> new Point2D(sp.x() + vel, sp.y() + vel),
            "Disintegratore"
        );
    }

    @org.junit.Test
    public void testStatsCorrect() {
        final WeaponStats currentWeaponStats = firstWeaponTest.getStats();
        assertEquals("Disintegratore", this.firstWeaponTest.getName());
        assertTrue(currentWeaponStats.projDamage() == 1.0);
        assertTrue(currentWeaponStats.projVelocity() == 1.0);
        assertTrue(currentWeaponStats.projType() == Type.WATER);
    }

    @org.junit.Test
    public void testProjectileCreation() {
        final Projectile testProj = this.firstWeaponTest.attack(new Point2D(0, 0));
        assertTrue(testProj.getClass().getSimpleName().equals("ConcreteProjectile"));
        assertTrue(testProj.getPosition().isEqual(new Point2D(0, 0)));
        assertTrue(testProj.getType() == Type.WATER);
        assertTrue(testProj.getDamage() == 1.0);
    }
    @org.junit.Test
    public void testProjectileMovement() {
        final Projectile testProj = this.firstWeaponTest.attack(new Point2D(0, 0));
        assertTrue(testProj.getPosition().isEqual(new Point2D(0, 0)));
        testProj.move();
        assertTrue(testProj.getPosition().isEqual(new Point2D(1, 1)));
        testProj.move();
        assertFalse(testProj.getPosition().isEqual(new Point2D(1, 1)));
        testProj.move();
        assertTrue(testProj.getPosition().isEqual(new Point2D(3, 3)));
    }
}
