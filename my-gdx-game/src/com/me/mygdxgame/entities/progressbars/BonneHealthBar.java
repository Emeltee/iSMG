package com.me.mygdxgame.entities.progressbars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.me.mygdxgame.utilities.ProgressBar;
import com.me.mygdxgame.utilities.Renderer;

public class BonneHealthBar implements ProgressBar {

    // Texture extraction coordinates and dimensions
    public static final int LOGO_X = 0;
    public static final int LOGO_Y = 56;
    public static final int LOGO_W = 32;
    public static final int LOGO_H = 32;
    
    // Offset distance for the healthbar from the extracted TextureRegion
    public static final int BAR_W = 600;
    public static final int BAR_H = 12;
    public static final int BAR_X = LOGO_W / 2;
    public static final int BAR_Y = LOGO_W / 2 - BAR_H / 2;    
    
    private static final Color BAR_COLOR = Color.RED;
    private static final Color BAR_OUTLINE = Color.BLACK;
    
    // Parameters affecting state of the bar
    protected float value = 1.0f;
    private int barSize;
    
    // Bar position and graphic holders
    private int x, y;
    private TextureRegion logo;
    public BonneHealthBar (Texture spriteSheet, int x, int y) {
        this.logo = new TextureRegion(spriteSheet, LOGO_X, LOGO_Y, LOGO_W, LOGO_H);
        this.x = x;
        this.y = y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    @Override
    public void update(float deltaTime) {
        // N/A
    }

    @Override
    public void draw(Renderer renderer) {
        // Draw the bar
        renderer.drawRect(ShapeType.Filled, BAR_COLOR, this.x + BAR_X, this.y + BAR_Y, this.barSize, BAR_H);
        renderer.drawRect(ShapeType.Line, BAR_OUTLINE, this.x + BAR_X, this.y + BAR_Y, this.barSize, BAR_H);
        
        // Draw the logo
        renderer.drawRegion(this.logo, this.x, this.y);
    }

    @Override
    public void setValue(float value) {
        // Clamp to [0, 1].
        this.value = Math.max(1, Math.min(0, value));
        this.barSize = Math.max(0, (int) Math.ceil(this.value * BAR_W));
    }

    @Override
    public float getValue() {
        return this.value;
    }
}
