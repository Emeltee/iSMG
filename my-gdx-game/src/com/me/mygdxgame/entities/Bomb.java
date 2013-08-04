package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class Bomb implements GameEntity {
    
    // Just controlling which way it goes.
    public static enum BombDirection { LEFT, RIGHT };
    
    // Constants for extracting bullet from Texture
    private static final int BOMB_X = 210;
    private static final int BOMB_Y = 175;
    private static final int BOMB_W = 16;
    private static final int BOMB_H = 16;

    private static final float GRAVITY = 4; 
    
    /** TextureRegion representing the idle frame. */
    private TextureRegion bomb;
    /** Tracks the number of frames that have passed. Used to time animation. */
    private short animationTimer = 0;
    /** General-purpose sprite sheet. */
    private Texture spriteSheet = null;

    /** Direction of bullet trajectory */
    private BombDirection dir;

    /** Current position in 3D space. */
    private Vector3 position = new Vector3(0, 0, 0);
    /** Current velocity. */
    private Vector3 velocity = new Vector3(0, 0, 0);
    /** */
    private Rectangle [] watchOut;
    /** */
    private EntityState status;
  
    public Bomb(Texture spriteSheet, Vector3 position, Vector3 velocity, BombDirection dir, Rectangle [] watchOut) {
        this.spriteSheet = spriteSheet;
        this.position.set(position);
        this.velocity = velocity;
        this.dir = dir;
        this.bomb = new TextureRegion(this.spriteSheet, BOMB_X, BOMB_Y, BOMB_W, BOMB_H);
        this.watchOut = watchOut;
        this.status = EntityState.Running;
    }
    
    
    @Override
    public void update(float deltaTime) {
        if (this.status == EntityState.Running) {
            // Assumes speed is always positive.
            if (this.dir == BombDirection.LEFT) {
                this.position.x -= this.velocity.x * deltaTime;
            } else {
                this.position.x += this.velocity.x * deltaTime;;
            }
            
            this.position.y +=  this.velocity.y * deltaTime;
            this.velocity.y -= GRAVITY;
            
            for (Rectangle r: this.watchOut) {
                if (r.contains(this.position.x, this.position.y)) {
                    this.status = EntityState.Destroyed;
                    return;
                }
            }
        }
        
    }

    @Override
    public void draw() {
        if (this.status == EntityState.Running){
            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            // Prepare the game's spriteBatch for drawing.
            MyGdxGame.currentGame.spriteBatch
            .setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
            
            MyGdxGame.currentGame.spriteBatch.begin();
            
            MyGdxGame.currentGame.spriteBatch.draw(bomb, this.position.x, this.position.y, this.bomb.getRegionWidth() / 2.0f, this.bomb.getRegionHeight() / 2.0f, BOMB_W, BOMB_H, 1, 1, 30 * this.animationTimer);
            
            MyGdxGame.currentGame.spriteBatch.end();
        }
    }

    @Override
    public EntityState getState() {
        // TODO Auto-generated method stub
        return null;
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
