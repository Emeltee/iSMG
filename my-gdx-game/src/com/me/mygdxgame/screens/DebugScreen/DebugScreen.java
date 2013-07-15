package com.me.mygdxgame.screens.DebugScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.me.mygdxgame.GameState;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.screens.GameScreen;

/**
 * Screen for testing. Roughly analogous to a game "level".
 */
public class DebugScreen implements GameScreen {

    /** Object that handles player updating and rendering. */
    private SamplePlayer player = new SamplePlayer();

    /** Constructor. */
    public DebugScreen() {
    }

    /**
     * Update logic. Handle input, update entities, play sound, etc.
     */
    private void update(float deltaTime, int difficulty) {

        // Exit on esc.
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        } else {

            // SamplePlayer controls.
            this.player.update(deltaTime);

            // Camera controls. Moves at 180 units a second.
            if (Gdx.input.isKeyPressed(Keys.A)) {
                MyGdxGame.currentGame.orthoCamera.position.x -= (180 * deltaTime);
            }
            if (Gdx.input.isKeyPressed(Keys.D)) {
                MyGdxGame.currentGame.orthoCamera.position.x += (180 * deltaTime);
            }
            MyGdxGame.currentGame.orthoCamera.update();
        }
    }

    /**
     * Rendering logic. Clear screen and draw textures.
     */
    private void draw() {
        // Set screen clear color. Cornflower Blue, just because.
        Gdx.gl.glClearColor((100.0f / 256.0f), (149.0f / 256.0f),
                (237.0f / 256.0f), 0.0f);

        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Draw player.
        this.player.draw();
    }

    @Override
    public void load() {
        this.player.load();
    }

    @Override
    public void unload() {
        this.player.unload();
    }

    @Override
    public void initialize() {
        this.player.initialize();
        MyGdxGame.currentGame.orthoCamera.position.set(0, 0, 0);
    }

    @Override
    public GameState getState() {
        // No winning or losing state for this test game. Just return running.
        return GameState.Running;
    }

    @Override
    public void render(float deltaTime, int difficulty) {

        // Nice to separate update logic and rendering logic.
        this.update(deltaTime, difficulty);
        this.draw();
    }
}
