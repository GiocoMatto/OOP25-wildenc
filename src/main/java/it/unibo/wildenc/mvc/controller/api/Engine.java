package it.unibo.wildenc.mvc.controller.api;

import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.Game;
import it.unibo.wildenc.mvc.view.api.GameView;

/**
 * Responsible of handling the main GameLoop and communicate with Views and the Model.
 */
public interface Engine {

    /**
     * Accept che movement of the player.
     * 
     * @param movement the movemente of type {@link MovementInput}.
     */
    void processInput(MovementInput movement);

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
     * Select the player type.
     * 
     * @param playerType the player type to game.
     */
    void chosePlayerType(Game.PlayerType playerType);

    /**
     * Start the game loopl.
     */
    void startGameLoop();

    /**
     * Show the menu.
     */
    void menu();

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

}
