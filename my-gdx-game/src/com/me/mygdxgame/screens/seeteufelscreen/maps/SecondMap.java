package com.me.mygdxgame.screens.seeteufelscreen.maps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.Renderer;

public class SecondMap extends GameMap {
    
    private int height;
    private Sprite wallSprite;
    private Sprite smallMazeSprite;;

    public static final int GROUND_DIM = 32; // Width of ground tile
    public static final int GROUND_ORIGIN_X = 0,  // Origin X point
            GROUND_ORIGIN_Y = 0, // Origin Y point
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 20; // Columns of ground tile
    
    private Rectangle[] obstacles = null;
    private static final Vector3 INIT_POS = new Vector3(FirstMap.GROUND_DIM, FirstMap.GROUND_DIM / 2, 0); 

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    public static final int GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically
    
    public SecondMap(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, int height) {
        
        this.height = height;
        
        this.wallSprite = new Sprite(tiles.wallTex);
        this.smallMazeSprite = new Sprite(tiles.smallMazeTex);
        this.smallMazeSprite.setBounds(GROUND_ORIGIN_X + GROUND_DIM, GROUND_START_Y, (GROUND_WIDTH - 2) * GROUND_DIM, (this.height - 1) * GROUND_DIM);
        this.smallMazeSprite.setU2(GROUND_WIDTH - 2);
        this.smallMazeSprite.setV2(this.height - 1);
        
        this.obstacles = new Rectangle [] {
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_WIDTH * GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM, this.height * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH-1), GROUND_ORIGIN_Y, GROUND_DIM, this.height * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y + height * GROUND_DIM, GROUND_DIM * GROUND_WIDTH, GROUND_DIM)
        };
    }
    
    @Override
    public Vector3 getInitialPosition() {
        return SecondMap.INIT_POS;
    }

    @Override
    public Rectangle[] getObstacles() {
        return this.obstacles;
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
