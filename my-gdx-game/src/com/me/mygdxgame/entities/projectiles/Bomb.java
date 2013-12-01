package com.me.mygdxgame.entities.projectiles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.particles.Explosion;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

/**
 * Simple {@link Damager} that moves in an arch. It starts with an initial
 * velocity and slowly gets pulled downwards by gravity.
 * <p>
 * Is capable of moving and colliding with objects along the z axis, although
 * this behavior has not been tested. For basic usage in 2D space, simply set
 * the z velocity to 0.
 */
public class Bomb implements GameEntity, Damager {
    
    // Constants for extracting image from Texture
    private static final int BOMB_X = 210;
    private static final int BOMB_Y = 175;
    private static final int BOMB_W = 16;
    private static final int BOMB_H = 16;

    private static final float GRAVITY = 250;
    private static final int EXPLOSION_EXPANSION = 30;
    private static final float MIN_GRAV = -180.0f;
    private static final float SFX_VOLUME = 0.5f;
    
    /** TextureRegion representing the idle frame. */
    private TextureRegion bomb;
    /** Tracks the number of frames that have passed. Used to time animation. */
    private float animationTimer = 0;
    /** General-purpose sprite sheet. */
    private Texture spriteSheet = null;

    /** Current position in 3D space. */
    private Vector3 position = new Vector3(0, 0, 0);
    /** Current velocity. */
    private Vector3 velocity = new Vector3(0, 0, 0);
    /** Collision detection rectangles */
    private GameEntity[] obstacles;
    /** Targets to power. */
    private Damageable[] targets;
    /** Status of object */
    private EntityState status;
    /** Previous z-coord for collision detection */
    private float prevZ;
    /** Hit box. */
    private Rectangle hitBox = new Rectangle(0, 0, BOMB_W, BOMB_H);
    /** Damage done to targets. */
    private int power = 0;
    /** Force to apply upon hitting a target. */
    private int knockback = 0;
    /** Sound to play upon explosion.*/
    private Sound explosion = null;
    
    private Explosion[] explosions;
    
    private Rectangle[] hitAreas = new Rectangle[1];
  
    public Bomb(Texture spriteSheet, Sound explosion, Vector3 position, Vector3 velocity, int power,
            int knockback, GameEntity [] obstacles, Damageable[] targets) {
        this.spriteSheet = spriteSheet;
        this.explosion = explosion;
        this.position.set(position);
        this.position.x -= BOMB_W / 2;
        this.position.y -= BOMB_H / 2;
        this.velocity.set(velocity);
        this.bomb = new TextureRegion(this.spriteSheet, BOMB_X, BOMB_Y, BOMB_W, BOMB_H);
        this.obstacles = obstacles;
        this.power = power;
        this.knockback = knockback;
        this.targets = targets;
        this.status = EntityState.Running;
        this.hitAreas[0] = this.hitBox;
    }
    
    
    @Override
    public void update(float deltaTime) {
        this.prevZ = this.position.z;
        
        if (this.status == EntityState.Running) {
            // Move.
            this.position.x += this.velocity.x * deltaTime;;
            this.position.y +=  this.velocity.y * deltaTime;
            this.velocity.y = Math.max(MIN_GRAV, this.velocity.y - GRAVITY * deltaTime);
            this.position.z += this.velocity.z * deltaTime;
            this.hitBox.setPosition(this.position.x, this.position.y);
            
            // Check for obstacle collisions.
            for (GameEntity entity : this.obstacles) {
                for (Rectangle r: entity.getHitArea()) {
                    if (r.overlaps(this.hitBox)
                            && (((this.prevZ < 0 && this.position.z > 0) // Bomb from behind 
                             || (this.prevZ > 0 && this.position.z < 0)) // Bomb from in front
                             || this.position.z == 0)) { // Bomb at precisely 0 (unlikely)
                        explode();
                        // Expand hitbox on collision for check against targets.
                        int hitboxOffset = EXPLOSION_EXPANSION / 2;
                        this.hitBox.x -= hitboxOffset;
                        this.hitBox.y -= hitboxOffset;
                        this.hitBox.width += EXPLOSION_EXPANSION;
                        this.hitBox.height += EXPLOSION_EXPANSION;
                        explode();
                        break;
                    }
                }
                if (this.status == EntityState.Destroyed) {
                    break;
                }
            }
            
            // Check for target collisions.
            for (Damageable d: this.targets) {
                for (Rectangle r : d.getHitArea()) {
                    if (r.overlaps(this.hitBox)
                            && (((this.prevZ < 0 && this.position.z > 0) // Bomb from behind 
                             || (this.prevZ > 0 && this.position.z < 0)) // Bomb from in front
                             || this.position.z == 0)) { // Bomb at precisely 0 (unlikely)
                        // Apply power and force, and explode. Don't apply force on z.
                        d.damage(this);
                        explode();
                    }
                }
            }
            
            // Every call, increment the animationTimer.
            this.animationTimer += deltaTime;
        }
    }

    @Override
    public void draw(Renderer renderer) {
        if (this.status == EntityState.Running){
            renderer.drawRegion(bomb, this.position.x, this.position.y, Color.WHITE, 
                    1, 1, (int) (300.0f * this.animationTimer));
        }
    }

    @Override
    public EntityState getState() {
        return this.status;
    }

    @Override
    public boolean hasCreatedEntities() {
        return this.explosions != null;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        if (this.explosions == null) {
            throw new NoSuchElementException();
        }
        
        GameEntity[] returnList = this.explosions;
        this.explosions = null;
        
        return returnList;
    }

    private void explode() {
        if (this.status != EntityState.Destroyed) {
            // Adjust position vector to center of sprite.
            this.position.x += Bomb.BOMB_W / 2;
            this.position.y += Bomb.BOMB_H / 2;
            
            
            // Create explosions.
            this.explosions = new Explosion[4];
            this.explosions[0] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x+12, this.position.y+12, this.position.z));
            this.explosions[1] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x-12, this.position.y+12, this.position.z));
            this.explosions[2] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x+12, this.position.y-12, this.position.z));
            this.explosions[3] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x-12, this.position.y-12, this.position.z));
            
            // Play sound.
            this.explosion.stop();
            this.explosion.play(SFX_VOLUME);
            
            // Set state.
            this.status = EntityState.Destroyed;
        }
    }


    @Override
    public Rectangle[] getHitArea() {
        return this.hitAreas;
    }


    @Override
    public void destroy() {
        this.status = EntityState.Destroyed;
    }


    @Override
    public Vector3 getPosition() {
        return new Vector3(this.position);
    }


    @Override
    public int getWidth() {
        return Bomb.BOMB_W;
    }


    @Override
    public int getHeight() {
        return Bomb.BOMB_H;
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
