package it.unibo.wildenc.mvc.view.api;

import org.joml.Vector2dc;

/**
 * Interface for views that track mouse pointer.
 */
public interface GamePointerView {
    /**
     * Getter method for getting the pointer location.
     * * @return the location of the pointer on the screen in form of a {@link Vector2dc}
     */
    Vector2dc getMousePointerInfo();
}
