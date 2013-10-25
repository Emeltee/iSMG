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

public class Bomb implements GameEntity, Damager {
    
    // Constants for extracting bullet from Texture
    private static final int BOMB_X = 210;
    private static final int BOMB_Y = 175;
    private static final int BOMB_W = 16;
    private static final int BOMB_H = 16;

    private static final float GRAVITY = 250;
    private static final int EXPLOSION_EXPANSION = 30;
    private static final float MIN_GRAV = -180.0f;
    
    /** TextureRegion representing the idle frame. */
    private TextureRegion bomb;
    /** Tracks the number of frames that have passed. Used to time animation. */
    private short animationTimer = 0;
    /** General-purpose sprite sheet. */
    private Texture spriteSheet = null;

    /** Current position in 3D space. */
    private Vector3 position = new Vector3(0, 0, 0);
    /** Current velocity. */
    private Vector3 velocity = new Vector3(0, 0, 0);
    /** Collision detection rectangles */
    private Rectangle [] obstacles;
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
  
    public Bomb(Texture spriteSheet, Sound explosion, Vector3 position, Vector3 velocity, int power, int knockback, Rectangle [] obstacles, Damageable[] targets) {
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
            for (Rectangle r: this.obstacles) {
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
                    break;
                }
            }
            
            // Check for target collisions.
            Rectangle[] hitAreas = null;
            for (Damageable d: this.targets) {
                hitAreas = d.getHitArea();
                for (Rectangle r : hitAreas) {
                    if (r.overlaps(this.hitBox)
                            && (((this.prevZ < 0 && this.position.z > 0) // Bomb from behind 
                             || (this.prevZ > 0 && this.position.z < 0)) // Bomb from in front
                             || this.position.z == 0)) { // Bomb at precisely 0 (unlikely)
                        // Apply power and force, and explode. Don't apply force on z.
                        d.damage(this);
                        explode();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Renderer renderer) {
        if (this.status == EntityState.Running){
            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            renderer.drawRegion(bomb, this.position.x, this.position.y, Color.WHITE, 1, 1, 30 * this.animationTimer);
            // TODO spriteBatch's draw() function won't manage the z-axis automatically. Fix manually with scaleX/scaleY params? 
            //MyGdxGame.currentGame.spriteBatch.draw(bomb, this.position.x, this.position.y, this.bomb.getRegionWidth() / 2.0f, this.bomb.getRegionHeight() / 2.0f, BOMB_W, BOMB_H, 1, 1, 30 * this.animationTimer);
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
        // Adjust position vector to center of sprite.
        this.position.x += Bomb.BOMB_W / 2;
        this.position.y += Bomb.BOMB_H / 2;
        
        
        // Create explosions.
        this.explosions = new Explosion[4];
        this.explosions[0] = new Explosion(this.spriteSheet, new Vector3(this.position.x+12, this.position.y+12, this.position.z));
        this.explosions[1] = new Explosion(this.spriteSheet, new Vector3(this.position.x-12, this.position.y+12, this.position.z));
        this.explosions[2] = new Explosion(this.spriteSheet, new Vector3(this.position.x+12, this.position.y-12, this.position.z));
        this.explosions[3] = new Explosion(this.spriteSheet, new Vector3(this.position.x-12, this.position.y-12, this.position.z));
        
        // Play sound.
        this.explosion.play();
        
        // Set object state.
        this.status = EntityState.Destroyed;
    }


    @Override
    public Rectangle[] getHitArea() {
        return new Rectangle[] {this.hitBox};
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
