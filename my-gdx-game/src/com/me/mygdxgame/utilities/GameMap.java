package com.me.mygdxgame.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;

/**
 * Manages static elements of a "level". This includes background cosmetics and
 * static obstacles. Also contains utility methods.
 */
public abstract class GameMap {
    
    /**
     * Flag indicating whether or not obstacles should be drawn onto screen as
     * an overlay.
     */
    protected boolean debugMode = false;
    
    /** A semi-transparent red GameMaps can pass to drawObstacles.*/
    public static final Color DEFAULT_OBSTACLE_COLOR = new Color(1.0f, 0.0f, 0.0f, 0.5f);

    /**
     * Loads all resources (textures and sounds) specific to this game. Does
     * nothing if resources have already been loaded. TODO Probably a good idea
     * to pull out common resources and place them into some separate pool to
     * avoid having multiple copies of resources in memory at once.
     */
    public abstract void load();

    /**
     * Unloads all resources (textures and sounds) that have been previously
     * loaded by this game through load(). Does nothing if no resources are
     * currently loaded.
     */
    public abstract void unload();

    /** Retrieves the initial position of the player.
     * 
     * @return A Vector3 containing the desired initial player position.
     */
    public abstract Vector3 getInitialPosition();

    /**
     * Creates and returns an array of the static obstacles in this map.
     * 
     * @return A new array of static obstacles associated with this map.
     */
    public abstract Rectangle[] getObstacles();

    /**
     * Updates any logic and draws all entities to the screen. TODO, some
     * (enforced) form of frustum culling would eventually be nice.
     * 
     * @param deltaTime
     *            The amount of time in seconds that has passed since the
     *            previous render call.
     * @param transformMatrix
     *            Matrix4 to pass down to SpriteBatch and/or ShapeRenderer for
     *            drawing.
     */
    public abstract void render(float deltaTime, Matrix4 transformMatrix);
    
    /**
     * Sets the debug flag. In debug mode, obstacles should be drawn to the
     * screen as translucent overlays on calls to render.
     * 
     * @param debugMode
     *            The value to set the debug mode flag to.
     */
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Utility method that draws all Rectangles returned by getObstacles to the
     * screen in the given color.
     * 
     * @param transformMatrix
     *            The Camera to use for drawing. Camera will not be updated or
     *            otherwise modified; caller should prepare the camera prior to
     *            calling this method.
     * @param obstacles
     *            Array of Rectangles to draw.
     * @param obstacleColor
     *            Color to fill Rectangles with.
     */
    protected static void drawObstacles(Matrix4 transformMatrix, Rectangle[] obstacles, Color obstacleColor) {
        
        // They seem to require you to call this RIGHT before drawing.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        MyGdxGame.currentGame.shapeRenderer.begin(ShapeType.Filled);
        MyGdxGame.currentGame.shapeRenderer.setColor(obstacleColor);
        MyGdxGame.currentGame.shapeRenderer.setProjectionMatrix(transformMatrix);
        
        for (Rectangle rect : obstacles) {
                    
            MyGdxGame.currentGame.shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        
        MyGdxGame.currentGame.shapeRenderer.end();
        
        
    }

    protected static void diagonalRight(TextureRegion pattern, int x, int y, int count) {
        for (int i = 0; i < count; i++) {
            MyGdxGame.currentGame.spriteBatch.draw(pattern,
                    x + (i * pattern.getRegionWidth()),
                    y + (i * pattern.getRegionHeight()));
        }
    }

    protected static void diagonalLeft(TextureRegion pattern, int x, int y, int count) {
        for (int i = 0; i < count; i++) {
            MyGdxGame.currentGame.spriteBatch.draw(pattern,
                    x - (i * pattern.getRegionWidth()),
                    y + (i * pattern.getRegionHeight()));
        }
    }

    /** Automatically tile a Texture region horizontally
     * @param pattern Region to be tiled
     * @param x initial X coordinate (tiles to the right)
     * @param y initial Y coordinate (fixed)
     * @param count number of iterations for tile
     */
    protected static void tileX(TextureRegion pattern, int x, int y, int count) {
        for (int i = 0; i < count; i++) {
            MyGdxGame.currentGame.spriteBatch.draw(pattern, x + (i * pattern.getRegionWidth()), y);
        }
    }

    /** Automatically tile a TextureRegion vertically
     * @param pattern Region to be tiled
     * @param x initial X coordinate (fixed)
     * @param y initial Y coordinate (tiles upward)
     * @param count number of iterations for tile
     */
    protected static void tileY(TextureRegion pattern, int x, int y, int count) {
        for (int i = 0; i < count; i++) {
            MyGdxGame.currentGame.spriteBatch.draw(pattern, x, y + (i * pattern.getRegionHeight()));
        }
    }

    /** Automatically tile a TextureRegion horizontally and vertically
     * @param pattern Region to be tiled
     * @param x initial X coordinate (tiles to the right)
     * @param y initial Y coordinate (tiles upward)
     * @param columns number of columns (Xs to repeat pattern)
     * @param rows number of rows (Ys to repeat pattern)
     */
    protected static void tileXY(TextureRegion pattern, int x, int y, int columns, int rows) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                MyGdxGame.currentGame.spriteBatch.draw(
                        pattern,
                        (x + j * pattern.getRegionWidth()),
                        (y + i * pattern.getRegionHeight())
                        );
            }
        }
    }

    /** Tiles horizontally, alternating between pattern and same-width nothing
     * TODO maybe have a double pattern checkerer
     * @param pattern Pattern to be checkered
     * @param x initial X coordinate (tiles to the right, skipping one tile each iteration)
     * @param y initial Y coordinate (fixed, or tiles upward based on rows)
     * @param count number of iterations pattern appears per row, not including blank spaces
     * @param rows number of rows to apply the pattern
     */
    protected static void checkerX(TextureRegion pattern, int x, int y, int count, int rows) {
        for (int i = 0; i < rows; i++) {
            for (int j = (i % 2 == 0) ? 0 : 1; j < 2*count; j+=2) {
                MyGdxGame.currentGame.spriteBatch.draw(pattern, x
                        + (j * pattern.getRegionWidth()),
                        (y + i * pattern.getRegionHeight()));
            }
        }
    }

    /** Tiles vertically, alternating between pattern and same-height nothing
     * TODO again, make a double checkerer
     * @param pattern Pattern to be checkered
     * @param x initial X coordinate (fixed, or tiles right based on columns)
     * @param y initial Y coordinate (tiles upward, skipping one tile each iteration)
     * @param count number of iterations pattern appears per column, not including blank spaces
     * @param columns number of columns to apply the pattern
     */
    protected static void checkerY(TextureRegion pattern, int x, int y, int count, int columns) {
        for (int i = 0; i < columns; i++) {
            for (int j = (i % 2 == 0) ? 0 : 1; j < 2*count; j+=2) {
                MyGdxGame.currentGame.spriteBatch.draw(pattern, x
                        + (i * pattern.getRegionWidth()),
                        y + (j * pattern.getRegionHeight()));
            }
        }
    }
}
