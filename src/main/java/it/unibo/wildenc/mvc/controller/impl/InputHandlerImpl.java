package it.unibo.wildenc.mvc.controller.impl;

import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2dc;

import it.unibo.wildenc.mvc.controller.api.InputHandler;
import it.unibo.wildenc.util.Utilities;

public class InputHandlerImpl implements InputHandler {

    private volatile boolean isGamePaused;
    private volatile boolean isGameClosable;

    @Override
    public boolean isPaused() {
        return this.isGamePaused;
    }

    @Override
    public boolean isClosable() {
        return this.isGameClosable;
    }

    @Override
    public Vector2dc handleMovement(Set<MovementInput> movementCommands) {
        System.out.println(movementCommands);
        final Vector2d effectiveMovementVersor = new Vector2d(0, 0);
        movementCommands.stream()
            .forEach(movInput -> effectiveMovementVersor.add(new Vector2d(movInput.getVector())));
        return Utilities.normalizeVector(effectiveMovementVersor);
    }

    @Override
    public void handleCommand(CommandInput d) {
        switch(d) {
            case PAUSE -> this.isGamePaused = true;
            case RESUME -> this.isGamePaused = false;
            case QUIT -> this.isGameClosable = true; 
        }
    }

    @Override
    public Vector2dc handleAttackDirection(Vector2dc target) {
        // TODO: Still to figure out what the view sends to this method.
        // It could be a Vector2d or not.
       return Utilities.normalizeVector(target);
    }
}
