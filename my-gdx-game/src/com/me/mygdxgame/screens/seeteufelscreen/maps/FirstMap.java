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

//TODO Horizontally tiled wallSprite appear to have gaps between them.
public class FirstMap extends GameMap {

    private static final String TEXTURE_PATH = "img/seeTiles1.png";
    private Texture spriteSheet;
    private boolean resourcesLoaded;
    private int animationFrame;

    private TextureRegion rockSprite;
    private TextureRegion wallSprite;
    private TextureRegion smallMazeSprite;
    private TextureRegion greyBlockSprite;
    private TextureRegion pedistalSprite;
    private TextureRegion pillarSprite;
    private TextureRegion pillarTopBaseSprite;
    private TextureRegion pillarBottomBaseSprite;
    private TextureRegion nadiaSprite;
    
    private TextureRegion[] borderFrames;

    private TextureRegion grate;
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

    public FirstMap() {}    
    
    public FirstMap(boolean debugMode) {
        this.debugMode = debugMode;
    }
    
    @Override
    public void load() {
        if (!this.resourcesLoaded) {
            this.spriteSheet = new Texture(Gdx.files.internal(TEXTURE_PATH));
            this.spriteSheet.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
            this.rockSprite = new TextureRegion(spriteSheet, 0, 57, GROUND_DIM, GROUND_DIM);
            this.wallSprite = new TextureRegion(spriteSheet, 210, 17, GROUND_DIM, GROUND_DIM);
            this.smallMazeSprite = new TextureRegion(spriteSheet, 165, 17, GROUND_DIM, GROUND_DIM);
            this.greyBlockSprite = new TextureRegion(spriteSheet, 165, 120, GROUND_DIM, GROUND_DIM);
            this.pedistalSprite = new TextureRegion(spriteSheet, 165, 0, 55, 17);

            this.borderFrames = new TextureRegion[5];
            this.borderFrames[0] = new TextureRegion(spriteSheet, 35, 3, 10, 10);
            this.borderFrames[1] = new TextureRegion(spriteSheet, 35, 14, 10, 10);
            this.borderFrames[2] = new TextureRegion(spriteSheet, 35, 25, 10, 10);
            this.borderFrames[3] = new TextureRegion(spriteSheet, 35, 36, 10, 10);
            this.borderFrames[4] = new TextureRegion(spriteSheet, 35, 47, 10, 10);

            this.grate = new TextureRegion(spriteSheet, 22, 102, 23, 23);
            this.waterfall = new TextureRegion[5];
            this.waterfall[0] = new TextureRegion(spriteSheet, 0, 125, 31, 84);
            this.waterfall[1] = new TextureRegion(spriteSheet, 32, 125, 31, 84);
            this.waterfall[2] = new TextureRegion(spriteSheet, 64, 125, 31, 84);
            this.waterfall[3] = new TextureRegion(spriteSheet, 96, 125, 31, 84);
            this.waterfall[4] = new TextureRegion(spriteSheet, 128, 125, 31, 84);
            
            this.pillarSprite = new TextureRegion(spriteSheet, 224, 124, 19, 36);
            this.pillarBottomBaseSprite = new TextureRegion(spriteSheet, 224, 160, 19, 15);
            this.pillarTopBaseSprite = new TextureRegion(spriteSheet, 224, 160, 19, 15);
            this.pillarTopBaseSprite.flip(false, true);

            this.nadiaSprite = new TextureRegion(spriteSheet, 165, 165, GROUND_DIM, GROUND_DIM);
            
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
        return new Vector3(GROUND_END_X - GROUND_DIM, GROUND_START_Y, 0);
    }

    @Override
    public Rectangle[] getObstacles() {
        // TODO Auto-generated method stub
        Rectangle[] result = {
            new Rectangle(GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM * GROUND_WIDTH, GROUND_DIM * GROUND_HEIGHT), // Ground
            new Rectangle(PLATFORM_START_X, GROUND_START_Y, GROUND_DIM * 3, GROUND_DIM * 1), // Platform
            new Rectangle(PLATFORM_START_X + GROUND_DIM - 5, GROUND_START_Y + GROUND_DIM, this.pedistalSprite.getRegionWidth(), (int)this.pedistalSprite.getRegionHeight() / 2.0f), // Goal
            //new Rectangle(PLATFORM_START_X + GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM + (int) (this.goalSprite.getRegionHeight() / 2.0f), (this.goalSprite.getRegionWidth()-20), (int) (this.goalSprite.getRegionHeight() / 2.0f)),
            new Rectangle(GROUND_ORIGIN_X, GROUND_START_Y + (5 * GROUND_DIM), GROUND_DIM * GROUND_WIDTH, GROUND_DIM ), // Ceiling
            new Rectangle(GROUND_ORIGIN_X - GROUND_DIM, GROUND_START_Y - GROUND_DIM, GROUND_DIM, GROUND_DIM * 7), // Left boundary
            new Rectangle(GROUND_END_X, GROUND_START_Y - GROUND_DIM, GROUND_DIM, GROUND_DIM * 7) // Right boundary
        };
        return result;
    }

    @Override
    public void render(float deltaTime, Matrix4 transformMatrix) {
        // Prepare the game's spriteBatch for drawing.
        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();
        
        // Draw the lower wall in columns, alternating between decorative tiles by refractor
        tileXY(this.smallMazeSprite, GROUND_ORIGIN_X, GROUND_START_Y, GROUND_WIDTH, 6);
        // for (int i = 0; i < 5; i++) { checkerX(this.wallSprite, GROUND_ORIGIN_X, GROUND_START_Y + (i * GROUND_DIM), (int)Math.ceil(GROUND_WIDTH / 2), 1); }
        checkerX(this.pillarBottomBaseSprite, GROUND_ORIGIN_X, GROUND_START_Y, 4, 1);
        for (int i=0; i < 6; i++) { checkerX(this.pillarSprite, GROUND_ORIGIN_X, GROUND_START_Y + (i * this.pillarSprite.getRegionHeight()) + this.pillarTopBaseSprite.getRegionHeight(), 4, 1); }
        checkerX(this.pillarTopBaseSprite, GROUND_ORIGIN_X, GROUND_START_Y + (5 * GROUND_DIM) - this.pillarTopBaseSprite.getRegionHeight(), 4, 1);
        checkerX(this.pillarBottomBaseSprite, PLATFORM_START_X + GROUND_DIM * 6, GROUND_START_Y, 8, 1);
        for (int i=0; i < 6; i++) { checkerX(this.pillarSprite, PLATFORM_START_X + GROUND_DIM * 6, GROUND_START_Y + (i * this.pillarSprite.getRegionHeight()) + this.pillarTopBaseSprite.getRegionHeight(), 8, 1); }
        checkerX(this.pillarTopBaseSprite, PLATFORM_START_X + GROUND_DIM * 6, GROUND_START_Y + (5 * GROUND_DIM) - this.pillarTopBaseSprite.getRegionHeight(), 8, 1);
        
        
        tileXY(this.smallMazeSprite, PLATFORM_START_X - (3 * GROUND_DIM), GROUND_START_Y, 9, 6);
        diagonalRight(this.greyBlockSprite, PLATFORM_START_X - (2 * GROUND_DIM), GROUND_START_Y, 4);
        diagonalLeft(this.greyBlockSprite, PLATFORM_START_X + (4 * GROUND_DIM), GROUND_START_Y, 3);
        tileXY(this.rockSprite, GROUND_ORIGIN_X, GROUND_START_Y + (5 * GROUND_DIM), GROUND_WIDTH, 1);
        
        // Draw the upper wall, using one repeating pattern
//         tileX(this.borderFrames[animationFrame / 3 % 5], GROUND_ORIGIN_X, GROUND_START_Y + (6 * GROUND_DIM), 132);
//         tileXY(this.largeMazeSprite, GROUND_ORIGIN_X, GROUND_START_Y + (GROUND_DIM * 6) + 10, (int) Math.ceil(30 * (this.rockSprite.getRegionWidth()/(double)this.largeMazeSprite.getRegionWidth())), 2);
        
        // Create the refractor platform
        tileX(this.wallSprite, PLATFORM_START_X, GROUND_START_Y, 3);
        MyGdxGame.currentGame.spriteBatch.draw(this.greyBlockSprite, PLATFORM_START_X + (1 * GROUND_DIM), GROUND_START_Y + (1 * GROUND_DIM));
        MyGdxGame.currentGame.spriteBatch.draw(this.pedistalSprite, PLATFORM_START_X + (1 * GROUND_DIM - 5), GROUND_START_Y + (1* GROUND_DIM));
        
        // Draw the Waterfall
        MyGdxGame.currentGame.spriteBatch.draw(this.grate, PLATFORM_START_X - (3 * GROUND_DIM) + 12, GROUND_START_Y + 64);
        MyGdxGame.currentGame.spriteBatch.draw(this.grate, PLATFORM_START_X + (5 * GROUND_DIM) + 12, GROUND_START_Y + 64);
        MyGdxGame.currentGame.spriteBatch.draw(this.waterfall[animationFrame / 4 % 5], PLATFORM_START_X - (3 * GROUND_DIM) + 8, GROUND_START_Y);
        MyGdxGame.currentGame.spriteBatch.draw(this.waterfall[animationFrame / 4 % 5], PLATFORM_START_X + (5 * GROUND_DIM) + 8, GROUND_START_Y);
        
        // Draw the ground (last so it goes on top of everything.
        tileXY(this.wallSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_WIDTH, GROUND_HEIGHT);
        //tileY(this.wallSprite, GROUND_ORIGIN_X - GROUND_DIM, GROUND_START_Y - GROUND_DIM, 7);
        //tileY(this.wallSprite, GROUND_END_X, GROUND_START_Y - GROUND_DIM, 7);

        // NADIA
        // MyGdxGame.currentGame.spriteBatch.draw(this.nadiaSprite, PLATFORM_START_X - GROUND_DIM * 4, GROUND_ORIGIN_Y);
        
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
