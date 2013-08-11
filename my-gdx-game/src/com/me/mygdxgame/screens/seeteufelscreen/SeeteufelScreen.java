package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.ArrayDeque;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.Door;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.entities.Refractor;
import com.me.mygdxgame.entities.SeeteufelFront;
import com.me.mygdxgame.entities.WatchNadia;
import com.me.mygdxgame.entities.progressbars.MegaHealthBar;
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
public class SeeteufelScreen implements GameScreen {
    
    // Edge constants, for use only in 2D
    private static final int SCREEN_ORIGIN = 0;    
    private static final int SCREEN_TOP = MyGdxGame.currentGame.SCREEN_HEIGHT / 2;
    private static final int SCREEN_BOTTOM = -MyGdxGame.currentGame.SCREEN_HEIGHT / 2;
    private static final int SCREEN_LEFT = -MyGdxGame.currentGame.SCREEN_WIDTH / 2;
    private static final int SCREEN_RIGHT = MyGdxGame.currentGame.SCREEN_WIDTH / 2;
    private static final Vector2 PLAYER_HEALTH_POS = new Vector2(SCREEN_LEFT + 10, SCREEN_BOTTOM + 10);
    private static int MAP1_CAM_Y = SeeteufelScreen.SCREEN_BOTTOM / 5;
    
    // Constant locations for the texture paths, just because
    private static final String PATH_TILES_1 = "img/seeTiles1.png";
    private static final String PATH_TILES_2 = "img/seeTiles2.png";
    private static final String PATH_PLAYER = "img/mmd.png";
    private static final String PATH_SEETEUFEL = "img/seeteufel.png";
    
    // State.
    private GameState state = GameState.Running;
        
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
    private MegaPlayer.MegaPlayerResources playerResources = new MegaPlayer.MegaPlayerResources();
    
    // Containers for managing entities generically. Keep a pointer to any entities that need specialized mgmt
    private ArrayDeque<GameEntity> entities = new ArrayDeque<GameEntity> ();
    private ArrayDeque<GameEntity> toRemove = new ArrayDeque<GameEntity> ();
    private ArrayDeque<GameEntity[]> toAdd = new ArrayDeque<GameEntity[]> ();
    
    // Common Entities for entire program
    private MegaPlayer player;
    private MegaHealthBar playerHealth;
    private Refractor refractor;
    private Door door;
    private WatchNadia bonus;
    private SeeteufelFront seeFront;
    
    private ArrayDeque<Rectangle> obstacles = new ArrayDeque<Rectangle>();
    private ArrayDeque<Damageable> playerTargets = new ArrayDeque<Damageable>();
    private ArrayDeque<Damageable> seeteufelTargets = new ArrayDeque<Damageable>();
    
    public SeeteufelScreen() {
        this.map1 = new FirstMap();
        this.map2 = new SecondMap();
        this.map3 = new ThirdMap();
        this.currentMap = this.map1;
    }
    
    @Override
    public void load() {
        this.t_tiles1 = new Texture(PATH_TILES_1);
        this.t_tiles2 = new Texture(PATH_TILES_2);
        this.t_player = new Texture(PATH_PLAYER);
        this.t_seeteufel = new Texture(PATH_SEETEUFEL);
        
        this.playerResources.load();
        
        this.map1.load();
        this.map2.load();
        this.map3.load();
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
        
        // Unload player recs.
        this.playerResources.unload();
    }

    @Override
    public void render(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {   
        
        // Update logic based on current map.
        if (this.currentMap == this.map1) {
            this.updateMap1(deltaTime, difficulty, perspCam, orthoCam);
        } else if (this.currentMap == this.map2) {
            this.updateMap2(deltaTime, difficulty, perspCam, orthoCam);
        } else {
            this.updateMap3(deltaTime, difficulty, perspCam, orthoCam);
        }
        
        // Check if you've lost.
        if (this.player.getState() == EntityState.Destroyed) {
            this.state = GameState.Lose;
        }
    }

    @Override
    public void initialize() {
        // Reset values.
        this.currentMap = this.map1;
        this.entities.clear();
        this.obstacles.clear();
        this.playerTargets.clear();
        this.seeteufelTargets.clear();
        
        // Create fresh instances of vital objects.
        this.player = new MegaPlayer(this.playerResources,
                this.currentMap.getInitialPosition(),
                Collections.unmodifiableCollection(this.obstacles),
                Collections.unmodifiableCollection(this.playerTargets));
        this.playerHealth = new MegaHealthBar(this.t_tiles2,
                (int) SeeteufelScreen.PLAYER_HEALTH_POS.x,
                (int) SeeteufelScreen.PLAYER_HEALTH_POS.y);
        this.refractor = new Refractor(this.t_tiles1,
                (int) Math.ceil(FirstMap.PLATFORM_START_X + FirstMap.GROUND_DIM
                        * 1.5 - Refractor.REFRACTOR_W / 2),
                (int) Math.ceil(FirstMap.GROUND_START_Y + FirstMap.GROUND_DIM
                        + 22));
        this.door = new Door(this.t_tiles2, FirstMap.GROUND_END_X
                - (int) (FirstMap.GROUND_DIM * 1.5), FirstMap.GROUND_START_Y);
        this.bonus = new WatchNadia(this.t_tiles1, FirstMap.PLATFORM_START_X,
                FirstMap.GROUND_START_Y);
        
        // Add objects to lists as needed.
        this.entities.add(this.refractor);
        this.entities.add(this.door);
        this.entities.add(this.bonus);
        Rectangle[] mapObstacles = this.map1.getObstacles();
        for (Rectangle rect : mapObstacles) {
            this.obstacles.add(rect);
        }
        this.playerTargets.add(this.bonus);
    }

    @Override
    public GameState getState() {
        return this.state;
    }
    
//    public Damageable [] getTargets() {
//        // Grab all the Damageables out of entities, store in a temporary ArrayDeque
//        ArrayDeque<Damageable> temp = new ArrayDeque<Damageable> ();
//        for (GameEntity e: this.entities) {
//            if (e instanceof Damageable) {
//                temp.add((Damageable) e);
//            }
//        }
//        
//        Damageable[] result = new Damageable[temp.size()];
//        return temp.toArray(result);
//    }
    
    
    private void updateMap1(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        // Update camera.
        orthoCam.position.x = this.player.getPosition().x;
        orthoCam.position.y = SeeteufelScreen.MAP1_CAM_Y;
        orthoCam.update();
        
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);   
        
        // Draw the map
        this.currentMap.render(deltaTime, orthoCam.combined);        
        
        // Update and draw all generic entities.
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            e.draw(orthoCam.combined);            
            if (e.getState() == EntityState.Destroyed) {
                if (e instanceof WatchNadia) {
                    this.player.setGeminiEnabled(true);
                }
                this.toRemove.push(e);              
            }
            if (e.hasCreatedEntities()) {

                this.toAdd.push(e.getCreatedEntities());
            }
        }
        
        // Update and draw all specially-managed entities.
        
        // Player.
        this.player.update(deltaTime);
        this.player.draw(orthoCam.combined);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.push(this.player.getCreatedEntities());
        }
        
        // Health bar.
        this.playerHealth.setInDanger(false);
        this.playerHealth.setValue(this.player.getHealth() / this.player.getMaxHealth());
        orthoCam.position.set(0, 0, 0);
        orthoCam.update();
        this.playerHealth.draw(orthoCam.combined);
        
        // Remove or add to generic entity list as needed.
        this.entities.removeAll(this.toRemove);
        for (GameEntity[] newEntities : this.toAdd) {
            for (GameEntity e : newEntities) {
                this.entities.add(e);
            }
        }
        this.toAdd.clear();
        this.toRemove.clear();
        
        // Special collision detection for player
        for (Rectangle box: player.getHitArea()) {
            // Grab the refractor
            if (refractor.getHitBox().overlaps(box)) {
                refractor.onTake();
            }
            
            // Exit the room. Do setup for room 2.
            if (door.getHitBox().overlaps(box)) {
                if (door.getDoorState() == Door.DoorState.OPEN) {
                    this.currentMap = map2;
                    this.setupMap2();
                }
            }
            
        }
        
        // Wait for Refractor to be declared Destroyed to open the door.
        if (refractor.getState() == EntityState.Destroyed && door.getDoorState() == Door.DoorState.SHUT) {
            door.onOpen();
        }
     }
    
    private void setupMap2() {
        this.entities.clear();
        this.playerTargets.clear();
        this.seeteufelTargets.clear();
        this.obstacles.clear();
        
        Rectangle[] map2Obstacles = this.map2.getObstacles();
        for (Rectangle rect : map2Obstacles) {
            this.obstacles.add(rect);
        }
        
        // TODO add destructable platforms to obstacles.
        
        this.player.setPosition(this.map2.getInitialPosition());
        
        this.seeFront = new SeeteufelFront(this.t_seeteufel, null);
        
    }
    
    private void updateMap2(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        // Update camera.
        orthoCam.position.x = this.player.getPosition().x;
        orthoCam.position.y = this.player.getPosition().y;
        orthoCam.update();
        
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);   
        
        // Draw the map
        this.currentMap.render(deltaTime, orthoCam.combined);
        
        // Update and draw all generic entities.
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            e.draw(orthoCam.combined);            
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.push(e);              
            }
            if (e.hasCreatedEntities()) {
                this.toAdd.push(e.getCreatedEntities());
            }
        }
        
        // Update and draw all specially-managed entities.
        
        // Seeteufel.
        this.seeFront.update(deltaTime);
        this.seeFront.draw(orthoCam.combined);
        
        // Player.
        this.player.update(deltaTime);
        this.player.draw(orthoCam.combined);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.push(this.player.getCreatedEntities());
        }
        
        // Health bar.
        this.playerHealth.setInDanger(false);
        this.playerHealth.setValue(this.player.getHealth() / this.player.getMaxHealth());
        orthoCam.position.set(0, 0, 0);
        orthoCam.update();
        this.playerHealth.draw(orthoCam.combined);
        
        // Remove or add to generic entity list as needed.
        this.entities.removeAll(this.toRemove);
        for (GameEntity[] newEntities : this.toAdd) {
            for (GameEntity e : newEntities) {
                this.entities.add(e);
            }
        }
        this.toAdd.clear();
        this.toRemove.clear();
    }
    
    private void updateMap3(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        // TODO
    }
    
}
