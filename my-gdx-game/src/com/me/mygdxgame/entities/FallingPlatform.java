package com.me.mygdxgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.me.mygdxgame.utilities.EntityState;

public class FallingPlatform extends Platform {
    
    /* This is untested, but it seems simple enough and I'm too tired to check,
     * so if you're planning on finishing this tonight and there's a problem,
     * there's a decent chance it's here.
     */
    
    private static final int GRAVITY = 3;
    private int destinationY;
    
    public FallingPlatform(Texture spriteSheet, int x, int y, int destinationY) {
        super(spriteSheet, x, y);
        this.destinationY = destinationY;
    }
    
    public void update() {
        if (this.status == EntityState.Running) {
            if (this.health <= 0) {
                this.explode();
                this.status = EntityState.Destroyed;
            }
            
            if (this.y > destinationY) {
                this.y -= GRAVITY;
            }
        }
    }

}
