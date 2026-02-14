package it.unibo.wildenc.mvc.view.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sun.media.jfxmedia.logging.Logger;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteManager;
import javafx.scene.image.Image;

/**
 * Implementation of SpriteManager. This class manages sprites, reading them
 * as images and processing them on screen to the views.
 */
public final class SpriteManagerImpl implements SpriteManager {

    private static final int SPRITE_SIZE = 64;
    private static final int DOT_PNG_PREFIX_LENGHT = 4;
    private static final int FRAME_LENGTH = 6;
    private static final double DELTA_STILL = 0.01;
    private static final String DOT_PNG = ".png";
    private static final String SPRITES_LOCATION = "images/sprites";

    private static final List<Integer> SPRITE_MAP = List.of(2, 1, 0, 7, 6, 5, 4, 3);

    private final Map<String, Image> loadedSpriteMap = new LinkedHashMap<>();
    private double playerPosX; // Needed for calculating versor when idle.
    private double playerPosY;

    /**
     * Constructor for the class. When invoked, loads all the sprites
     * inside of /resources/sprites directory.
     */
    public SpriteManagerImpl() {
        loadAllResources();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sprite getSprite(final int frameCount, final MapObjViewData objData) {
        if (objData.name().contains("projectile") || objData.name().contains("collectible")) {
            return new Sprite(
                this.loadedSpriteMap.get(objData.name().toLowerCase(Locale.ENGLISH).split(":")[1]),
                0, 0
            );
        } else {
            if (loadedSpriteMap.containsKey(objData.name().toLowerCase(Locale.ENGLISH).split(":")[1])) {
                final int totalFrames = (int) loadedSpriteMap.get(objData.name().toLowerCase(Locale.ENGLISH).split(":")[1])
                    .getWidth() / SPRITE_SIZE;
                final int currentFrameIndex = (frameCount / FRAME_LENGTH) % totalFrames; // NOPMD: Parenthesis needed.
                final int offset = currentFrameIndex * SPRITE_SIZE;
                return new Sprite(
                    loadedSpriteMap.get(objData.name().toLowerCase(Locale.ENGLISH).split(":")[1]),
                    convertVersorToDominant(objData),
                    offset
                );
            } else {
                return new Sprite(
                    loadedSpriteMap.get("missingno"),
                    0, 0
                );
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Image getGrassTile() {
        return this.loadedSpriteMap.get("grasstile");
    }

    private int convertVersorToDominant(final MapObjViewData data) {
        double dx = data.directionX().get();
        double dy = data.directionY().get();

        if (data.name().contains("player")) {
            this.playerPosX = data.x();
            this.playerPosY = data.y();

            if (Math.abs(dx) < DELTA_STILL && Math.abs(dy) < DELTA_STILL) {
                return 0;
            }
        }

        if (Math.abs(dx) < DELTA_STILL && Math.abs(dy) < DELTA_STILL) {
            dx = playerPosX - data.x();
            dy = playerPosY - data.y();
        }
        double effectiveAngle = Math.toDegrees(Math.atan2(dy, dx));
        if (effectiveAngle < 0) {
            effectiveAngle += 360;
        }
        final int slice = (int) Math.floor((effectiveAngle + 22.5) / 45) % 8;
        return SPRITE_MAP.get(slice);
    }

    private void loadAllResources() {
        // Getting the sprites + decoding the path into something readable to Java
        // This prevents blank spaces in folders and files!
        final URL resourceFolder = getClass().getClassLoader().getResource(SPRITES_LOCATION);
        final String decodedPath = URLDecoder.decode(resourceFolder.getPath(), StandardCharsets.UTF_8);

        if ("file".equals(resourceFolder.getProtocol())) {
            try (Stream<Path> paths = Files.list(Paths.get(resourceFolder.toURI()))) {
                paths.filter(p -> p.toString().endsWith(DOT_PNG))
                    .forEach(p -> {
                        final Path filename = p.getFileName();
                        if (filename != null) {
                        final String key = filename.toString().replace(DOT_PNG, "");
                        loadedSpriteMap.put(key, new Image(p.toUri().toString()));
                        }
                    });
            } catch (IOException | URISyntaxException e) {
                Logger.logMsg(Logger.ERROR, e.toString());
            }
        } else if ("jar".equals(resourceFolder.getProtocol())) {
            try {
                final String jarPath = decodedPath.substring(5, decodedPath.indexOf('!'));
                try (ZipInputStream zip = new ZipInputStream(new FileInputStream(jarPath))) {
                    ZipEntry entry = zip.getNextEntry();
                    while (entry != null) {
                        final String name = entry.getName();
                        if (name.startsWith(SPRITES_LOCATION + "/") && name.endsWith(".png")) {
                            final String key = name.substring(
                                SPRITES_LOCATION.length() + 1, name.length() - DOT_PNG_PREFIX_LENGHT
                            );
                            final Image img = new Image(getClass().getResourceAsStream("/" + name));
                            loadedSpriteMap.put(key, img);
                        }
                        entry = zip.getNextEntry();
                    }
                }
            } catch (final IOException e) { 
                Logger.logMsg(Logger.ERROR, e.toString());
            }
        }
    }
}

