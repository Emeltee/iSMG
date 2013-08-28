package com.me.mygdxgame.entities.projectiles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.particles.Explosion;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class Rocket implements GameEntity {
    
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
    /** Targets to damage. */
    private Damageable[] targets;
    /** State of object */
    private EntityState status;
    /** Previous z-coord (for collision detection) */
    private float prevZ;
    /** Hit box. */
    private Rectangle hitBox = null;
    /** Damage done to targets. */
    private int power = 0;
    /** Force to apply upon hitting a target. */
    private float knockback = 0;
    
    // Entities to be created by rocket
    private Explosion[] explosions;
  
    public Rocket(Texture spriteSheet, Vector3 position, Vector3 velocity, int power, float knockback, Rectangle [] obstacles, Damageable[] targets) {
        this.spriteSheet = spriteSheet;
        this.position.set(position);
        this.velocity = velocity;
        this.rocket = new TextureRegion(this.spriteSheet, ROCKET_X, ROCKET_Y, ROCKET_W, ROCKET_H);
        this.obstacles = obstacles;
        this.targets = targets;
        this.status = EntityState.Running;
        this.power = power;
        this.knockback = knockback;
        this.hitBox = new Rectangle(position.x, position.y, Rocket.ROCKET_W, Rocket.ROCKET_H);
    }
    
    
    @Override
    public void update(float deltaTime) {
        this.prevZ = this.position.z;
        
        if (this.status == EntityState.Running) {
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
                        // Apply damage and force, and explode. Don't apply force on z.
                        d.damage(this.power);
                        d.applyForce(new Vector3(this.position.x - r.x, this.position.y - r.y, 0).nor().scl(this.knockback));
                        explode();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        if (this.status == EntityState.Running){
            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            // Prepare the game's spriteBatch for drawing.
            MyGdxGame.currentGame.spriteBatch
            .setProjectionMatrix(transformMatrix);
            
            MyGdxGame.currentGame.spriteBatch.begin();
            MyGdxGame.currentGame.spriteBatch.setColor(Rocket.ROCKET_TINT); // Colorize rockets
            MyGdxGame.currentGame.spriteBatch.draw(rocket, this.position.x, this.position.y, this.rocket.getRegionWidth() / 2.0f, this.rocket.getRegionHeight() / 2.0f, ROCKET_W, ROCKET_H, 1, 1, 30 * this.animationTimer);
            MyGdxGame.currentGame.spriteBatch.setColor(Color.WHITE); // Reset colorization
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
        return this.explosions != null;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        // TODO Auto-generated method stub
        return this.explosions;
    }

    private void explode() {
        // TODO Make this look better.
        this.explosions = new Explosion[2];
        this.explosions[0] = new Explosion(this.spriteSheet, new Vector3(this.position.x+12, this.position.y+12, this.position.z));
        this.explosions[1] = new Explosion(this.spriteSheet, new Vector3(this.position.x-12, this.position.y-12, this.position.z));
        this.status = EntityState.Destroyed;
    }
}
