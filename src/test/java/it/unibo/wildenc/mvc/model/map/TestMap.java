package it.unibo.wildenc.mvc.model.map;

import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.GameMap.PlayerType;
import it.unibo.wildenc.mvc.model.enemies.EnemySpawnerImpl;
import it.unibo.wildenc.mvc.model.Enemy;

import it.unibo.wildenc.mvc.model.map.MapTestingCommons.MapObjectTest;
import it.unibo.wildenc.mvc.model.map.MapTestingCommons.MovableObjectTest;
import it.unibo.wildenc.mvc.model.map.MapTestingCommons.TestDirections;
import it.unibo.wildenc.mvc.model.map.MapTestingCommons.TestObject;
import it.unibo.wildenc.mvc.model.map.MapTestingCommons.TestWeapon;
import it.unibo.wildenc.mvc.model.map.objects.TestMapObjects;
import it.unibo.wildenc.mvc.model.player.PlayerImpl;
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
import java.util.function.Function;
import java.util.LinkedHashSet;


import org.joml.Vector2d;

import org.junit.jupiter.api.Test;

// FIXME: fix all tests: gamemap now handles projectiles and natural enemy spawn
public class TestMap {

    private GameMap getEmptyMapWithObjects(Player player, Set<MapObject> objs) {
        return new GameMapImpl(player, (p, s) -> Set.of(), objs);
    }

    private GameMap getMapWithEnemySpawner(Player player) {
        return new GameMapImpl(player, new EnemySpawnerImpl(player), Set.of());
    }

    private Player getEmptyPlayer() {
        return TestObject.PlayerObject.getAsPlayer();
    }
    
    private Player getArmedPlayer(Function<Player, Weapon> w) {
        Player p = getEmptyPlayer();
        p.addWeapon(w.apply(p));
        return p;
    }
    
    @Test
    void objectsShouldBeAddedToMap() {
        final TestObject objConf = TestObject.StaticObject;
        final MapObject obj = objConf.getAsStaticObj();
        final GameMap map = getEmptyMapWithObjects(getEmptyPlayer(), Set.of(obj));

        assertTrue(map.getAllObjects().contains(obj));
    }

    @Test
    void staticObjectsShouldNotMove() {
        final TestObject objConf = TestObject.StaticObject;
        final MapObjectTest obj = objConf.getAsStaticObj();
        final GameMap map = getEmptyMapWithObjects(getEmptyPlayer(), Set.of(obj));
        
        map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.vect);

        assertEquals(obj.getPosition(), objConf.pos);
    }

    @Test
    void movableObjWithNoDirectionShouldNotMove() {
        final TestObject objConf = TestObject.MovableObject;
        final MovableObjectTest obj = objConf.getAsMovableObj();
        final GameMap map = getEmptyMapWithObjects(getEmptyPlayer(), Set.of(obj));

        map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.vect);

        assertEquals(obj.getPosition(), objConf.pos);
    }

    @Test
    void movableObjWithDirectionShouldMoveCorrectly() {
        final TestObject objConf = TestObject.MovableObject;
        final TestDirections direction = TestDirections.RIGHT;
        final MovableObjectTest obj = objConf.getAsMovableObj();
        final GameMap map = getEmptyMapWithObjects(getEmptyPlayer(), Set.of(obj));

        obj.setDirection(direction.vect);
        map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.vect);

        assertNotEquals(objConf.pos, obj.getPosition(), "Object did not move");
        assertEquals(calculateMovement(objConf.pos, direction.vect, objConf.speed, TEST_TIME_SECONDS), obj.getPosition(), "Object moved wrong");
    }

    
    @Test
    void whenEnemyProjectileHitboxTouchesPlayerHitboxPlayerHealthShouldDecrease() {
        final TestObject enemyConf = TestObject.EnemyObject;
        final Player p = getEmptyPlayer();
        final Enemy enemy = enemyConf.getAsCloseRangeEnemy(new LinkedHashSet<>(), "testEnemy", Optional.of(p));
        final var weapon = TestWeapon.DEFAULT_WEAPON.getAsWeapon(enemy, p.getPosition());
        final GameMap map = getEmptyMapWithObjects(p, Set.of(enemy));
        
        enemy.addWeapon(weapon);

        // Enemy should arrive in player hitbox at the 20th tick
        for (int i = 0; i < TEST_SIMULATION_TICKS; i++) {
            map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.vect);
        }

        assertTrue(p.getCurrentHealth() < p.getMaxHealth(), "Player health didn't change.");
        assertTrue(enemy.getCurrentHealth() == enemy.getMaxHealth(), "Enemy health must not change.");
    }

    @Test
    void whenPlayerProjectileHitboxTouchesEnemyHitboxEnemyHealthShouldDecrease() {
        final TestObject enemyConf = TestObject.EnemyObject;
        final Player p = getArmedPlayer(o -> TestWeapon.DEFAULT_WEAPON.getAsWeapon(o, enemyConf.pos));
        final Enemy enemy = enemyConf.getAsCloseRangeEnemy(new LinkedHashSet<>(), "testEnemy", Optional.of(p));
        final GameMap map = getEmptyMapWithObjects(p, Set.of(enemy));

        // Enemy should arrive in player hitbox at the 20th tick
        for (int i = 0; i < TEST_SIMULATION_TICKS; i++) {
            map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.vect);
        }

        assertTrue(p.getCurrentHealth() == p.getMaxHealth(), "Player health must not change.");
        assertTrue(enemy.getCurrentHealth() < enemyConf.health, "Enemy health didn't change.");
    }
    
    @Test
    void mapSpawnsEnemiesCorrectly() {
        final GameMap map = getMapWithEnemySpawner(getEmptyPlayer());
        var initialSize = map.getAllObjects().size();
        
        map.spawnEnemies();

        assertTrue(map.getAllObjects().size() > initialSize, "No enemies were spawend.");
    }

    @Test
    void spawnedEnemiesFollowAndShootPlayer() {
        
    }
    
}
