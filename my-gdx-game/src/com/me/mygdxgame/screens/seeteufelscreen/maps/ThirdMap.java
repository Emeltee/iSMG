package com.me.mygdxgame.screens.seeteufelscreen.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.GameMap;

public class ThirdMap extends GameMap {

    private static final String TEXTURE_PATH = "img/seeTiles1.png";
    private Texture spriteSheet;
    private boolean resourcesLoaded;
    private int animationFrame;
    private int height;
    private boolean debugMode;
    
    private TextureRegion rockSprite;
    private TextureRegion wallSprite;
    private TextureRegion smallMazeSprite;
    private TextureRegion largeMazeSprite;
    private TextureRegion idkSprite;

    private TextureRegion[] borderFrames;
    
    private TextureRegion lightPillarTopBase;
    private TextureRegion lightPillarBottomBase;
    private TextureRegion[] lightPillar;
    
    private TextureRegion grate;
    private TextureRegion[] waterfall;
    
    // Since apparently this is a 3D space, which contains a sprawling infinity of
    // floating point nonsense, and no precise integer pixel coordinates that one might
    // use to find, say, the lower left corner of the screen ("field of view", whatever),
    // I'm making everything on screen relative to the ground, where the ground starts,
    // what size the ground tile is and how much it repeats. Because apparently as long
    // as everything makes sense relative to everything else, all you have to do is put
    // the camera in the right spot, and you can kind of get it to look the way you want..

    private final int GROUND_DIM = 45; // Width of ground tile
    private final int OFFSET = 0 * GROUND_DIM; // Extra padding tiles, since this is so imprecise
    private final int 
            GROUND_ORIGIN_X = 0,  // Origin X point
            GROUND_ORIGIN_Y = 0, // Origin Y point
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 9; // Columns of ground tile

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    private final int 
            GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically
    
    public ThirdMap() {}
    
    public ThirdMap(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    @Override
    public void load() {
        if (!this.resourcesLoaded) {
            // Make the various sprites
            this.spriteSheet = new Texture(Gdx.files.internal(TEXTURE_PATH));
            this.spriteSheet.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
            this.rockSprite = new TextureRegion(spriteSheet, 0, 57, GROUND_DIM, GROUND_DIM);
            this.wallSprite = new TextureRegion(spriteSheet, 210, 17, GROUND_DIM, GROUND_DIM);
            this.smallMazeSprite = new TextureRegion(spriteSheet, 165, 17, GROUND_DIM, GROUND_DIM);
            this.largeMazeSprite = new TextureRegion(spriteSheet, 45, 0, 120, 120);
            this.idkSprite = new TextureRegion(spriteSheet, 165, 120, GROUND_DIM, GROUND_DIM);
            
            // Make the border animation
            this.borderFrames = new TextureRegion[8];
            this.borderFrames[0] = new TextureRegion(spriteSheet, 35, 3, 10, 10);
            this.borderFrames[1] = new TextureRegion(spriteSheet, 35, 14, 10, 10);
            this.borderFrames[2] = new TextureRegion(spriteSheet, 35, 25, 10, 10);
            this.borderFrames[3] = new TextureRegion(spriteSheet, 35, 36, 10, 10);
            this.borderFrames[4] = new TextureRegion(spriteSheet, 35, 47, 10, 10);
            this.borderFrames[5] = new TextureRegion(spriteSheet, 35, 36, 10, 10);
            this.borderFrames[6] = new TextureRegion(spriteSheet, 35, 25, 10, 10);
            this.borderFrames[7] = new TextureRegion(spriteSheet, 35, 14, 10, 10);
            
            // Make the light pillar animation
            this.lightPillarTopBase = new TextureRegion(spriteSheet, 0, 44, 35, 13);
            lightPillarTopBase.flip(false, true);
            this.lightPillarBottomBase = new TextureRegion(spriteSheet, 0, 44, 35, 13);
            this.lightPillar = new TextureRegion[6];
            this.lightPillar[0] = new TextureRegion(spriteSheet, 0, 33, 35, 11);
            this.lightPillar[1] = new TextureRegion(spriteSheet, 0, 22, 35, 11);
            this.lightPillar[2] = new TextureRegion(spriteSheet, 0, 11, 35, 11);
            this.lightPillar[3] = new TextureRegion(spriteSheet, 0, 0, 35, 11);
            this.lightPillar[4] = new TextureRegion(spriteSheet, 0, 11, 35, 11);
            this.lightPillar[5] = new TextureRegion(spriteSheet, 0, 22, 35, 11);
            
            this.grate = new TextureRegion(spriteSheet, 22, 102, 23, 23);
            this.waterfall = new TextureRegion[5];
            this.waterfall[0] = new TextureRegion(spriteSheet, 0, 125, 31, 84);
            this.waterfall[1] = new TextureRegion(spriteSheet, 32, 125, 31, 84);
            this.waterfall[2] = new TextureRegion(spriteSheet, 64, 125, 31, 84);
            this.waterfall[3] = new TextureRegion(spriteSheet, 96, 125, 31, 84);
            this.waterfall[4] = new TextureRegion(spriteSheet, 128, 125, 31, 84);
            
            this.animationFrame = 0;

            this.resourcesLoaded = true;
        }
    }

    @Override
    public void unload() {
        if (this.resourcesLoaded) {
            this.spriteSheet.dispose();
            this.resourcesLoaded = false;
        }
    }

    @Override
    public Vector3 getInitialPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Rectangle[] getObstacles() {
        // TODO Auto-generated method stub
        Rectangle [] result = {                
            new Rectangle(GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM, 9 * GROUND_DIM), // Left Wall
            new Rectangle(GROUND_ORIGIN_X + 13 * GROUND_DIM, GROUND_ORIGIN_Y, GROUND_DIM, 9 * GROUND_DIM), // Right Wall
            new Rectangle(GROUND_ORIGIN_X, GROUND_ORIGIN_Y + 8 * GROUND_DIM, 13 * GROUND_DIM, GROUND_DIM), // Ceiling
            new Rectangle(GROUND_ORIGIN_X + GROUND_DIM, GROUND_ORIGIN_Y, 7 * GROUND_DIM, GROUND_DIM), // Lowest Floor
            new Rectangle(GROUND_ORIGIN_X + GROUND_DIM, GROUND_ORIGIN_Y + GROUND_DIM, 4 * GROUND_DIM, GROUND_DIM), // 2nd Lowest
            new Rectangle(GROUND_ORIGIN_X + GROUND_DIM, GROUND_ORIGIN_Y + 2*GROUND_DIM, 2 * GROUND_DIM, GROUND_DIM), // 3rd Lowest
            new Rectangle(GROUND_ORIGIN_X + GROUND_DIM, GROUND_ORIGIN_Y + 3*GROUND_DIM, 1 * GROUND_DIM, GROUND_DIM), // 4th Lowest
        };
        return result;
    }

    @Override
    public void render(float deltaTime, Matrix4 transformMatrix) {
        // Prepare the game's spriteBatch for drawing.
        MyGdxGame.currentGame.spriteBatch
        .setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();
        
        // Draw the bg
        tileXY(this.largeMazeSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, 5, 3);
        
        // Draw the border
        tileX(this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 5* GROUND_DIM - 8, GROUND_START_Y, 14);
        tileX(this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 3*GROUND_DIM - 2, GROUND_START_Y + GROUND_DIM, 9);
        tileX(this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 2*GROUND_DIM - 6, GROUND_START_Y + 2*GROUND_DIM, 5);
        tileX(this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 1*GROUND_DIM - 6, GROUND_START_Y + 3*GROUND_DIM, 5);
         
        // tileX(this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 9 * GROUND_DIM, GROUND_START_Y, 22);
        
        // Draw some light pillars
        tileY(lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 3 + 10, 18);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarBottomBase, GROUND_ORIGIN_X + GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 3 + 10);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarTopBase, GROUND_ORIGIN_X + GROUND_DIM + 5, GROUND_START_Y + 7 * GROUND_DIM - 20);
        
        tileY(lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + 3*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 1 + 10, 24);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarBottomBase, GROUND_ORIGIN_X + 3*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 1 + 10);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarTopBase, GROUND_ORIGIN_X + 3*GROUND_DIM + 5, GROUND_START_Y + 7 * GROUND_DIM - 20);
        
        tileY(lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + 5*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0 + 10, 28);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarBottomBase, GROUND_ORIGIN_X + 5*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0 + 10);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarTopBase, GROUND_ORIGIN_X + 5*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 7 - 20);

        tileY(lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + 7*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0 + 10, 28);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarBottomBase, GROUND_ORIGIN_X + 7*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0 + 10);
        MyGdxGame.currentGame.spriteBatch.draw(this.lightPillarTopBase, GROUND_ORIGIN_X + 7*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 7 - 20);
 
        tileX(this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + GROUND_DIM - 1, GROUND_START_Y + 7 * GROUND_DIM - 10, 53);
        
        // Draw /a Waterfall
        MyGdxGame.currentGame.spriteBatch.draw(this.idkSprite, GROUND_ORIGIN_X + 9 * GROUND_DIM + 10, GROUND_START_Y + 12);
        MyGdxGame.currentGame.spriteBatch.draw(this.grate, (int)Math.ceil(GROUND_ORIGIN_X + 9.5 * GROUND_DIM), GROUND_START_Y + 20);
        MyGdxGame.currentGame.spriteBatch.draw(this.waterfall[animationFrame / 4 % 5], (int)Math.ceil(GROUND_ORIGIN_X + 9.5 * GROUND_DIM -4), GROUND_ORIGIN_Y);
        
        MyGdxGame.currentGame.spriteBatch.draw(this.idkSprite, GROUND_ORIGIN_X + 11 * GROUND_DIM + 11, GROUND_START_Y + 12);
        MyGdxGame.currentGame.spriteBatch.draw(this.grate, (int)(GROUND_ORIGIN_X + 11.5 * GROUND_DIM), GROUND_START_Y + 20);
        MyGdxGame.currentGame.spriteBatch.draw(this.waterfall[animationFrame / 4 % 5], (int)Math.ceil(GROUND_ORIGIN_X + 11.5 * GROUND_DIM -4), GROUND_ORIGIN_Y);
        
        // Draw the stage on top of everything.
        tileX(this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, 8);
        tileX(this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + GROUND_DIM, 5);
        tileX(this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + 2*GROUND_DIM, 3);
        tileX(this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + 3* GROUND_DIM, 2);
        tileX(this.wallSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + 8 * GROUND_DIM, 13);
        tileY(this.wallSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, 9);
        tileY(this.wallSprite, GROUND_ORIGIN_X + 13 * GROUND_DIM, GROUND_ORIGIN_Y, 9);

        // Update animation frame
        animationFrame = (animationFrame < 96) ? animationFrame + 1 : 0;
        
        // Finalize drawing.
        MyGdxGame.currentGame.spriteBatch.end();
        
        // Debug
        if (this.debugMode) {
            drawObstacles(transformMatrix, this.getObstacles(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }
    }

}
