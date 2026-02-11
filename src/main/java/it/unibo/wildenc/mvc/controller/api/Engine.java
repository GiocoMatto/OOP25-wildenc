package it.unibo.wildenc.mvc.controller.api;

import java.util.List;
import java.util.Set;

import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.view.api.GameView;

/**
 * Responsible of handling the main GameLoop and communicate with Views and the Model.
 */
public interface Engine {

    void start(Game.PlayerType pt);

    /**
     * Accept the movement of the player to add.
     * 
     * @param movement the movemente of type {@link MovementInput}.
     */
    void addInput(MovementInput movement);

    /**
     * Accept the movemet of the player to remove.
     * 
     * @param movement the movemente of type {@link MovementInput}.
     */
    void removeInput(MovementInput movement);

    /**
     * Remove all input.
     */
    void removeAllInput();

    /**
     * Update the status of the gameLoop.
     * 
     * @param status True: running game Loop. False: stop game loop.
     */
    void setPause(boolean status);

    /**
     * Select the weapon to unlock or levelup.
     * 
     * @param choise the choise of the player.
     */
    void onLeveUpChoise(String choise);

    /**
     * Start the game loopl.
     */
    void startGameLoop();

    /**
     * Show the menu.
     */
    void menu(Game.PlayerType pt);

    /**
     * Show the shop.
     */
    void shop();

    /**
     * Show the Pokedex.
     */
    void pokedex();

    /**
     * Close the game and save the data.
     */
    void close();

    /**
     * Register the views.
     * 
     * @param gv view to register.
     */
    void registerView(GameView gv);

    /**
     * Remove the view from the views handled by this engine.
     * 
     * @param gv the view to remove.
     */
    void unregisterView(GameView gv);

    List<Game.PlayerType> getPlayerType();
    
    Game.PlayerType getPlayerTypeChoise();
}
