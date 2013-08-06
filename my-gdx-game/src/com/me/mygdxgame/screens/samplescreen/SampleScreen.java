package com.me.mygdxgame.screens.samplescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.GameScreen;
import com.me.mygdxgame.utilities.GameState;

/**
 * Screen for testing. Roughly analogous to a game "level".
 */
public class SampleScreen implements GameScreen {
    
    private static final Vector3 CAM_INITIAL_POSITION = new Vector3(0, 0, 256);

    /** Object that handles player updating and rendering. */
    private SamplePlayer player = new SamplePlayer();
    
    /** Tracks the location of the camera, since it cannot be relied upon to be unchanged between calls to render.*/
    private Vector3 camPos = new Vector3(SampleScreen.CAM_INITIAL_POSITION);

    /** Constructor. */
    public SampleScreen() {
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
        }
    }

    /**
     * Rendering logic. Clear screen and draw textures.
     */
    private void draw(PerspectiveCamera perspCam) {
        // Update camera with position calculated in update.
        perspCam.position.set(this.camPos);
        perspCam.update();
        
        // Set screen clear color. Cornflower Blue, just because.
        Gdx.gl.glClearColor((100.0f / 256.0f), (149.0f / 256.0f),
                (237.0f / 256.0f), 0.0f);

        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Draw player.
        this.player.draw(perspCam.combined);
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
        this.camPos.set(SampleScreen.CAM_INITIAL_POSITION);
    }

    @Override
    public GameState getState() {
        // No winning or losing state for this test game. Just return running.
        return GameState.Running;
    }

    @Override
    public void render(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {

        // Nice to separate update logic and rendering logic.
        this.update(deltaTime, difficulty);
        this.draw(perspCam);
    }
}