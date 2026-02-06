package it.unibo.wildenc.mvc.controller.api;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface for managing SaveData. This will be used to load
 * and save SaveData between different games.
 */
public interface SavedDataHandler {
    /**
     * Method to save data to local disk. This is possible thanks
     * to the fact that {@link SavedData} is Serializable.
     * 
     * @param data the data to be saved
     * @throws FileNotFoundException if the file is not found.
     * @throws IOException if there is a problem with the input/output.
     */
    void saveData(SavedData data) throws FileNotFoundException, IOException;

    /**
     * Method to load data from local disk.
     * 
     * @return the loaded data.
     * @throws ClassNotFoundException if the SavedData class is not present.
     * @throws IOException if there is a problem with the input/output.
     */
    SavedData loadData() throws ClassNotFoundException, IOException;
}
