package com.me.mygdxgame.entities.particles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class Explosion implements GameEntity {

    // Texture constants
    private static final int SPLODE_1_X = 0;
    private static final int SPLODE_2_X = 32;
    private static final int SPLODE_3_X = 64;
    private static final int SPLODE_4_X = 96;
    private static final int SPLODE_Y = 209;
    private static final int SPLODE_DIM = 32;
    
    private TextureRegion[] splode;
    private Vector3 position;
    private EntityState status;
    private short animationTimer;
    private int frame;
    
    private static final int FRAMERATE = 6;
    private static final int NUM_FRAMES = 4;
    
    public Explosion(Texture spriteSheet, Vector3 position) {
        this.position = position;
        this.splode = new TextureRegion[4];
        this.splode[0] = new TextureRegion(spriteSheet, SPLODE_1_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        this.splode[1] = new TextureRegion(spriteSheet, SPLODE_2_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        this.splode[2] = new TextureRegion(spriteSheet, SPLODE_3_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        this.splode[3] = new TextureRegion(spriteSheet, SPLODE_4_X, SPLODE_Y, SPLODE_DIM, SPLODE_DIM);
        this.animationTimer = 0;
        this.frame = 0;
        this.status = EntityState.Running;
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.status == EntityState.Running){
            if (animationTimer >= FRAMERATE) {
                this.frame++;
                this.animationTimer = 0;
            }        
                    
            animationTimer++;        
            if (this.frame >= NUM_FRAMES) { this.status = EntityState.Destroyed; }
        }

    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        if (this.status == EntityState.Running) {
            MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);          
            MyGdxGame.currentGame.spriteBatch.begin();
            MyGdxGame.currentGame.spriteBatch.draw(splode[this.frame], this.position.x, this.position.y);
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
