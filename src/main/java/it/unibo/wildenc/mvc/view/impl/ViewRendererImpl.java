package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteManager;
import it.unibo.wildenc.mvc.view.api.SpriteManager.Sprite;
import it.unibo.wildenc.mvc.view.api.ViewRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ViewRendererImpl implements ViewRenderer {

    private static final int SPRITE_SIZE = 64;

    private final SpriteManager spriteManager;
    private final Image grassTile;
    private Canvas canvas;
    private int frameCount;
    private double cameraX;
    private double cameraY;

    public ViewRendererImpl () {
        this.spriteManager = new SpriteManagerImpl();
        this.grassTile = spriteManager.getGrassTile();
    }

    @Override
    public void renderAll(Collection<MapObjViewData> objectDatas) {
        final GraphicsContext draw = canvas.getGraphicsContext2D();
        drawGrassTiles(draw);
        updateCamera(
            objectDatas.stream()
                .filter(e -> e.name().contains("player"))
                .findFirst()
                .orElse(null)
        );
        objectDatas.stream()
            .forEach(objectData -> {
                Sprite currentSprite = spriteManager.getSprite(frameCount, objectData);
                draw.drawImage(
                    currentSprite.spriteImage(), 
                    currentSprite.currentFrame(), 
                    SPRITE_SIZE * currentSprite.rotationFrame(), 
                    SPRITE_SIZE, SPRITE_SIZE,
                    objectData.x() - this.cameraX - (SPRITE_SIZE / 2), 
                    objectData.y() - this.cameraY - (SPRITE_SIZE / 2), 
                    64, 64
                );
        });

        frameCount++;
    }

    @Override
    public void setCanvas(Canvas c) {
        canvas = c;
    }

    @Override
    public void clean() {
        final var draw = canvas.getGraphicsContext2D();
        draw.clearRect(0, 0, canvas.widthProperty().get(), canvas.heightProperty().get());
    }

    @Override
    public void updateCamera(MapObjViewData playerObj) {
        this.cameraX = playerObj.x() - canvas.widthProperty().get() / 2;
        this.cameraY = playerObj.y() - canvas.heightProperty().get() / 2;
    }

    private void drawGrassTiles(GraphicsContext draw) {
        final double offsetX = -cameraX % SPRITE_SIZE;
        final double offsetY = -cameraY % SPRITE_SIZE;

        for (double x = offsetX - SPRITE_SIZE; x < canvas.getWidth(); x += SPRITE_SIZE) {
            for (double y = offsetY - SPRITE_SIZE; y < canvas.getHeight(); y += SPRITE_SIZE) {
                draw.drawImage(grassTile, x, y, SPRITE_SIZE, SPRITE_SIZE);
            }
        }
    }
}
