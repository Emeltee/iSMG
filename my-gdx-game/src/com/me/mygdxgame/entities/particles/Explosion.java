package com.me.mygdxgame.entities.particles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

/**
 * Simple yellow burst-looking particle. Hovers in the position it is created
 * at until its animation finishes. No effects other than being visual sweetener.
 */
public class Explosion implements GameEntity {

    // Texture constants
    private static final int SPLODE_1_X = 0;
    private static final int SPLODE_2_X = 32;
    private static final int SPLODE_3_X = 64;
    private static final int SPLODE_4_X = 96;
    private static final int SPLODE_Y = 209;
    private static final int SPLODE_DIM = 32;
    
    private static final float FRAMERATE = 0.06f;
    private static final int NUM_FRAMES = 4;
    
    private TextureRegion[] splode;
    private Vector3 position;
    private EntityState state;
    private float animationTimer;
    private int frame;
    private float spriteScale = 1.0f;
    
    private Rectangle hitbox = null;
    private Rectangle[] hitArea = null;
    
    public Explosion(Texture spriteSheet, Vector3 position) {
        float centerOffset = Explosion.SPLODE_DIM / 2;
        this.position = new Vector3(position.x - centerOffset, position.y - centerOffset, position.x);
        this.hitbox = new Rectangle(position.x, position.y, SPLODE_DIM, SPLODE_DIM);
        this.hitArea = new Rectangle[] {this.hitbox};
        
        this.splode = new TextureRegion[4];
        this.splode[0] = new TextureRegion(spriteSheet, SPLODE_1_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        this.splode[1] = new TextureRegion(spriteSheet, SPLODE_2_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        this.splode[2] = new TextureRegion(spriteSheet, SPLODE_3_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        this.splode[3] = new TextureRegion(spriteSheet, SPLODE_4_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        
        this.spriteScale = 0.5f + (float)Math.random() * 2.0f;
        
        this.animationTimer = 0;
        this.frame = 0;
        this.state = EntityState.Running;
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.state == EntityState.Running){
            if (animationTimer >= FRAMERATE) {
                this.frame++;
                this.animationTimer = 0;
            }        
                    
            animationTimer += deltaTime;        
            if (this.frame >= NUM_FRAMES) { this.state = EntityState.Destroyed; }
        }

    }

    @Override
    public void draw(Renderer renderer) {
        if (this.state == EntityState.Running) {
            renderer.drawRegion(splode[this.frame], this.position.x, this.position.y, Color.WHITE, spriteScale, spriteScale, 0);
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
        return this.hitArea;
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
        return (int) (this.spriteScale * Explosion.SPLODE_DIM);
    }

    @Override
    public int getHeight() {
        return this.getWidth();
    }

}
