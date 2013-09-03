package com.me.mygdxgame.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;

/**
 * Wraps the SpriteBatch and ShapeRenderer classes. Intended to make these
 * objects simpler to set up and pass around.
 * 
 */
public class Renderer {
    
    private static SpriteBatch spriteBatch = null;
    private static ShapeRenderer shapeRenderer = null;
    private static boolean isBatchRendering = false;
    private static ShapeType shapeType = ShapeType.Filled;
    private static int CURRENT_ID = 0;
    private static int LAST_MATRIX_PUSHED_TO_BATCH = -1;
    private static int LAST_MATRIX_PUSHED_TO_SHAPE = -1;
    
    private Matrix4 transformMatrix = null;
    private int rendererID;
    
    public Renderer(Matrix4 transformMatrix) {
        
        // Save transform matrix.
        this.transformMatrix = transformMatrix;

        // Save ID, which is used to track which Renderer's matrix is currently
        // loaded.
        this.rendererID = Renderer.CURRENT_ID;
        Renderer.CURRENT_ID++;
        
        // If this is the first Renderer, initialize statics.
        if (Renderer.shapeRenderer == null) {
            Renderer.shapeRenderer = new ShapeRenderer();
            Renderer.spriteBatch = new SpriteBatch();
            Renderer.spriteBatch.begin();
            Renderer.isBatchRendering = true;
        }
    }
    
    public static void flush() {
        if (Renderer.isBatchRendering) {
            Renderer.spriteBatch.flush();
        } else {
            Renderer.shapeRenderer.flush();
        }
    }
    
    public void drawRect(ShapeType shapeType, Color color, float x, float y, float width, float height) {
        
        this.prepareShapeRendererState(shapeType);
        
        Renderer.shapeRenderer.setColor(color);
        Renderer.shapeRenderer.rect(x, y, width, height);
    }
    
    public void drawRegion(TextureRegion region, float x, float y) {
        
        this.prepareSpriteBatchState();
        
        spriteBatch.draw(region, x, y);
    }

    public void drawRegion(TextureRegion region, float x, float y, Color tint,
            float scaleX, float scaleY, int rotation) {
        
        this.prepareSpriteBatchState();
        
        spriteBatch.setColor(tint);
        spriteBatch.draw(region, x, y, region.getRegionWidth() / 2,
                region.getRegionHeight() / 2, region.getRegionWidth(),
                region.getRegionHeight(), scaleX, scaleY, rotation);
        spriteBatch.setColor(Color.WHITE);
    }
    
    public void drawText(BitmapFont font, CharSequence mesg, float x, float y) {
        
        this.prepareSpriteBatchState();
        
        font.draw(Renderer.spriteBatch, mesg, x, y);
    }
    
    private void prepareShapeRendererState(ShapeType shapeType) {
        // Call begin/end as needed to prepare ShapeRenderer.
        if (Renderer.isBatchRendering) {
            Renderer.isBatchRendering = false;
            Renderer.spriteBatch.end();
            Renderer.shapeRenderer.begin(shapeType);
        } else if(Renderer.shapeType != shapeType) {
            shapeRenderer.end();
            shapeRenderer.begin(shapeType);
        }
        Renderer.shapeType = shapeType;
        
        // Set projection matrix if needed.
        if (Renderer.LAST_MATRIX_PUSHED_TO_SHAPE != this.rendererID) {
            Renderer.shapeRenderer.setProjectionMatrix(this.transformMatrix);
            Renderer.LAST_MATRIX_PUSHED_TO_SHAPE = this.rendererID;
        }
        
        // They seem to require you to call this RIGHT before rendering shapes.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    private void prepareSpriteBatchState() {
        // Call begin/end as needed to prepare SpriteBatch.
        if (!Renderer.isBatchRendering) {
            Renderer.isBatchRendering = true;
            Renderer.shapeRenderer.end();
            Renderer.spriteBatch.begin();
        }
        
        // Set projection matrix if needed.
        if (Renderer.LAST_MATRIX_PUSHED_TO_BATCH != this.rendererID) {
            Renderer.spriteBatch.setProjectionMatrix(this.transformMatrix);
            Renderer.LAST_MATRIX_PUSHED_TO_BATCH = this.rendererID;
        }
    }
}
