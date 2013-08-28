package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.ArrayDeque;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.Door;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.entities.Refractor;
import com.me.mygdxgame.entities.SeeteufelFront;
import com.me.mygdxgame.entities.WatchNadia;
import com.me.mygdxgame.entities.obstacles.Platform;
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
    private static final int MAP1_CAM_Y = SeeteufelScreen.SCREEN_BOTTOM / 5;
    private static final float MAP2_CAM_SPEED = 0.75f;
    private static final float MAP2_CAM_X = (SecondMap.GROUND_DIM * SecondMap.GROUND_WIDTH) / 2.0f;
    private static Vector3 MAP2_SEETEUFEL_INIT_POS = new Vector3(MAP2_CAM_X - SeeteufelFront.BASE_WIDTH / 2, SeeteufelScreen.MAP2_PIXEL_HEIGHT / 3, 0);
    private static final Color WATER_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
    private static final Color WATER_COLOR_ELECTRIC = new Color(1, 1, 0, 0);
    private static final int MAP2_HEIGHT = 40;
    private static final int MAP2_PIXEL_HEIGHT = MAP2_HEIGHT * SecondMap.GROUND_DIM;
    private static final int MAP2_ACTIVATION_X = SecondMap.GROUND_DIM * (SecondMap.GROUND_WIDTH - 5);
    private static final float MAP2_CAM_MAX_Y = MAP2_PIXEL_HEIGHT - Gdx.graphics.getHeight() / 3;
    private static final int MAP2_ENEMY_ATTACK_OFFSET = (int) (SecondMap.GROUND_DIM * 2.5);
    
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
    private float map2Y = -SCREEN_BOTTOM;
    private float map2WaterY = map2Y - 50;
    
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
    private ArrayDeque<ArrayDeque<Damageable>> seeteufelTargets = new ArrayDeque<ArrayDeque<Damageable>>();
    private ArrayDeque<Integer> seeteufelTargetLevels = new ArrayDeque<Integer>();
    private Rectangle map2Ceiling = null;
    
    private boolean isMap2Flooding = false;
    
    public SeeteufelScreen() {
        this.map1 = new FirstMap();
        this.map2 = new SecondMap(SeeteufelScreen.MAP2_HEIGHT);
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
        this.isMap2Flooding = false;
        
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
        
        // Set up first map.
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
        
        if ((orthoCam.position.x - MyGdxGame.SCREEN_WIDTH / 2.0f) <= FirstMap.GROUND_ORIGIN_X - FirstMap.GROUND_DIM) {
            orthoCam.position.x = (FirstMap.GROUND_ORIGIN_X - FirstMap.GROUND_DIM) + MyGdxGame.SCREEN_WIDTH / 2.0f;
        } else if ((orthoCam.position.x + MyGdxGame.SCREEN_WIDTH / 2.0f) >= FirstMap.GROUND_END_X + FirstMap.GROUND_DIM) {
            orthoCam.position.x = (FirstMap.GROUND_END_X + FirstMap.GROUND_DIM) - MyGdxGame.SCREEN_WIDTH / 2.0f;
        }
        
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
                if (door.getDoorState() == Door.DoorState.OPEN
                    && Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
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
        // Clear lists.
        this.entities.clear();
        this.playerTargets.clear();
        this.seeteufelTargets.clear();
        this.obstacles.clear();
        
        // Add player static obstacles.
        Rectangle[] map2Obstacles = this.map2.getObstacles();
        for (Rectangle rect : map2Obstacles) {
            this.obstacles.add(rect);
        }
        
        // Generate stairs of destructable platforms and load them into lists.
        this.generateMap2Stairs();
        
        // Add moving ceiling obstacle to obstacle list.
        this.map2Ceiling = new Rectangle(0, 0, Gdx.graphics.getWidth(), 50);
        this.obstacles.push(this.map2Ceiling);
        
        this.player.setPosition(this.map2.getInitialPosition());
    }
    
    /** Add player dynamic obstacles. Also generates Seeteufel target list(s).*/
    private void generateMap2Stairs() {
        
        // First flight of stairs. Do not add these to seeteufelTargets, no need to destroy them.
        int currentTileX = 0;
        int currentTileY = 1;
        int maxTileX = SecondMap.GROUND_WIDTH - 1;
        for (currentTileX = SecondMap.GROUND_WIDTH / 2; currentTileX < maxTileX; currentTileX++) {
            Platform destructableTile = new Platform(this.t_tiles1, currentTileX * SecondMap.GROUND_DIM, currentTileY * SecondMap.GROUND_DIM);
            this.obstacles.add(destructableTile.getHitArea()[0]);
            this.entities.add(destructableTile);
            
            if (currentTileX % 2 == 0) {
                currentTileY++;
            }
        }
        
        // The rest up to the ceiling.
        boolean platformDirection = false;
        currentTileX -= 5;
        this.seeteufelTargets.push(new ArrayDeque<Damageable>());
        this.seeteufelTargetLevels.push(currentTileY * SecondMap.GROUND_DIM);
        while (currentTileY < SeeteufelScreen.MAP2_HEIGHT) {
            
            int currentYCoord = currentTileY * SecondMap.GROUND_DIM;
            Platform destructableTile = new Platform(this.t_tiles1, currentTileX * SecondMap.GROUND_DIM, currentYCoord);
            this.seeteufelTargets.peek().add(destructableTile);
            this.obstacles.add(destructableTile.getHitArea()[0]);
            this.entities.add(destructableTile);
            
            if (platformDirection) {
                currentTileX++;
                if (currentTileX == maxTileX) {
                    if (currentTileX % 2 == 0) {
                        currentTileX -= 6;
                    } else {
                        currentTileX -= 5;
                    }
                    platformDirection = !platformDirection;
                    this.seeteufelTargets.push(new ArrayDeque<Damageable>());
                    this.seeteufelTargetLevels.push(currentYCoord);
                } else if (currentTileX % 2 == 0) {
                    currentTileY++;
                }
            } else {
                currentTileX--;
                if (currentTileX == 0) {
                    if (currentTileX % 2 == 0) {
                        currentTileX += 6;
                    } else {
                        currentTileX += 5;
                    }
                    platformDirection = !platformDirection;
                    this.seeteufelTargets.push(new ArrayDeque<Damageable>());
                    this.seeteufelTargetLevels.push(currentYCoord);
                } else if (currentTileX % 2 == 0) {
                    currentTileY++;
                }
            }
        }
        
        // Remove the second staircase (the first "real" one) from being
        // targeted, to give the player a moment to adapt. Remove the very last
        // stairs since the exit will be there.
        this.seeteufelTargets.removeLast();
        this.seeteufelTargets.pop();
        this.seeteufelTargetLevels.removeLast();
        this.seeteufelTargetLevels.pop();
    }
    
    private int[] getSeeteufelTargets(Damageable[] currentTargets) {
        
        int[] targets = new int[3];
        
        // First target, random platform that isn't the first one on the
        // stairs (so as not to create a ledge that can't be jumped up).
        targets[0] = Math.max(1, (int) (Math.random() * currentTargets.length));
        
        // Second target, random platform that isn't first target or the
        // first block of a staircase or adjacent to the first block.
        targets[1] = (int) (Math.random() * currentTargets.length);
        while (targets[1] < 1 || Math.abs(targets[0] - targets[1]) < 2) {
            targets[1] = (int) (Math.random() * currentTargets.length);
        }

        // Third target, random platform that isn't first or second
        // target, isn't adjacent to either, and isn't the first step on
        // the stairs. May want to remove this if the room is made
        // smaller.
        targets[2] = (int) (Math.random() * currentTargets.length);
        while (Math.abs(targets[2] - targets[0]) < 2 ||
                Math.abs(targets[2] - targets[1]) < 2 ||
                targets[2] == targets[1] ||
                targets[2] == targets[0] ||
                targets[2] < 1) {
            targets[2] = (int) (Math.random() * currentTargets.length);
        }
        
        return targets;
    }
    
    private void updateMap2(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        
        // Update camera according to current map state.
        // Rises slowly as room floods. Stays static prior.
        Vector3 playerPos = this.player.getPosition();
        orthoCam.position.x = SeeteufelScreen.MAP2_CAM_X;
        
        // Activate flooding once player moves above certain x.
        if (this.isMap2Flooding) {
            if (this.map2Y < SeeteufelScreen.MAP2_CAM_MAX_Y) {
                this.map2Y += SeeteufelScreen.MAP2_CAM_SPEED;
                this.map2WaterY += SeeteufelScreen.MAP2_CAM_SPEED;
            }
        } else if (playerPos.x > SeeteufelScreen.MAP2_ACTIVATION_X) {
            this.isMap2Flooding = true;
            this.playerHealth.setInDanger(true);
            this.seeFront = new SeeteufelFront(this.t_seeteufel, this.t_tiles1, SeeteufelScreen.MAP2_SEETEUFEL_INIT_POS);
        }
        orthoCam.position.y = (float) Math.floor(this.map2Y);
        orthoCam.update();
        
        // Update moving barrier at the top of the screen.
        this.map2Ceiling.setPosition(0, orthoCam.position.y + Gdx.graphics.getHeight() / 2);
        
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);   
        
        // Draw the map
        this.currentMap.render(deltaTime, orthoCam.combined);
        
        // Update Seeteufel only after water starts rising.
        if (this.isMap2Flooding) {
            this.seeFront.setTargetY((int)this.map2WaterY);
            this.seeFront.update(deltaTime);
            this.seeFront.draw(orthoCam.combined);
            
            // Attack if water level is one block below the next staircase.
            if (!this.seeteufelTargetLevels.isEmpty() &&
                    this.map2WaterY >= this.seeteufelTargetLevels.peekLast() -
                    SeeteufelScreen.MAP2_ENEMY_ATTACK_OFFSET) {
                Damageable[] currentTargets = new Damageable[this.seeteufelTargets.peekLast().size()];
                currentTargets = this.seeteufelTargets.removeLast().toArray(currentTargets);
                int[] targets = this.getSeeteufelTargets(currentTargets);
                
                for (int x : targets) {
                    this.seeFront.attack(currentTargets[x]);
                }
                
                this.seeteufelTargetLevels.removeLast();
            }
            
            if (this.seeFront.hasCreatedEntities()) {
                GameEntity[] newEntities = this.seeFront.getCreatedEntities();
                for (GameEntity entity : newEntities) {
                    this.entities.addLast(entity);
                }
            }
        }
        
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
        
        // Player.
        if (playerPos.y < this.map2Y) {
            this.player.setIsUnderwater(true);
        } else {
            this.player.setIsUnderwater(false);
        }
        this.player.update(deltaTime);
        this.player.draw(orthoCam.combined);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.push(this.player.getCreatedEntities());
        }
        
        // Water, which follows just below the camera.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        MyGdxGame.currentGame.shapeRenderer.begin(ShapeType.Filled);
        MyGdxGame.currentGame.shapeRenderer.setColor(SeeteufelScreen.WATER_COLOR);
        MyGdxGame.currentGame.shapeRenderer.setProjectionMatrix(orthoCam.combined);
        MyGdxGame.currentGame.shapeRenderer.rect(orthoCam.position.x - Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth(), this.map2WaterY);
        MyGdxGame.currentGame.shapeRenderer.end();
        
        // Health bar.
        this.playerHealth.setValue(this.player.getHealth() / this.player.getMaxHealth());
        orthoCam.position.set(0, 0, 0);
        orthoCam.update();
        this.playerHealth.draw(orthoCam.combined);
        
        // Remove or add to generic entity list as needed.
        this.entities.removeAll(this.toRemove);
        for (GameEntity e : this.toRemove) {
            // Remove from obstacle list if this was one of the platforms.
            // TODO Hackish, fix eventually.
            if (e instanceof Platform) {
                this.obstacles.remove(((Platform) e).getHitArea()[0]);
            }
        }
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
