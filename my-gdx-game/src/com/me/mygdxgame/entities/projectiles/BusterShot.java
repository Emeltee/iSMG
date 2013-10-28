package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class BusterShot implements GameEntity, Damager {
    
    // Just controlling which way it goes.
    public static enum ShotDirection { LEFT, RIGHT };
    
    // Constants for extracting bullet from Texture
    protected static final int BULLET_X = 226;
    protected static final int BULLET_Y = 175;
    protected static final int BULLET_W = 7;
    protected static final int BULLET_H = 5;
 
    /** TextureRegion representing the idle frame. */
    protected TextureRegion bullet;
    /** Tracks the number of frames that have passed. Used to time animation. */
    protected short animationTimer = 0;
    /** General-purpose sprite sheet. */
    protected Texture spriteSheet = null;
    /** Sound to play upon destruction when no targets are hit.*/
    protected Sound missSound = null;

    /** Direction of bullet trajectory */
    protected ShotDirection dir;

    /** Current position in 3D space. */
    protected Vector3 position = new Vector3(0, 0, 0);
    /** Current velocity. */
    protected int speed =  0;
    /** Damage done on collision.*/
    protected int power = 0;
    /** Distance shot may travel.*/
    protected float range = 0;
    protected float distanceTraveled = 0;
    /** Hitbox.*/
    protected Rectangle hitBox = new Rectangle(0, 0, BusterShot.BULLET_W, BusterShot.BULLET_H);
    
    protected EntityState state;
    
    /** Areas of the map to look out for. */
    protected Collection<GameEntity> obstacles;
    /** Stuff to hurt.*/
    protected Collection<Damageable> targets;
  
    public BusterShot() {}
    
    public BusterShot(Texture spriteSheet, Sound missSound, Vector3 position, int speed,
            ShotDirection dir, int power, float range, Collection<GameEntity> obstacles,
            Collection<Damageable> targets) {
        this.spriteSheet = spriteSheet;
        this.missSound = missSound;
        this.position.set(position);
        this.speed = speed;
        this.dir = dir;
        this.range = range;
        this.power = power;
        this.bullet = new TextureRegion(this.spriteSheet, BULLET_X, BULLET_Y, BULLET_W, BULLET_H);
        this.obstacles = obstacles;
        this.targets = targets;
        this.state = EntityState.Running;
    }
    
    
    @Override
    public void update(float deltaTime) {
        
        if (this.state == EntityState.Running) {
            // Move. Assumes speed is always positive.
            float toTravel = this.speed * deltaTime;
            if (this.dir == ShotDirection.LEFT) {
                this.position.x -= toTravel;            
            } else {
                this.position.x += toTravel;
            }
            
            // Check if range has been traveled. If so, destroy.
            this.distanceTraveled += toTravel;
            if (this.distanceTraveled >= this.range) {
                this.state = EntityState.Destroyed;
                this.missSound.play();
                return;
            }
            
            // Update hitbox.
            this.hitBox.x = this.position.x;
            this.hitBox.y = this.position.y;
            
            // Check collisions with potential targets.
            for (Damageable target : this.targets) {
                for (Rectangle hitBox : target.getHitArea()) {
                    if (hitBox.overlaps(this.hitBox)) {
                        target.damage(this);
                        this.state = EntityState.Destroyed;
                        return;
                    }
                }
            }
            
            // Check for collisions with obstacles.
            for (GameEntity entity : this.obstacles) {
                for (Rectangle r: entity.getHitArea()) {
                    if (r.overlaps(this.hitBox)) {
                        this.state = EntityState.Destroyed;
                        this.missSound.play();
                        return;
                    }
                }
            }
        }

    }

    @Override
    public void draw(Renderer renderer) {
        
        if (this.state == EntityState.Running) {
            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            renderer.drawRegion(bullet, this.position.x, this.position.y, Color.WHITE, 1, 1, 30 * this.animationTimer);
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
        return new Rectangle[] {this.hitBox};
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
        return BusterShot.BULLET_W;
    }

    @Override
    public int getHeight() {
        return BusterShot.BULLET_H;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public int getKnockback() {
        return 0;
    }

}
