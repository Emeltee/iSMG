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

public class Rocket implements GameEntity, Damager {
    
    // Constants for extracting bullet from Texture
    private static final int ROCKET_X = 210;
    private static final int ROCKET_Y = 175;
    private static final int ROCKET_W = 16;
    private static final int ROCKET_H = 16;
    private static final Color ROCKET_TINT = new Color(1, 0.75f, 0.75f, 1);

    /** TextureRegion representing the idle frame. */
    private TextureRegion rocket;
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
    /** State of object */
    private EntityState state;
    /** Previous z-coord (for collision detection) */
    private float prevZ;
    /** Hit box. */
    private Rectangle hitBox = null;
    private Rectangle[] allHitAreas = null;
    /** Damage done to targets. */
    private int power = 0;
    /** Force to apply upon hitting a target. */
    private int knockback = 0;
    /** Sound to play upon explosion.*/
    private Sound explosion = null;
    
    // Entities to be created by rocket
    private Explosion[] explosions;
  
    public Rocket(Texture spriteSheet, Sound explosion, Vector3 position, Vector3 velocity, int power, int knockback, Rectangle [] obstacles, Damageable[] targets) {
        this.spriteSheet = spriteSheet;
        this.explosion = explosion;
        this.position.set(position);
        this.position.x -= ROCKET_W / 2;
        this.position.y -= ROCKET_H / 2;
        this.velocity = velocity;
        this.rocket = new TextureRegion(this.spriteSheet, ROCKET_X, ROCKET_Y, ROCKET_W, ROCKET_H);
        this.obstacles = obstacles;
        this.targets = targets;
        this.state = EntityState.Running;
        this.power = power;
        this.knockback = knockback;
        this.hitBox = new Rectangle(position.x, position.y, Rocket.ROCKET_W, Rocket.ROCKET_H);
        this.allHitAreas = new Rectangle[] {this.hitBox};
    }
    
    
    @Override
    public void update(float deltaTime) {
        this.prevZ = this.position.z;
        
        if (this.state == EntityState.Running) {
            // Move.
            this.position.x += this.velocity.x * deltaTime;;
            this.position.y +=  this.velocity.y * deltaTime;
            this.position.z += this.velocity.z * deltaTime;
            this.hitBox.setPosition(this.position.x, this.position.y);
            
            // Check for obstacle collisions.
            for (Rectangle r: this.obstacles) {
                if (r.overlaps(this.hitBox)
                        && (((this.prevZ < 0 && this.position.z > 0) // Bomb from behind 
                         || (this.prevZ > 0 && this.position.z < 0)) // Bomb from in front
                         || this.position.z == 0)) { // Bomb at precisely 0 (unlikely)
                    explode();
                    return;
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
        if (this.state == EntityState.Running){
            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            renderer.drawRegion(rocket, this.position.x, this.position.y, ROCKET_TINT, 1, 1, 30 * this.animationTimer);
        }
    }

    @Override
    public EntityState getState() {
        return this.state;
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
        this.position.x += Rocket.ROCKET_W / 2;
        this.position.y += Rocket.ROCKET_H / 2;
        
        
        // Create explosions.
        this.explosions = new Explosion[4];
        this.explosions[0] = new Explosion(this.spriteSheet, new Vector3(this.position.x+12, this.position.y+12, this.position.z));
        this.explosions[1] = new Explosion(this.spriteSheet, new Vector3(this.position.x-12, this.position.y+12, this.position.z));
        this.explosions[2] = new Explosion(this.spriteSheet, new Vector3(this.position.x+12, this.position.y-12, this.position.z));
        this.explosions[3] = new Explosion(this.spriteSheet, new Vector3(this.position.x-12, this.position.y-12, this.position.z));
        
        // Play sound.
        this.explosion.play();
        
        // Set object state.
        this.state = EntityState.Destroyed;
    }


    @Override
    public Rectangle[] getHitArea() {
        return this.allHitAreas;
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
        return Rocket.ROCKET_W;
    }


    @Override
    public int getHeight() {
        return Rocket.ROCKET_H;
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
