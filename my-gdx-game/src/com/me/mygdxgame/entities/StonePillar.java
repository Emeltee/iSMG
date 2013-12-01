package com.me.mygdxgame.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.Renderer;
import com.me.mygdxgame.utilities.Updatable;

/**
 * Simple cosmetic object that resembles a stone pillar. Can be stretched to
 * fill any height.
 */
public class StonePillar implements Updatable {

    public static final int PILLAR_BASE_X = 224;
    public static final int PILLAR_BASE_Y = 160;
    public static final int PILLAR_BASE_W = 19;
    public static final int PILLAR_BASE_H = 15;
    
    public static final int PILLAR_W = 32;
    public static final int PILLAR_H = 32;
    
    public static final int PILLAR_OFFSET_X = 6; // Just trust me.
    public static final int PILLAR_OFFSET_Y = -PILLAR_BASE_H;
    
    private Sprite pillar;
    private TextureRegion upperPillarBase, lowerPillarBase;
    private int x, y;
    private int height;
    
    public StonePillar(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, int x, int y, int height) {
        this.lowerPillarBase = new TextureRegion(spriteSheet, PILLAR_BASE_X, PILLAR_BASE_Y, PILLAR_BASE_W, PILLAR_BASE_H);
        this.upperPillarBase = new TextureRegion(lowerPillarBase);
        this.upperPillarBase.flip(false, true);     
        this.x = x;
        this.y = y;
        this.height = height;
        
        float pillarHeight = (height - 2 * PILLAR_BASE_H);
        this.pillar = new Sprite(tiles.pillarTex);
        this.pillar.setBounds(this.x - PILLAR_OFFSET_X, this.y - PILLAR_OFFSET_Y, PILLAR_W, pillarHeight);
        this.pillar.setV2(pillarHeight / (float) PILLAR_H);
    }
    
    @Override
    public void update(float deltaTime) {
        // Do nothing ever.
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.drawSprite(this.pillar);
        renderer.drawRegion(this.lowerPillarBase, this.x, this.y);
        renderer.drawRegion(this.upperPillarBase, this.x, this.y + this.height - PILLAR_BASE_H);
    }
    
    /**
     * Render just the vertical bit. Can be timed to reduce texture binding and
     * possibly increase performance slightly.
     * @param renderer Renderer to use.
     */
    public void renderBody(Renderer renderer) {
        renderer.drawSprite(this.pillar);
    }
    
    /**
     * Render just the top and bottom bit. Can be timed to reduce texture
     * binding and possibly increase performance slightly.
     * @param renderer Renderer to use.
     */
    public void renderBases(Renderer renderer) {
        renderer.drawRegion(this.lowerPillarBase, this.x, this.y);
        renderer.drawRegion(this.upperPillarBase, this.x, this.y + this.height - PILLAR_BASE_H);
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.x, this.y, 0);
    }

    @Override
    public int getWidth() {
        return StonePillar.PILLAR_BASE_W;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

}
