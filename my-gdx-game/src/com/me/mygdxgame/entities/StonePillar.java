package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class StonePillar implements GameEntity {

    // this.pillarTopBaseRegion = new TextureRegion(spriteSheet, 224, 160, 19, 15);
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
        // None to speak of
        return null;
    }

    @Override
    public void destroy() {
        // Never ever do anything
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        // Never returns anything
        throw new NoSuchElementException();
    }

}
