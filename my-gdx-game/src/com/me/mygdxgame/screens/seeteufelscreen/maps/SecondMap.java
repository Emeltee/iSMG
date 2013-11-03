package com.me.mygdxgame.screens.seeteufelscreen.maps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.LightBorder;
import com.me.mygdxgame.entities.LightPillar;
import com.me.mygdxgame.entities.StonePillar;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.GenericEntity;
import com.me.mygdxgame.utilities.Renderer;
import com.me.mygdxgame.utilities.Updatable;

public class SecondMap extends GameMap {
    
    private int height;
    private Sprite wallSprite;
    private Sprite shaftBackgroundSprite;
    private Sprite arenaBackgroundSprite;
    private Sprite arenaTransitionSprite;
    private LightPillar [] pillars;
    private List<Updatable> stairPillars;
    private LightBorder upperBorder;
    private Texture spriteSheet;
    private SeeteufelScreen.MapTiles tiles;

    public static final int GROUND_DIM = 32; // Width of ground tile
    public static final int GROUND_ORIGIN_X = 0,  // Origin X point
            GROUND_ORIGIN_Y = 0, // Origin Y point
            GROUND_HEIGHT = 1, // Rows of ground tile
            GROUND_WIDTH = 20, // Columns of ground tile
            ARENA_HEIGHT = 10, // Height of upper arena from top of shaft
            ARENA_WIDTH = 10, // Ground area of upper area
            ENTRANCE_PLAT_HEIGHT = 3; // Height of the platform the entrance is to be on.
    
    private Rectangle[] obstacles = null;
    private GenericEntity returnObstacles = null;
    private static final Vector3 INIT_POS = new Vector3(FirstMap.GROUND_DIM + 10, FirstMap.GROUND_DIM * (ENTRANCE_PLAT_HEIGHT + 1), 0); 

    // Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y)
    public static final int GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
            GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically
    
    public SecondMap(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, int height) {
        
        this.spriteSheet = spriteSheet;
        this.tiles = tiles;
        this.height = height;
        
        this.wallSprite = new Sprite(tiles.wallTex);
        this.shaftBackgroundSprite = new Sprite(tiles.smallMazeTex);
        this.shaftBackgroundSprite.setBounds(GROUND_DIM, GROUND_START_Y, (GROUND_WIDTH - 2) * GROUND_DIM, (this.height - 1) * GROUND_DIM);
        this.shaftBackgroundSprite.setU2(GROUND_WIDTH - 2);
        this.shaftBackgroundSprite.setV2(this.height - 1);
        this.arenaBackgroundSprite = new Sprite(tiles.largeMazeTex);
        this.arenaBackgroundSprite.setBounds(GROUND_ORIGIN_X - GROUND_DIM * (ARENA_WIDTH - 1), GROUND_ORIGIN_Y + (this.height+1) * GROUND_DIM, (ARENA_WIDTH + GROUND_WIDTH - 1) * GROUND_DIM, (ARENA_HEIGHT - 1) * GROUND_DIM);
        this.arenaBackgroundSprite.setU2((GROUND_WIDTH + ARENA_WIDTH - 1) / (float) 2);
        this.arenaBackgroundSprite.setV2((ARENA_HEIGHT - 1) / (float) 2);
        
        this.arenaTransitionSprite = new Sprite(tiles.greyBlockTex);
        this.arenaTransitionSprite.setBounds(GROUND_ORIGIN_X, GROUND_ORIGIN_Y + this.height * GROUND_DIM, (GROUND_WIDTH - 1) * GROUND_DIM, GROUND_DIM);
        this.arenaTransitionSprite.setU2(GROUND_WIDTH - 1);
        this.arenaTransitionSprite.setV2(1);
           
        int arenaStartX = GROUND_ORIGIN_X - GROUND_DIM * (ARENA_WIDTH - 1);
        int arenaStartY = GROUND_ORIGIN_Y + (this.height + 1) * GROUND_DIM;      
        int borderWidth = (ARENA_WIDTH + GROUND_WIDTH - 2) * GROUND_DIM;
        
        int borderOffsetY = 10;
        
        this.upperBorder = new LightBorder(spriteSheet, arenaStartX, arenaStartY + ((ARENA_HEIGHT - 2) * GROUND_DIM) - borderOffsetY, borderWidth);
        
        this.pillars = new LightPillar[] {
                new LightPillar(spriteSheet, arenaStartX + GROUND_DIM * 1, arenaStartY, (ARENA_HEIGHT - 2) * GROUND_DIM - (1 * borderOffsetY)),
                new LightPillar(spriteSheet, arenaStartX + GROUND_DIM * 4, arenaStartY, (ARENA_HEIGHT - 2) * GROUND_DIM - (1 * borderOffsetY)),
                new LightPillar(spriteSheet, arenaStartX + GROUND_DIM * 7, arenaStartY, (ARENA_HEIGHT - 2) * GROUND_DIM - (1 * borderOffsetY))
        };
        
        this.stairPillars = this.makeStairPillars();
        
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
    public void render(float deltaTime, Renderer renderer) {

        // Background.
        renderer.drawSprite(this.shaftBackgroundSprite);
        renderer.drawSprite(this.arenaBackgroundSprite);
        renderer.drawSprite(this.arenaTransitionSprite);

        // Walls
        for (Rectangle rect : this.obstacles) {
            this.wallSprite.setBounds(rect.x, rect.y, rect.width, rect.height);
            this.wallSprite.setU2(rect.width / GROUND_DIM);
            this.wallSprite.setV2(rect.height / GROUND_DIM);
            renderer.drawSprite(this.wallSprite);
        }
        
        // Pillars
        for (LightPillar pillar: this.pillars) {
            pillar.draw(renderer);
        }
        
        /*
        for (Updatable pillar: this.stairPillars) {
            pillar.draw(renderer);
        }*/
        
        //this.drawStopGaps(renderer, this.tiles);
        
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
        /*
        for (Updatable pillar: this.stairPillars) {
            pillar.update(deltaTime);
        }*/
    }
    
    private List<Updatable> makeStairPillars() {
        List<Updatable> result = new ArrayList<Updatable>();
        float beginY = 9.5f;
        float stairH = 4.5f;
        float height = this.height - beginY - stairH;
        
        int pHeight1 = 2 * GROUND_DIM;
        int pHeight2 = 3 * GROUND_DIM;
        int pHeight3 = 4 * GROUND_DIM;
        int pHeight4 = 7 * GROUND_DIM;
        
        int xRight = 7 * GROUND_DIM;
        int xLeft = (GROUND_WIDTH - 7) * GROUND_DIM;
        int xOffset = 3 * GROUND_DIM;
        int yOffset = GROUND_DIM / 2;
        
        int stoneOffset = (GROUND_DIM - new StonePillar(this.spriteSheet, this.tiles, -100, -100, 0).getWidth()) / 2;
        
        float pos = beginY;
        while (pos <= height) {
            int currentY = (int)(pos * GROUND_DIM);
            // Right side pillars
            result.add(new StonePillar(this.spriteSheet, this.tiles, xRight + (0 * xOffset) + stoneOffset, currentY + (0 * yOffset), pHeight3));
            result.add(new LightPillar(this.spriteSheet, xRight + (1 * xOffset), currentY + (1 * yOffset), pHeight2));
            result.add(new StonePillar(this.spriteSheet, this.tiles, xRight + (2 * xOffset) + stoneOffset, currentY + (2 * yOffset), pHeight1));
            result.add(new LightPillar(this.spriteSheet, xRight + (3 * xOffset) + GROUND_DIM, currentY + (3 * yOffset), pHeight4));
                        
            currentY += stairH * GROUND_DIM;
            
            // Left side pillars
            result.add(new LightPillar(this.spriteSheet, xLeft - (0 * xOffset), currentY - (1 * yOffset), pHeight3));
            result.add(new StonePillar(this.spriteSheet, this.tiles, xLeft - (1 * xOffset) + stoneOffset, currentY + (0 * yOffset), pHeight2));
            result.add(new LightPillar(this.spriteSheet, xLeft - (2 * xOffset), currentY + (1 * yOffset), pHeight1));
            result.add(new StonePillar(this.spriteSheet, this.tiles, xLeft - (3 * xOffset) - 2*GROUND_DIM + stoneOffset, currentY + (3 * yOffset), pHeight4));
            
            pos += 2 * stairH - 1;
        }
        
        
        return result;
    }
    
    private void drawStopGaps(Renderer renderer, SeeteufelScreen.MapTiles tiles) {
    	Sprite gapSprite = new Sprite(tiles.greyBlockTex);
    	for (Updatable u: this.stairPillars) {
    		int gapY = (int)u.getPosition().y + u.getHeight();
    		int gapX = (int)u.getPosition().x;
    		gapX -= gapX % GROUND_DIM;
    		gapSprite.setBounds(gapX, gapY, gapSprite.getWidth(), gapSprite.getHeight());
    		gapSprite.setU2(1);
    		gapSprite.setV2(1);
    		renderer.drawSprite(gapSprite);
    	}
    }

}
