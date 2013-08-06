package com.me.mygdxgame.entities.progressbars;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.ProgressBar;

public class BonneHealthBar implements ProgressBar {

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
    
    @Override
    public void update(float deltaTime) {
        // N/A
    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        // Since the bar draws away from the logo to the left, need to manually adjust coords
        int temp_offset = BAR_W - this.barSize;
        
        // Draw the bar
        MyGdxGame.currentGame.shapeRenderer.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.shapeRenderer.begin(ShapeType.Filled);
        MyGdxGame.currentGame.shapeRenderer.rect(this.x + BAR_X + temp_offset, this.y + BAR_Y, this.barSize, BAR_H, BAR_COLOR, BAR_COLOR, BAR_COLOR, BAR_COLOR);
        MyGdxGame.currentGame.shapeRenderer.end();
        
        // Draw the logo
        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();
        MyGdxGame.currentGame.spriteBatch.draw(this.logo, this.x, this.y);
        MyGdxGame.currentGame.spriteBatch.end();
    }

    @Override
    public void setValue(float value) {
        // Clamp to [0, 1].
        this.value = Math.max(1, Math.min(0, value));
        this.barSize = Math.max(0, (int) Math.ceil(this.value * BAR_H));
    }

    @Override
    public float getValue() {
        return this.value;
    }
}
