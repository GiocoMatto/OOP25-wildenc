package it.unibo.wildenc.mvc.model.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import it.unibo.wildenc.mvc.model.controller.api.SavedData;
import it.unibo.wildenc.mvc.model.controller.api.SavedDataHandler;

public class SavedDataHandlerImpl implements SavedDataHandler{
    private static final File DATA_LOCATION = new File("build", "saveData.wenc");

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveData(SavedData data) throws FileNotFoundException, IOException {
        try (
            final ObjectOutputStream out = new ObjectOutputStream(
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
            final ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(DATA_LOCATION)
            )
        ) {
            return (SavedData) in.readObject();
        }     
    }

}
