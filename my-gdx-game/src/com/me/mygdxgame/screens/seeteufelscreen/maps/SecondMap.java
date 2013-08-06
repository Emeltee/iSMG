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

public class SecondMap extends GameMap {

    private static final String TEXTURE_PATH = "img/seeTiles1.png";
    private Texture spriteSheet;
    private boolean resourcesLoaded;
    private int animationFrame;
    private int height;
    
    private TextureRegion rockSprite;
    private TextureRegion wallSprite;
    private TextureRegion smallMazeSprite;
    private TextureRegion largeMazeSprite;
    private TextureRegion idkSprite;

    private TextureRegion[] borderFrames;
    
    // Since apparently this is a 3D space, which contains a sprawling infinity of
    // floating point nonsense, and no precise integer pixel coordinates that one might
    // use to find, say, the lower left corner of the screen ("field of view", whatever),
    // I'm making everything on screen relative to the ground, where the ground starts,
    // what size the ground tile is and how much it repeats. Because apparently as long
    // as everything makes sense relative to everything else, all you have to do is put
    // the camera in the right spot, and you can kind of get it to look the way you want..

    private final int GROUND_DIM = 44; // Width of ground tile
    private final int OFFSET = 0 * GROUND_DIM; // Extra padding tiles, since this is so imprecise
    private final int GROUND_ORIGIN_X = 0,  // Origin X point
            GROUND_ORIGIN_Y = 0, // Origin Y point
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 9; // Columns of ground tile

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    private final int GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically

    
    public SecondMap() {
        this.height=15;
    }
    
    public SecondMap(int height) {
        this.height=height;
    }
    
    public SecondMap(int height, boolean debugMode) {
        this.height = height;
        this.debugMode = debugMode;
    }
    
    @Override
    public void load() {
        if (!this.resourcesLoaded) {
            this.spriteSheet = new Texture(Gdx.files.internal(TEXTURE_PATH));
            this.spriteSheet.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
            this.rockSprite = new TextureRegion(spriteSheet, 0, 57, 44, 44);
            this.wallSprite = new TextureRegion(spriteSheet, 209, 17, 44, 44);
            this.smallMazeSprite = new TextureRegion(spriteSheet, 165, 17, 44, 44);
            this.largeMazeSprite = new TextureRegion(spriteSheet, 45, 0, 120, 120);
            this.idkSprite = new TextureRegion(spriteSheet, 165, 120, 44, 44);
            
            this.borderFrames = new TextureRegion[5];
            this.borderFrames[0] = new TextureRegion(spriteSheet, 35, 3, 10, 10);
            this.borderFrames[1] = new TextureRegion(spriteSheet, 35, 14, 10, 10);
            this.borderFrames[2] = new TextureRegion(spriteSheet, 35, 25, 10, 10);
            this.borderFrames[3] = new TextureRegion(spriteSheet, 35, 36, 10, 10);
            this.borderFrames[4] = new TextureRegion(spriteSheet, 35, 47, 10, 10);
            
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
        Rectangle [] result = {
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_WIDTH * GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM, this.height * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH-1), GROUND_ORIGIN_Y, GROUND_DIM, this.height * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y + height * GROUND_DIM, GROUND_DIM * GROUND_WIDTH, GROUND_DIM)
        };
        return result;
    }

    @Override
    public void render(float deltaTime, Matrix4 transformMatrix) {
        // Prepare the game's spriteBatch for drawing.
        MyGdxGame.currentGame.spriteBatch
        .setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();

        //tileXY(this.smallMazeSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_WIDTH, height);
        tileY(this.wallSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, this.height);
        tileY(this.wallSprite, GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH-1), GROUND_ORIGIN_Y, this.height);
        tileX(this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_WIDTH);
        tileX(this.wallSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + GROUND_DIM * height, GROUND_WIDTH);
        
        // Update animation frame
        animationFrame = (animationFrame < 60) ? animationFrame + 1 : 0;
        
        // Finalize drawing.
        MyGdxGame.currentGame.spriteBatch.end();
        
        // Debug
        if (this.debugMode) {
            drawObstacles(transformMatrix, this.getObstacles(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }
    }

}
