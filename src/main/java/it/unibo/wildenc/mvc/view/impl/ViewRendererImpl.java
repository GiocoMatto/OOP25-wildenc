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
    private static final int INITIAL_CANVAS_WIDTH = 1600;

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
        final double scale = canvas.getWidth() / INITIAL_CANVAS_WIDTH;

        draw.save();
        draw.scale(scale, scale);

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
                    SPRITE_SIZE, SPRITE_SIZE
                );
        });
        draw.restore();

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
        double effectiveWidth = INITIAL_CANVAS_WIDTH;
        double effectiveHeight = canvas.getHeight() / (canvas.getWidth() / INITIAL_CANVAS_WIDTH);

        this.cameraX = playerObj.x() - effectiveWidth / 2;
        this.cameraY = playerObj.y() - effectiveHeight / 2;
    }

    private void drawGrassTiles(GraphicsContext draw) {
        final double scale = canvas.getWidth() / INITIAL_CANVAS_WIDTH;
        final double effectiveWidth = canvas.getWidth() / scale;
        final double effectiveHeight = canvas.getHeight() / scale;

        final double offsetX = -cameraX % SPRITE_SIZE;
        final double offsetY = -cameraY % SPRITE_SIZE;
    
        for (double x = offsetX - SPRITE_SIZE; x < effectiveWidth; x += SPRITE_SIZE) {
            for (double y = offsetY - SPRITE_SIZE; y < effectiveHeight; y += SPRITE_SIZE) {
                draw.drawImage(grassTile, x, y, SPRITE_SIZE, SPRITE_SIZE);
            }
        }
    }
}
