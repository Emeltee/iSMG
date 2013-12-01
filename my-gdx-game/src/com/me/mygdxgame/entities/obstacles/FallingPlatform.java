package com.me.mygdxgame.entities.obstacles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.EntityState;

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
    
    public Rectangle [] getHitArea() {
        // Prevent collision detection until the platform lands
        return (this.hasLanded) ? this.hitArea : this.emptyHitArea;
    }
    
    public void setTargetY(int targetY) {
        this.destinationY = targetY;
    }
    
    public boolean hasFallen() {
        return this.hasLanded;
    }
    
    public void fall() {
        this.hasLanded = false;
    }

}
