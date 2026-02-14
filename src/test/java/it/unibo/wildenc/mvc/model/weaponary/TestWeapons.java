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
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Movable;
import it.unibo.wildenc.mvc.model.Projectile;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.dataloaders.StatLoader;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
import it.unibo.wildenc.mvc.model.weaponary.projectiles.ProjectileStats.ProjStatType;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponStats;

/**
 * Testing class for weapons.
 */
class TestWeapons {
    private static final int TEST_COOLDOWN = 1;
    private static final int TEST_DAMAGE = 10;
    private static final int TEST_HITBOX = 1;
    private static final int TEST_VELOCITY = 2;
    private static final int TEST_TTL = 99;
    private static final int TEST_BURST_SIZE = 3;
    private static final int TEST_PROJ_AT_ONCE = 3;
    private static final int TEST_MAX_HEALTH = 100;
    private static final int LEVEL_2 = 2;
    private static final Entity TEST_OWNER = new PlayerImpl(
        "TestPlayer",
        new Vector2d(0, 0), 
        TEST_HITBOX,
        TEST_VELOCITY,
        TEST_MAX_HEALTH
    );
    private static final Vector2dc TEST_DIRECTION_VERSOR_RIGHT = new Vector2d(1, 0);
    private static final Vector2dc TEST_DIRECTION_VERSOR_UP = new Vector2d(0, 1);
    private static final String TEST_OWNER_NAME = "player:TestPlayer";
    private static final Vector2dc POSITION_TO_HIT = new Vector2d(30, 0);
    private static final Vector2dc NEW_POSITION_TO_HIT = new Vector2d(0, 30);
    private static final int TEST_TICKS = 30;
    private static final double DELTA_NOMOV = 1E-9;
    private static final int TEST_MULT = 5;

    private static final double TEST_TICK = 0.2;
    private static final Logger LOGGER = LogManager.getLogger("Ciao!");

    private Weapon myWeapon;
    private Vector2dc currentPosToHit = POSITION_TO_HIT;
    private Set<Projectile> generatedProjectiles;

    /**
     * Setups a weapon before every test.
     * A TestingPistol is here used.
     */
    @BeforeEach
    void setup() {
        this.myWeapon = StatLoader.getInstance().getWeaponFactoryForWeapon(
            "testingpistol", 
            TEST_OWNER, 
            () -> currentPosToHit
        );
        this.generatedProjectiles = new LinkedHashSet<>();
        Configurator.setRootLevel(Level.DEBUG);
    }

    /**
     * Tests the weapon to be correctly initialized.
     */
    @Test
    void testCorrectInitialization() {
        final WeaponStats testWeaponStats = myWeapon.getStats();
        assertEquals(testWeaponStats.getProjStats().getStatValue(ProjStatType.DAMAGE), TEST_DAMAGE);
        assertEquals(testWeaponStats.getProjStats().getStatValue(ProjStatType.VELOCITY), TEST_VELOCITY);
        assertEquals(testWeaponStats.getProjStats().getStatValue(ProjStatType.HITBOX), TEST_HITBOX);
        assertEquals(testWeaponStats.getProjStats().getTTL(), TEST_TTL);
        assertEquals(testWeaponStats.getCooldown(), TEST_COOLDOWN);
        assertEquals(testWeaponStats.getCurrentBurstSize(), TEST_BURST_SIZE);
        assertEquals(testWeaponStats.getProjectilesShotAtOnce(), TEST_PROJ_AT_ONCE);
        assertEquals(testWeaponStats.getProjStats().getOwnerName(), TEST_OWNER_NAME);
        assertFalse(testWeaponStats.getProjStats().isImmortal());
    }

    /**
     * Tests if the weapon can correctly shoot projectiles.
     */
    @Test
    void testCorrectProjectileGeneration() {
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        assertFalse(this.generatedProjectiles.isEmpty());
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE);
        assertTrue(
            this.generatedProjectiles.stream()
                .map(Movable::getDirection)
                .allMatch(projDir -> projDir.equals(TEST_DIRECTION_VERSOR_RIGHT, DELTA_NOMOV))
        );
        assertTrue(
            this.generatedProjectiles.stream()
                .map(MapObject::getPosition)
                .allMatch(projPos -> projPos.equals(TEST_OWNER.getPosition(), DELTA_NOMOV))
        );
    }

    /**
     * Tests if a projectile is correctly generated, changing direction.
     */
    @Test
    void testCorrectProjectileGenerationWithDirectionChange() {
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        assertFalse(this.generatedProjectiles.isEmpty());
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE);
        assertTrue(
            this.generatedProjectiles.stream()
                .map(Movable::getDirection)
                .allMatch(projDir -> projDir.equals(TEST_DIRECTION_VERSOR_RIGHT, DELTA_NOMOV))
        );
        this.generatedProjectiles.clear();
        this.currentPosToHit = NEW_POSITION_TO_HIT;
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_COOLDOWN));
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE);
        assertTrue(
            this.generatedProjectiles.stream()
                .map(Movable::getDirection)
                .allMatch(projDir -> projDir.equals(TEST_DIRECTION_VERSOR_UP, DELTA_NOMOV))
        );
    }

    /**
     * Tests if the weapon upgrades correctly.
     */
    @Test
    void testCorrectUpgrade() {
        this.myWeapon.upgrade();
        assertEquals(this.myWeapon.getStats().getLevel(), LEVEL_2);
        assertEquals(this.myWeapon.getStats().getProjStats().getStatValue(ProjStatType.DAMAGE), LEVEL_2 * TEST_DAMAGE);
        assertEquals(this.myWeapon.getStats().getProjStats().getStatValue(ProjStatType.VELOCITY), LEVEL_2 * TEST_VELOCITY);
        assertEquals(this.myWeapon.getStats().getProjStats().getStatValue(ProjStatType.HITBOX), TEST_HITBOX + LEVEL_2);
        assertEquals(this.myWeapon.getStats().getCurrentBurstSize(), LEVEL_2);
    }

    /**
     * Tests cooldown and burst of the weapon.
     */
    @Test
    void testCorretBurstAndCooldown() {
        this.myWeapon.upgrade();
        // 6 * TICK = 1.2s which is (0 + 200ms of burst + 1000ms of cooldown)
        for (int i = 0; i < TEST_TICKS; i++) {
            this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        }
        assertEquals(this.generatedProjectiles.size(), TEST_BURST_SIZE * LEVEL_2 * TEST_MULT);
    }

    /**
     * Tests for multiple projectile attacks.
     */
    @Test
    void testMultipleProjectileAttack() {
        this.generatedProjectiles.addAll(this.myWeapon.attack(TEST_TICK));
        assertEquals(this.generatedProjectiles.size(), TEST_PROJ_AT_ONCE);
        for (int i = 0; i < 10; i++) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Iterazione " + i);
            }
            this.generatedProjectiles.stream()
                .peek(proj -> LOGGER.info("X: " + proj.getPosition().x() + " Y: " + proj.getPosition().y()))
                .forEach(proj -> proj.updatePosition(TEST_TICK));
        }
    }
}


