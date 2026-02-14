package it.unibo.wildenc.mvc.controller.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import it.unibo.wildenc.mvc.controller.api.SavedData;

/**
 * Implementation for SavedData. This is a {@link Serializable}, and
 * it's used to save data (such as coins, unlocked characters, pokedex)
 * from previous games.
 */
public class SavedDataImpl implements SavedData, Serializable {

    private static final long serialVersionUID = 1L;

    private int totalCoins;
    private final Map<String, Integer> pokedexMap = new LinkedHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCoins(final int earnedCoins) {
        this.totalCoins += earnedCoins;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePokedex(final String name, final int newKills) {
        if (pokedexMap.containsKey(name)) {
            this.pokedexMap.replace(
                name, 
                this.pokedexMap.get(name) + newKills
            );
        } else {
            this.pokedexMap.put(name, newKills);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCoins() {
        return this.totalCoins;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer> getPokedex() {
        return Collections.unmodifiableMap(this.pokedexMap);
    }

}
