package com.me.mygdxgame.screens.maptestscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.me.mygdxgame.GameState;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.maps.GameMap;
import com.me.mygdxgame.screens.GameScreen;

/** Just loads some GameMap and lets you view it.*/
public class MapTestScreen implements GameScreen {

    /** GameMap to display.*/
    private GameMap map = null;

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
    public void render(float deltaTime, int difficulty) {

        // Camera controls. Moves at 180 units a second.
        // Camera controls. Moves at 180 units a second.
        if (Gdx.input.isKeyPressed(Keys.A)) {
            MyGdxGame.currentGame.perspectiveCamera.position.x -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            MyGdxGame.currentGame.perspectiveCamera.position.x += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            MyGdxGame.currentGame.perspectiveCamera.position.y -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            MyGdxGame.currentGame.perspectiveCamera.position.y += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.E)) {
            MyGdxGame.currentGame.perspectiveCamera.position.z -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.Q)) {
            MyGdxGame.currentGame.perspectiveCamera.position.z += (180 * deltaTime);
        }
        MyGdxGame.currentGame.perspectiveCamera.update();

        // Set screen clear color. Black, like a proper 8-bit game.
        Gdx.gl.glClearColor(0, 0, 0, 0);

        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        this.map.render(deltaTime);
    }

    @Override
    public void initialize() {
        // Initialize camera position.
        MyGdxGame.currentGame.perspectiveCamera.position.set(0, 0, 0);
    }

    @Override
    public GameState getState() {
        // Test screen, run forever.
        return GameState.Running;
    }

}
