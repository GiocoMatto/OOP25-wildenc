package it.unibo.wildenc.mvc.controller.api;

import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;
import it.unibo.wildenc.mvc.model.GameMap;

/**
 * Responsible of handling the main GameLoop and communicate with Views and the Model.
 */
public interface Engine {

    // esporre solo per aggiungere il metodo.
    void processInput(MovementInput movement);

    void setPause(boolean status);

    void onLeveUpChoise(String choise);

    void chosePlayerType(GameMap.PlayerType playerType);

    void startGameLoop();

    void menu();

    void shop();

    void Pokedex();

    void close();

}
