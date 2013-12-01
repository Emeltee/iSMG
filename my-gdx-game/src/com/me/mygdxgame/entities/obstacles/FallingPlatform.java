package com.me.mygdxgame.entities.obstacles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.EntityState;

/**
 * An obstacle that can be made to fall downwards to some specified y position.
 * While falling, an empty array will be returned when the hit area is queried.
 * Therefore, collisions should only register with it when it is at rest.
 * <p>
 * Once the destination y position is reached, the platform will stop falling,
 * regardless of whether the target y is changed later. It can be made to fall
 * again {@link #fall()}.
 */
public class FallingPlatform extends Platform {
    
    private static final int GRAVITY = 400;
    private int destinationY;
    private boolean hasLanded;
    private Sound landingSound;
    private Rectangle[] emptyHitArea = new Rectangle[0];
    private Rectangle[] hitArea = new Rectangle[1];
    
    public FallingPlatform(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, Sound landSound, int x, int y, int destinationY) {
        super(spriteSheet, tiles, x, y);
        this.destinationY = destinationY;
        this.hasLanded = true;
        this.landingSound = landSound;
        this.hitArea[0] = this.hitbox;
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.status == EntityState.Running) {
            if (this.health <= 0) {
                this.explode();
                this.status = EntityState.Destroyed;
            }
            
            if (!this.hasLanded) {
                if (this.y > destinationY) {                
                    // If gravity pulls the platform past the destination, bump it back up
                    this.y = (int) Math.max(this.y - (GRAVITY * deltaTime), this.destinationY);
                    this.hitbox.y = this.y;
                } else {
                    this.hasLanded = true;
                    this.landingSound.stop();
                    this.landingSound.play();
                }
            }
        }
    }
    
    @Override
    public Rectangle [] getHitArea() {
        // Prevent collision detection until the platform lands
        return (this.hasLanded) ? this.hitArea : this.emptyHitArea;
    }
    
    /**
     * Sets the y coordinate that this platform should fall to upon calling
     * {@link #fall()}. Target y may be changed mid-fall. Changing the target y
     * to a position greater than or equal to the platform's current position
     * will cause it to land.
     * 
     * @param targetY The y coordinate this platform should fall to.
     */
    public void setTargetY(int targetY) {
        this.destinationY = targetY;
    }
    
    /**
     * Check whether or not this platform is falling or has fallen.
     * 
     * @return True if platform is at rest, false if it is falling.
     */
    public boolean hasFallen() {
        return this.hasLanded;
    }
    
    /**
     * If platform is at rest, triggers a fall. Platform will begin moving
     * downwards to its target y coordinate. Has no effect if platform is
     * already falling.
     */
    public void fall() {
        this.hasLanded = false;
    }

}
