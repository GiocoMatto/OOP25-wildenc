package it.unibo.wildenc.mvc.view.impl;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

import com.sun.media.jfxmedia.logging.Logger;

import it.unibo.wildenc.mvc.view.api.SoundManager;

/**
 * Implementation for the SoundManager interface.
 */
public final class SoundManagerImpl implements SoundManager {

    private static final double TEN_PERCENT = 0.1;

    private final Map<String, AudioClip> sounds = new HashMap<>();
    private MediaPlayer backgroundMusic;

    /**
     * Constructor for the class. Loads the sounds used.
     */
    public SoundManagerImpl() {
        loadSound("collect", "/sounds/collect.wav");
        loadSound("walk", "/sounds/walk.wav");
        loadSound("levelUp", "/sounds/levelUp.mp3");
    }

    private void loadSound(final String name, final String path) {
        if (getClass().getResource(path) != null) {
            sounds.put(name, new AudioClip(getClass().getResource(path).toExternalForm()));
        } else {
            Logger.logMsg(Logger.ERROR, "Sounds can't be loaded.");
        } 
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void play(final String name) {
        if (sounds.containsKey(name)) {
            sounds.get(name).play();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playMusic(final String filename) {
        final String path = "/sounds/" + filename;
        if (getClass().getResource(path) == null) {
            Logger.logMsg(Logger.ERROR, "Sounds can't be loaded.");
            return;
        }

        final Media media = new Media(getClass().getResource(path).toExternalForm());
        backgroundMusic = new MediaPlayer(media);

        // Impostazioni Musica
        backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop infinito
        backgroundMusic.setVolume(TEN_PERCENT); // Volume al 10%
        backgroundMusic.play();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            backgroundMusic.pause();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resumeMusic() {
        if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PAUSED) {
            backgroundMusic.play();
        }
    }
}
