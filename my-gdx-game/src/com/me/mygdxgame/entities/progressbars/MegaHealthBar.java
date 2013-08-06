package com.me.mygdxgame.entities.progressbars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.ProgressBar;

public class MegaHealthBar implements ProgressBar {

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
    protected float value = 1.0f;
    private int barSize;
    
    // Bar position and graphic holders
    private int x, y;
    private TextureRegion chrome;
    private TextureRegion dangerSignal, safetySignal;
    private boolean inDanger;
    
    public MegaHealthBar(Texture spriteSheet, int x, int y) {
        this.chrome = new TextureRegion(spriteSheet, CHROME_X, CHROME_Y, CHROME_W, CHROME_H);
        this.dangerSignal = new TextureRegion(spriteSheet, DANGER_X, DANGER_Y, SIGNAL_W, SIGNAL_H);
        this.safetySignal = new TextureRegion(spriteSheet, SAFETY_X, SAFETY_Y, SIGNAL_W, SIGNAL_H);
        this.x = x;
        this.y = y;
        this.inDanger = true;
    }
    
    public void setInDanger(boolean inDanger) {
        // Let something outside the Healthbar manage this
        this.inDanger = inDanger;
    }
    
    @Override
    public void update(float deltaTime) {
        // N/A
    }

    @Override
    public void draw(Matrix4 transformMatrix) {        
        // Draw the bar
        MyGdxGame.currentGame.shapeRenderer.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.shapeRenderer.begin(ShapeType.Filled);
        MyGdxGame.currentGame.shapeRenderer.rect(this.x + BAR_X, this.y + BAR_Y, BAR_W, this.barSize, BAR_COLOR, BAR_COLOR, BAR_COLOR, BAR_COLOR);
        MyGdxGame.currentGame.shapeRenderer.end();
        
        // Draw the chrome and signal light
        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();
        MyGdxGame.currentGame.spriteBatch.draw(this.chrome, this.x, this.y);
        MyGdxGame.currentGame.spriteBatch.draw((this.inDanger) ? this.dangerSignal : this.safetySignal, this.x + SIGNAL_X, this.y + SIGNAL_Y);
        MyGdxGame.currentGame.spriteBatch.end();
    }

    @Override
    public void setValue(float value) {
        // Clamp to range [0, 1].
        this.value = Math.max(1, Math.min(0, value));
        this.barSize = Math.max(0, (int) Math.ceil(this.value * BAR_H));
    }

    @Override
    public float getValue() {
        return this.value;
    }
}
