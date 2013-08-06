package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.ArrayDeque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.Door;
import com.me.mygdxgame.entities.MegaHealthBar;
import com.me.mygdxgame.entities.Refractor;
import com.me.mygdxgame.screens.seeteufelscreen.maps.FirstMap;
import com.me.mygdxgame.screens.seeteufelscreen.maps.SecondMap;
import com.me.mygdxgame.screens.seeteufelscreen.maps.ThirdMap;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameMap;
import com.me.mygdxgame.utilities.GameScreen;
import com.me.mygdxgame.utilities.GameState;

/**
 * TODO
 */
public class SeeteufellScreen implements GameScreen {
    
    // Edge constants, for use only in 2D
    private int SCREEN_ORIGIN = 0;    
    private int SCREEN_TOP = MyGdxGame.currentGame.SCREEN_HEIGHT / 2;
    private int SCREEN_BOTTOM = -MyGdxGame.currentGame.SCREEN_HEIGHT / 2;
    private int SCREEN_LEFT = -MyGdxGame.currentGame.SCREEN_WIDTH / 2;
    private int SCREEN_RIGHT = MyGdxGame.currentGame.SCREEN_WIDTH / 2;    
    
    // Constant locations for the texture paths, just because
    private static final String PATH_TILES_1 = "img/seeTiles1.png";
    private static final String PATH_TILES_2 = "img/seeTiles2.png";
    private static final String PATH_PLAYER = "img/mmd.png";
    private static final String PATH_SEETEUFEL = "img/seeteufel.png";
        
    // Each of the maps and a holder for the current map
    private FirstMap map1;
    private SecondMap map2;
    private ThirdMap map3;
    private GameMap currentMap;
    
    // Texture files for building sprites
    private Texture t_tiles1;
    private Texture t_tiles2;
    private Texture t_player;
    private Texture t_seeteufel;    
    
    // Containers for managing entities generically. Keep a pointer to any entities that need specialized mgmt
    private ArrayDeque<GameEntity> entities = new ArrayDeque<GameEntity> ();
    private ArrayDeque<GameEntity> toRemove = new ArrayDeque<GameEntity> ();
    private ArrayDeque<GameEntity[]> toAdd = new ArrayDeque<GameEntity[]> ();
    
    // Common Entities for entire program
    private MegaPlayer player;
    private MegaHealthBar playerHealth;
    
    public SeeteufellScreen() {
        this.map1 = new FirstMap();
        //this.map2 = new SecondMap();
        //this.map3 = new ThirdMap();
        this.currentMap = this.map1;
    }
    
    @Override
    public void load() {
        this.t_tiles1 = new Texture(PATH_TILES_1);
        this.t_tiles2 = new Texture(PATH_TILES_2);
        this.t_player = new Texture(PATH_PLAYER);
        this.t_seeteufel = new Texture(PATH_SEETEUFEL);
        
        this.currentMap.load();
        
        // TODO Move Entity creation back to initialize once it's fixed
        this.player = new MegaPlayer(this.t_player, this.t_tiles1, this.currentMap.getInitialPosition(), this.getObstacles(), this.getTargets());
        this.playerHealth = new MegaHealthBar(this.t_tiles2, SCREEN_LEFT + MegaHealthBar.CHROME_W + 20, SCREEN_BOTTOM + MegaHealthBar.CHROME_H + 20, player);        
        this.entities.add(player);
        this.entities.add(playerHealth);
    }

    @Override
    public void unload() {
        // Dispose of Textures
        this.t_tiles1.dispose();
        this.t_tiles2.dispose();
        this.t_player.dispose();
        this.t_seeteufel.dispose();
        
        // Unload Maps
        this.map1.unload();
        this.map2.unload();
        this.map3.unload();
    }

    @Override
    public void render(float deltaTime, int difficulty) {
     // Camera controls. Moves at 180 units a second.
        if (Gdx.input.isKeyPressed(Keys.A)) {
            MyGdxGame.currentGame.perspectiveCamera.position.x -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            MyGdxGame.currentGame.perspectiveCamera.position.x += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            MyGdxGame.currentGame.perspectiveCamera.position.y -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.W)) {
            MyGdxGame.currentGame.perspectiveCamera.position.y += (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.E)) {
            MyGdxGame.currentGame.perspectiveCamera.position.z -= (180 * deltaTime);
        }
        if (Gdx.input.isKeyPressed(Keys.Q)) {
            MyGdxGame.currentGame.perspectiveCamera.position.z += (180 * deltaTime);
        }
        MyGdxGame.currentGame.perspectiveCamera.update();        
        
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);   
        
        // Draw the map
        this.currentMap.render(deltaTime);
                
        // Update and draw all current entities.
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            e.draw();            
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.push(e);              
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
    }

    @Override
    public void initialize() {
        // Initialize camera position.
        MyGdxGame.currentGame.perspectiveCamera.position.set(0, 0, 256);
    }

    @Override
    public GameState getState() {
        return GameState.Running;
    }

    public Rectangle [] getObstacles() {
        return this.currentMap.getObstacles();
    }
    
    public Damageable [] getTargets() {
        // Grab all the Damageabls out of entities, store in a temporary ArrayDeque
        ArrayDeque<Damageable> result = new ArrayDeque<Damageable> ();
        for (GameEntity e: this.entities) {
            if (e instanceof Damageable) {
                result.add((Damageable) e);
            }
        }
        
        return result.toArray(new Damageable[] {});
    }
    
}
