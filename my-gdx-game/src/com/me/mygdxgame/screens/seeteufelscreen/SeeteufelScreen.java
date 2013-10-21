package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.Door;
import com.me.mygdxgame.entities.Door.DoorState;
import com.me.mygdxgame.entities.InfinityWaterfall;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.entities.Refractor;
import com.me.mygdxgame.entities.SeeteufelFront;
import com.me.mygdxgame.entities.SeeteufelSide;
import com.me.mygdxgame.entities.WatchNadia;
import com.me.mygdxgame.entities.obstacles.Platform;
import com.me.mygdxgame.entities.progressbars.BonneHealthBar;
import com.me.mygdxgame.entities.progressbars.MegaHealthBar;
import com.me.mygdxgame.screens.seeteufelscreen.maps.FirstMap;
import com.me.mygdxgame.screens.seeteufelscreen.maps.SecondMap;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameScreen;
import com.me.mygdxgame.utilities.GameState;
import com.me.mygdxgame.utilities.Renderer;

/**
 * TODO
 */
public class SeeteufelScreen implements GameScreen {
    
    // Edge constants, for use only in 2D
    private static final int SCREEN_TOP = MyGdxGame.SCREEN_HEIGHT / 2;
    private static final int SCREEN_BOTTOM = -MyGdxGame.SCREEN_HEIGHT / 2;
    private static final int SCREEN_LEFT = -MyGdxGame.SCREEN_WIDTH / 2;
    private static final int ENEMY_HEALTHBAR_MOVE_SPEED = 40;
    private static final Vector2 PLAYER_HEALTH_POS = new Vector2(SCREEN_LEFT + 10, SCREEN_BOTTOM + 10);
    private static final int ENEMY_HEALTH_Y = SCREEN_TOP - BonneHealthBar.LOGO_H - 10;
    private static final int ENEMY_HEALTH_TARGET_X = (int) (SeeteufelScreen.PLAYER_HEALTH_POS.x);
    private static final int MAP1_CAM_Y = SeeteufelScreen.SCREEN_BOTTOM / 5;
    private static final float MAP2_CAM_SPEED_1 = 30f;
    private static final float MAP2_CAM_SPEED_2 = 50f;
    private static final float MAP2_CAM_X = (SecondMap.GROUND_DIM * SecondMap.GROUND_WIDTH) / 2.0f;
    private static final Vector3 MAP2_SEETEUFEL_INIT_POS = new Vector3(MAP2_CAM_X - SeeteufelFront.BASE_WIDTH / 2, SeeteufelScreen.MAP2_PIXEL_HEIGHT / 2, 0);
    private static final Color WATER_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
    private static final int MAP2_HEIGHT = 48;
    private static final int MAP2_PIXEL_HEIGHT = MAP2_HEIGHT * SecondMap.GROUND_DIM;
    private static final int MAP2_ACTIVATION_X = SecondMap.GROUND_DIM * (SecondMap.GROUND_WIDTH - 5);
    private static final float MAP2_CAM_MAX_Y = MAP2_PIXEL_HEIGHT;
    private static final float MAP2_CAM_INCREASE_SPEED_TIRGGER_Y = MAP2_PIXEL_HEIGHT - (MAP2_CAM_MAX_Y / 2);
    private static final int MAP2_ENEMY_ATTACK_OFFSET = (SecondMap.GROUND_DIM * 4);
    private static final int MAP2_PLAYER_DROWN_OFFSET = MegaPlayer.HITBOX_HEIGHT * 2;
    private static final float MAP2_INITIAL_CAM_Y = -SCREEN_BOTTOM;
    private static final float MAP2_WATER_Y_OFFSET = 75;
    private static final int MAP2_CAMERA_SHAKE = 15;
    private static final float MAP2_CAMERA_SHAKE_FALLOFF = 0.5f;
    private static final float MAP2_STAIR_STEP_HEIGHT = 0.5f;
    private static final float MAP2_STAIR_FLIGHT_HEIGHT = 4.5f;
    private static final int MAP2_STAIR_FLIGHT_X_OFFSET = 5;
    private static final int MAP2_STAIR_STEP_LENGTH = 3;
    private static final int MAP2_STAIR_IGNORE_LAST_TARGETS = 2;
    private static final int MAP2_WATER_LATENT_RISE_RATE = 3;
    private static final int MAP2_WATER_WIDTH = SecondMap.GROUND_WIDTH * SecondMap.GROUND_DIM;
    private static final int MAP3_WATER_BASE_X = MAP2_PIXEL_HEIGHT - 500;
    private static final int MAP3_CAM_Y = 1700;
    
    // State.
    private GameState state = GameState.Running;
        
    // Each of the maps and a holder for the current map
    private FirstMap map1;
    private SecondMap map2;
    private int currentMap;
    private float map2Y = MAP2_INITIAL_CAM_Y;
    private float map2WaterY = MAP2_INITIAL_CAM_Y - MAP2_WATER_Y_OFFSET;
    private float map2CameraShake = 0;
    private Vector2 map3CamPos = new Vector2();
    
    // Resource files.
    private Texture t_tiles1;
    private Texture t_tiles2;
    private Texture t_player;
    private Texture t_seeteufel;
    private Texture waterfall;
    private Texture waterfallEnd;
    private BitmapFont font;
    private MegaPlayer.MegaPlayerResources playerResources = new MegaPlayer.MegaPlayerResources();
    private MapTiles mapTiles = new MapTiles();
    private Sound explosion;
    private Sound splash;
    private Sound seeSplash;
    private Sound bombShoot;
    private Sound doorOpen;
    private Sound doorClose;
    private Sound itemGet;
    private Sound enemyDamage;
    private Music music1;
    private Music music2;
    
    // Containers for managing entities generically. Keep a pointer to any entities that need specialized mgmt
    private LinkedList<GameEntity> entities = new LinkedList<GameEntity> ();
    private LinkedList<GameEntity> toRemove = new LinkedList<GameEntity> ();
    private LinkedList<GameEntity[]> toAdd = new LinkedList<GameEntity[]> ();
    
    // Common Entities for entire program
    private MegaPlayer player;
    private MegaHealthBar playerHealth;
    private BonneHealthBar enemyHealth;
    private Refractor refractor;
    private Door room1Exit;
    private Door room2Entrance;
    private WatchNadia bonus;
    private SeeteufelFront seeFront;
    private SeeteufelSide seeSide;
    private Renderer hudRenderer;
    private InfinityWaterfall room2Fall;
    
    private LinkedList<Rectangle> obstacles = new LinkedList<Rectangle>();
    private LinkedList<Damageable> playerTargets = new LinkedList<Damageable>();
    private LinkedList<LinkedList<Damageable>> seeteufelTargets = new LinkedList<LinkedList<Damageable>>();
    private LinkedList<Integer> seeteufelTargetLevels = new LinkedList<Integer>();
    private Rectangle map2Ceiling = null;
    
    private boolean isMap2Flooding = false;
    private boolean displayFps = false;
    private boolean displayFpsButtonTrigger = false;
    
    /** Container class for textures used by the SeeteufelScreen maps.*/
    public static class MapTiles {
        public Texture rockTex = null;
        public Texture wallTex = null;
        public Texture smallMazeTex = null;
        public Texture stairTex = null;
        public Texture pillarTex = null;
        public Texture greyBlockTex = null;
        public Texture bonusTex = null;
        
        private boolean isLoaded = false;
        
        public void load() {
            if (!this.isLoaded) {
                
                this.rockTex = new Texture("img/tile1.png");
                this.rockTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                
                this.wallTex = new Texture("img/tile2.png");
                this.wallTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                
                this.smallMazeTex = new Texture("img/tile4.png");
                this.smallMazeTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                
                this.pillarTex = new Texture("img/tile3.png");
                this.pillarTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                
                this.greyBlockTex = new Texture("img/idk.png");
                this.greyBlockTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                
                this.bonusTex  = new Texture("img/nadia.png");
                this.bonusTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);

                this.isLoaded = true;
            }
        }
        
        public void unload() {
            if (this.isLoaded) {
                
                this.rockTex.dispose();
                this.wallTex.dispose();
                this.smallMazeTex.dispose();
                this.pillarTex.dispose();

                this.isLoaded = false;
            }
        }
    }
    
    public SeeteufelScreen() {
        this.hudRenderer = new Renderer(new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()).combined);
    }
    
    @Override
    public void load() {
        
        this.mapTiles.load();
        this.t_tiles1 = new Texture("img/seeTiles1.png");
        this.t_tiles2 = new Texture("img/seeTiles2.png");
        this.t_player = new Texture("img/mmd.png");
        this.t_seeteufel = new Texture("img/seeteufel.png");
        this.waterfall = new Texture("img/waterfall1.png");
        this.waterfall.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        this.waterfall.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        this.waterfallEnd = new Texture("img/waterfallEnd.png");
        
        this.playerResources.load();
        
        this.explosion = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-grenade-explode1.ogg"));
        this.splash = Gdx.audio.newSound(Gdx.files.internal("sound/splash.ogg"));
        this.seeSplash = Gdx.audio.newSound(Gdx.files.internal("sound/see_crash.ogg"));
        this.bombShoot = Gdx.audio.newSound(Gdx.files.internal("sound/bomb_fire.ogg"));
        this.music1 = Gdx.audio.newMusic(Gdx.files.internal("sound/Seeteufel_the_Mighty_1.ogg"));
        this.music2 = Gdx.audio.newMusic(Gdx.files.internal("sound/Seeteufel_the_Mighty_2.ogg"));
        this.doorOpen = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-ruindoor-open1.ogg"));
        this.doorClose = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-ruindoor-close1.ogg"));
        this.itemGet = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-item-get.ogg"));
        this.enemyDamage = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-reaverhurt1.ogg"));
        this.font = new BitmapFont(Gdx.files.internal("data/emulogic.fnt"), Gdx.files.internal("data/emulogic.png"), false);
        
        // Play all sounds once to force them to be loaded, since the HTML
        // version does some kind of lazy loading.
        playerResources.footstepSound.play();
        playerResources.footstepSound.stop();
        playerResources.hurtSound.play();
        playerResources.hurtSound.stop();
        playerResources.jumpSound.play();
        playerResources.jumpSound.stop();
        playerResources.landSound.play();
        playerResources.landSound.stop();
        playerResources.shootSound.play();
        playerResources.shootSound.stop();
        playerResources.shotMissSound.play();
        playerResources.shotMissSound.stop();
        explosion.play();
        explosion.stop();
        splash.play();
        splash.stop();
        seeSplash.play();
        seeSplash.stop();
        bombShoot.play();
        bombShoot.stop();
        doorOpen.play();
        doorOpen.stop();
        doorClose.play();
        doorClose.stop();
        music1.play();
        music1.stop();
        music2.play();
        music2.stop();
        
        // Load maps.
        this.map1 = new FirstMap(this.t_tiles1, this.mapTiles);
        this.map2 = new SecondMap(this.t_tiles1, this.mapTiles, SeeteufelScreen.MAP2_HEIGHT);
        this.currentMap = 1;
    }

    @Override
    public void unload() {
        // Dispose of Textures
        this.mapTiles.unload();
        this.t_tiles1.dispose();
        this.t_tiles2.dispose();
        this.t_player.dispose();
        this.t_seeteufel.dispose();
        this.waterfall.dispose();
        this.waterfallEnd.dispose();
        
        // Unload other recs.
        this.playerResources.unload();
        this.explosion.dispose();
        this.explosion.dispose();
        this.splash.dispose();
        this.seeSplash.dispose();
        this.bombShoot.dispose();
        this.music1.dispose();
        this.music2.dispose();
        this.doorOpen.dispose();
        this.doorClose.dispose(); 
        this.itemGet.dispose();
        this.font.dispose();
    }

    @Override
    public void render(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {   
        
        // Update logic based on current map.
        if (this.currentMap == 1) {
            this.updateMap1(deltaTime, difficulty, perspCam, orthoCam);
        } else if (this.currentMap == 2) {
            this.updateMap2(deltaTime, difficulty, perspCam, orthoCam);
        } else {
            this.updateMap3(deltaTime, difficulty, perspCam, orthoCam);
        }
        
        // Draw fps for testing, if enabled.
        if (!Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            this.displayFpsButtonTrigger = true;
        }
        if (this.displayFpsButtonTrigger && Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) {
            this.displayFps = !this.displayFps;
            this.displayFpsButtonTrigger = false;
        }
        if (this.displayFps) {
           this.hudRenderer.drawText(this.font, Integer.toString(Gdx.graphics.getFramesPerSecond()), 0, 0);
        }
        
        // Flush renderer.
        Renderer.flush();
        
        // Check if you've lost.
        if (this.player.getState() == EntityState.Destroyed) {
            this.state = GameState.Lose;
            this.music1.stop();
            this.music2.stop();
        }
    }

    @Override
    public void initialize() {
        // Reset values.
        this.currentMap = 1;
        this.obstacles.clear();
        this.playerTargets.clear();
        this.seeteufelTargets.clear();
        this.isMap2Flooding = false;
        this.state = GameState.Running;
        this.map2Y = SeeteufelScreen.MAP2_INITIAL_CAM_Y;
        this.map2WaterY = SeeteufelScreen.MAP2_INITIAL_CAM_Y - SeeteufelScreen.MAP2_WATER_Y_OFFSET;
        this.seeteufelTargets.clear();
        this.seeteufelTargetLevels.clear();
        this.map2CameraShake = 0;
        
        for (GameEntity e : this.entities) {
            e.destroy();
        }
        this.entities.clear();
        
        // Create fresh instances of vital objects.
        this.player = new MegaPlayer(this.playerResources,
                this.map1.getInitialPosition(),
                Collections.unmodifiableCollection(this.obstacles),
                Collections.unmodifiableCollection(this.playerTargets));
        this.playerHealth = new MegaHealthBar(this.t_tiles2,
                (int) SeeteufelScreen.PLAYER_HEALTH_POS.x,
                (int) SeeteufelScreen.PLAYER_HEALTH_POS.y);
        this.enemyHealth = new BonneHealthBar(this.t_tiles2, SeeteufelScreen.SCREEN_LEFT - BonneHealthBar.BAR_W * 2, ENEMY_HEALTH_Y);
        this.refractor = new Refractor(this.t_tiles1, this.itemGet, this.font,
                (int) Math.ceil(FirstMap.PLATFORM_START_X + FirstMap.GROUND_DIM
                        * 1.5 - Refractor.REFRACTOR_W / 2),
                (int) Math.ceil(FirstMap.GROUND_START_Y + FirstMap.GROUND_DIM * 2
                        + 22));
        this.room1Exit = new Door(this.t_tiles2, this.doorOpen, this.doorClose,
                FirstMap.GROUND_END_X - (int) (FirstMap.GROUND_DIM * 1.5),
                FirstMap.GROUND_START_Y);
        this.bonus = new WatchNadia(this.mapTiles.bonusTex, FirstMap.PLATFORM_START_X - FirstMap.GROUND_DIM,
                FirstMap.GROUND_START_Y);
        
        // Set up first map.
        this.entities.add(this.refractor);
        this.entities.add(this.room1Exit);
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
        
        // Make renderer.
        Renderer renderer = new Renderer(orthoCam.combined);
        
        // Draw the map
        this.map1.render(deltaTime, renderer);
        
        // Update and draw all generic entities.
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            e.draw(renderer);            
            if (e.getState() == EntityState.Destroyed) {
                if (e instanceof WatchNadia) {
                    this.player.setGeminiEnabled(true);
                }
                this.toRemove.addFirst(e);              
            }
            if (e.hasCreatedEntities()) {

                this.toAdd.addFirst(e.getCreatedEntities());
            }
        }
        
        // Update and draw all specially-managed entities.
        
        // Player.
        this.player.update(deltaTime);
        this.player.draw(renderer);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.addFirst(this.player.getCreatedEntities());
        }
        
        // Health bar.
        this.playerHealth.setInDanger(false);
        this.playerHealth.setValue((float)this.player.getHealth() / this.player.getMaxHealth());
        this.playerHealth.draw(this.hudRenderer);
        
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
            if (room1Exit.getDoorState() == Door.DoorState.OPEN) {
                if (room1Exit.getHitBox().overlaps(box) &&
                        Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                    this.currentMap = 2;
                    this.setupMap2();
                }
            }
            
        }
        
        // Wait for Refractor to be declared Destroyed to open the room1Exit.
        if (refractor.getState() == EntityState.Destroyed && room1Exit.getDoorState() == Door.DoorState.SHUT) {
            room1Exit.setIsOpen(DoorState.OPEN, true);
        }
     }
    
    private void setupMap2() {
        // Clear lists.
        for (GameEntity e : this.entities) {
            e.destroy();
        }
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
        this.obstacles.addFirst(this.map2Ceiling);
        
        // Set player state and initial position.
        Vector3 map2InitPos = this.map2.getInitialPosition();
        this.player.setPosition(map2InitPos);
        this.player.setIsUnderwater(true, false);

        // Add doors. Also, remove the highest block on the last staircase,
        // since the exit will be there.
        this.room2Entrance = new Door(this.t_tiles2, this.doorOpen,
                this.doorClose, (int) map2InitPos.x, SecondMap.GROUND_DIM);
        this.room2Entrance.setIsOpen(DoorState.SHUT, false);
        this.seeteufelTargets.peek().removeLast();
        
        // Create decorative waterfall.
        this.room2Fall = new InfinityWaterfall(
                this.waterfall, this.waterfallEnd, 100, SeeteufelScreen.MAP2_PIXEL_HEIGHT, 
                (int) (SeeteufelScreen.MAP2_PIXEL_HEIGHT - SeeteufelScreen.MAP2_INITIAL_CAM_Y - SeeteufelScreen.MAP2_WATER_Y_OFFSET));
    }
    
    /** Add player dynamic obstacles. Also generates Seeteufel target list(s).*/
    private void generateMap2Stairs() {
        
        // First flight of stairs. Do not add these to seeteufelTargets, no need to destroy them.
        int currentTileX = SecondMap.GROUND_WIDTH / 2;
        int currentTileY = (int) (1.0f / SeeteufelScreen.MAP2_STAIR_STEP_HEIGHT);
        int maxTileX = SecondMap.GROUND_WIDTH - 1;
        Platform destructableTile = null;
        for (currentTileX = SecondMap.GROUND_WIDTH / 2; currentTileX < maxTileX; currentTileX++) {
            destructableTile = new Platform(this.t_tiles1, this.mapTiles, currentTileX * SecondMap.GROUND_DIM, (int) (currentTileY * SecondMap.GROUND_DIM * SeeteufelScreen.MAP2_STAIR_STEP_HEIGHT));
            this.obstacles.add(destructableTile.getHitArea()[0]);
            this.entities.add(destructableTile);
            
            if (currentTileX % SeeteufelScreen.MAP2_STAIR_STEP_LENGTH == 0) {
                currentTileY++;
            }
        }
        
        // The rest up to the ceiling.
        boolean platformDirection = false;
        currentTileX -= SeeteufelScreen.MAP2_STAIR_FLIGHT_X_OFFSET;
        currentTileY += SeeteufelScreen.MAP2_STAIR_FLIGHT_HEIGHT; 
        this.seeteufelTargets.addFirst(new LinkedList<Damageable>());
        this.seeteufelTargetLevels.addFirst(currentTileY * (SecondMap.GROUND_DIM / 2));
        while (currentTileY * SeeteufelScreen.MAP2_STAIR_STEP_HEIGHT < SeeteufelScreen.MAP2_HEIGHT) {
            
            int currentYCoord = (int) (currentTileY * (SecondMap.GROUND_DIM * SeeteufelScreen.MAP2_STAIR_STEP_HEIGHT));
            destructableTile = new Platform(this.t_tiles1, this.mapTiles, currentTileX * SecondMap.GROUND_DIM, currentYCoord);
            this.seeteufelTargets.peek().add(destructableTile);
            this.obstacles.add(destructableTile.getHitArea()[0]);
            this.entities.add(destructableTile);
            
            if (platformDirection) {
                currentTileX++;
                if (currentTileX == maxTileX) {
                    if (currentTileX % SeeteufelScreen.MAP2_STAIR_STEP_LENGTH == 0) {
                        currentTileX -= SeeteufelScreen.MAP2_STAIR_FLIGHT_X_OFFSET + 1;
                    } else {
                        currentTileX -= SeeteufelScreen.MAP2_STAIR_FLIGHT_X_OFFSET;
                    }
                    currentTileY += SeeteufelScreen.MAP2_STAIR_FLIGHT_HEIGHT;
                    platformDirection = !platformDirection;
                    this.seeteufelTargets.addFirst(new LinkedList<Damageable>());
                    this.seeteufelTargetLevels.addFirst(currentYCoord);
                } else if (currentTileX % SeeteufelScreen.MAP2_STAIR_STEP_LENGTH == 0) {
                    currentTileY++;
                }
            } else {
                currentTileX--;
                if (currentTileX == 0) {
                    if (currentTileX % SeeteufelScreen.MAP2_STAIR_STEP_LENGTH == 0) {
                        currentTileX += SeeteufelScreen.MAP2_STAIR_FLIGHT_X_OFFSET + 1;
                    } else {
                        currentTileX += SeeteufelScreen.MAP2_STAIR_FLIGHT_X_OFFSET;
                    }
                    currentTileY += SeeteufelScreen.MAP2_STAIR_FLIGHT_HEIGHT;
                    platformDirection = !platformDirection;
                    this.seeteufelTargets.addFirst(new LinkedList<Damageable>());
                    this.seeteufelTargetLevels.addFirst(currentYCoord);
                } else if (currentTileX % SeeteufelScreen.MAP2_STAIR_STEP_LENGTH == 0) {
                    currentTileY++;
                }
            }
        }
        
        // Remove the second and third staircases (the first two "real" one)
        // from being targeted, to give the player a moment to adapt. Remove
        // the very last stairs since they will only be partially finished.
        this.seeteufelTargets.removeLast();
        this.seeteufelTargets.removeLast();
        this.seeteufelTargetLevels.removeLast();
        this.seeteufelTargetLevels.removeLast();
        
        LinkedList<Damageable> topStairs = this.seeteufelTargets.removeFirst();
        for (Damageable d : topStairs) {
            this.obstacles.remove(d.getHitArea()[0]);
            this.entities.remove(d);
        }
        this.seeteufelTargetLevels.removeFirst();
    }
    
    private int[] getSeeteufelTargets(Damageable[] currentTargets) {
        
        int[] targets = new int[4];

        // First target, random platform that isn't the first one on the
        // stairs (so as not to create a ledge that can't be jumped up).
        // Also, leave the last MAP2_STAIR_IGNORE_LAST_TARGETS alone.
        // Destroying them has little effect on difficulty, and the
        // door goes there on the last flight.
        targets[0] = Math.max(1, (int) (Math.random() * currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));

        // Second target, random platform that isn't first target or the
        // first block of a staircase. Also, don't destroy both 8 and 9,
        // as this results in an overly tricky jump.
        do {
            targets[1] = Math.max(1, (int) (Math.random() * currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));
        }
        while (targets[1] == targets[0] ||
                (targets[0] == 8 && targets[1] == 9) ||
                (targets[0] == 9 && targets[1] == 8));

        // Third target. Random platform that isn't first or second
        // target, isn't step 0, and isn't adjacent to both 0 and 1.
        do {
            targets[2] = Math.max(1, (int) (Math.random() * currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));
        }
        while (targets[2] == targets[1] ||
                targets[2] == targets[0] ||
                Math.abs(targets[2] - targets[0]) == 1 ||
                Math.abs(targets[2] - targets[1]) == 1);

        // Fourth target. Random platform that isn't first or second or third
        // target, isn't step 0, and isn't adjacent to both 0 and 1 and 2.
        do {
            targets[3] = Math.max(1, (int) (Math.random() * currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));
        }
        while (targets[3] == targets[0] ||
                targets[3] == targets[1] ||
                targets[3] == targets[2] ||
                Math.abs(targets[3] - targets[0]) == 1 ||
                Math.abs(targets[3] - targets[1]) == 1 ||
                Math.abs(targets[3] - targets[2]) == 1);

        return targets;
    }
    
    private void updateMap2(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        
        // Update camera according to current map state.
        // Rises slowly as room floods. Stays static prior.
        Vector3 playerPos = this.player.getPosition();
        orthoCam.position.x = SeeteufelScreen.MAP2_CAM_X;
        orthoCam.position.y = (float) Math.floor(this.map2Y + this.map2CameraShake);
        orthoCam.update();
        if (this.map2CameraShake > 0) {
            this.map2CameraShake = -this.map2CameraShake + SeeteufelScreen.MAP2_CAMERA_SHAKE_FALLOFF;
        } else if (this.map2CameraShake < 0) {
            this.map2CameraShake = -this.map2CameraShake - SeeteufelScreen.MAP2_CAMERA_SHAKE_FALLOFF;
        }
        
        // Activate flooding once player moves above certain x.
        if (this.isMap2Flooding) {
            if (!this.music1.isPlaying() && !this.music2.isPlaying()) {
                this.music2.setLooping(true);
                this.music2.play();
            }
            if (this.map2Y < SeeteufelScreen.MAP2_CAM_MAX_Y) {
                // Move cam faster once you reach a certain point.
                if (this.map2Y > SeeteufelScreen.MAP2_CAM_INCREASE_SPEED_TIRGGER_Y) {
                    float movement = SeeteufelScreen.MAP2_CAM_SPEED_2 * deltaTime;
                    this.map2Y += movement;
                } else {
                    float movement = SeeteufelScreen.MAP2_CAM_SPEED_1 * deltaTime;
                    this.map2Y += movement;
                }
                this.map2WaterY = this.map2Y - SeeteufelScreen.MAP2_WATER_Y_OFFSET;
            } else if (this.map2WaterY < SeeteufelScreen.MAP2_PIXEL_HEIGHT){
                this.map2WaterY += SeeteufelScreen.MAP2_WATER_LATENT_RISE_RATE;
            }
        } else if (playerPos.x > SeeteufelScreen.MAP2_ACTIVATION_X) {
            // Activate chase sequence.
            this.isMap2Flooding = true;
            this.music1.play();
            this.seeSplash.play();
            this.map2CameraShake = SeeteufelScreen.MAP2_CAMERA_SHAKE;
            this.playerHealth.setInDanger(true);
            this.seeFront = new SeeteufelFront(this.t_seeteufel, this.t_tiles1,
                    this.explosion, this.seeSplash, this.bombShoot,
                    SeeteufelScreen.MAP2_SEETEUFEL_INIT_POS);
        }
        
        // Update moving barrier at the top of the screen.
        float screenTop = orthoCam.position.y + Gdx.graphics.getHeight() / 2;
        this.map2Ceiling.setPosition(0, screenTop);
        
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // Create Renderer.
        Renderer renderer = new Renderer(orthoCam.combined);
        
        // Draw the map
        this.map2.render(deltaTime, renderer);
        
        // Draw falls.
        this.room2Fall.update(deltaTime);
        this.room2Fall.setHeight(SeeteufelScreen.MAP2_PIXEL_HEIGHT - (int)this.map2WaterY);
        this.room2Fall.draw(renderer);
        
        // Update Seeteufel only after water starts rising.
        if (this.isMap2Flooding) {
            this.seeFront.setTargetY((int)this.map2WaterY);
            this.seeFront.update(deltaTime);
            this.seeFront.draw(renderer);
            
            // Attack if water level is one block below the next staircase.
            if (!this.seeteufelTargetLevels.isEmpty()) {
                Integer currentLevel = this.seeteufelTargetLevels.removeLast();
                if (this.map2WaterY >= currentLevel - SeeteufelScreen.MAP2_ENEMY_ATTACK_OFFSET) {
                    LinkedList<Damageable> currentTargetList = this.seeteufelTargets.removeLast();
                    Damageable[] currentTargets = new Damageable[currentTargetList.size()];
                    currentTargets = currentTargetList.toArray(currentTargets);
                    int[] targets = this.getSeeteufelTargets(currentTargets);
                    
                    for (int x : targets) {
                        this.seeFront.attack(currentTargets[x]);
                    }
                } else {
                    this.seeteufelTargetLevels.addLast(currentLevel);
                }
            }
            
            if (this.seeFront.hasCreatedEntities()) {
                GameEntity[] newEntities = this.seeFront.getCreatedEntities();
                for (GameEntity entity : newEntities) {
                    this.entities.addLast(entity);
                }
            }
        }
        
        // Update and draw all generic entities.
        float entityY = 0;
        float screenBottom = orthoCam.position.y - (Gdx.graphics.getHeight() / 2) - Door.DOOR_H;
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            entityY = e.getHitArea()[0].y;
            if (entityY < screenTop && entityY > screenBottom) {
                e.draw(renderer); 
            }
                
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.addFirst(e);              
            }
            if (e.hasCreatedEntities()) {
                this.toAdd.addFirst(e.getCreatedEntities());
            }
        }
        
        // Player.
        // Kill if player falls below screen.
        if (playerPos.y + SeeteufelScreen.MAP2_PLAYER_DROWN_OFFSET < this.map2Y - Gdx.graphics.getHeight() / 2) {
            this.player.damage(this.player.getMaxHealth());
        }
        // Set under water status on/off as needed.
        if (playerPos.y < this.map2WaterY) {
            if (!this.player.getIsUnderwater()) {
                this.splash.play();
                this.player.setIsUnderwater(true, true);
            }
        } else if (this.player.getIsUnderwater()) {
            this.splash.play();
            this.player.setIsUnderwater(false, true);
        }
        // Update internal logic and draw.
        this.player.update(deltaTime);
        this.player.draw(renderer);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.addFirst(this.player.getCreatedEntities());
        }
        
        // Water, which follows just below the camera.
        renderer.drawRect(ShapeType.Filled, SeeteufelScreen.WATER_COLOR,
                0, 0, SeeteufelScreen.MAP2_WATER_WIDTH, this.map2WaterY);
        
        // Health bar.
        this.playerHealth.setValue((float)this.player.getHealth() / this.player.getMaxHealth());
        this.playerHealth.draw(this.hudRenderer);
        
        // Exit the room. Do setup for room 3.
        if (!this.player.getIsInAir() &&
                playerPos.y >= (SeeteufelScreen.MAP2_HEIGHT + 0.5)* SecondMap.GROUND_DIM) {
            this.currentMap = 3;
            this.setupMap3();
        }
        
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
            this.entities.addAll(Arrays.asList(newEntities));
        }
        this.toAdd.clear();
        this.toRemove.clear();
    }
    
    private void setupMap3() {
        map3CamPos.x = SeeteufelScreen.MAP2_CAM_X;
        map3CamPos.y = this.map2Y;
        this.obstacles.remove(this.map2Ceiling);
        
        for (GameEntity entity : this.entities) {
            // TODO hackish. Get rid of this instanceof.
            if (entity instanceof Platform) {
                ((Platform) entity).damage(Platform.MAX_HEALTH);
                entity.update(1);
                if (entity.hasCreatedEntities()) {
                    this.toAdd.add(entity.getCreatedEntities());
                }
                this.toRemove.add(entity);
            }
        }
        this.seeteufelTargets.clear();
        this.explosion.play();
        
        this.enemyHealth.setPosition(SeeteufelScreen.SCREEN_LEFT - 1000, ENEMY_HEALTH_Y);
        
        this.seeteufelTargets.offer(new LinkedList<Damageable>());
        this.seeteufelTargets.get(0).add(this.player);
        Vector3 seeSideStartPos = new Vector3(MAP2_SEETEUFEL_INIT_POS);
        seeSideStartPos.y = this.seeFront.getTargetY() - SeeteufelFront.TARGET_Y_OFFSET;
        this.seeSide = new SeeteufelSide(this.t_seeteufel, this.t_tiles1,
                this.explosion, this.bombShoot, this.enemyDamage, seeSideStartPos, this.seeteufelTargets.get(0), this.obstacles);
        
        this.playerTargets.add(this.seeSide);
    }
    
    private void updateMap3(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        // If water has not yet reached the top of the shaft, keep raising it.
        if (this.map2WaterY < SeeteufelScreen.MAP2_PIXEL_HEIGHT) {
            this.map2WaterY += SeeteufelScreen.MAP2_WATER_LATENT_RISE_RATE;
            this.seeFront.setTargetY((int)map2WaterY);
        }
        
        // Have camera track player.
        Vector3 playerPos = this.player.getPosition();
        if (map3CamPos.x > playerPos.x) {
            map3CamPos.x -= Math.min(MegaPlayer.MAX_SPEED, map3CamPos.x - playerPos.x);
        } else if (map3CamPos.x < playerPos.x) {
            map3CamPos.x += Math.min(MegaPlayer.MAX_SPEED, playerPos.x - map3CamPos.x);
        }
        if (map3CamPos.y < MAP3_CAM_Y) {
            map3CamPos.y += Math.min(MegaPlayer.MAX_SPEED, MAP3_CAM_Y - map3CamPos.y);
        }
        orthoCam.position.x = map3CamPos.x;
        orthoCam.position.y = map3CamPos.y;
        orthoCam.update();
        
        // Create Renderer.
        Renderer renderer = new Renderer(orthoCam.combined);
        
        // Draw the map.
        this.map2.render(deltaTime, renderer);
        
        // Update and draw all generic entities.
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
        
        // Update/Draw player.
        if (playerPos.y + SeeteufelScreen.MAP2_PLAYER_DROWN_OFFSET < this.map2Y - Gdx.graphics.getHeight() / 2) {
            this.player.damage(this.player.getMaxHealth());
        }
        if (playerPos.y < this.map2WaterY) {
            if (!this.player.getIsUnderwater()) {
                this.splash.play();
                this.player.setIsUnderwater(true, true);
            }
        } else if (this.player.getIsUnderwater()) {
            this.splash.play();
            this.player.setIsUnderwater(false, true);
        }
        this.player.update(deltaTime);
        this.player.draw(renderer);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.addFirst(this.player.getCreatedEntities());
        }
        
        // Update/Draw Seeteufel.
        this.seeSide.setTargetY((int)this.map2WaterY);
        this.seeSide.update(deltaTime);
        this.seeSide.draw(renderer);
        if (this.seeSide.hasCreatedEntities()) {
            this.toAdd.addFirst(this.seeSide.getCreatedEntities());
        }
        
        // Draw water.
        renderer.drawRect(ShapeType.Filled, SeeteufelScreen.WATER_COLOR,
                0, SeeteufelScreen.MAP3_WATER_BASE_X, SeeteufelScreen.MAP2_WATER_WIDTH,
                this.map2WaterY - SeeteufelScreen.MAP3_WATER_BASE_X);
        
        // Health bars.
        this.playerHealth.setValue((float)this.player.getHealth() / this.player.getMaxHealth());
        this.playerHealth.draw(this.hudRenderer);
        if (this.enemyHealth.getX() < ENEMY_HEALTH_TARGET_X) {
            int moveAmount = (int) Math.min(ENEMY_HEALTHBAR_MOVE_SPEED,
                    ENEMY_HEALTH_TARGET_X - this.enemyHealth.getX());
            this.enemyHealth.setPosition(this.enemyHealth.getX() + moveAmount, this.enemyHealth.getY());
        }
        this.enemyHealth.setValue((float)this.seeSide.getHealth() / this.seeSide.getMaxHealth());
        this.enemyHealth.draw(this.hudRenderer);
        
        // Remove or add to generic entity list as needed.
        this.entities.removeAll(this.toRemove);
        for (GameEntity[] newEntities : this.toAdd) {
            this.entities.addAll(Arrays.asList(newEntities));
        }
        this.toAdd.clear();
        this.toRemove.clear();
    }
    
}
