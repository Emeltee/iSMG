package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.screens.samplescreen.SamplePlayer;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class BusterShot implements GameEntity {
    
    // Just controlling which way it goes.
    public static enum ShotDirection { LEFT, RIGHT };

    // Various speed control constants
    public static final float MAX_SPEED = 5.0f;
    public static final float ACCELERATION = 120.0f;
    public static final float DECELERATION = 0.0f;
    public static final short SHOT_FRAMERATE = 3;
    public static final short MAX_SHOT_FRAMES = 6; // 0 deg, 30 deg, 60 deg, 90 deg, 120 deg, 150 deg
    
    // Constants for extracting bullet from Texture
    private static final int BULLET_X = 226;
    private static final int BULLET_Y = 175;
    private static final int BULLET_W = 7;
    private static final int BULLET_H = 5;

    /** Flag indicating if resources are currently loaded. */
    private boolean areResourcesLoaded = false;    
    /** TextureRegion representing the idle frame. */
    private TextureRegion bullet;
    /** Tracks animation frame. */
    private int prevFrame = 0;
    /** Tracks the number of frames that have passed. Used to time animation. */
    private short animationTimer = 0;
    /** General-purpose sprite sheet. */
    private Texture spriteSheet = null;
    /** Initial coords onscreen for bullet */
    private int x, y;
    /** Direction of bullet trajectory */
    private ShotDirection dir;

    /** Current position in 3D space. */
    private Vector3 position = new Vector3(0, 0, 0);
    /** Current velocity. */
    private Vector3 velocity = new Vector3(0, 0, 0);
  
    public BusterShot(Texture spriteSheet, int x, int y, ShotDirection dir) {
        this.spriteSheet = spriteSheet;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.bullet = new TextureRegion(this.spriteSheet, BULLET_X, BULLET_Y, BULLET_W, BULLET_H);
    }
    
    
    @Override
    public void update(float deltaTime) {
        if (this.dir == ShotDirection.LEFT) {
            // Ripped straight from your code.
            this.velocity.x = Math.max(this.velocity.x
                    - (BusterShot.ACCELERATION * deltaTime),
                    -BusterShot.MAX_SPEED);
        } else {
            // Ripped straight from your code
            this.velocity.x = Math.min(this.velocity.x
                    + (BusterShot.ACCELERATION * deltaTime),
                    BusterShot.MAX_SPEED);
        }  
        
        // Deceleration. Applied constantly.
        if (velocity.x < 0) {
            this.velocity.x = Math.min(this.velocity.x
                    + (BusterShot.DECELERATION * deltaTime), 0);
        } else if (this.velocity.x > 0) {
            this.velocity.x = Math.max(this.velocity.x
                    - (BusterShot.DECELERATION * deltaTime), 0);
        }

        // Update position.
        this.position.add(this.velocity);
        
       
    }

    @Override
    public void draw() {
        // Prepare the game's spriteBatch for drawing.
        MyGdxGame.currentGame.spriteBatch
        .setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
        MyGdxGame.currentGame.spriteBatch.begin();
        
        // Every call, increment the animationTimer.
        this.animationTimer++;

        // Determine current frame based on the animationTimer and
        // RUN_FRAMERATE. Reset animationTimer if needed.
        int frame = this.prevFrame;
        if (this.animationTimer >= BusterShot.SHOT_FRAMERATE) {
            frame = (frame + 1) % BusterShot.MAX_SHOT_FRAMES;
            this.animationTimer = 0;
        }
        
        MyGdxGame.currentGame.spriteBatch.draw(bullet, this.position.x, this.position.y, this.position.x, this.position.y, BULLET_W, BULLET_H, 1, 1, 30 * this.animationTimer);
        MyGdxGame.currentGame.spriteBatch.end();
    }

    @Override
    public EntityState getState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasCreatedEntities() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        // TODO Auto-generated method stub
        return null;
    }

}
