package com.me.mygdxgame.screens.entitytestscreen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.Renderer;

/**
 * Simple {@link GameMap} used for testing {@link GameEntity} objects. Creates
 * an open area boxed by obstacles, with an initial position in its center.
 * Ignores debug mode flag, obstacles are always drawn.
 */
public class EntityTestMap extends GameMap {

    private Rectangle[] obstacles = new Rectangle[] {new Rectangle(), new Rectangle(), new Rectangle(), new Rectangle()};
    public static float DEFAULT_THICKNESS = 10;
    
    /**
     * Constructor.
     * @param width Width of open area in pixels.
     * @param height Height of open area in pixels.
     * @param obstacleThinkness The thickness of the obstacles.
     */
    public EntityTestMap(float width, float height, float thickness) {
        
        // Floor.
        obstacles[0].x = -width / 2.0f;
        obstacles[0].y = -height / 2.0f - thickness;
        obstacles[0].width = width;
        obstacles[0].height = thickness;
        
        // Ceiling.
        obstacles[1].x = obstacles[0].x;
        obstacles[1].y = height / 2.0f;
        obstacles[1].width = width;
        obstacles[1].height = thickness;
        
        // Left wall.
        obstacles[2].x = obstacles[0].x - thickness;
        obstacles[2].y = obstacles[0].y + thickness;
        obstacles[2].width = thickness;
        obstacles[2].height = height;
        
        // Right wall.
        obstacles[3].x = -obstacles[0].x;
        obstacles[3].y = obstacles[2].y;
        obstacles[3].width = thickness;
        obstacles[3].height = height;
    }
    
    
    @Override
    public void load() {
        // N/A
    }

    @Override
    public void unload() {
        // N/A
    }

    @Override
    public Vector3 getInitialPosition() {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Rectangle[] getObstacles() {
        return this.obstacles;
    }

    @Override
    public void render(float deltaTime, Renderer renderer) {
        GameMap.drawObstacles(renderer, obstacles, Color.RED);
    }

}
