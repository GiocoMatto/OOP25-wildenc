package it.unibo.wildenc.mvc.view.impl;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private final Map<String, AudioClip> sounds = new HashMap<>();
    private MediaPlayer backgroundMusic;

    public SoundManager() {
        loadSound("collect", "/sounds/collect.wav");
        loadSound("walk", "/sounds/walk.wav");
        loadSound("levelUp", "/sounds/levelUp.mp3");
    }

    private void loadSound(String name, String path) {
        if(getClass().getResource(path) != null) {
            sounds.put(name, new AudioClip(getClass().getResource(path).toExternalForm()));
        } else {
            System.err.println("File audio non trovato "+path);
        } 
    }

    public void play(String name) {
        if(sounds.containsKey(name)) {
            sounds.get(name).play();
        }
    }

    public void playMusic(String filename) {
        try {
            String path = "/sounds/" + filename;
            if (getClass().getResource(path) == null) {
                System.err.println("Musica non trovata: " + path);
                return;
            }

            Media media = new Media(getClass().getResource(path).toExternalForm());
            backgroundMusic = new MediaPlayer(media);

            // Impostazioni Musica
            backgroundMusic.setCycleCount(MediaPlayer.INDEFINITE); // Loop infinito
            backgroundMusic.setVolume(0.1); // Volume al 10%
            backgroundMusic.play();
        } catch (Exception e) {
            System.err.println("Errore avvio musica: " + e.getMessage());
        }
    }
    
    public void stopMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void pauseMusic() {
        if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PLAYING) {
            backgroundMusic.pause();
        }
    }

    public void resumeMusic() {
        if (backgroundMusic != null && backgroundMusic.getStatus() == MediaPlayer.Status.PAUSED) {
            backgroundMusic.play();
        }
    }
}