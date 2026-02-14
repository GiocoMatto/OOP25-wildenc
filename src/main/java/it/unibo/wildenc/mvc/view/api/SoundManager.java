package it.unibo.wildenc.mvc.view.api;

/**
 * Interface for managing sound effects & BGM.
 */
public interface SoundManager {

    /**
     * Method to play a specified sound.
     * 
     * @param name the name of the sound to play.
     */
    void play(String name);

    /**
     * Method to play a specified music in a loop.
     * This was made for the BGM.
     * 
     * @param filename the name of the BGM.
     */
    void playMusic(String filename);

    /**
     * Method for stopping the music from playing.
     */
    void stopMusic();

    /**
     * Method for pausing the music from playing.
     */
    void pauseMusic();

    /**
     * Method for resuming the sound.
     */
    void resumeMusic();
}
