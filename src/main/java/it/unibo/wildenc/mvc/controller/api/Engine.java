package it.unibo.wildenc.mvc.controller.api;

import it.unibo.wildenc.mvc.controller.api.InputHandler.MovementInput;

/**
 * Responsible of handling the main GameLoop and communicate with Views and the Model.
 */
public interface Engine {

    // esporre solo per aggiungere il metodo.
    void processInput(MovementInput movement);

    void onLeveUpChoise(String choise);

}
