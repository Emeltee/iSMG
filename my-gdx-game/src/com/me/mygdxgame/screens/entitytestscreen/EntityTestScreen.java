package com.me.mygdxgame.screens.entitytestscreen;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameScreen;
import com.me.mygdxgame.utilities.GameState;
import com.me.mygdxgame.utilities.Renderer;

/**
 * {@link GameScreen} used to test {@link GameEntity} objects. Uses {@link EntityTestMap}.
 */
public class EntityTestScreen implements GameScreen {
    
    private static Vector3 CAM_INITIAL_POSITION = new Vector3(0, 0, 256);

    private Vector3 camPos = new Vector3(EntityTestScreen.CAM_INITIAL_POSITION);
    private LinkedList<GameEntity> entities = new LinkedList<GameEntity>();
    private LinkedList<GameEntity> toRemove = new LinkedList<GameEntity>();
    private LinkedList<GameEntity[]> toAdd = new LinkedList<GameEntity[]>();
    private EntityTestMap map = null;
    private Rectangle visibleArea = new Rectangle(-1000, -1000, 2000, 2000);
    
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
        this.entities.addFirst(entity);
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
     * 
     * @return A GameEntity whose hit areas represent the obstacles in the
     *         Screen's GameMap.
     */
    public GameEntity getObstacles() {
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
    public void render(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        
        // Camera controls. Moves at 180 units a second.
        if (Gdx.input.isKeyPressed(Keys.A)) {
            this.camPos.x -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            this.camPos.x += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            this.camPos.y -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            this.camPos.y += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.E)) {
            this.camPos.z -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.Q)) {
            this.camPos.z += (180 * deltaTime);
        }
        perspCam.position.set(this.camPos);
        perspCam.update();
        
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // Create Renderer.
        Renderer renderer = new Renderer(perspCam.combined);
        
        // Update and draw all current entities.
        for (GameEntity e : this.entities) {
            
            e.update(deltaTime);
            e.draw(renderer);
            
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.addFirst(e);              
            }
            if (e.hasCreatedEntities()) {
                this.toAdd.addFirst(e.getCreatedEntities());
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
        this.map.render(deltaTime, visibleArea, renderer);
    }

    @Override
    public void initialize() {
        this.camPos.set(EntityTestScreen.CAM_INITIAL_POSITION);
    }

    @Override
    public GameState getState() {
        // Test screen, run forever.
        return GameState.Running;
    }

}
