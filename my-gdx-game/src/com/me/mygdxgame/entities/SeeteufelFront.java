package com.me.mygdxgame.entities;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class SeeteufelFront implements GameEntity {
    
    private static final int FRONT_ARM_FRAMERATE = 30;
    private static final int BACK_ARM_FRAMERATE = 15;
    private static final int GRAVITY_FACTOR = 40;
    
    private Vector3 position = new Vector3();
    
    private Collection<Damageable> targets = null;
    
    private TextureRegion front = null;
    private TextureRegion[] frontArmRight = new TextureRegion[2];
    private TextureRegion[] frontArmLeft = new TextureRegion[2];
    private TextureRegion[] backArmRight = new TextureRegion[3];
    private TextureRegion[] backArmLeft = new TextureRegion[3];
    
    private boolean frontArmFrame = true;
    private int backArmFrame = 0;
    private int frontArmTimer = 0;
    private int backArmTimer = 0;
    private int targetY = 0;
    
    public SeeteufelFront(Texture spritesheet, Collection<Damageable> targets) {
        this.targets = targets;
        
        this.front = new TextureRegion(spritesheet, 73, 211, 98, 115);
        
        this.frontArmLeft[0] = new TextureRegion(spritesheet, 175, 271, 88, 54);
        this.frontArmLeft[1] = new TextureRegion(spritesheet, 269, 267, 82, 55);
        this.frontArmRight[0] = new TextureRegion(spritesheet, 175, 271, 88, 54);
        this.frontArmRight[0].flip(true, false);
        this.frontArmRight[1] = new TextureRegion(spritesheet, 269, 267, 82, 55);
        this.frontArmRight[1].flip(true, false);
        
        this.backArmLeft[0] = new TextureRegion(spritesheet, 0, 370, 113, 28);
        this.backArmLeft[0].flip(true, false);
        this.backArmLeft[1] = new TextureRegion(spritesheet, 114, 370, 113, 24);
        this.backArmLeft[1].flip(true, false);
        this.backArmLeft[2] = new TextureRegion(spritesheet, 228, 362, 113, 29);
        this.backArmLeft[2].flip(true, false);
        this.backArmRight[0] = new TextureRegion(spritesheet, 0, 370, 113, 28);
        this.backArmRight[1] = new TextureRegion(spritesheet, 114, 370, 113, 24);
        this.backArmRight[2] = new TextureRegion(spritesheet, 228, 362, 113, 29);
    }
    
    public void setTargetY(int targetY) {
        
    }
    
    public int getTargetY() {
        return this.targetY;
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.position.y > this.targetY) {
            this.position.y += SeeteufelFront.GRAVITY_FACTOR;
        } else {
            this.position.y = this.targetY;
        }
    }

    @Override
    public void draw(Matrix4 transformMatrix) {

        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();
        
        // Draw the front. Always the same.
        MyGdxGame.currentGame.spriteBatch.draw(this.front, this.position.x, this.position.y);
        
        // Draw the long tentacles on the sides.
        this.backArmTimer++;
        if (this.backArmTimer >= SeeteufelFront.BACK_ARM_FRAMERATE) {
            this.backArmFrame = (this.backArmFrame + 1) % 3;
            this.backArmTimer = 0;
        }
        if (this.backArmFrame == 0) {
            MyGdxGame.currentGame.spriteBatch.draw(this.backArmLeft[0], this.position.x - 81, this.position.y + 20);
            MyGdxGame.currentGame.spriteBatch.draw(this.backArmRight[0], this.position.x + 65, this.position.y + 20);
        } else if (this.backArmFrame == 1) {
            MyGdxGame.currentGame.spriteBatch.draw(this.backArmLeft[1], this.position.x - 81, this.position.y + 24);
            MyGdxGame.currentGame.spriteBatch.draw(this.backArmRight[1], this.position.x + 65, this.position.y + 24);
        } else {
            MyGdxGame.currentGame.spriteBatch.draw(this.backArmLeft[2], this.position.x - 81, this.position.y + 27);
            MyGdxGame.currentGame.spriteBatch.draw(this.backArmRight[2], this.position.x + 65, this.position.y + 27);
        }
        
        // Draw the front tentacles.
        this.frontArmTimer++;
        if (this.frontArmTimer >= SeeteufelFront.FRONT_ARM_FRAMERATE) {
            this.frontArmFrame = !this.frontArmFrame;
            this.frontArmTimer = 0;
        }
        if (this.frontArmFrame) {
            MyGdxGame.currentGame.spriteBatch.draw(this.frontArmLeft[0], this.position.x - 48, this.position.y + 8);
            MyGdxGame.currentGame.spriteBatch.draw(this.frontArmRight[0], this.position.x + 58, this.position.y + 8);
        } else {
            MyGdxGame.currentGame.spriteBatch.draw(this.frontArmLeft[1], this.position.x - 42, this.position.y + 11);
            MyGdxGame.currentGame.spriteBatch.draw(this.frontArmRight[1], this.position.x + 58, this.position.y + 11);
        }
        
        MyGdxGame.currentGame.spriteBatch.end();
    }

    @Override
    public EntityState getState() {
        return EntityState.Running;
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
