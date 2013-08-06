package com.me.mygdxgame.utilities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;


/**
 * Interface through which the ApplicationListener will interact with and manage
 * each game.
 */
public interface GameScreen {

    /**
     * Loads all resources (textures and sounds) specific to this game. Does
     * nothing if resources have already been loaded. TODO Probably a good idea
     * to pull out common resources and place them into some separate pool to
     * avoid having multiple copies of resources in memory at once.
     */
    public void load();

    /**
     * Unloads all resources (textures and sounds) that have been previously
     * loaded by this game through load(). Does nothing if no resources are
     * currently loaded.
     */
    public void unload();

    /**
     * Execute this game's logic. This includes updating values, playing sounds,
     * and rendering to the screen. Called once each frame.
     * 
     * @param deltaTime
     *            The amount of time in seconds that has passed since the
     *            previous render call. Physics, timing, and other game logic
     *            should scale according to this value.
     * @param difficulty
     *            The current difficulty level of the application. The game
     *            should become "harder" as this value increases. What this
     *            means is left to the implementer to decide. Value should
     *            always be >= 0 and may go arbitrarily high.
     * @param perpCam
     *            PerspectiveCamera to use for perspective rendering.
     * @param orthoCam
     *            OrthographicCamera to use for 2D rendering.
     */
    public void render(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam);

    /**
     * Should reset all stateful data. The result should a game that is in the
     * same logical state it was in upon creation.
     */
    public void initialize();

    /**
     * Retrieves the current state of the game.
     * 
     * @return A GameState indicating the current status of the game.
     * @see GameState
     */
    public GameState getState();
}
