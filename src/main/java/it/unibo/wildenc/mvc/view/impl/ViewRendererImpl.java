package it.unibo.wildenc.mvc.view.impl;

import java.util.Collection;
import java.util.Objects;

import it.unibo.wildenc.mvc.controller.api.MapObjViewData;
import it.unibo.wildenc.mvc.view.api.SpriteManager;
import it.unibo.wildenc.mvc.view.api.SpriteManager.Sprite;
import it.unibo.wildenc.mvc.view.api.ViewRenderer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;

/**
 * Implementation for ViewRenderer.
 */
public class ViewRendererImpl implements ViewRenderer {

    private static final int SPRITE_SIZE = 64;
    private static final int SPRITE_PADDING = 2;
    private static final int INITIAL_CANVAS_WIDTH = 1600;

    private final SpriteManager spriteManager;
    private Canvas canvas;
    private int frameCount;
    private double cameraX;
    private double cameraY;

    private Region backgroundContainer;

    /**
     * Constructor for the class. The SpriteManager to be used
     * is set here.
     */
    public ViewRendererImpl() {
        this.spriteManager = new SpriteManagerImpl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void renderAll(final Collection<MapObjViewData> objectDatas) {
        if (Objects.isNull(canvas) || Objects.isNull(backgroundContainer)) {
            return;
        }
        final GraphicsContext draw = canvas.getGraphicsContext2D();
        final double scale = canvas.getWidth() / INITIAL_CANVAS_WIDTH;

        draw.save();
        draw.scale(scale, scale);

        updateCamera(
            objectDatas.stream()
                .filter(e -> e.name().contains("player"))
                .findFirst()
                .orElse(null)
        );

        final double bgX = -this.cameraX % SPRITE_SIZE * scale;
        final double bgY = -this.cameraY % SPRITE_SIZE * scale;
        final double scaledTileSize = SPRITE_SIZE * scale;

        if (Objects.nonNull(backgroundContainer)) {
            backgroundContainer.setStyle(
                "-fx-background-position: " + bgX + "px " + bgY + "px;" 
                + "-fx-background-size: " + scaledTileSize + "px " + scaledTileSize + "px;"
            );
        }


        objectDatas.stream()
            .forEach(objectData -> {
                final Sprite currentSprite = spriteManager.getSprite(frameCount, objectData);
                final double radius = objectData.hbRad();
                final double renderSize = radius * 2 * SPRITE_PADDING;

                draw.drawImage(
                    currentSprite.spriteImage(), 
                    currentSprite.currentFrame(), 
                    SPRITE_SIZE * currentSprite.rotationFrame(), 
                    SPRITE_SIZE, SPRITE_SIZE,
                    objectData.x() - this.cameraX - (renderSize / 2), 
                    objectData.y() - this.cameraY - (renderSize / 2), 
                    renderSize, 
                    renderSize
                );
                /*
                draw.setStroke(javafx.scene.paint.Color.LIME);
                draw.setLineWidth(1);
                draw.strokeOval(
                    objectData.x() - this.cameraX - radius, 
                    objectData.y() - this.cameraY - radius, 
                    radius * 2, 
                    radius * 2
                );
                 */

        });
        draw.restore();

        frameCount++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCanvas(final Canvas c) {
        canvas = c;
        this.canvas.getGraphicsContext2D().setImageSmoothing(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clean() {
        if (Objects.nonNull(canvas)) {
            final var draw = canvas.getGraphicsContext2D();
            draw.clearRect(0, 0, canvas.widthProperty().get(), canvas.heightProperty().get());
        }
    }

    private void updateCamera(final MapObjViewData playerObj) {
        final double effectiveWidth = INITIAL_CANVAS_WIDTH;
        final double effectiveHeight = canvas.getHeight() / (canvas.getWidth() / INITIAL_CANVAS_WIDTH);

        this.cameraX = playerObj.x() - effectiveWidth / 2;
        this.cameraY = playerObj.y() - effectiveHeight / 2;
    }

    public final void setStyleToContainer(final Region container, final String css) {
        this.backgroundContainer = container;
        container.getStylesheets().add(ClassLoader.getSystemResource(css).toExternalForm());
    }
}
