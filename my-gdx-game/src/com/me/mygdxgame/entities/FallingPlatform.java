package com.me.mygdxgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.utilities.EntityState;

public class FallingPlatform extends Platform {
    
    /* This is untested, but it seems simple enough and I'm too tired to check,
     * so if you're planning on finishing this tonight and there's a problem,
     * there's a decent chance it's here.
     */
    
    private static final int GRAVITY = 3;
    private int destinationY;
    private boolean hasLanded;
    
    public FallingPlatform(Texture spriteSheet, int x, int y, int destinationY) {
        super(spriteSheet, x, y);
        this.destinationY = destinationY;
        this.hasLanded = false;
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
                    this.y = Math.max(this.y - GRAVITY, this.destinationY);
                } else {
                    this.hasLanded = true;
                }
            }
        }
    }
    
    public Rectangle [] getHitArea() {
        // Prevent collision detection until the platform lands
        return (this.hasLanded) ? new Rectangle [] { this.hitbox } : new Rectangle [] {};
    }

}
