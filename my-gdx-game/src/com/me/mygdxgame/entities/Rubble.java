package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public abstract class Rubble implements GameEntity {
    
    /** Sprite to be drawn */
    protected TextureRegion rubble;
    /** Location in stage */
    protected Vector3 position;
    /** Velocity for 3 axes */
    protected Vector3 velocity;
    /** Running|Destroyed */
    protected EntityState status;
    /** List of objects stopping rubble */
    protected Rectangle [] obstacles;
    /** List of damageable objects stopping rubble */
    protected Damageable [] targets;
    
    /** Factor for creating descending motion of rubble */
    protected static final int GRAVITY = 2;
    /** Texture-control variables (can't be constants because of superclass/subclass relationship) */
    protected int x, y, w, h;
    /** Damage factor for falling rubble */
    protected int damage;    
    
    @Override
    public void update(float deltaTime) {
        if (this.status == EntityState.Running) {
            // Update the coords
            this.position.x += this.velocity.x * deltaTime;
            this.position.y += this.velocity.y * deltaTime;
            this.position.y -= GRAVITY;
            this.position.z += this.velocity.z * deltaTime;
            
            // Check for harmless collisions
            for (Rectangle r: obstacles) {
                if (r.overlaps(new Rectangle(this.position.x, this.position.y, this.w, this.h))) {
                    this.status = EntityState.Destroyed;                    
                    System.out.println(this.getClass().getName() + " : collision with object at " + r.toString());
                }
            }
            
            // Check for harmful collisions.
            for (Damageable d: targets) {
                for (Rectangle r: d.getHitArea()) {
                    if (r.overlaps(new Rectangle(
                            this.position.x, this.position.y, this.w, this.h))) {
                        d.damage(damage);
                        this.status = EntityState.Destroyed;
                        // System.out.println(this.getClass().getName() + " : collision with object at " + r.toString());
                    }
                }
            }
        }
    }

    @Override
    public void draw() {
        if (this.status == EntityState.Running){            
            // Prepare the game's spriteBatch for drawing.
            MyGdxGame.currentGame.spriteBatch
            .setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);            
            MyGdxGame.currentGame.spriteBatch.begin();
            
            // TODO spriteBatch's draw() function won't manage the z-axis automatically. Fix manually with scaleX/scaleY params? 
            MyGdxGame.currentGame.spriteBatch.draw(this.rubble, this.position.x, this.position.y);
            
            MyGdxGame.currentGame.spriteBatch.end();            
        }
    }

    @Override
    public EntityState getState() {
        return this.status;
    }

    @Override
    public boolean hasCreatedEntities() {
        return false;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        return null;
    }

}
