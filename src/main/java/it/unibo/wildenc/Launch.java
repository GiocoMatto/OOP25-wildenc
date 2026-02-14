package it.unibo.wildenc;

import javafx.application.Application;

/**
 * Class for launching the application.
 */
public final class Launch {
    private Launch() { }

    /**
     * Main method for launching the application.
     * 
     * @param args ignored
     */
    public static void main(final String[] args) {
        Application.launch(EntryPoint.class, args);
    }
}
