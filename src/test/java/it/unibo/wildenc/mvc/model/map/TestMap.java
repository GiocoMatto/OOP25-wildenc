package it.unibo.wildenc.mvc.model.map;

import it.unibo.wildenc.mvc.model.GameMap;
import it.unibo.wildenc.mvc.model.MapObject;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.enemies.EnemySpawnerImpl;
import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.MapObjectTest;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.MovableObjectTest;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.TestDirections;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.TestObject;
import it.unibo.wildenc.mvc.model.map.MapTestingConstants.TestWeapon;

import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.TEST_SIMULATION_TICKS;
import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.TEST_TIME_NANOSECONDS;
import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.TEST_TIME_SECONDS;
import static it.unibo.wildenc.mvc.model.map.MapTestingConstants.calculateMovement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

/**
 * Testing for {@link GameMap}.
 */
public class TestMap {

    private GameMap getEmptyMapWithObjects(final Player player, final Set<MapObject> objs) {
        return new GameMapImpl(player, (p, s, t) -> Set.of(), objs);
    }

    private GameMap getMapWithEnemySpawner(final Player player) {
        return new GameMapImpl(player, new EnemySpawnerImpl(player), Set.of());
    }

    private Player getEmptyPlayer() {
        return TestObject.PlayerObject.getAsPlayer();
    }

    private Player getArmedPlayer(final Function<Player, Weapon> w) {
        final Player p = getEmptyPlayer();
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

        map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.getVect());

        assertEquals(obj.getPosition(), objConf.getPos());
    }

    @Test
    void movableObjWithNoDirectionShouldNotMove() {
        final TestObject objConf = TestObject.MovableObject;
        final MovableObjectTest obj = objConf.getAsMovableObj();
        final GameMap map = getEmptyMapWithObjects(getEmptyPlayer(), Set.of(obj));

        map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.getVect());

        assertEquals(obj.getPosition(), objConf.getPos());
    }

    @Test
    void movableObjWithDirectionShouldMoveCorrectly() {
        final TestObject objConf = TestObject.MovableObject;
        final TestDirections direction = TestDirections.RIGHT;
        final MovableObjectTest obj = objConf.getAsMovableObj();
        final GameMap map = getEmptyMapWithObjects(getEmptyPlayer(), Set.of(obj));

        obj.setDirection(direction.getVect());
        map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.getVect());

        assertNotEquals(objConf.getPos(), obj.getPosition(), "Object did not move");
        assertEquals(
            calculateMovement(objConf.getPos(), direction.getVect(), objConf.getSpeed(), TEST_TIME_SECONDS), 
            obj.getPosition(), 
            "Object moved wrong"
        );
    }

    @Test
    void whenEnemyProjectileHitboxTouchesPlayerHitboxPlayerHealthShouldDecrease() {
        final TestObject enemyConf = TestObject.EnemyObject;
        final Player p = getEmptyPlayer();
        final Enemy enemy = enemyConf.getAsCloseRangeEnemy(new LinkedHashSet<>(), "testEnemy", Optional.of(p));
        final var weapon = TestWeapon.DEFAULT_WEAPON.getAsWeapon(enemy, () -> p.getPosition());
        final GameMap map = getEmptyMapWithObjects(p, Set.of(enemy));
        enemy.addWeapon(weapon);

        // Enemy should arrive in player hitbox at the 20th tick
        for (int i = 0; i < TEST_SIMULATION_TICKS; i++) {
            map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.getVect());
        }

        assertTrue(enemy.getCurrentHealth() == enemy.getMaxHealth(), "Enemy health must not change.");
        assertTrue(p.getCurrentHealth() < p.getMaxHealth(), "Player health didn't change.");
    }

    @Test
    void whenPlayerProjectileHitboxTouchesEnemyHitboxEnemyHealthShouldDecrease() {
        final TestObject enemyConf = TestObject.EnemyObject;
        final Player p = getArmedPlayer(o -> TestWeapon.DEFAULT_WEAPON.getAsWeapon(o, () -> enemyConf.getPos()));
        final Enemy enemy = enemyConf.getAsCloseRangeEnemy(new LinkedHashSet<>(), "testEnemy", Optional.of(p));
        final GameMap map = getEmptyMapWithObjects(p, Set.of(enemy));

        // Enemy should arrive in player hitbox at the 20th tick
        for (int i = 0; i < TEST_SIMULATION_TICKS; i++) {
            map.updateEntities(TEST_TIME_NANOSECONDS, TestDirections.STILL.getVect());
        }

        assertTrue(p.getCurrentHealth() == p.getMaxHealth(), "Player health must not change.");
        assertTrue(enemy.getCurrentHealth() < enemyConf.getHealth(), "Enemy health didn't change.");
    }

    @Test
    void mapSpawnsEnemiesCorrectly() {
        final GameMap map = getMapWithEnemySpawner(getEmptyPlayer());
        final var initialSize = map.getAllObjects().size();

        map.spawnEnemies(TEST_TIME_SECONDS);

        assertTrue(map.getAllObjects().size() > initialSize, "No enemies were spawend.");
    }
}
