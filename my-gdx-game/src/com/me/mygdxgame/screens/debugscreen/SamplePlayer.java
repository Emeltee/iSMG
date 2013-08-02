package com.me.mygdxgame.screens.debugscreen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.me.mygdxgame.MyGdxGame;

/**
 * Simple class representing the player.
 */
public class SamplePlayer {

    public static final float MAX_SPEED = 5.0f;
    public static final float ACCELERATION = 120.0f;
    public static final float DECELERATION = 30.0f;
    public static final short RUN_FRAMERATE = 3;
    public static final short MAX_RUN_FRAMES = 6;

    /** Flag indicating if resources are currently loaded. */
    private boolean areResourcesLoaded = false;
    /** Used to flag whether or not player is holding a movement key. */
    private boolean isRunning = false;
    /**
     * Used to flag whether or not player sprite should be flipped horizontally.
     */
    private boolean isFlipped = false;
    /** TextureRegions representing the individual walking frames. */
    private TextureRegion[] runFrames = new TextureRegion[SamplePlayer.MAX_RUN_FRAMES];
    /** TextureRegion representing the idle frame. */
    private TextureRegion standFrame;
    /** Tracks animation frame. */
    private int prevFrame = 0;
    /** Tracks the number of frames that have passed. Used to time animation. */
    private short animationTimer = 0;
    /** Footstep sound effect. */
    Sound footstep = null;
    /** SamplePlayer sprite sheet. */
    Texture player = null;

    /** Current position in 3D space. */
    private Vector3 position = new Vector3(0, 0, 0);
    /** Current velocity. */
    private Vector3 velocity = new Vector3(0, 0, 0);

    /**
     * Load sound/texture resources.
     */
    public void load() {

        if (!this.areResourcesLoaded) {
            // Grab link to the footstep sound effect and set up TextureRegions
            // for each sprite frame.
            this.footstep = Gdx.audio.newSound(Gdx.files
                    .internal("sound/footstep2.ogg"));
            this.player = new Texture(Gdx.files.internal("img/playerM.png"));
            this.player.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
            this.runFrames[0] = new TextureRegion(player, 28, 0, 28, 58);
            this.runFrames[1] = new TextureRegion(player, 56, 0, 28, 58);
            this.runFrames[2] = new TextureRegion(player, 84, 0, 28, 58);
            this.runFrames[3] = new TextureRegion(player, 112, 0, 28, 58);
            this.runFrames[4] = new TextureRegion(player, 140, 0, 28, 58);
            this.runFrames[5] = new TextureRegion(player, 168, 0, 28, 58);
            this.standFrame = new TextureRegion(player, 0, 0, 28, 58);

            this.areResourcesLoaded = true;
        }
    }

    /**
     * Unload sound/texture resources.
     */
    public void unload() {
        if (this.areResourcesLoaded) {
            this.footstep.dispose();
            this.player.dispose();

            this.areResourcesLoaded = false;
        }
    }

    public SamplePlayer() {

    }

    /**
     * Reset to initial state.
     */
    public void initialize() {
        this.position.set(0, 0, 0);
        this.velocity.set(0, 0, 0);
        this.isFlipped = false;
        this.prevFrame = 0;
        this.animationTimer = 0;
    }

    /**
     * Render object to screen.
     */
    public void draw() {

        // Prepare the game's spriteBatch for drawing.
        MyGdxGame.currentGame.spriteBatch
                .setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
        MyGdxGame.currentGame.spriteBatch.begin();

        // If player is running, current frame and sound effects need to be
        // managed.
        if (this.isRunning) {

            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            // Determine current frame based on the animationTimer and
            // RUN_FRAMERATE. Reset animationTimer if needed.
            int frame = this.prevFrame;
            if (this.animationTimer >= SamplePlayer.RUN_FRAMERATE) {
                frame = (frame + 1) % SamplePlayer.MAX_RUN_FRAMES;
                this.animationTimer = 0;
            }

            // Flip sprite if needed.
            if (this.isFlipped != this.runFrames[frame].isFlipX()) {
                this.runFrames[frame].flip(true, false);
            }

            // Draw current sprite frame at current position.
            MyGdxGame.currentGame.spriteBatch.draw(this.runFrames[frame],
                    this.position.x, this.position.y);

            // If a new frame has just been switched to and that frame is one of
            // the ones where the sprite
            // puts its foot down, play the footstep sound effect.
            if (this.prevFrame != frame && (frame == 0 || frame == 3)) {

                // Play sound.
                this.footstep.play();
            }

            // Log current frame for later use.
            this.prevFrame = frame;

        } else {
            // SamplePlayer is standing still. Ensure the standing frame is
            // facing the
            // right way and draw it.
            if (this.isFlipped != this.standFrame.isFlipX()) {
                this.standFrame.flip(true, false);
            }
            MyGdxGame.currentGame.spriteBatch.draw(this.standFrame,
                    this.position.x, this.position.y);
        }

        // Finalize drawing.
        MyGdxGame.currentGame.spriteBatch.end();
    }

    /**
     * Update logic.
     */
    public void update(float deltaTime) {

        // Acceleration based on key states.
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            this.velocity.x = Math.max(this.velocity.x
                    - (SamplePlayer.ACCELERATION * deltaTime),
                    -SamplePlayer.MAX_SPEED);
            this.isRunning = true;
            this.isFlipped = false;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            this.velocity.x = Math.min(this.velocity.x
                    + (SamplePlayer.ACCELERATION * deltaTime),
                    SamplePlayer.MAX_SPEED);
            this.isRunning = true;
            this.isFlipped = true;
        }
        if (!Gdx.input.isKeyPressed(Keys.RIGHT)
                && !Gdx.input.isKeyPressed(Keys.LEFT)) {
            this.isRunning = false;
        }

        // Deceleration. Applied constantly.
        if (velocity.x < 0) {
            this.velocity.x = Math.min(this.velocity.x
                    + (SamplePlayer.DECELERATION * deltaTime), 0);
        } else if (this.velocity.x > 0) {
            this.velocity.x = Math.max(this.velocity.x
                    - (SamplePlayer.DECELERATION * deltaTime), 0);
        }

        // Update position.
        this.position.add(this.velocity);
    }
}