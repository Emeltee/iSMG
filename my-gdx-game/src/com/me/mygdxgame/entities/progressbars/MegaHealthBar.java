package com.me.mygdxgame.entities.progressbars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.ProgressBar;
import com.me.mygdxgame.utilities.Renderer;

public class MegaHealthBar implements ProgressBar {

    // Texture extraction coordinates and dimensions (Healthbar-chrome texture)
    private static final int CHROME_X = 38;
    private static final int CHROME_Y = 57;
    private static final int CHROME_H = 68;
    private static final int CHROME_W = 14;

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
    private static final Color BAR_BACK_COLOR = Color.BLACK;
    
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
    public void draw(Renderer renderer) {
        
        // Draw background to bar.
        renderer.drawRect(ShapeType.Filled, BAR_BACK_COLOR, this.x + BAR_X, this.y + BAR_Y, BAR_W, BAR_H);
        
        // Draw the bar
        renderer.drawRect(ShapeType.Filled, BAR_COLOR, this.x + BAR_X, this.y + BAR_Y, BAR_W, this.barSize);
        
        // Draw the chrome and signal light
        renderer.drawRegion(this.chrome, this.x, this.y);
        renderer.drawRegion((this.inDanger) ? this.dangerSignal : this.safetySignal, this.x + SIGNAL_X, this.y + SIGNAL_Y);
    }

    @Override
    public void setValue(float value) {
        // Clamp to [0, 1].
        this.value = Math.max(0.0f, Math.min(1.0f, value));
        this.barSize = Math.max(0, (int) Math.ceil(this.value * BAR_H));
    }

    @Override
    public float getValue() {
        return this.value;
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.x, this.y, 0);
    }

    @Override
    public int getWidth() {
        return BAR_W;
    }

    @Override
    public int getHeight() {
        return BAR_H;
    }
}
