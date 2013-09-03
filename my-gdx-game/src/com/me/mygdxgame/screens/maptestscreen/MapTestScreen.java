package com.me.mygdxgame.screens.maptestscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.GameScreen;
import com.me.mygdxgame.utilities.GameState;
import com.me.mygdxgame.utilities.Renderer;

/** Just loads some GameMap and lets you view it.*/
public class MapTestScreen implements GameScreen {

    private static final Vector3 CAM_INITIAL_POSITION = new Vector3(0, 0, 256);
    
    /** GameMap to display.*/
    private GameMap map = null;
    
    /** Tracks camera position.*/
    private Vector3 camPos = new Vector3(MapTestScreen.CAM_INITIAL_POSITION);

    /** Constructor.
     * 
     * @param map The GameMap to display.
     */
    public MapTestScreen(GameMap map) {
        this.map = map;
    }

    @Override
    public void load() {
        this.map.load();
    }

    @Override
    public void unload() {
        this.map.unload();
    }

    @Override
    public void render(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {

        // Camera controls. Moves at 180 units a second.
        if (Gdx.input.isKeyPressed(Keys.A)) {
            this.camPos.x -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            this.camPos.x += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            this.camPos.y -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            this.camPos.y += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.E)) {
            this.camPos.z -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.Q)) {
            this.camPos.z += (180 * deltaTime);
        }
        perspCam.position.set(this.camPos);
        perspCam.update();

        // Set screen clear color. Black, like a proper 8-bit game.
        Gdx.gl.glClearColor(0, 0, 0, 0);

        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // Make a renderer.
        Renderer renderer = new Renderer(perspCam.combined);

        this.map.render(deltaTime, renderer);
    }

    @Override
    public void initialize() {
        // Initialize camera position.
        this.camPos.set(MapTestScreen.CAM_INITIAL_POSITION);
    }

    @Override
    public GameState getState() {
        // Test screen, run forever.
        return GameState.Running;
    }

}
