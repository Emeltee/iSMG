package com.me.mygdxgame.entities.projectiles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class BusterShot implements GameEntity {
    
    // Just controlling which way it goes.
    public static enum ShotDirection { LEFT, RIGHT };
    
    // Constants for extracting bullet from Texture
    public static final int BULLET_X = 226;
    public static final int BULLET_Y = 175;
    public static final int BULLET_W = 7;
    public static final int BULLET_H = 5;
 
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
    
    protected EntityState status;
    
    /** Areas of the map to look out for. */
    protected Rectangle [] watchOut;
    /** Stuff to hurt.*/
    protected Damageable[] targets;
  
    public BusterShot() {}
    
    public BusterShot(Texture spriteSheet, Sound missSound, Vector3 position, int speed, ShotDirection dir, int power, float range, Rectangle[] obstacles, Damageable[] targets) {
        this.spriteSheet = spriteSheet;
        this.missSound = missSound;
        this.position.set(position);
        this.speed = speed;
        this.dir = dir;
        this.range = range;
        this.power = power;
        this.bullet = new TextureRegion(this.spriteSheet, BULLET_X, BULLET_Y, BULLET_W, BULLET_H);
        this.watchOut = obstacles;
        this.targets = targets;
        this.status = EntityState.Running;
    }
    
    
    @Override
    public void update(float deltaTime) {
        
        if (this.status == EntityState.Running) {
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
                this.status = EntityState.Destroyed;
                this.missSound.play();
                return;
            }
            
            // Update hitbox.
            this.hitBox.x = this.position.x;
            this.hitBox.y = this.position.y;
            
            // Check collisions with potential targets.
            for (Damageable target : this.targets) {
                Rectangle[] hitAreas = target.getHitArea();
                for (Rectangle hitBox : hitAreas) {
                    if (hitBox.overlaps(this.hitBox)) {
                        target.damage(this.power);
                        this.status = EntityState.Destroyed;
                        return;
                    }
                }
            }
            
            // Check for collisions with obstacles.
            for (Rectangle r: this.watchOut) {
                if (r.overlaps(this.hitBox)) {
                    this.status = EntityState.Destroyed;
                    this.missSound.play();
                    return;
                }
            }
        }

    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        
        if (this.status == EntityState.Running) {
            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            // Prepare the game's spriteBatch for drawing.
            MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);
            
            MyGdxGame.currentGame.spriteBatch.begin();
            
            MyGdxGame.currentGame.spriteBatch.draw(bullet, this.position.x, this.position.y, this.bullet.getRegionWidth() / 2.0f, this.bullet.getRegionHeight() / 2.0f, BULLET_W, BULLET_H, 1, 1, 30 * this.animationTimer);
            
            MyGdxGame.currentGame.spriteBatch.end();
        }
    }

    @Override
    public EntityState getState() {
        // TODO Auto-generated method stub
        return this.status;
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
