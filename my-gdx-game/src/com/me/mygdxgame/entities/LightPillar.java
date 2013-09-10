package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class LightPillar implements GameEntity {

    /* This is a background element, which isn't usually an entity,
     * but it just makes sense for this, I think..
     */
    
    public static final int PILLAR_BASE_X = 0;
    public static final int PILLAR_BASE_Y = 44;
    public static final int PILLAR_BASE_W = 35;
    public static final int PILLAR_BASE_H = 13;
    
    public static final int BEAM_W = 35;
    public static final int BEAM_H = 11;
    public static final int BEAM_OFFSET_Y = 13;
    
    public static final int FRAMES_X = 0;
    public static final int FRAME1_Y = 33;
    public static final int FRAME2_Y = 22;
    public static final int FRAME3_Y = 11;
    public static final int FRAME4_Y = 0;            
    
    public static final int FRAMERATE = 3;
    
    private TextureRegion [] pillarFrames;
    private TextureRegion lowerBase, upperBase;
    private int animationTimer;
    private int frame;
    private Texture spriteSheet;
    private int x;
    private int y;
    private int height;
    
    public LightPillar(Texture spriteSheet, int x, int y, int height) {
        this.spriteSheet = spriteSheet;
        this.lowerBase = new TextureRegion(spriteSheet, PILLAR_BASE_X, PILLAR_BASE_Y, PILLAR_BASE_W, PILLAR_BASE_H);
        this.upperBase = new TextureRegion(lowerBase);
        this.upperBase.flip(false, true);
        
        this.pillarFrames = new TextureRegion[6];
        this.pillarFrames[0] = new TextureRegion(spriteSheet, FRAMES_X, FRAME1_Y, BEAM_W, BEAM_H);
        this.pillarFrames[1] = new TextureRegion(spriteSheet, FRAMES_X, FRAME2_Y, BEAM_W, BEAM_H);
        this.pillarFrames[2] = new TextureRegion(spriteSheet, FRAMES_X, FRAME3_Y, BEAM_W, BEAM_H);
        this.pillarFrames[3] = new TextureRegion(spriteSheet, FRAMES_X, FRAME4_Y, BEAM_W, BEAM_H);
        this.pillarFrames[4] = this.pillarFrames[3];
        this.pillarFrames[5] = this.pillarFrames[2];
                
        this.x = x; 
        this.y = y;        
        this.height = height;
        
        this.animationTimer = 0;
        this.frame = 0;        
    }
    
    @Override
    public void update(float deltaTime) {
        animationTimer++;
        if (animationTimer > FRAMERATE) {
            animationTimer = 0;
            frame = (frame + 1) % (this.pillarFrames.length);
        }
    }

    @Override
    public void draw(Renderer renderer) {            
            TextureRegion currentFrame = this.pillarFrames[frame];
            Sprite currentSprite = new Sprite(currentFrame);
            currentSprite.setBounds(this.x, this.y + BEAM_OFFSET_Y, BEAM_W, this.height - (2 * BEAM_OFFSET_Y));
            renderer.drawSprite(currentSprite);
            renderer.drawRegion(this.lowerBase, this.x, this.y);
            renderer.drawRegion(this.upperBase, this.x, this.y + (this.height - BEAM_OFFSET_Y));
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
