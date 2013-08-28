package com.me.mygdxgame.entities.projectiles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
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
    /** Time to remain active before automatically being destroyed.*/
    protected float lifetime = 10.0f;
    
    /** Factor for creating descending motion of rubble */
    protected static final int GRAVITY = 600;
    /** Factor that determines the rate at which x velocity moves towards 0.*/
    protected static final int INERTIA = 200;
    /** X velocity at which object stops moving horizontally.*/
    protected static final float MIN_X_VELOCITY = 0.5f;
    /** Maximum fall rate.*/
    private static final int MIN_Y_VELOCITY = -300;
    /** Texture-control variables (can't be constants because of superclass/subclass relationship) */
    protected int x, y, w, h;
    /** Damage factor for falling rubble */
    protected int damage;    
    
    @Override
    public void update(float deltaTime) {
        if (this.status == EntityState.Running) {
            
            // Update the coords
            this.position.add(this.velocity.cpy().scl(deltaTime));
            
            // Adjust velocity. Y goes down, x moves towards 0.
            this.velocity.y = Math.max(Rubble.MIN_Y_VELOCITY, this.velocity.y - Rubble.GRAVITY * deltaTime);
            if (this.velocity.x < Rubble.MIN_X_VELOCITY) {
                this.velocity.x = 0;
            } else {
                if (this.velocity.x > 0) {
                    this.velocity.x -= Rubble.INERTIA * deltaTime;
                } else {
                    this.velocity.x += Rubble.INERTIA * deltaTime;
                }
            }
            
            // Destroy if lifetime has been reached.
            this.lifetime -= deltaTime;
            if (this.lifetime < 0) {
                this.status = EntityState.Destroyed;
                return;
            }
            
            // Check for harmless collisions
            for (Rectangle r: obstacles) {
                if (r.overlaps(new Rectangle(this.position.x, this.position.y, this.w, this.h))) {
                    this.status = EntityState.Destroyed;
                    return;
                }
            }
            
            // Check for harmful collisions.
            for (Damageable d: targets) {
                for (Rectangle r: d.getHitArea()) {
                    if (r.overlaps(new Rectangle(
                            this.position.x, this.position.y, this.w, this.h))) {
                        d.damage(damage);
                        this.status = EntityState.Destroyed;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        if (this.status == EntityState.Running){            
            // Prepare the game's spriteBatch for drawing.
            MyGdxGame.currentGame.spriteBatch
            .setProjectionMatrix(transformMatrix);            
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
