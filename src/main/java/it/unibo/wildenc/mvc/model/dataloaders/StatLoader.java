package it.unibo.wildenc.mvc.model.dataloaders;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.unibo.wildenc.mvc.model.Entity;
import it.unibo.wildenc.mvc.model.Weapon;
import it.unibo.wildenc.mvc.model.weaponary.weapons.WeaponFactory;
import it.unibo.wildenc.mvc.model.weaponary.weapons.factories.BurstingArcFactory;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.joml.Vector2d;
import org.joml.Vector2dc;



public class StatLoader {

    private static final String WEAPON_JSON_PATH = "/json/weapons.json";
    private static final String ENEMY_JSON_PATH = "/json/enemies.json";
    private static final String PLAYER_JSON_PATH = "/json/players.json";

    private static StatLoader instance;

    private Map<String, LoadedWeaponStats> loadedWeaponConfigs;
    private Map<String, LoadedEntityStats> loadedPlayerConfigs;
    private Map<String, LoadedEntityStats> loadedEnemyConfigs;
    private ObjectMapper jsonToObjectMapper = new ObjectMapper();

    public StatLoader() {
        try {
            InputStream jsonInputStream = getClass().getResourceAsStream(WEAPON_JSON_PATH);
            loadedWeaponConfigs = jsonToObjectMapper.readValue(
                jsonInputStream,
                new TypeReference<Map<String, LoadedWeaponStats>>() {}
            );
            jsonInputStream = getClass().getResourceAsStream(ENEMY_JSON_PATH);
            loadedEnemyConfigs = jsonToObjectMapper.readValue(
                jsonInputStream,
                new TypeReference<Map<String, LoadedEntityStats>>() {}
            );
            jsonInputStream = getClass().getResourceAsStream(PLAYER_JSON_PATH);
            loadedPlayerConfigs = jsonToObjectMapper.readValue(
                jsonInputStream,
                new TypeReference<Map<String, LoadedEntityStats>>() {}
            );
        } catch (final IOException ioExc) {
            loadedWeaponConfigs = new LinkedHashMap<>();
        }
    }

    public static synchronized StatLoader getInstance() {
        if (Objects.isNull(instance)) {
            instance = new StatLoader();
        }
        return instance;
    }

    public Collection<LoadedEntityStats> getAllEnemyData() {
        return this.loadedEnemyConfigs.values();
    }

    public LoadedWeaponStats getLoadedWeaponStats(final String weaponName) {
        if (loadedWeaponConfigs.containsKey(weaponName)) {
            return loadedWeaponConfigs.get(weaponName);
        } else {
            return LoadedWeaponStats.empty(weaponName);
        }
    }

    public LoadedEntityStats getLoadedEnemyStats(final String enemyName) {
        if (loadedEnemyConfigs.containsKey(enemyName)) {
            return loadedEnemyConfigs.get(enemyName);
        } else {
            return LoadedEntityStats.empty(enemyName);
        }
    }

    public LoadedEntityStats getLoadedPlayerStats(final String playerName) {
        if (loadedPlayerConfigs.containsKey(playerName)) {
            return loadedPlayerConfigs.get(playerName);
        } else {
            return LoadedEntityStats.empty(playerName);
        }
    }

    public Collection<LoadedWeaponStats> getAllLoadedWeapons() {
        return this.loadedWeaponConfigs.values();
    }

    public Weapon getWeaponFactoryForWeapon(
        final String weaponName, 
        Entity ownedBy, 
        Supplier<Vector2dc> posToHit
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
        } catch (final Exception e) {
            weaponToGenStats = LoadedWeaponStats.empty(weaponName);
            return (new BurstingArcFactory()).createWeapon(
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

    public record LoadedWeaponStats (
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
        Map<String, Double> special
    ) {
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
                Map.of()
            );
        }
    }

    public record LoadedEntityStats (
        String entityName,
        double hbRadius,
        double maxHealth,
        double velocity,
        String rarity
    ) {
        public static LoadedEntityStats empty(final String entityName) {
            return new LoadedEntityStats(entityName, 0, 0, 0, null);
        }
    }
}
