package com.me.mygdxgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Renderer;
import com.me.mygdxgame.utilities.Updatable;

/**
 * Cosmetic {@link Updatable}. A thin horizontal border that may be tiled to
 * fill any width.
 */
public class LightBorder implements Updatable {

    private static final int BORDER_W = 10;
    private static final int BORDER_H = 10;
    
    private static final int FRAMES_X = 35;
    private static final int FRAME1_Y = 3;
    private static final int FRAME2_Y = 14;
    private static final int FRAME3_Y = 25;
    private static final int FRAME4_Y = 36;
    private static final int FRAME5_Y = 47;
    
    private static final float FRAMERATE = 0.25f;
    
    private Sprite [] borderFrames;
    private float animationTimer;
    private int frame;
    private int x;
    private int y;
    private int width;
    
    public LightBorder(Texture spriteSheet, int x, int y, int width) {
        this.borderFrames = new Sprite[8];
        this.borderFrames[0] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME1_Y, BORDER_W, BORDER_H));
        this.borderFrames[1] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME2_Y, BORDER_W, BORDER_H));
        this.borderFrames[2] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME3_Y, BORDER_W, BORDER_H));
        this.borderFrames[3] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME4_Y, BORDER_W, BORDER_H));
        this.borderFrames[4] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME5_Y, BORDER_W, BORDER_H));
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
        animationTimer += deltaTime;
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
    public Vector3 getPosition() {
        return new Vector3(this.x, this.y, 0);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return LightBorder.BORDER_H;
    }

}
