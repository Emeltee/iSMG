package com.me.mygdxgame.entities.projectiles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.particles.Explosion;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class Bomb implements GameEntity {
    
    // Constants for extracting bullet from Texture
    public static final int BOMB_X = 210;
    public static final int BOMB_Y = 175;
    public static final int BOMB_W = 16;
    public static final int BOMB_H = 16;

    private static final float GRAVITY = 4; 
    
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
    private Rectangle [] watchOut;
    /** Status of object */
    private EntityState status;
    /** Previous z-coord for collision detection */
    private float prevZ;
    
    private Explosion[] explosions;
  
    public Bomb(Texture spriteSheet, Vector3 position, Vector3 velocity, Rectangle [] watchOut) {
        this.spriteSheet = spriteSheet;
        this.position.set(position);
        this.velocity = velocity;
        this.bomb = new TextureRegion(this.spriteSheet, BOMB_X, BOMB_Y, BOMB_W, BOMB_H);
        this.watchOut = watchOut;
        this.status = EntityState.Running;
    }
    
    
    @Override
    public void update(float deltaTime) {
        this.prevZ = this.position.z;
        
        if (this.status == EntityState.Running) {
            this.position.x += this.velocity.x * deltaTime;;
            this.position.y +=  this.velocity.y * deltaTime;
            this.velocity.y -= GRAVITY;
            this.position.z += this.velocity.z * deltaTime;
            
            for (Rectangle r: this.watchOut) {
                if (r.overlaps(new Rectangle(this.position.x, this.position.y, BOMB_W, BOMB_H))
                    && (((this.prevZ < 0 && this.position.z > 0) // Bomb from behind 
                         || (this.prevZ > 0 && this.position.z < 0)) // Bomb from in front
                         || this.position.z == 0)) { // Bomb at precisely 0 (unlikely)
                    explode();
                    return;
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
            MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);            
            MyGdxGame.currentGame.spriteBatch.begin();
            
            // TODO spriteBatch's draw() function won't manage the z-axis automatically. Fix manually with scaleX/scaleY params? 
            MyGdxGame.currentGame.spriteBatch.draw(bomb, this.position.x, this.position.y, this.bomb.getRegionWidth() / 2.0f, this.bomb.getRegionHeight() / 2.0f, BOMB_W, BOMB_H, 1, 1, 30 * this.animationTimer);
            
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
        this.explosions[0] = new Explosion(this.spriteSheet, new Vector3(this.position.x+2, this.position.y+2, this.position.z));
        this.explosions[1] = new Explosion(this.spriteSheet, new Vector3(this.position.x-2, this.position.y-2, this.position.z));
        this.status = EntityState.Destroyed;
    }
}
