package com.me.mygdxgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Renderer;
import com.me.mygdxgame.utilities.Updatable;

public class LightPillar implements Updatable {

    private static final int PILLAR_BASE_X = 0;
    private static final int PILLAR_BASE_Y = 44;
    private static final int PILLAR_BASE_W = 35;
    private static final int PILLAR_BASE_H = 13;
    
    private static final int BEAM_W = 35;
    private static final int BEAM_H = 11;
    
    private static final int FRAMES_X = 0;
    private static final int FRAME1_Y = 33;
    private static final int FRAME2_Y = 22;
    private static final int FRAME3_Y = 11;
    private static final int FRAME4_Y = 0;            
    
    private static final int FRAMERATE = 3;
    
    private Sprite [] pillarFrames;
    private TextureRegion lowerBase, upperBase;
    private int animationTimer;
    private int frame;
    private int x;
    private int y;
    private int height;
    
    public LightPillar(Texture spriteSheet, int x, int y, int height) {
        this.lowerBase = new TextureRegion(spriteSheet, PILLAR_BASE_X, PILLAR_BASE_Y, PILLAR_BASE_W, PILLAR_BASE_H);
        this.upperBase = new TextureRegion(lowerBase);
        this.upperBase.flip(false, true);
        
        this.pillarFrames = new Sprite[6];
        this.pillarFrames[0] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME1_Y, BEAM_W, BEAM_H));
        this.pillarFrames[1] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME2_Y, BEAM_W, BEAM_H));
        this.pillarFrames[2] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME3_Y, BEAM_W, BEAM_H));
        this.pillarFrames[3] = new Sprite(new TextureRegion(spriteSheet, FRAMES_X, FRAME4_Y, BEAM_W, BEAM_H));
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
            Sprite currentSprite = this.pillarFrames[frame];
            currentSprite.setBounds(this.x, this.y + PILLAR_BASE_H, BEAM_W, this.height - (2 * PILLAR_BASE_H));
            renderer.drawSprite(currentSprite);
            renderer.drawRegion(this.lowerBase, this.x, this.y);
            renderer.drawRegion(this.upperBase, this.x, this.y + (this.height - PILLAR_BASE_H));
    }
    
    /**
     * Render just the vertical bit. Can be timed to reduce texture binding and
     * possibly increase performance slightly.
     * @param renderer Renderer to use.
     */
    public void renderBody(Renderer renderer) {
        Sprite currentSprite = this.pillarFrames[frame];
        currentSprite.setBounds(this.x, this.y + PILLAR_BASE_H, BEAM_W, this.height - (2 * PILLAR_BASE_H));
        renderer.drawSprite(currentSprite);
    }
    
    /**
     * Render just the top and bottom bit. Can be timed to reduce texture
     * binding and possibly increase performance slightly.
     * @param renderer Renderer to use.
     */
    public void renderBases(Renderer renderer) {
        renderer.drawRegion(this.lowerBase, this.x, this.y);
        renderer.drawRegion(this.upperBase, this.x, this.y + (this.height - PILLAR_BASE_H));
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.x, this.y, 0);
    }

    @Override
    public int getWidth() {
        return LightPillar.PILLAR_BASE_W;
    }

    @Override
    public int getHeight() {
        return this.height;// + LightPillar.BEAM_H * 2;
    }
}