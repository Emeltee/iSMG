package com.me.mygdxgame.screens.seeteufelscreen.maps;

import java.util.Arrays;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.LightBorder;
import com.me.mygdxgame.entities.LightPillar;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.GenericEntity;
import com.me.mygdxgame.utilities.Renderer;

public class SecondMap extends GameMap {
    
    public static final int 
        // Width of ground tile
        GROUND_DIM = 32,
        // Origin X point
        GROUND_ORIGIN_X = 0, 
        // Origin Y point
        GROUND_ORIGIN_Y = 0,
        // Rows of ground tile
        GROUND_HEIGHT = 1,
        // Columns of ground tile
        GROUND_WIDTH = 20,
        // Height of upper arena from top of shaft
        ARENA_HEIGHT = 10,
        // Ground area of upper area
        ARENA_WIDTH = 10,
        // Height of the platform the entrance is to be on.
        ENTRANCE_PLAT_HEIGHT = 3;
    
    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    public static final int
        // Where ground stops horizontally
        GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM),
        // Where ground begins vertically
        GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); 
    
    private static final Vector3 INIT_POS = 
            new Vector3(FirstMap.GROUND_DIM + 10, FirstMap.GROUND_DIM * (ENTRANCE_PLAT_HEIGHT + 1), 0);
    private static final float PARALLAX_FACTOR = 0.5f;
    
    
    private int totalPixelHeight;
    private int height;
    private Sprite wallSprite;
    private Sprite shaftBackgroundSprite;
    private Sprite arenaBackgroundSprite;
    private Sprite arenaTransitionSprite;
    private LightPillar [] pillars;
    private LightBorder upperBorder;
    private Rectangle[] obstacles = null;
    private GenericEntity returnObstacles = null;
    
    public SecondMap(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, int height) {
        
        this.height = height;
        
        this.totalPixelHeight = ARENA_HEIGHT * GROUND_DIM + height * GROUND_DIM;
        
        this.wallSprite = new Sprite(tiles.wallTex);
        this.shaftBackgroundSprite = new Sprite(tiles.smallMazeTex);
        this.shaftBackgroundSprite.setBounds(GROUND_DIM, GROUND_START_Y,
                (GROUND_WIDTH - 2) * GROUND_DIM, (this.height - 1) * GROUND_DIM);
        this.shaftBackgroundSprite.setU2(GROUND_WIDTH - 2);
        this.shaftBackgroundSprite.setV2(MyGdxGame.SCREEN_HEIGHT / SecondMap.GROUND_DIM);
        this.arenaBackgroundSprite = new Sprite(tiles.largeMazeTex);
        this.arenaBackgroundSprite.setBounds(GROUND_ORIGIN_X - GROUND_DIM * (ARENA_WIDTH - 1),
                GROUND_ORIGIN_Y + (this.height+1) * GROUND_DIM,
                (ARENA_WIDTH + GROUND_WIDTH - 1) * GROUND_DIM, (ARENA_HEIGHT - 1) * GROUND_DIM);
        this.arenaBackgroundSprite.setU2((GROUND_WIDTH + ARENA_WIDTH - 1) / (float) 2);
        this.arenaBackgroundSprite.setV2((ARENA_HEIGHT - 1) / (float) 2);
        
        this.arenaTransitionSprite = new Sprite(tiles.greyBlockTex);
        this.arenaTransitionSprite.setBounds(GROUND_ORIGIN_X, GROUND_ORIGIN_Y + this.height * GROUND_DIM,
                (GROUND_WIDTH - 1) * GROUND_DIM, GROUND_DIM);
        this.arenaTransitionSprite.setU2(GROUND_WIDTH - 1);
        this.arenaTransitionSprite.setV2(1);
           
        int arenaStartX = GROUND_ORIGIN_X - GROUND_DIM * (ARENA_WIDTH - 1);
        int arenaStartY = GROUND_ORIGIN_Y + (this.height + 1) * GROUND_DIM;      
        int borderWidth = (ARENA_WIDTH + GROUND_WIDTH - 2) * GROUND_DIM;
        
        int borderOffsetY = 10;
        this.upperBorder = new LightBorder(spriteSheet, arenaStartX,
                arenaStartY + ((ARENA_HEIGHT - 2) * GROUND_DIM) - borderOffsetY, borderWidth);
        
        this.pillars = new LightPillar[] {
                new LightPillar(spriteSheet, arenaStartX + GROUND_DIM * 1, arenaStartY,
                        (ARENA_HEIGHT - 2) * GROUND_DIM -  borderOffsetY),
                new LightPillar(spriteSheet, arenaStartX + GROUND_DIM * 4, arenaStartY,
                        (ARENA_HEIGHT - 2) * GROUND_DIM - borderOffsetY),
                new LightPillar(spriteSheet, arenaStartX + GROUND_DIM * 7, arenaStartY,
                        (ARENA_HEIGHT - 2) * GROUND_DIM - borderOffsetY)
        };
                
        // Create walls/floors.
        this.obstacles = new Rectangle [] {
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y,
                        GROUND_WIDTH * GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X, GROUND_ORIGIN_Y,
                        GROUND_DIM, this.height * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM, GROUND_ORIGIN_Y + GROUND_DIM,
                        GROUND_DIM, ENTRANCE_PLAT_HEIGHT * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * 2, GROUND_ORIGIN_Y + GROUND_DIM,
                        GROUND_DIM, ENTRANCE_PLAT_HEIGHT * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * 3, GROUND_ORIGIN_Y + GROUND_DIM,
                        GROUND_DIM, (ENTRANCE_PLAT_HEIGHT - 1) * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH-1), GROUND_ORIGIN_Y,
                        GROUND_DIM, (this.height + ARENA_HEIGHT) * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X - GROUND_DIM * ARENA_WIDTH,
                        GROUND_ORIGIN_Y + this.height * GROUND_DIM, (ARENA_WIDTH + 1) * GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X - GROUND_DIM * ARENA_WIDTH,
                        GROUND_ORIGIN_Y + this.height * GROUND_DIM, GROUND_DIM, ARENA_HEIGHT * GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH - 3),
                        GROUND_ORIGIN_Y + this.height * GROUND_DIM, GROUND_DIM * 2, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X + GROUND_DIM * (GROUND_WIDTH - 2),
                        GROUND_ORIGIN_Y + (this.height - 1) * GROUND_DIM, GROUND_DIM, GROUND_DIM),
                new Rectangle (GROUND_ORIGIN_X - GROUND_DIM * ARENA_WIDTH,
                        GROUND_ORIGIN_Y + (this.height + ARENA_HEIGHT - 1) * GROUND_DIM,
                        GROUND_DIM * (ARENA_WIDTH + GROUND_WIDTH), GROUND_DIM),
        };
        this.returnObstacles = new GenericEntity(Arrays.asList(this.obstacles));
    }
    
    @Override
    public Vector3 getInitialPosition() {
        return SecondMap.INIT_POS;
    }

    @Override
    public GameEntity getObstacles() {
        return this.returnObstacles;
    }

    @Override
    public void render(float deltaTime, Rectangle visibleRegion, Renderer renderer) {

        // Shaft background.
        float maxVisibleY = visibleRegion.getY() + visibleRegion.height; 
        float adjustedMinY = Math.max(visibleRegion.y, 0);
        float adjustedRegionHeight = visibleRegion.height;
        if (maxVisibleY > this.totalPixelHeight) {
            adjustedRegionHeight -= maxVisibleY - this.totalPixelHeight;
        }
        this.shaftBackgroundSprite.setBounds(this.shaftBackgroundSprite.getX(), adjustedMinY,
                this.shaftBackgroundSprite.getWidth(), adjustedRegionHeight);
        float adjustedBackgroundV = -visibleRegion.getY() * PARALLAX_FACTOR / GROUND_DIM;
        this.shaftBackgroundSprite.setV2(adjustedBackgroundV);
        this.shaftBackgroundSprite.setV(adjustedBackgroundV - adjustedRegionHeight / GROUND_DIM);
        renderer.drawSprite(this.shaftBackgroundSprite);
        
        // Arena background.
        if (maxVisibleY >= this.arenaTransitionSprite.getY()) {
            renderer.drawSprite(this.arenaBackgroundSprite);
            renderer.drawSprite(this.arenaTransitionSprite);
        } 

        // Walls
        for (Rectangle rect : this.obstacles) {
            this.wallSprite.setBounds(rect.x, rect.y, rect.width, rect.height);
            this.wallSprite.setU2(rect.width / GROUND_DIM);
            this.wallSprite.setV2(rect.height / GROUND_DIM);
            renderer.drawSprite(this.wallSprite);
        }
        
        // Arena pillars.
        if (maxVisibleY >= this.arenaTransitionSprite.getY()) {
            for (LightPillar pillar: this.pillars) {
                pillar.draw(renderer);
            }
        } 
        
        // Borders
        this.upperBorder.draw(renderer);
        
        // Debug
        if (this.debugMode) {
            drawObstacles(renderer, this.getObstacles().getHitArea(), GameMap.DEFAULT_OBSTACLE_COLOR);
        }
    }

    @Override
    public void update(float deltaTime) {
        for (LightPillar pillar: this.pillars) {
            pillar.update(deltaTime);
        }
      
        this.upperBorder.update(deltaTime);
    }
}
