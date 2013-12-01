package com.me.mygdxgame.entities.projectiles;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.particles.Splash;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

/**
 * Simple {@link Damager}s. Initial velocity is specified. Rubble then begins
 * getting pulled downwards by gravity. Horizontal movement also slows to 0
 * over time.
 */
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
    protected GameEntity [] obstacles;
    /** List of damageable objects stopping rubble */
    protected Damageable [] targets;
    /** Time to remain active before automatically being destroyed.*/
    protected float lifetime = 2.0f;
    
    protected Rectangle hitbox = new Rectangle(0, 0, 0, 0);
    protected Rectangle[] area = new Rectangle[] {this.hitbox};
    
    /** Factor for creating descending motion of rubble */
    protected static final float GRAVITY = 800.0f;
    /** Factor that determines the rate at which x velocity moves towards 0.*/
    protected static final float INERTIA = 200.0f;
    /** X velocity at which object stops moving horizontally.*/
    protected static final float MIN_X_VELOCITY = 12.0f;
    /** Maximum fall rate.*/
    private static final float MIN_Y_VELOCITY = -20000.0f;
    /** Texture-control variables (can't be constants because of superclass/subclass relationship) */
    protected int x, y, w, h;
    /** Damage factor for falling rubble */
    protected int power = 0;
    /** Knockback for falling rubble */
    protected int knockback = 0;
    protected float scale = 1.0f;
    
    protected boolean isUnderwater = true;
    
    protected ArrayList<GameEntity> createdEntities = new ArrayList<GameEntity>();
    
    public Rubble(int x, int y, int width, int height) {
        this.hitbox.set(x, y, width, height);
    }
    
    public void setWaterLevel(float waterLevel, Sound splashSound) {
        
        if (this.isUnderwater) {
            if (this.position.y > waterLevel) {
                this.isUnderwater = false;
            }
        }
        else if (this.position.y < waterLevel) {
            this.isUnderwater = true;
            splash(splashSound);
        }
    }
    
    private void splash(Sound splashSound) {
        float adjustedX = this.position.x + this.rubble.getRegionWidth() / 2;
        for (int x = 0; x < 2; x++) {
            this.createdEntities.add(new Splash(adjustedX, this.position.y,
                    (float) (Math.random() * Splash.MAX_INIT_X_VEL),
                    (float) ((Math.random() * 0.25 + 0.75) * (Splash.MAX_INIT_Y_VEL * 1.5f)),
                    Splash.DEFAULT_RADIUS));
            this.createdEntities.add(new Splash(adjustedX, this.position.y,
                    (float) (-Math.random() * Splash.MAX_INIT_X_VEL),
                    (float) ((Math.random() * 0.25 + 0.75) * (Splash.MAX_INIT_Y_VEL * 1.5f)),
                    Splash.DEFAULT_RADIUS));

        }
        //splashSound.play(0.2f);
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.state == EntityState.Running) {
            
            // Update the coords
            this.position.add(this.velocity.cpy().scl(deltaTime));
            
            // Adjust velocity. Y goes down, x moves towards 0.
            this.velocity.y = Math.max(Rubble.MIN_Y_VELOCITY * 
                    deltaTime, this.velocity.y - Rubble.GRAVITY * deltaTime);
            if (Math.abs(this.velocity.x) < Rubble.MIN_X_VELOCITY * deltaTime) {
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
            for (GameEntity entity : this.obstacles) {
                for (Rectangle r: entity.getHitArea()) {
                    if (r.overlaps(new Rectangle(this.position.x, this.position.y, this.w, this.h))) {
                        this.state = EntityState.Destroyed;
                        return;
                    }
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
            renderer.drawRegion(this.rubble, (int)this.position.x, 
                    (int)this.position.y, Color.WHITE, this.scale, this.scale, 0);
        }
    }

    @Override
    public EntityState getState() {
        return this.state;
    }

    @Override
    public boolean hasCreatedEntities() {
        return !this.createdEntities.isEmpty();
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        if (this.createdEntities.isEmpty()) {
            throw new NoSuchElementException();
        }
        GameEntity[] entities = new GameEntity[this.createdEntities.size()];
        entities = this.createdEntities.toArray(entities);
        this.createdEntities.clear();
        return entities;
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
