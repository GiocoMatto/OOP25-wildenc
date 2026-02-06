package it.unibo.wildenc.mvc.model.weaponary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.wildenc.mvc.model.Entity;

import it.unibo.wildenc.mvc.model.Projectile;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponStats;

public class TestWeapons {
    
    private static final int TEST_COOLDOWN = 1;
    private static final int TEST_DAMAGE = 1;
    private static final int TEST_HITBOX = 1;
    private static final int TEST_VELOCITY = 1;
    private static final int TEST_TTL = 1;
    private static final int TEST_BURST_SIZE = 1;
    private static final int TEST_PROJ_AT_ONCE = 1;
    private static final int TEST_MAX_HEALTH = 100;
    private static final int LEVEL_2 = 2;
    private static final Entity TEST_OWNER = new PlayerImpl(
        new Vector2d(0, 0), 
        TEST_HITBOX,
        TEST_VELOCITY,
        TEST_MAX_HEALTH
    );

    private static final Vector2dc TEST_DIRECTION_VERSOR_RIGHT = new Vector2d(1, 0);
    private static final Vector2dc TEST_DIRECTION_VERSOR_UP = new Vector2d(0, 1);
    private static final double TEST_TICK = 0.2;
    private static final Logger LOGGER = LogManager.getLogger("Ciao!");

    private Weapon myWeapon;
    private WeaponFactory weapFactory = new WeaponFactory();
    private Vector2dc positionToHit = new Vector2d(30, 0);
    private Set<Projectile> generatedProjectiles;

    @BeforeEach
    public void setup() {
        this.myWeapon = weapFactory.getDefaultWeapon(
            TEST_COOLDOWN, 
            TEST_DAMAGE, 
            TEST_HITBOX, 
            TEST_VELOCITY, 
            TEST_TTL, 
            TEST_PROJ_AT_ONCE,
            TEST_BURST_SIZE, 
            TEST_OWNER,
            () -> positionToHit
        );
        this.generatedProjectiles = new LinkedHashSet<>();
        Configurator.setRootLevel(Level.DEBUG);
    }

    @Test
    public void testCorrectInitialization() {
        final WeaponStats testWeaponStats = myWeapon.getStats();
        assertEquals(testWeaponStats.getProjStats().getStatValue(ProjStatType.DAMAGE), TEST_DAMAGE);
        assertEquals(testWeaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY), TEST_VELOCITY);
        assertEquals(testWeaponStats.getProjStats().getStatValue(ProjStatType.HITBOX), TEST_HITBOX);
        assertEquals(testWeaponStats.getProjStats().getTTL(), TEST_TTL);
        assertEquals(testWeaponStats.getCooldown(), TEST_COOLDOWN);
        assertEquals(testWeaponStats.getCurrentBurstSize(), TEST_BURST_SIZE);
        assertEquals(testWeaponStats.getProjectilesShotAtOnce(), TEST_PROJ_AT_ONCE);
        assertEquals(testWeaponStats.getProjStats().getOwner(), TEST_OWNER);
    }

    @Test
    public void testCorrectProjectileGeneration() {
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        assertFalse(this.generatedProjectiles.isEmpty());
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE);
        assertTrue(
            this.generatedProjectiles.stream()
                .map(proj -> proj.getDirection())
                .allMatch(projDir -> projDir.equals(TEST_DIRECTION_VERSOR_RIGHT, 1E-9))
        );
        assertTrue(
            this.generatedProjectiles.stream()
                .map(proj -> proj.getPosition())
                .allMatch(projPos -> projPos.equals(TEST_OWNER.getPosition(), 1E-9))  
        );
    }

    @Test
    public void testCorrectProjectileGenerationWithDirectionChange() {
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        assertFalse(this.generatedProjectiles.isEmpty());
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE);
        assertTrue(
            this.generatedProjectiles.stream()
                .map(proj -> proj.getDirection())
                .allMatch(projDir -> projDir.equals(TEST_DIRECTION_VERSOR_RIGHT, 1E-9))
        );
        this.generatedProjectiles.clear();
        this.positionToHit = new Vector2d(0, 30);
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_COOLDOWN));
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE);
        assertTrue(
            this.generatedProjectiles.stream()
                .map(proj -> proj.getDirection())
                .allMatch(projDir -> projDir.equals(TEST_DIRECTION_VERSOR_UP, 1E-9))
        );
    }

    @Test
    public void testCorrectUpgrade() {
        this.myWeapon.upgrade();
        assertEquals(this.myWeapon.getStats().getLevel(), LEVEL_2);
        assertEquals(this.myWeapon.getStats().getProjStats().getStatValue(ProjStatType.DAMAGE), LEVEL_2 * 5);
        assertEquals(this.myWeapon.getStats().getProjStats().getStatValue(ProjStatType.VELOCITY), LEVEL_2);
        assertEquals(this.myWeapon.getStats().getProjStats().getStatValue(ProjStatType.HITBOX), TEST_HITBOX + LEVEL_2);
        assertEquals(this.myWeapon.getStats().getCurrentBurstSize(), LEVEL_2);
    }

    @Test
    public void testCorretBurstAndCooldown() {
        this.myWeapon.upgrade();
        // 6 * TICK = 1.2s which is (0 + 200ms of burst + 1000ms of cooldown)
        for(int i = 0; i < 30; i++) {
            this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        }
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE * LEVEL_2 * 5);
    }

    @Test
    public void testMultipleProjectileAttack() {
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        assertEquals(this.generatedProjectiles.size(), TEST_PROJ_AT_ONCE);
        for(int i = 0; i < 10; i++) {
            LOGGER.info("Iterazione " + i);
            this.generatedProjectiles.stream()
                .peek(proj -> LOGGER.info("X: " + proj.getPosition().x() + " Y: " + proj.getPosition().y()))
                .forEach(proj -> proj.updatePosition(TEST_TICK));
        }
    }
}


