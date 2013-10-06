package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class LightBorder implements GameEntity {

    /* This is a background element, which isn't usually an entity,
     * but it just makes sense for this, I think..
     */   
    
    public static final int BORDER_W = 10;
    public static final int BORDER_H = 10;
    
    public static final int FRAMES_X = 35;
    public static final int FRAME1_Y = 3;
    public static final int FRAME2_Y = 14;
    public static final int FRAME3_Y = 25;
    public static final int FRAME4_Y = 36;
    public static final int FRAME5_Y = 47;
    
    public static final int FRAMERATE = 3;
    
    private Sprite [] borderFrames;
    private int animationTimer;
    private int frame;
    private int x;
    private int y;
    private int width;
    
    public LightBorder(Texture spriteSheet, int x, int y, int width) {
        this.borderFrames = new Sprite[8];
        this.borderFrames[0] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME1_Y, BORDER_W, BORDER_H));
        this.borderFrames[1] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME2_Y, BORDER_W, BORDER_H));
        this.borderFrames[2] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME2_Y, BORDER_W, BORDER_H));
        this.borderFrames[3] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME2_Y, BORDER_W, BORDER_H));
        this.borderFrames[4] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME2_Y, BORDER_W, BORDER_H));
        this.borderFrames[5] = this.borderFrames[4];
        this.borderFrames[6] = this.borderFrames[3];
        this.borderFrames[7] = this.borderFrames[2];
        
        this.x = x;
        this.y = y;
        this.width = width;
        
        this.animationTimer = 0;
    }
    
    @Override
    public void update(float deltaTime) {
        animationTimer++;
        if (animationTimer > FRAMERATE) { 
            animationTimer = 0;
            frame = (frame + 1) % borderFrames.length;
        }
        
    }

    @Override
    public void draw(Renderer renderer) {
        Sprite currentSprite = borderFrames[frame];
        currentSprite.setBounds(this.x, this.y, this.width, BORDER_H);
        renderer.drawSprite(currentSprite);
    }

    @Override
    public EntityState getState() {
        // Always running
        return EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        // Always false
        return false;
    }

    @Override
    public Rectangle[] getHitArea() {
        // No hitArea
        return null;
    }

    @Override
    public void destroy() {
        // Do nothing
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        // No created entities
        throw new NoSuchElementException();
    }

}
