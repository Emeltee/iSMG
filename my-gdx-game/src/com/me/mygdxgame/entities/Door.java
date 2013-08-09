package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class Door implements GameEntity {
    
    // Door has its own state, separate from Running/Destroyed
    public static enum DoorState { OPEN, SHUT };
    
    // Texture control constants
    public static final int DOOR_SHUT_X = 0;
    public static final int DOOR_SHUT_Y = 0;
    public static final int DOOR_OPEN_X = 54;
    public static final int DOOR_OPEN_Y = 0;
    public static final int DOOR_W = 54;
    public static final int DOOR_H = 56;
    
    private TextureRegion doorShut; // Open door sprite
    private TextureRegion doorOpen; // Closed door sprite
    private int x; // x-coord
    private int y; // y coord
    private DoorState doorStatus; // Door-specific status variable
    
    private static final Sound DOOR_OPEN_SFX = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-ruindoor-open1.ogg"));
    private static final Sound DOOR_SHUT_SFX = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-ruindoor-close1.ogg")); 
    
    public Door(Texture spriteSheet, int x, int y) {
        this.doorShut = new TextureRegion(spriteSheet, DOOR_SHUT_X, DOOR_SHUT_Y, DOOR_W, DOOR_H);
        this.doorOpen = new TextureRegion(spriteSheet, DOOR_OPEN_X, DOOR_OPEN_Y, DOOR_W, DOOR_H);
        this.x = x; this.y = y;
        doorStatus = DoorState.SHUT;
    }
    
    @Override
    public void update(float deltaTime) {        
        // Umm . . . ?
    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();
        
        if (this.doorStatus == DoorState.SHUT) {
            MyGdxGame.currentGame.spriteBatch.draw(this.doorShut, this.x, this.y);
        } else {
            MyGdxGame.currentGame.spriteBatch.draw(this.doorOpen, this.x, this.y);
        }

        MyGdxGame.currentGame.spriteBatch.end();
    }

    @Override
    public EntityState getState() {
        // This never gets destroyed..
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

    /** Return the door-specific state value, because regular state is always Running. */
    public DoorState getDoorState() {
        return this.doorStatus;
    }
    
    /** Opens the door, should be called when refractor is obtained */
    public void onOpen() {
        if (this.doorStatus == DoorState.SHUT) {
            DOOR_OPEN_SFX.play();
            this.doorStatus = DoorState.OPEN;
        }
    }
    
    /** CLOSE THE HATCH! */
    public void onShut() {
        // Even though this one never really gets called..
        if (this.doorStatus == DoorState.OPEN) {
            DOOR_SHUT_SFX.play();
            this.doorStatus = DoorState.SHUT;
        }
    }
    
    public Rectangle getHitBox() {
        return new Rectangle(this.x, this.y, DOOR_W, DOOR_H);
    }
    
    
}