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
    private Sprite shaftBackgroundSprite;
    private Sprite arenaBackgroundSprite;

    public static final int GROUND_DIM = 32; // Width of ground tile
    public static final int GROUND_ORIGIN_X = 0,  // Origin X point
            GROUND_ORIGIN_Y = 0, // Origin Y point
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 20, // Columns of ground tile
            ARENA_HEIGHT = 10, // Height of upper arena from top of shaft
            ARENA_WIDTH = 10, // Ground area of upper area
            ENTRANCE_PLAT_HEIGHT = 3; // Height of the platform the entrance is to be on.
    
    private Rectangle[] obstacles = null;
    private static final Vector3 INIT_POS = new Vector3(FirstMap.GROUND_DIM + 10, FirstMap.GROUND_DIM * (ENTRANCE_PLAT_HEIGHT + 1), 0); 

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    public static final int GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically
    
    public SecondMap(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, int height) {
        
        this.height = height;
        
        this.wallSprite = new Sprite(tiles.wallTex);
        this.shaftBackgroundSprite = new Sprite(tiles.smallMazeTex);
        this.shaftBackgroundSprite.setBounds(GROUND_DIM, GROUND_START_Y, (GROUND_WIDTH - 2) * GROUND_DIM, (this.height - 1) * GROUND_DIM);
        this.shaftBackgroundSprite.setU2(GROUND_WIDTH - 2);
        this.shaftBackgroundSprite.setV2(this.height - 1);
        this.arenaBackgroundSprite = new Sprite(tiles.smallMazeTex);
        this.arenaBackgroundSprite.setBounds(GROUND_ORIGIN_X - GROUND_DIM * (ARENA_WIDTH - 1), GROUND_ORIGIN_Y + this.height * GROUND_DIM, (ARENA_WIDTH + GROUND_WIDTH - 1) * GROUND_DIM, (ARENA_HEIGHT - 1) * GROUND_DIM);
        this.arenaBackgroundSprite.setU2(GROUND_WIDTH + ARENA_WIDTH - 1);
        this.arenaBackgroundSprite.setV2(ARENA_HEIGHT - 1);
        
        // Create walls/floors.
        this.obstacles = new Rectangle [] {
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_WIDTH * GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM, this.height * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM, GROUND_ORIGIN_Y + GROUND_DIM, GROUND_DIM, ENTRANCE_PLAT_HEIGHT * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * 2, GROUND_ORIGIN_Y + GROUND_DIM, GROUND_DIM, ENTRANCE_PLAT_HEIGHT * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * 3, GROUND_ORIGIN_Y + GROUND_DIM, GROUND_DIM, (ENTRANCE_PLAT_HEIGHT - 1) * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH-1), GROUND_ORIGIN_Y, GROUND_DIM, (this.height + ARENA_HEIGHT) * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X - GROUND_DIM * ARENA_WIDTH, GROUND_ORIGIN_Y + this.height * GROUND_DIM, (ARENA_WIDTH + 1) * GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X - GROUND_DIM * ARENA_WIDTH, GROUND_ORIGIN_Y + this.height * GROUND_DIM, GROUND_DIM, ARENA_HEIGHT * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH - 3), GROUND_ORIGIN_Y + this.height * GROUND_DIM, GROUND_DIM * 2, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH - 2), GROUND_ORIGIN_Y + (this.height - 1) * GROUND_DIM, GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X - GROUND_DIM * ARENA_WIDTH, GROUND_ORIGIN_Y + (this.height + ARENA_HEIGHT - 1) * GROUND_DIM, GROUND_DIM * (ARENA_WIDTH + GROUND_WIDTH), GROUND_DIM),
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
        renderer.drawSprite(this.shaftBackgroundSprite);
        renderer.drawSprite(this.arenaBackgroundSprite);

        // Walls
        for (Rectangle rect : this.obstacles) {
            this.wallSprite.setBounds(rect.x, rect.y, rect.width, rect.height);
            this.wallSprite.setU2(rect.width / GROUND_DIM);
            this.wallSprite.setV2(rect.height / GROUND_DIM);
            renderer.drawSprite(this.wallSprite);
        }
        
        // Debug
        if (this.debugMode) {
            drawObstacles(renderer, this.getObstacles(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }
    }

}
