package it.unibo.wildenc.mvc.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import it.unibo.wildenc.mvc.controller.api.SavedData;
import it.unibo.wildenc.mvc.controller.api.SavedDataHandler;

public class SavedDataHandlerImpl implements SavedDataHandler{
    private static final File DATA_LOCATION = getSaveDirBasedOffOS();

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

    private static File getSaveDirBasedOffOS() {
        final String osType = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        final String userHome = System.getProperty("user.home");
        final String gameFolder = ".wildenc";
        File saveFileDir;

        if (osType.contains("win")) {
            saveFileDir = new File(System.getenv("APPDATA"), gameFolder);
        } else if (osType.contains("mac")) {
            saveFileDir = new File(
                userHome, "Library/Application Support/" + gameFolder
            );
        } else {
            saveFileDir = new File(userHome, ".local/share/" + gameFolder);
        }
        if (!saveFileDir.exists()) {
            saveFileDir.mkdirs();
        }
        return new File(saveFileDir + File.separator + "saveData.wenc");
    }
}
