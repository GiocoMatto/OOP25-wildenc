package it.unibo.wildenc.mvc.model.map;

import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.Enemy;

import it.unibo.wildenc.mvc.model.map.MapTestingCommons.MapObjectTest;
import it.unibo.wildenc.mvc.model.map.MapTestingCommons.MovableObjectTest;
import it.unibo.wildenc.mvc.model.map.MapTestingCommons.TestDirections;
import it.unibo.wildenc.mvc.model.map.MapTestingCommons.TestObject;
import it.unibo.wildenc.mvc.model.weaponary.AttackContext;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;

import static it.unibo.wildenc.mvc.model.map.MapTestingCommons.TEST_SIMULATION_TICKS;
import static it.unibo.wildenc.mvc.model.map.MapTestingCommons.TEST_TIME_NANOSECONDS;
import static it.unibo.wildenc.mvc.model.map.MapTestingCommons.TEST_TIME_SECONDS;
import static it.unibo.wildenc.mvc.model.map.MapTestingCommons.calculateMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;

import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestMap {
    
    GameMap map;
    Player player;

    @BeforeEach
    void setup() {
        player = TestObject.PlayerObject.getAsPlayer();
        map = new GameMapImpl(player);
    }

    @Test
    void objectsShouldBeAddedToMap() {
        final TestObject objConf = TestObject.StaticObject;
        final MapObjectTest obj = objConf.getAsStaticObj();

        map.addObject(obj);

        assertTrue(map.getAllObjects().contains(obj));
    }

    @Test
    void staticObjectsShouldNotMove() {
        final TestObject objConf = TestObject.StaticObject;
        final MapObjectTest obj = objConf.getAsStaticObj();

        map.addObject(obj);
        map.updateEntities(TEST_TIME_NANOSECONDS);

        assertEquals(obj.getPosition(), objConf.pos);
    }

    @Test
    void movableObjWithNoDirectionShouldNotMove() {
        final TestObject objConf = TestObject.MovableObject;
        final MovableObjectTest obj = objConf.getAsMovableObj();

        map.addObject(obj);
        map.updateEntities(TEST_TIME_NANOSECONDS);

        assertEquals(obj.getPosition(), objConf.pos);
    }

    @Test
    void movableObjWithDirectionShouldMoveCorrectly() {
        final TestObject objConf = TestObject.MovableObject;
        final TestDirections direction = TestDirections.RIGHT;
        final MovableObjectTest obj = objConf.getAsMovableObj();
        map.addObject(obj);

        obj.setDirection(direction.vect);
        map.updateEntities(TEST_TIME_NANOSECONDS);

        assertNotEquals(objConf.pos, obj.getPosition(), "Object did not move");
        assertEquals(calculateMovement(objConf.pos, direction.vect, objConf.speed, TEST_TIME_SECONDS), obj.getPosition(), "Object moved wrong");
    }

    @Test
    void whenEnemyProjectileHitboxTouchesPlayerHitboxPlayerHealthShouldDecrease() {
        final TestObject enemyConf = TestObject.EnemyObject;
        final Enemy enemy = enemyConf.getAsCloseRangeEnemy(new LinkedHashSet<>(), "testEnemy", Optional.of(player));
        final var weapon = new WeaponFactory().getDefaultWeapon(5, 10, 2, 2, 100101, 1, enemy);
        enemy.addWeapons(weapon);
        map.addObject(enemy);

        // Enemy should arrive in player hitbox at the 20th tick
        for (int i = 0; i < TEST_SIMULATION_TICKS; i++) {
            map.updateEntities(TEST_TIME_NANOSECONDS);
            enemy.getWeapons()
                .forEach(e -> e.attack(List.of(new AttackContext(
                    enemy.getPosition(), 
                    new Vector2d(player.getPosition()).sub(enemy.getPosition()),
                    Optional.empty())), 0)
                .forEach(e2 -> map.addObject(e2)));
        }

        assertTrue(player.getCurrentHealth() < player.getMaxHealth(), "Player health didn't change.");
        assertTrue(enemy.getCurrentHealth() == enemy.getMaxHealth(), "Enemy health must not change.");
    }

    @Test
    void whenPlayerProjectileHitboxTouchesEnemyHitboxEnemyHealthShouldDecrease() {
        final TestObject enemyConf = TestObject.EnemyObject;
        final Enemy enemy = enemyConf.getAsCloseRangeEnemy(new LinkedHashSet<>(), "testEnemy", Optional.of(player));
        map.addObject(enemy);
        final var weapon = new WeaponFactory().getDefaultWeapon(0.009, 10, 2, 2, 100101, 1, player);
        player.addWeapons(weapon);
        
        // Enemy should arrive in player hitbox at the 20th tick
        for (int i = 0; i < TEST_SIMULATION_TICKS; i++) {
            map.updateEntities(TEST_TIME_NANOSECONDS);
            player.getWeapons()
                .forEach(e -> e.attack(List.of(new AttackContext(
                    player.getPosition(), 
                    new Vector2d(enemy.getPosition()).sub(player.getPosition()),
                    Optional.empty())), 0)
                .forEach(e2 -> map.addObject(e2)));
        }

        assertTrue(player.getCurrentHealth() == player.getMaxHealth(), "Player health must not change.");
        assertTrue(enemy.getCurrentHealth() < enemyConf.health, "Enemy health didn't change.");
    }
}
