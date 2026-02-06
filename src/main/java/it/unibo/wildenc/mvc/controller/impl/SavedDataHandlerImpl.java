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

public class SavedDataHandlerImpl implements SavedDataHandler{

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveData(SavedData data) throws FileNotFoundException, IOException {
        try (
            final ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream("build" + File.separator + "saveData.wenc")
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
                new FileInputStream("build" + File.separator + "savedData.wenc")
            )
        ) {
            return (SavedData) in.readObject();
        }     
    }

}
