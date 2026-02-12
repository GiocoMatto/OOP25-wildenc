package it.unibo.wildenc.mvc.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import it.unibo.wildenc.mvc.controller.api.SavedData;
import it.unibo.wildenc.mvc.controller.api.SavedDataHandler;

/**
 * Class for managing saved data. This class loads and saves
 * instances of {@link SavedData} which are {@link Serializable}.
 */
public class SavedDataHandlerImpl implements SavedDataHandler {
    private static final File DATA_LOCATION = new File("build", "saveData.wenc");

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveData(final SavedData data) throws FileNotFoundException, IOException {
        try (
            ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(DATA_LOCATION)
            )
        ) {
            out.writeObject(data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SavedData loadData() throws ClassNotFoundException, IOException {
        try (
            ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(DATA_LOCATION)
            )
        ) {
            return (SavedData) in.readObject();
        }
    }
}
