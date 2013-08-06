package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class BonneHealthBar implements GameEntity {

    // Texture extraction coordinates and dimensions
    public static final int LOGO_X = 0;
    public static final int LOGO_Y = 56;
    public static final int LOGO_W = 32;
    public static final int LOGO_H = 32;
    
    // Offset distance for the healthbar from the extracted TextureRegion
    private static final int BAR_W = 200;
    private static final int BAR_H = 6;
    private static final int BAR_X = 4 - BAR_W;
    private static final int BAR_Y = 14;    
    
    private static final Color BAR_COLOR = Color.RED;
    private static final Color BAR_OUTLINE = Color.BLACK;
    
    // Parameters affecting state of the bar
    private Damageable character;
    private double percentHealth;
    private int barSize;
    
    // Bar position and graphic holders
    private int x, y;
    private TextureRegion logo;
    private Texture spriteSheet;
    
    public BonneHealthBar (Texture spriteSheet, int x, int y, Damageable character) {
        this.spriteSheet = spriteSheet;
        this.logo = new TextureRegion(spriteSheet, LOGO_X, LOGO_Y, LOGO_W, LOGO_H);
        this.x = x;
        this.y = y;
        this.character = character;
    }
    
    @Override
    public void update(float deltaTime) {
        this.percentHealth = this.character.getHealth() / (double) this.character.getMaxHealth();
        this.barSize = Math.max(0, (int) Math.ceil(percentHealth * BAR_W));
    }

    @Override
    public void draw() {
        // Since the bar draws away from the logo to the left, need to manually adjust coords
        int temp_offset = BAR_W - this.barSize;
        
        // Draw the bar
        MyGdxGame.currentGame.shapeRenderer.setProjectionMatrix(MyGdxGame.currentGame.orthoCamera.combined);
        MyGdxGame.currentGame.shapeRenderer.begin(ShapeType.Filled);
        MyGdxGame.currentGame.shapeRenderer.rect(this.x + BAR_X + temp_offset, this.y + BAR_Y, this.barSize, BAR_H, BAR_COLOR, BAR_COLOR, BAR_COLOR, BAR_COLOR);
        MyGdxGame.currentGame.shapeRenderer.end();
        
        // Draw the logo
        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(MyGdxGame.currentGame.orthoCamera.combined);
        MyGdxGame.currentGame.spriteBatch.begin();
        MyGdxGame.currentGame.spriteBatch.draw(this.logo, this.x, this.y);
        MyGdxGame.currentGame.spriteBatch.end();
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
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        return null;
    }

}
