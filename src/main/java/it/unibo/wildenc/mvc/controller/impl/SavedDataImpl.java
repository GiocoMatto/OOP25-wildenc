package it.unibo.wildenc.mvc.controller.impl;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import it.unibo.wildenc.mvc.controller.api.SavedData;


public class SavedDataImpl implements SavedData, Serializable {

    private int totalCoins;
    private final Map<String, Integer> pokedexMap = new LinkedHashMap<>();

    public SavedDataImpl() { } 

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCoins(int earnedCoins) {
        this.totalCoins += earnedCoins;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePokedex(String name, int newKills) {
        if(pokedexMap.containsKey(name)) {
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
        return this.pokedexMap;
    }

}
