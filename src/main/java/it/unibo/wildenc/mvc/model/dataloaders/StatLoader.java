package it.unibo.wildenc.mvc.model.dataloaders;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unibo.wildenc.mvc.model.Enemy;
import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Player;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;
import it.unibo.wildenc.mvc.model.weaponary.weapons.factories.BurstingArcFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;

/**
 * Singleton for loading stats from .json files. This was made
 * to add enemies, player types and weapons easily.
 */
public class StatLoader {

    private static final String WEAPON_JSON_PATH = "/json/weapons.json";
    private static final String ENEMY_JSON_PATH = "/json/enemies.json";
    private static final String PLAYER_JSON_PATH = "/json/players.json";

    private static StatLoader instance;

    private Map<String, LoadedWeaponStats> loadedWeaponConfigs;
    private Map<String, LoadedEntityStats> loadedPlayerConfigs;
    private Map<String, LoadedEntityStats> loadedEnemyConfigs;

    /**
     * Constructor for the class. This loads weapons, enemies and players jsons
     * inside of /resources/ follow, and builds all the corresponding maps.
     */
    public StatLoader() {
        final ObjectMapper jsonToObjectMapper = new ObjectMapper();
        try {
            InputStream jsonInputStream = getClass().getResourceAsStream(WEAPON_JSON_PATH);
            loadedWeaponConfigs = jsonToObjectMapper.readValue(
                jsonInputStream,
                new TypeReference<Map<String, LoadedWeaponStats>>() { }
            );
            jsonInputStream = getClass().getResourceAsStream(ENEMY_JSON_PATH);
            loadedEnemyConfigs = jsonToObjectMapper.readValue(
                jsonInputStream,
                new TypeReference<Map<String, LoadedEntityStats>>() { }
            );
            jsonInputStream = getClass().getResourceAsStream(PLAYER_JSON_PATH);
            loadedPlayerConfigs = jsonToObjectMapper.readValue(
                jsonInputStream,
                new TypeReference<Map<String, LoadedEntityStats>>() { }
            );
            jsonInputStream.close();
        } catch (final IOException ioExc) {
            loadedWeaponConfigs = new LinkedHashMap<>();
        }
    }

    /**
     * Method for getting the instance of the singleton. Not using this
     * function means reloading all the data each time this class is used,
     * which is very slow and inefficient.
     * 
     * @return an instance of this StatLoader.
     */
    public static synchronized StatLoader getInstance() {
        if (Objects.isNull(instance)) {
            instance = new StatLoader();
        }
        return instance;
    }

    /**
     * Method for getting all the loaded enemy data, 
     * in a form of a generic {@link Collection}.
     * 
     * @return a {@link Collection} containing all loaded enemies data.
     */
    public Collection<LoadedEntityStats> getAllEnemyData() {
        return this.loadedEnemyConfigs.values();
    }

    /**
     * Method for getting the statistics of a specific, 
     * {@link Weapon}, given the name in form of a string.
     * 
     * @param weaponName the name of the {@link Weapon}.
     * 
     * @return a {@link LoadedWeaponStats} containing all the statistics
     *      for that weapon.
     */
    public LoadedWeaponStats getLoadedWeaponStats(final String weaponName) {
        if (loadedWeaponConfigs.containsKey(weaponName)) {
            return loadedWeaponConfigs.get(weaponName);
        } else {
            return LoadedWeaponStats.empty(weaponName);
        }
    }

    /**
     * Method for getting the statistics of a specific, 
     * {@link Enemy}, given the name in form of a string.
     * 
     * @param enemyName the name of the {@link Enemy}.
     * 
     * @return a {@link LoadedEntityStats} containing all the statistics
     *      for that enemy.
     */
    public LoadedEntityStats getLoadedEnemyStats(final String enemyName) {
        if (loadedEnemyConfigs.containsKey(enemyName)) {
            return loadedEnemyConfigs.get(enemyName);
        } else {
            return LoadedEntityStats.empty(enemyName);
        }
    }

    /**
     * Method for getting the statistics of a specific, 
     * {@link Player}, given the name in form of a string.
     * 
     * @param playerName the name of the {@link Player}.
     * 
     * @return a {@link LoadedEntityStats} containing all the statistics
     *      for that player.
     */
    public LoadedEntityStats getLoadedPlayerStats(final String playerName) {
        if (loadedPlayerConfigs.containsKey(playerName)) {
            return loadedPlayerConfigs.get(playerName);
        } else {
            return LoadedEntityStats.empty(playerName);
        }
    }

    /**
     * Method for getting all the statistics for all the loaded weapons.
     * 
     * @return a {@link Collection} of {@link LoadedWeaponStats} of all the
     *      loaded weapons.
     */
    public Collection<LoadedWeaponStats> getAllLoadedWeapons() {
        return this.loadedWeaponConfigs.values();
    }

    /**
     * Method for getting the weapon given its name. This will use the
     * {@link LoadedWeaponStats}' specified factory for generating the weapon.
     * 
     * @param weaponName the name of the weapon to generate.
     * @param ownedBy the owner of the weapon.
     * @param posToHit the initial position which the weapon has to aim to.
     * @return the generated {@link Weapon}, or an "empty"/placeholder one if it
     *      was not found in the loaded weapons.
     */
    public Weapon getWeaponFactoryForWeapon(
        final String weaponName, 
        final Entity ownedBy, 
        final Supplier<Vector2dc> posToHit
    ) {
        LoadedWeaponStats weaponToGenStats = this.getLoadedWeaponStats(weaponName);
        try {
            final Class<?> weaponFactoryClass = Class.forName(weaponToGenStats.factoryType());
            final Constructor<?> weaponFactoryConst = weaponFactoryClass.getConstructors()[0];

            final WeaponFactory effectiveFactory = (WeaponFactory) weaponFactoryConst.newInstance(
                (Objects.isNull(weaponToGenStats.special) ? Map.of() : weaponToGenStats.special)
                .values()
                .toArray()
            );

            return effectiveFactory.createWeapon(
                weaponName, 
                weaponToGenStats.baseCooldown(), 
                weaponToGenStats.baseDamage(), 
                weaponToGenStats.hbRadius(), 
                weaponToGenStats.baseVelocity(), 
                weaponToGenStats.baseTTL(), 
                weaponToGenStats.baseProjAtOnce(), 
                weaponToGenStats.baseBurst(), 
                ownedBy,
                weaponToGenStats.immortal(),
                posToHit
            );
        } catch (
            IllegalAccessException 
            | InvocationTargetException 
            | InstantiationException 
            | ClassNotFoundException exception
        ) {
            weaponToGenStats = LoadedWeaponStats.empty(weaponName);
            return new BurstingArcFactory().createWeapon(
                weaponName,
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                ownedBy,
                false,
                () -> new Vector2d(0, 0)
            );
        }
    }

    /**
     * Record for managing the stats for loaded weapons.
     * These statistics will be used for creating the weapons.
     * 
     * @param weaponName the name of the weapon.
     * @param factoryType the factory which is used to create the weapon.
     * @param baseCooldown the base cooldown of the weapon.
     * @param baseDamage base damage for the projectiles generated from the weapon.
     * @param hbRadius the hitbox of the projectiles generated from the weapon.
     * @param baseVelocity the base velocity of the the projectiles generated from the weapon.
     * @param baseTTL the base time to live the projectiles generated from the weapon.
     * @param baseProjAtOnce the number of projectiles the weapon will initially generate.
     * @param baseBurst the number of projectile in a burst, initially.
     * @param availableToPlayer whether the weapon is available to the player to be
     *      given through upgrades.
     * @param immortal whether the projectiles destroy on collision.
     * @param special some extra, special arguments if available.
     */
    public record LoadedWeaponStats(
        String weaponName,
        String factoryType,
        double baseCooldown,
        double baseDamage,
        double hbRadius,
        double baseVelocity,
        double baseTTL,
        int baseProjAtOnce,
        int baseBurst,
        boolean immortal,
        boolean availableToPlayer,
        List<String> peculiarTo,
        Map<String, Double> special
    ) {
        /**
         * Returns an empty, placeholder, {@link LoadedWeaponStats}.
         * 
         * @param weaponName the name of the weapon.
         * @return the empty, placeholder {@link LoadedWeaponStats}.
         */
        public static LoadedWeaponStats empty(final String weaponName) {
            return new LoadedWeaponStats(
                weaponName, 
                "NotFoundFactory",
                0, 
                0, 
                0, 
                0, 
                0, 
                0, 
                0,
                false,
                false,
                List.of(),
                Map.of()
            );
        }
    }

    /**
     * Record for managing the stats for loaded entities.
     * These will be used when generating entities (players or enemies).
     * 
     * @param entityName the name of the entity
     * @param hbRadius the hitbox of the entity
     * @param maxHealth the maximum health of the entity
     * @param velocity the initial velocity of the entity
     * @param rarity for enemies, the rarity which the enemy spawns.
     */
    public record LoadedEntityStats(
        String entityName,
        double hbRadius,
        double maxHealth,
        double velocity,
        String rarity,
        String weapon
    ) {
        /**
         * Returns an empty, placeholder, {@link LoadedEntityStats}.
         * 
         * @param entityName the name of the entity.
         * @return the empty, placeholder {@link LoadedEntityStats}.
         */
        public static LoadedEntityStats empty(final String entityName) {
            return new LoadedEntityStats(entityName, 0, 0, 0, null, null);
        }
    }
}
