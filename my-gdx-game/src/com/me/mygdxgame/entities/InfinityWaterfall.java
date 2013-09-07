package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class InfinityWaterfall implements GameEntity {

    public static final int MIN_HEIGHT = 1;
    public static final int ANIMATION_SPEED = 4;
    private static final float TEX_V_DELTA = ANIMATION_SPEED / 50.0f;
    
    private final int y;
    private final int x;
    
    private int frame = 0;
    private int animationTimer = 0;
    private Sprite waterfall = null;
    private Rectangle hitbox = null;
    private Rectangle[] hitAreas = null;
    private TextureRegion[] waterfallEnd = new TextureRegion[5];
    
    public InfinityWaterfall(Texture waterfall, Texture waterfallEnd, int x, int y, int height) {
        
        this.x = x;
        this.y = y;
        
        height = Math.max(height, InfinityWaterfall.MIN_HEIGHT);
        
        this.waterfall = new Sprite(waterfall);
        this.waterfall.setBounds(x, y - height, waterfall.getWidth(), height);
        this.waterfall.setV2(height / (float)waterfall.getHeight());
        
        this.waterfallEnd[0] = new TextureRegion(waterfallEnd, 0, 0, 32, 26);
        this.waterfallEnd[1] = new TextureRegion(waterfallEnd, 34, 0, 31, 26);
        this.waterfallEnd[2] = new TextureRegion(waterfallEnd, 66, 0, 32, 26);
        this.waterfallEnd[3] = new TextureRegion(waterfallEnd, 97, 0, 29, 26);
        this.waterfallEnd[4] = new TextureRegion(waterfallEnd, 128, 0, 32, 26);
        
        this.hitbox = new Rectangle(x, y, waterfall.getWidth(), height);
        this.hitAreas = new Rectangle[] {this.hitbox};
    }
    
    public void setHeight(int height) {
        // Determine new v2 as a function of v1, so this change doesn't throw off the animation.
        height = Math.max(height, InfinityWaterfall.MIN_HEIGHT);
        this.waterfall.setBounds(this.x, this.y - height,
                this.waterfall.getTexture().getWidth(), height);
        this.waterfall.setV2(this.waterfall.getV() + height / (float)this.waterfall.getTexture().getHeight());
        this.hitbox.height = height;
    }
    
    @Override
    public void update(float deltaTime) {
        // Nothing.
    }

    @Override
    public void draw(Renderer renderer) {
        
        // Animate waterfall body by shifting the texture coords.
        this.waterfall.setV(this.waterfall.getV() - InfinityWaterfall.TEX_V_DELTA);
        this.waterfall.setV2(this.waterfall.getV2() - InfinityWaterfall.TEX_V_DELTA);
        renderer.drawSprite(this.waterfall);
        
        // Draw bottom.
        if (this.animationTimer >= InfinityWaterfall.ANIMATION_SPEED) {
            this.frame = (this.frame + 1) % this.waterfallEnd.length;
            this.animationTimer = 0;
        } else {
            this.animationTimer++;
        }
        renderer.drawRegion(this.waterfallEnd[this.frame], this.x, this.y - this.waterfall.getHeight());
    }

    @Override
    public EntityState getState() {
        return EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        return false;
    }

    @Override
    public Rectangle[] getHitArea() {
        return this.hitAreas;
    }

    @Override
    public void destroy() {
        // Nothing.
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

}
