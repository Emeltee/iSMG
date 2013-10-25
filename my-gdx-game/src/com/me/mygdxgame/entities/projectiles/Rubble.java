package com.me.mygdxgame.entities.projectiles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public abstract class Rubble implements GameEntity, Damager {
    
    /** Sprite to be drawn */
    protected TextureRegion rubble;
    /** Location in stage */
    protected Vector3 position;
    /** Velocity for 3 axes */
    protected Vector3 velocity;
    /** Running|Destroyed */
    protected EntityState state;
    /** List of objects stopping rubble */
    protected Rectangle [] obstacles;
    /** List of damageable objects stopping rubble */
    protected Damageable [] targets;
    /** Time to remain active before automatically being destroyed.*/
    protected float lifetime = 2.0f;
    
    protected Rectangle hitbox = new Rectangle(0, 0, 0, 0);
    protected Rectangle[] area = new Rectangle[] {this.hitbox};
    
    /** Factor for creating descending motion of rubble */
    protected static final int GRAVITY = 800;
    /** Factor that determines the rate at which x velocity moves towards 0.*/
    protected static final int INERTIA = 200;
    /** X velocity at which object stops moving horizontally.*/
    protected static final float MIN_X_VELOCITY = 0.2f;
    /** Maximum fall rate.*/
    private static final int MIN_Y_VELOCITY = -500;
    /** Texture-control variables (can't be constants because of superclass/subclass relationship) */
    protected int x, y, w, h;
    /** Damage factor for falling rubble */
    protected int power = 0;
    /** Knockback for falling rubble */
    protected int knockback = 0;
    
    public Rubble(int x, int y, int width, int height) {
        this.hitbox.set(x, y, width, height);
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.state == EntityState.Running) {
            
            // Update the coords
            this.position.add(this.velocity.cpy().scl(deltaTime));
            
            // Adjust velocity. Y goes down, x moves towards 0.
            this.velocity.y = Math.max(Rubble.MIN_Y_VELOCITY, this.velocity.y - Rubble.GRAVITY * deltaTime);
            if (Math.abs(this.velocity.x) < Rubble.MIN_X_VELOCITY) {
                this.velocity.x = 0;
            } else {
                if (this.velocity.x > 0) {
                    this.velocity.x -= Rubble.INERTIA * deltaTime;
                } else {
                    this.velocity.x += Rubble.INERTIA * deltaTime;
                }
            }
            this.hitbox.setPosition(this.position.x, this.position.y);
            
            // Destroy if lifetime has been reached.
            this.lifetime -= deltaTime;
            if (this.lifetime < 0) {
                this.state = EntityState.Destroyed;
                return;
            }
            
            // Check for harmless collisions
            for (Rectangle r: obstacles) {
                if (r.overlaps(new Rectangle(this.position.x, this.position.y, this.w, this.h))) {
                    this.state = EntityState.Destroyed;
                    return;
                }
            }
            
            // Check for harmful collisions.
            for (Damageable d: targets) {
                for (Rectangle r: d.getHitArea()) {
                    if (r.overlaps(new Rectangle(
                            this.position.x, this.position.y, this.w, this.h))) {
                        d.damage(this);
                        this.state = EntityState.Destroyed;
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Renderer renderer) {
        if (this.state == EntityState.Running){            
            renderer.drawRegion(this.rubble, (int)this.position.x, (int)this.position.y);
        }
    }

    @Override
    public EntityState getState() {
        return this.state;
    }

    @Override
    public boolean hasCreatedEntities() {
        return false;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        throw new NoSuchElementException();
    }
    
    @Override
    public Rectangle[] getHitArea() {
        return this.area;
    }
    
    @Override
    public void destroy() {
        this.state = EntityState.Destroyed;
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.position);
    }

    @Override
    public int getWidth() {
        return (int) this.hitbox.width;
    }

    @Override
    public int getHeight() {
        return (int) this.hitbox.height;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public int getKnockback() {
        return this.knockback;
    }

}
