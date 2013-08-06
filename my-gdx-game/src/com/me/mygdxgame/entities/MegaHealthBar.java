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

public class MegaHealthBar implements GameEntity {

    // Texture extraction coordinates and dimensions (Healthbar-chrome texture)
    public static final int CHROME_X = 38;
    public static final int CHROME_Y = 57;
    public static final int CHROME_H = 68;
    public static final int CHROME_W = 14;

   // Offset distance for the healthbar from the Chrome
    private static final int BAR_X = 4;
    private static final int BAR_Y = 20;
    private static final int BAR_W = 7;
    private static final int BAR_H = 39;
    
    // Texture extraction coordinates and dimensions (Signal light texture)
    private static final int DANGER_X = 9;
    private static final int DANGER_Y = 89;
    private static final int SAFETY_X = 0;
    private static final int SAFETY_Y = 89;    
    private static final int SIGNAL_W = 9;
    private static final int SIGNAL_H = 9;
    
    // Offset distance for the signal light from the Chrome 
    private static final int SIGNAL_X = 3;
    private static final int SIGNAL_Y = 2;
    
    private static final Color BAR_COLOR = Color.YELLOW;
    
    // Parameters affecting state of the bar
    private Damageable character;
    private double percentHealth;
    private int barSize;
    
    // Bar position and graphic holders
    private int x, y;
    private TextureRegion chrome;
    private TextureRegion dangerSignal, safetySignal;
    private Texture spriteSheet;
    private boolean inDanger;
    
    public MegaHealthBar(Texture spriteSheet, int x, int y, Damageable character) {
        this.spriteSheet = spriteSheet;
        this.chrome = new TextureRegion(spriteSheet, CHROME_X, CHROME_Y, CHROME_W, CHROME_H);
        this.dangerSignal = new TextureRegion(spriteSheet, DANGER_X, DANGER_Y, SIGNAL_W, SIGNAL_H);
        this.safetySignal = new TextureRegion(spriteSheet, SAFETY_X, SAFETY_Y, SIGNAL_W, SIGNAL_H);
        this.x = x;
        this.y = y;
        this.character = character;
        this.inDanger = true;
    }
    
    public void setInDanger(boolean inDanger) {
        // Let something outside the Healthbar manage this
        this.inDanger = inDanger;
    }
    
    @Override
    public void update(float deltaTime) {
        this.percentHealth = this.character.getHealth() / (double) this.character.getMaxHealth();
        this.barSize = Math.max(0, (int) Math.ceil(percentHealth * BAR_H));
    }

    @Override
    public void draw() {        
        // Draw the bar
        MyGdxGame.currentGame.shapeRenderer.setProjectionMatrix(MyGdxGame.currentGame.orthoCamera.combined);
        MyGdxGame.currentGame.shapeRenderer.begin(ShapeType.Filled);
        MyGdxGame.currentGame.shapeRenderer.rect(this.x + BAR_X, this.y + BAR_Y, BAR_W, this.barSize, BAR_COLOR, BAR_COLOR, BAR_COLOR, BAR_COLOR);
        MyGdxGame.currentGame.shapeRenderer.end();
        
        // Draw the chrome and signal light
        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(MyGdxGame.currentGame.orthoCamera.combined);
        MyGdxGame.currentGame.spriteBatch.begin();
        MyGdxGame.currentGame.spriteBatch.draw(this.chrome, this.x, this.y);
        MyGdxGame.currentGame.spriteBatch.draw((this.inDanger) ? this.dangerSignal : this.safetySignal, this.x + SIGNAL_X, this.y + SIGNAL_Y);
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
