package it.unibo.wildenc.mvc.controller.api;

import java.util.Map;

/**
 * Responsible of storing all data between every game.
 */
public interface SavedData {
    
    /**
     * Update player coins.
     */
    void updateCoins(int earnedCoins);

    /**
     * Update infos about killed enemies. 
     */
    void updatePokedex(String name, int newKills);

    /**
     * Get coins the player has earned between every game.
     * 
     * @return the number of coins.
     */
    int getCoins();

    /**
     * Get information about every enemy in the game and how many times they have been killed.
     * 
     * @return A {@link Map} with enemy id as key and how many times it has been killed as value.
     */
    Map<String, Integer> getPokedex();

}
