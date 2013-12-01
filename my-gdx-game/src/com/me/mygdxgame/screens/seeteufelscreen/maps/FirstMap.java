package com.me.mygdxgame.screens.seeteufelscreen.maps;

import java.util.Arrays;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.StonePillar;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.GenericEntity;
import com.me.mygdxgame.utilities.Renderer;

/**
 * A {@link GameMap} used by {@link SeeteufelScreen}.
 */
public class FirstMap extends GameMap {
    
    private int animationFrame = 0;
    private float animationTimer = 0;
    
    private Sprite rockSprite;
    private Sprite wallSprite;
    private Sprite smallMazeSprite;
    private TextureRegion greyBlockRegion;
    private TextureRegion pedistalRegion;
    private Sprite pillarSprite;

    private TextureRegion grateRegion;
    private TextureRegion[] waterfall;
    private StonePillar [] stonePillars;
    
    private Rectangle[] obstacles = null;
    private GenericEntity returnObstacles = null;

    // Width of ground tile
    public static final int GROUND_DIM = 32;

    public static final int 
            // Origin X point, off-screen to the left
            GROUND_ORIGIN_X = (-1 * MyGdxGame.SCREEN_WIDTH / 2),
            // Origin Y point, off-screen below.
            GROUND_ORIGIN_Y = (-1 * MyGdxGame.SCREEN_HEIGHT / 2),
            // Rows of ground tile.
            GROUND_HEIGHT = 1,
            // Columns of ground tile.
            GROUND_WIDTH = 26,
            // Height of room in tiles.
            ROOM_HEIGHT = 7;

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    public static final int
            // Where ground stops horizontally
            GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM),
            // Where ground begins vertically
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM);

    public static final int PLATFORM_START_X = GROUND_ORIGIN_X + (7 * GROUND_DIM);
    
    public static final Vector3 INIT_POS = new Vector3(GROUND_END_X - GROUND_DIM, GROUND_START_Y, 0);

    public FirstMap(Texture spriteSheet, SeeteufelScreen.MapTiles tiles) {
        
        this.rockSprite = new Sprite(tiles.rockTex);
        this.rockSprite.setBounds(GROUND_ORIGIN_X, GROUND_START_Y + (ROOM_HEIGHT * GROUND_DIM),
                GROUND_WIDTH * GROUND_DIM, GROUND_DIM);
        this.rockSprite.setU2(GROUND_WIDTH);
        this.wallSprite = new Sprite(tiles.wallTex);
        this.smallMazeSprite = new Sprite(tiles.smallMazeTex);
        this.smallMazeSprite.setBounds(GROUND_ORIGIN_X, GROUND_START_Y,
                GROUND_WIDTH * GROUND_DIM, ROOM_HEIGHT * GROUND_DIM);
        this.smallMazeSprite.setU2(GROUND_WIDTH);
        this.smallMazeSprite.setV2(ROOM_HEIGHT);
        this.pillarSprite = new Sprite(tiles.pillarTex);
        this.pillarSprite.setSize(32, 36 * 6);
        this.pillarSprite.setV2(ROOM_HEIGHT);
        
        // This could be a sprite but diagonalLeft/Right need to be updated..
        this.greyBlockRegion = new TextureRegion(tiles.greyBlockTex, 0, 0, GROUND_DIM, GROUND_DIM);
        this.pedistalRegion = new TextureRegion(spriteSheet, 165, 0, 55, 17);
        this.grateRegion = new TextureRegion(spriteSheet, 22, 102, 23, 23);
        this.waterfall = new TextureRegion[5];
        this.waterfall[0] = new TextureRegion(spriteSheet, 0, 125, 31, 84);
        this.waterfall[1] = new TextureRegion(spriteSheet, 32, 125, 31, 84);
        this.waterfall[2] = new TextureRegion(spriteSheet, 64, 125, 31, 84);
        this.waterfall[3] = new TextureRegion(spriteSheet, 96, 125, 31, 84);
        this.waterfall[4] = new TextureRegion(spriteSheet, 128, 125, 31, 84);
        
        // Obstacles.
        this.obstacles = new Rectangle[] {
            // Ground
            new Rectangle(GROUND_ORIGIN_X, GROUND_ORIGIN_Y, GROUND_DIM * GROUND_WIDTH, GROUND_DIM * GROUND_HEIGHT),
            // Platform I
            new Rectangle(PLATFORM_START_X - GROUND_DIM, GROUND_START_Y, GROUND_DIM * 5, GROUND_DIM),
            // Platform II
            new Rectangle(PLATFORM_START_X, GROUND_START_Y + GROUND_DIM, GROUND_DIM * 3, GROUND_DIM),
            // Goal
            new Rectangle(PLATFORM_START_X + 20, GROUND_START_Y + 2 * GROUND_DIM,
                    this.pedistalRegion.getRegionWidth(), (int)this.pedistalRegion.getRegionHeight()),
            // Ceiling
            new Rectangle(GROUND_ORIGIN_X, GROUND_START_Y + (7 * GROUND_DIM), GROUND_DIM * GROUND_WIDTH, GROUND_DIM ),
            // Left boundary
            new Rectangle(GROUND_ORIGIN_X - GROUND_DIM, GROUND_START_Y - GROUND_DIM, GROUND_DIM, GROUND_DIM * ROOM_HEIGHT),
            // Right boundary
            new Rectangle(GROUND_END_X, GROUND_START_Y - GROUND_DIM, GROUND_DIM, GROUND_DIM * ROOM_HEIGHT)
        };
        this.returnObstacles = new GenericEntity(Arrays.asList(this.obstacles));
        

        this.stonePillars = new StonePillar[10];
        for (int i = 0; i < 2; i++) {
            this.stonePillars[i] = new StonePillar(spriteSheet, tiles,
                    GROUND_ORIGIN_X + (2*i+1) * (GROUND_DIM/2) - (StonePillar.PILLAR_BASE_W / 2),
                    GROUND_START_Y, 7*GROUND_DIM);
        }
        for (int i = 0; i < 8; i++) {
            this.stonePillars[i + 2] = new StonePillar(spriteSheet, tiles,
                    PLATFORM_START_X + (int)(GROUND_DIM * 8.5) - (StonePillar.PILLAR_BASE_W / 2) + (GROUND_DIM * i),
                    GROUND_START_Y, 7*GROUND_DIM);
        }
    }
    
    @Override
    public Vector3 getInitialPosition() {
        return FirstMap.INIT_POS;
    }

    @Override
    public GameEntity getObstacles() {
        return this.returnObstacles;
    }

    @Override
    public void render(float deltaTime, Rectangle visibleRegion, Renderer renderer) {
        
        // Main background.
        renderer.drawSprite(this.smallMazeSprite);
        
        // Alternate background for refractor.
        diagonalRight(renderer, this.greyBlockRegion, PLATFORM_START_X - (3 * GROUND_DIM), GROUND_START_Y, 5);
        diagonalLeft(renderer, this.greyBlockRegion, PLATFORM_START_X + (5 * GROUND_DIM), GROUND_START_Y, 4);
        
        // Ceiling and floor.
        renderer.drawSprite(this.rockSprite);
        this.wallSprite.setBounds(GROUND_ORIGIN_X, GROUND_START_Y - GROUND_DIM, GROUND_WIDTH * GROUND_DIM, GROUND_DIM);
        this.wallSprite.setU2(GROUND_WIDTH);
        renderer.drawSprite(this.wallSprite);
        
        // Refractor and platform.
        this.wallSprite.setBounds(PLATFORM_START_X - GROUND_DIM, GROUND_START_Y, 5 * GROUND_DIM, GROUND_DIM);
        this.wallSprite.setU2(5);
        renderer.drawSprite(this.wallSprite);
        this.wallSprite.setBounds(PLATFORM_START_X, GROUND_START_Y + GROUND_DIM, 3 * GROUND_DIM, GROUND_DIM);
        this.wallSprite.setU2(3);
        renderer.drawSprite(this.wallSprite);
        renderer.drawRegion(this.greyBlockRegion, PLATFORM_START_X + GROUND_DIM, GROUND_START_Y + 2 * GROUND_DIM);
        renderer.drawRegion(this.pedistalRegion, PLATFORM_START_X + 20, GROUND_START_Y + GROUND_DIM * 2);
        
        // Draw the waterfalls.
        renderer.drawRegion(this.grateRegion, PLATFORM_START_X - (int)(4.5 * GROUND_DIM) + 12, GROUND_START_Y + 64);
        renderer.drawRegion(this.grateRegion, PLATFORM_START_X + (int)(6 * GROUND_DIM) + 12, GROUND_START_Y + 64);
        renderer.drawRegion(this.waterfall[animationFrame], PLATFORM_START_X - (int)(4.5 * GROUND_DIM) + 8, GROUND_START_Y);
        renderer.drawRegion(this.waterfall[animationFrame], PLATFORM_START_X + (int)(6 * GROUND_DIM) + 8, GROUND_START_Y);
        
        for (StonePillar pillar: this.stonePillars) {
            pillar.renderBody(renderer);
        }
        for (StonePillar pillar: this.stonePillars) {
            pillar.renderBases(renderer);
        }
        
        // Debug
        if (this.debugMode) {
            drawObstacles(renderer, this.getObstacles().getHitArea(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }
    }

    @Override
    public void update(float deltaTime) {
        // Update animation frame
        animationTimer += deltaTime;
        if (animationTimer >= 0.1) {
            animationFrame = (animationFrame + 1) % 5;
            animationTimer = 0;
        }
    }
}
