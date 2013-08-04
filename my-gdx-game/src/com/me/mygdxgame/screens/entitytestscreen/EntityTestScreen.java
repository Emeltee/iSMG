package com.me.mygdxgame.screens.entitytestscreen;

import java.util.ArrayDeque;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.Bomb;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameScreen;
import com.me.mygdxgame.utilities.GameState;

/**
 * {@link GameScreen} used to test {@link GameEntity} objects. Uses {@link EntityTestMap}.
 */
public class EntityTestScreen implements GameScreen {

    private int width = 0;
    private int height = 0;
    private ArrayDeque<GameEntity> entities = new ArrayDeque<GameEntity>();
    private ArrayDeque<GameEntity> toRemove = new ArrayDeque<GameEntity>();
    private ArrayDeque<GameEntity[]> toAdd = new ArrayDeque<GameEntity[]>();
    private EntityTestMap map = null;
    
    /**
     * Constructor.
     * 
     * @param width Test area width in pixels.
     * @param height Test area height in pixels.
     */
    public EntityTestScreen(int width, int height) {
        
        this.map = new EntityTestMap(width, height, EntityTestMap.DEFAULT_THICKNESS);
    }
    
    /**
     * Add a {@link GameEntity} to the Screen.
     * 
     * @param entity GameEntity to add.
     */
    public void addEntity(GameEntity entity) {
        this.entities.push(entity);
    }
    
    /**
     * Retrieves the number of {@link GameEntity} objects in this Screen.
     * 
     * @return The number of GameEntity objects in this Screen;
     */
    public int getEntityCount() {
        return this.entities.size();
    }
    
    /**
     * Retrieves the list of obstacles from the underlying {@link GameMap}.
     * @return A list of Rectangles representing the obstacles in the Screen's GameMap.
     */
    public Rectangle[] getObstacles() {
        return this.map.getObstacles();
    }
    
    /**
     * Retrieves the initial position from the underlying {@link GameMap}.
     * @return A Vector3 containing the initial position in the Screen's GameMap.
     */
    public Vector3 getInitialPosition() {
        return this.map.getInitialPosition();
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
    public void render(float deltaTime, int difficulty) {
        
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // Update and draw all current entities.
        for (GameEntity e : this.entities) {
            
            e.update(deltaTime);
            e.draw();
            
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.push(e);
                if (e instanceof Bomb && ((Bomb)e).hasCreatedEntities()){
                    for (GameEntity f: e.getCreatedEntities()) {
                        this.entities.add(f);
                    }
                }
            }
            if (e.hasCreatedEntities()) {
                this.toAdd.push(e.getCreatedEntities());
            }
        }
        
        // Remove or add to entity list as needed.
        this.entities.removeAll(this.toRemove);
        for (GameEntity[] newEntities : this.toAdd) {
            for (GameEntity e : newEntities) {
                this.entities.add(e);
            }
        }
        this.toAdd.clear();
        this.toRemove.clear();

        // Render obstacles.
        this.map.render(deltaTime);
    }

    @Override
    public void initialize() {
        // Initialize camera position.
        MyGdxGame.currentGame.perspectiveCamera.position.set(0, 0, 256);
    }

    @Override
    public GameState getState() {
        // Test screen, run forever.
        return GameState.Running;
    }

}
