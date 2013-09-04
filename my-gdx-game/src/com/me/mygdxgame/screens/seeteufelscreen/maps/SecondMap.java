package com.me.mygdxgame.screens.seeteufelscreen.maps;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.Renderer;

public class SecondMap extends GameMap {
    
    private Texture spriteSheet;
    private boolean resourcesLoaded;
    private int height;
    
    private Texture rockTex;
    private Sprite rockSprite;
    private Texture wallTex;
    private Sprite wallSprite;
    private Texture smallMazeTex;
    private Sprite smallMazeSprite;;

    public static final int GROUND_DIM = 45; // Width of ground tile
    public static final int GROUND_ORIGIN_X = 0,  // Origin X point
            GROUND_ORIGIN_Y = 0, // Origin Y point
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 15; // Columns of ground tile

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    public static final int GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically
    
    public SecondMap(Texture spriteSheet, int height) {
        this.spriteSheet = spriteSheet;
        this.height = height;
    }
    
    @Override
    public void load() {
        if (!this.resourcesLoaded) {
            
            Pixmap rockMap = new Pixmap(GROUND_DIM, GROUND_DIM, Format.RGBA8888);
            this.spriteSheet.getTextureData().prepare();
            rockMap.drawPixmap(this.spriteSheet.getTextureData().consumePixmap(), 0, 0, 0, 57, GROUND_DIM, GROUND_DIM);
            this.rockTex = new Texture(rockMap);
            this.rockTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            this.rockSprite = new Sprite(this.rockTex);
            this.rockSprite.setBounds(GROUND_ORIGIN_X, GROUND_START_Y + (5 * GROUND_DIM), GROUND_WIDTH * GROUND_DIM, GROUND_DIM);
            this.rockSprite.setU2(GROUND_WIDTH);
            
            Pixmap wallMap = new Pixmap(GROUND_DIM, GROUND_DIM, Format.RGBA8888);
            this.spriteSheet.getTextureData().prepare();
            wallMap.drawPixmap(this.spriteSheet.getTextureData().consumePixmap(), 0, 0, 210, 17, GROUND_DIM, GROUND_DIM);
            this.wallTex = new Texture(wallMap);
            this.wallTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            this.wallSprite = new Sprite(this.wallTex);
            
            Pixmap smallMazeMap = new Pixmap(GROUND_DIM, GROUND_DIM, Format.RGBA8888);
            this.spriteSheet.getTextureData().prepare();
            smallMazeMap.drawPixmap(this.spriteSheet.getTextureData().consumePixmap(), 0, 0, 165, 17, GROUND_DIM, GROUND_DIM);
            this.smallMazeTex = new Texture(smallMazeMap);
            this.smallMazeTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            this.smallMazeSprite = new Sprite(this.smallMazeTex);
            this.smallMazeSprite.setBounds(GROUND_ORIGIN_X + GROUND_DIM, GROUND_START_Y, (GROUND_WIDTH - 2) * GROUND_DIM, (this.height - 1) * GROUND_DIM);
            this.smallMazeSprite.setU2(GROUND_WIDTH - 2);
            this.smallMazeSprite.setV2(this.height - 1);

            this.resourcesLoaded = true;
        }

    }

    @Override
    public void unload() {
        if (this.resourcesLoaded) {
            this.rockTex.dispose();
            this.smallMazeTex.dispose();
            this.wallTex.dispose();
            this.resourcesLoaded = false;
        }
    }

    @Override
    public Vector3 getInitialPosition() {
        //return new Vector3(50, 50, 0);
        return new Vector3(FirstMap.GROUND_DIM, FirstMap.GROUND_DIM / 2, 0);
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
    public void render(float deltaTime, Renderer renderer) {

        // Background.
        renderer.drawSprite(this.smallMazeSprite);

        // Floor.
        this.wallSprite.setBounds(GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM * GROUND_WIDTH, GROUND_DIM);
        this.wallSprite.setU2(GROUND_WIDTH);
        this.wallSprite.setV2(1);
        renderer.drawSprite(this.wallSprite);

        // Walls.
        this.wallSprite.setBounds(GROUND_ORIGIN_X, GROUND_ORIGIN_Y + GROUND_DIM, GROUND_DIM, (this.height - 1) * GROUND_DIM);
        this.wallSprite.setU2(1);
        this.wallSprite.setV2(this.height - 1);
        renderer.drawSprite(this.wallSprite);
        this.wallSprite.setBounds(GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH - 1), GROUND_ORIGIN_Y + GROUND_DIM, GROUND_DIM, (this.height - 1) * GROUND_DIM);
        renderer.drawSprite(this.wallSprite);

        // Ceiling.
        this.wallSprite.setBounds(GROUND_ORIGIN_X, GROUND_ORIGIN_Y + GROUND_DIM * this.height, GROUND_DIM * GROUND_WIDTH, GROUND_DIM);
        this.wallSprite.setU2(GROUND_WIDTH);
        this.wallSprite.setV2(1);
        renderer.drawSprite(this.wallSprite);

        // Debug
        if (this.debugMode) {
            drawObstacles(renderer, this.getObstacles(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }
    }

}
