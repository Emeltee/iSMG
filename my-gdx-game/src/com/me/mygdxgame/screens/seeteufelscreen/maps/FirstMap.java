package com.me.mygdxgame.screens.seeteufelscreen.maps;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.Renderer;

//TODO Horizontally tiled wallTex appear to have gaps between them.
public class FirstMap extends GameMap {
    
    private Texture spriteSheet;
    private boolean resourcesLoaded;
    private int animationFrame;

    private Texture rockTex;
    private Sprite rockSprite;
    private Texture wallTex;
    private Sprite wallSprite;
    private Texture smallMazeTex;
    private Sprite smallMazeSprite;
    private TextureRegion greyBlockRegion;
    private TextureRegion pedistalRegion;
    private Texture pillarTex;
    private Sprite pillarSprite;
    private TextureRegion pillarTopBaseRegion;
    private TextureRegion pillarBottomBaseRegion;

    private TextureRegion grateRegion;
    private TextureRegion[] waterfall;

    // Since apparently this is a 3D space, which contains a sprawling infinity of
    // floating point nonsense, and no precise integer pixel coordinates that one might
    // use to find, say, the lower left corner of the screen ("field of view", whatever),
    // I'm making everything on screen relative to the ground, where the ground starts,
    // what size the ground tile is and how much it repeats. Because apparently as long
    // as everything makes sense relative to everything else, all you have to do is put
    // the camera in the right spot, and you can kind of get it to look the way you want..

    public static final int GROUND_DIM = 45; // Width of ground tile
    public static final int OFFSET = 0 * GROUND_DIM; // Extra padding tiles, since this is so imprecise
    public static final int GROUND_ORIGIN_X = (-1 * MyGdxGame.SCREEN_WIDTH / 2) - OFFSET,  // Origin X point, off-screen to the left
            GROUND_ORIGIN_Y = (-1 * MyGdxGame.SCREEN_HEIGHT / 2) - OFFSET, // Origin Y point, off-screen below
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 20; // Columns of ground tile

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    public static final int GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically

    public static final int PLATFORM_START_X = GROUND_ORIGIN_X + (6 * GROUND_DIM);

    public FirstMap(Texture spriteSheet) {
        this.spriteSheet = spriteSheet;
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
            this.smallMazeSprite.setBounds(GROUND_ORIGIN_X, GROUND_START_Y, GROUND_WIDTH * GROUND_DIM, 5 * GROUND_DIM);
            this.smallMazeSprite.setU2(GROUND_WIDTH);
            this.smallMazeSprite.setV2(5);
            
            
            this.greyBlockRegion = new TextureRegion(spriteSheet, 165, 120, GROUND_DIM, GROUND_DIM);
            this.pedistalRegion = new TextureRegion(spriteSheet, 165, 0, 55, 17);

            this.grateRegion = new TextureRegion(spriteSheet, 22, 102, 23, 23);
            this.waterfall = new TextureRegion[5];
            this.waterfall[0] = new TextureRegion(spriteSheet, 0, 125, 31, 84);
            this.waterfall[1] = new TextureRegion(spriteSheet, 32, 125, 31, 84);
            this.waterfall[2] = new TextureRegion(spriteSheet, 64, 125, 31, 84);
            this.waterfall[3] = new TextureRegion(spriteSheet, 96, 125, 31, 84);
            this.waterfall[4] = new TextureRegion(spriteSheet, 128, 125, 31, 84);
            
            Pixmap pillarMap = new Pixmap(19, 36, Format.RGBA8888);
            this.spriteSheet.getTextureData().prepare();
            pillarMap.drawPixmap(this.spriteSheet.getTextureData().consumePixmap(), 0, 0, 224, 124, 19, 36);
            this.pillarTex = new Texture(pillarMap);
            this.pillarTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            this.pillarSprite = new Sprite(this.pillarTex);
            this.pillarSprite.setSize(19, 36 * 6);
            this.pillarSprite.setV2(6);
            
            this.pillarBottomBaseRegion = new TextureRegion(spriteSheet, 224, 160, 19, 15);
            this.pillarTopBaseRegion = new TextureRegion(spriteSheet, 224, 160, 19, 15);
            this.pillarTopBaseRegion.flip(false, true);
            
            this.animationFrame = 0;

            this.resourcesLoaded = true;
        }
    }

    @Override
    public void unload() {
        if (this.resourcesLoaded) {
            this.pillarTex.dispose();
            this.rockTex.dispose();
            this.smallMazeTex.dispose();
            this.wallTex.dispose();
            this.resourcesLoaded = false;
        }

    }

    @Override
    public Vector3 getInitialPosition() {
        return new Vector3(GROUND_END_X - GROUND_DIM, GROUND_START_Y, 0);
    }

    @Override
    public Rectangle[] getObstacles() {
        // TODO Auto-generated method stub
        Rectangle[] result = {
            new Rectangle(GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM * GROUND_WIDTH, GROUND_DIM * GROUND_HEIGHT), // Ground
            new Rectangle(PLATFORM_START_X, GROUND_START_Y, GROUND_DIM * 3, GROUND_DIM * 1), // Platform
            new Rectangle(PLATFORM_START_X + GROUND_DIM - 5, GROUND_START_Y + GROUND_DIM, this.pedistalRegion.getRegionWidth(), (int)this.pedistalRegion.getRegionHeight() / 2.0f), // Goal
            //new Rectangle(PLATFORM_START_X + GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM + (int) (this.goalSprite.getRegionHeight() / 2.0f), (this.goalSprite.getRegionWidth()-20), (int) (this.goalSprite.getRegionHeight() / 2.0f)),
            new Rectangle(GROUND_ORIGIN_X, GROUND_START_Y + (5 * GROUND_DIM), GROUND_DIM * GROUND_WIDTH, GROUND_DIM ), // Ceiling
            new Rectangle(GROUND_ORIGIN_X - GROUND_DIM, GROUND_START_Y - GROUND_DIM, GROUND_DIM, GROUND_DIM * 7), // Left boundary
            new Rectangle(GROUND_END_X, GROUND_START_Y - GROUND_DIM, GROUND_DIM, GROUND_DIM * 7) // Right boundary
        };
        return result;
    }

    @Override
    public void render(float deltaTime, Renderer renderer) {
        
        // Main background.
        renderer.drawSprite(this.smallMazeSprite);
        
        // Alternate background for refractor.
        diagonalRight(renderer, this.greyBlockRegion, PLATFORM_START_X - (2 * GROUND_DIM), GROUND_START_Y, 4);
        diagonalLeft(renderer, this.greyBlockRegion, PLATFORM_START_X + (4 * GROUND_DIM), GROUND_START_Y, 3);
        
        // Pillars.
        for (int i = 0; i < 4; i++) { 
            this.pillarSprite.setPosition(GROUND_ORIGIN_X + this.pillarSprite.getWidth() * (2 * i), GROUND_START_Y);
            renderer.drawSprite(this.pillarSprite);
        }
        float collumnStartX = PLATFORM_START_X + GROUND_DIM * 6;
        for (int i = 0; i < 8; i++) {
            this.pillarSprite.setPosition(collumnStartX + this.pillarSprite.getWidth() * (2 * i), GROUND_START_Y);
            renderer.drawSprite(this.pillarSprite);
        }
        checkerX(renderer, this.pillarBottomBaseRegion, GROUND_ORIGIN_X, GROUND_START_Y, 4, 1);
        checkerX(renderer, this.pillarTopBaseRegion, GROUND_ORIGIN_X, GROUND_START_Y + (5 * GROUND_DIM) - this.pillarTopBaseRegion.getRegionHeight(), 4, 1);
        checkerX(renderer, this.pillarBottomBaseRegion, PLATFORM_START_X + GROUND_DIM * 6, GROUND_START_Y, 8, 1);
        checkerX(renderer, this.pillarTopBaseRegion, PLATFORM_START_X + GROUND_DIM * 6, GROUND_START_Y + (5 * GROUND_DIM) - this.pillarTopBaseRegion.getRegionHeight(), 8, 1);
        
        // Ceiling and floor.
        renderer.drawSprite(this.rockSprite);
        this.wallSprite.setBounds(GROUND_ORIGIN_X, GROUND_START_Y - GROUND_DIM, GROUND_WIDTH * GROUND_DIM, GROUND_DIM);
        this.wallSprite.setU2(GROUND_WIDTH);
        renderer.drawSprite(this.wallSprite);
        
        // Refractor and platform.
        this.wallSprite.setBounds(PLATFORM_START_X, GROUND_START_Y, 3 * GROUND_DIM, GROUND_DIM);
        this.wallSprite.setU2(3);
        renderer.drawSprite(this.wallSprite);
        renderer.drawRegion(this.greyBlockRegion, PLATFORM_START_X + GROUND_DIM, GROUND_START_Y + GROUND_DIM);
        renderer.drawRegion(this.pedistalRegion, PLATFORM_START_X + GROUND_DIM - 5, GROUND_START_Y + GROUND_DIM);
        
        // Draw the waterfalls.
        renderer.drawRegion(this.grateRegion, PLATFORM_START_X - (3 * GROUND_DIM) + 12, GROUND_START_Y + 64);
        renderer.drawRegion(this.grateRegion, PLATFORM_START_X + (5 * GROUND_DIM) + 12, GROUND_START_Y + 64);
        renderer.drawRegion(this.waterfall[animationFrame / 4 % 5], PLATFORM_START_X - (3 * GROUND_DIM) + 8, GROUND_START_Y);
        renderer.drawRegion(this.waterfall[animationFrame / 4 % 5], PLATFORM_START_X + (5 * GROUND_DIM) + 8, GROUND_START_Y);
        
        // Update animation frame
        animationFrame = (animationFrame < 60) ? animationFrame + 1 : 0;
        
        // Debug
        if (this.debugMode) {
            drawObstacles(renderer, this.getObstacles(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }

    }
}
