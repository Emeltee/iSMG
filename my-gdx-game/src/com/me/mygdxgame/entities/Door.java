package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class Door implements GameEntity {
    
    // Door has its own state, separate from Running/Destroyed
    public static enum DoorState { OPEN, SHUT };
    
    // Texture control constants
    public static final int DOOR_SHUT_X = 0;
    public static final int DOOR_SHUT_Y = 0;
    public static final int DOOR_OPEN_X = 44;
    public static final int DOOR_OPEN_Y = 0;
    public static final int DOOR_W = 44;
    public static final int DOOR_H = 56;
    
    private TextureRegion doorShut; // Open door sprite
    private TextureRegion doorOpen; // Closed door sprite
    private int x; // x-coord
    private int y; // y coord
    private DoorState doorStatus; // Door-specific state variable
    private Sound openSound;
    private Sound closeSound;
    private Rectangle hitbox = null;
    private Rectangle[] allHitAreas = null;
    
    
    
    public Door(Texture spriteSheet, Sound openSound, Sound closeSound, int x, int y) {
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(x, y, DOOR_W, DOOR_H);
        this.allHitAreas = new Rectangle[] {this.hitbox};
        this.openSound = openSound;
        this.closeSound = closeSound;
        this.doorShut = new TextureRegion(spriteSheet, DOOR_SHUT_X, DOOR_SHUT_Y, DOOR_W, DOOR_H);
        this.doorOpen = new TextureRegion(spriteSheet, DOOR_OPEN_X, DOOR_OPEN_Y, DOOR_W, DOOR_H);
        doorStatus = DoorState.SHUT;
    }
    
    @Override
    public void update(float deltaTime) {        
        // Umm . . . ?
    }

    @Override
    public void draw(Renderer renderer) {
        if (this.doorStatus == DoorState.SHUT) {
            renderer.drawRegion(this.doorShut, this.x, this.y);
        } else {
            renderer.drawRegion(this.doorOpen, this.x, this.y);
        }
    }

    @Override
    public EntityState getState() {
        // This never gets destroyed..
        return EntityState.Running;
     }

    @Override
    public boolean hasCreatedEntities() {
        return false;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    /** Return the door-specific state value, because regular state is always Running. */
    public DoorState getDoorState() {
        return this.doorStatus;
    }
    
    /** CLOSE THE HATCH! */
    public void setIsOpen(DoorState state, boolean playSound) {
        if (this.doorStatus != state) {
            this.doorStatus = state;
            if (playSound) {
                if (state == DoorState.OPEN) {
                    this.openSound.play();
                } else {
                    this.closeSound.play();
                }
            }
        }
    }
    
    public Rectangle getHitBox() {
        return this.hitbox;
    }

    @Override
    public Rectangle[] getHitArea() {
        return this.allHitAreas;
    }

    @Override
    public void destroy() {
        // Do nothing.
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.x, this.y, 0);
    }

    @Override
    public int getWidth() {
        return Door.DOOR_W;
    }

    @Override
    public int getHeight() {
        return Door.DOOR_H;
    }
    
    
}
