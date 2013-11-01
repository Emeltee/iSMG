package com.me.mygdxgame.screens.seeteufelscreen.maps;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.GenericEntity;
import com.me.mygdxgame.utilities.Renderer;

public class ThirdMap extends GameMap {

    private int animationFrame;
    private boolean debugMode;
    
    private TextureRegion rockSprite;
    private TextureRegion wallSprite;
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
    private final int 
            GROUND_ORIGIN_X = 0,  // Origin X point
            GROUND_ORIGIN_Y = 0, // Origin Y point
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 9; // Columns of ground tile

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    private final int 
            GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically
    
    public ThirdMap(Texture spriteSheet, SeeteufelScreen.MapTiles tiles) {
        
        this.rockSprite = new TextureRegion(spriteSheet, 0, 57, GROUND_DIM, GROUND_DIM);
        this.wallSprite = new TextureRegion(spriteSheet, 210, 17, GROUND_DIM, GROUND_DIM);
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
    }

    @Override
    public Vector3 getInitialPosition() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GameEntity getObstacles() {
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
        return new GenericEntity(Arrays.asList(result));
    }

    @Override
    public void render(float deltaTime, Renderer renderer) {
        
        // Draw the bg
        tileXY(renderer, this.largeMazeSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, 5, 3);
        
        // Draw the border
        tileX(renderer, this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 5*GROUND_DIM-5, GROUND_START_Y, 14);
        tileX(renderer, this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 3*GROUND_DIM, GROUND_START_Y + GROUND_DIM, 9);
        tileX(renderer, this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 2*GROUND_DIM-5, GROUND_START_Y + 2*GROUND_DIM, 5);
        tileX(renderer, this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 1*GROUND_DIM-5, GROUND_START_Y + 3*GROUND_DIM, 5);
         
        // tileX(this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + 9 * GROUND_DIM, GROUND_START_Y, 22);
        
        tileX(renderer, this.borderFrames[animationFrame / 6 % 8], GROUND_ORIGIN_X + GROUND_DIM, GROUND_START_Y + 7 * GROUND_DIM - 10, 54);
        
        // Draw some light pillars
        tileY(renderer, lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 3 + 10, 18);
        renderer.drawRegion(this.lightPillarBottomBase, GROUND_ORIGIN_X + GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 3);
        renderer.drawRegion(this.lightPillarTopBase, GROUND_ORIGIN_X + GROUND_DIM + 5, GROUND_START_Y + 7 * GROUND_DIM - 10);
        
        tileY(renderer, lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + 3*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 1 + 10, 24);
        renderer.drawRegion(this.lightPillarBottomBase, GROUND_ORIGIN_X + 3*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 1);
        renderer.drawRegion(this.lightPillarTopBase, GROUND_ORIGIN_X + 3*GROUND_DIM + 5, GROUND_START_Y + 7 * GROUND_DIM - 10);
        
        tileY(renderer, lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + 5*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0 + 10, 28);
        renderer.drawRegion(this.lightPillarBottomBase, GROUND_ORIGIN_X + 5*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0);
        renderer.drawRegion(this.lightPillarTopBase, GROUND_ORIGIN_X + 5*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 7 - 10);

        tileY(renderer, lightPillar[animationFrame / 6 % 6], GROUND_ORIGIN_X + 7*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0 + 10, 28);
        renderer.drawRegion(this.lightPillarBottomBase, GROUND_ORIGIN_X + 7*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 0);
        renderer.drawRegion(this.lightPillarTopBase, GROUND_ORIGIN_X + 7*GROUND_DIM + 5, GROUND_START_Y + GROUND_DIM * 7 - 10);
        
        // Draw /a Waterfall
        renderer.drawRegion(this.idkSprite, GROUND_ORIGIN_X + 9 * GROUND_DIM + 10, GROUND_START_Y + 12);
        renderer.drawRegion(this.grate, (int)Math.ceil(GROUND_ORIGIN_X + 9.5 * GROUND_DIM), GROUND_START_Y + 20);
        renderer.drawRegion(this.waterfall[animationFrame / 4 % 5], (int)Math.ceil(GROUND_ORIGIN_X + 9.5 * GROUND_DIM -4), GROUND_ORIGIN_Y);
        
        renderer.drawRegion(this.idkSprite, GROUND_ORIGIN_X + 11 * GROUND_DIM + 11, GROUND_START_Y + 12);
        renderer.drawRegion(this.grate, (int)(GROUND_ORIGIN_X + 11.5 * GROUND_DIM), GROUND_START_Y + 20);
        renderer.drawRegion(this.waterfall[animationFrame / 4 % 5], (int)Math.ceil(GROUND_ORIGIN_X + 11.5 * GROUND_DIM -4), GROUND_ORIGIN_Y);
        
        // Draw the stage on top of everything.
        tileX(renderer, this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, 8);
        tileX(renderer, this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + GROUND_DIM, 5);
        tileX(renderer, this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + 2*GROUND_DIM, 3);
        tileX(renderer, this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + 3* GROUND_DIM, 2);
        tileX(renderer, this.wallSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y + 8 * GROUND_DIM, 13);
        tileY(renderer, this.wallSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, 9);
        tileY(renderer, this.wallSprite, GROUND_ORIGIN_X + 13 * GROUND_DIM, GROUND_ORIGIN_Y, 9);
        
        // Debug
        if (this.debugMode) {
            drawObstacles(renderer, this.getObstacles().getHitArea(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }
    }

    @Override
    public void update(float deltaTime) {
        // Update animation frame
        animationFrame = (animationFrame < 96) ? animationFrame + 1 : 0;
    }

}
