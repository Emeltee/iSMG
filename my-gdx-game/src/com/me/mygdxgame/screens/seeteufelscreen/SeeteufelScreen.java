package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.cheats.BusterMaxCheat;
import com.me.mygdxgame.cheats.GeminiShotCheat;
import com.me.mygdxgame.cheats.HadoukenCheat;
import com.me.mygdxgame.cheats.JumpSpringsCheat;
import com.me.mygdxgame.cheats.KevlarOmegaArmorCheat;
import com.me.mygdxgame.entities.Door;
import com.me.mygdxgame.entities.Door.DoorState;
import com.me.mygdxgame.entities.InfinityWaterfall;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.entities.Refractor;
import com.me.mygdxgame.entities.SeeteufelFront;
import com.me.mygdxgame.entities.SeeteufelSide;
import com.me.mygdxgame.entities.WatchNadia;
import com.me.mygdxgame.entities.obstacles.DamagingPlatform;
import com.me.mygdxgame.entities.obstacles.FallingPlatform;
import com.me.mygdxgame.entities.obstacles.Platform;
import com.me.mygdxgame.entities.progressbars.BonneHealthBar;
import com.me.mygdxgame.entities.progressbars.MegaHealthBar;
import com.me.mygdxgame.entities.projectiles.Rubble;
import com.me.mygdxgame.screens.seeteufelscreen.maps.FirstMap;
import com.me.mygdxgame.screens.seeteufelscreen.maps.SecondMap;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameCheat;
import com.me.mygdxgame.utilities.GameCheatListener;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.GameScreen;
import com.me.mygdxgame.utilities.GameState;
import com.me.mygdxgame.utilities.GenericDamager;
import com.me.mygdxgame.utilities.GenericEntity;
import com.me.mygdxgame.utilities.Renderer;

/**
 * A more difficult platforming {@link GameScreen} intended to be used as a
 * "boss" level. Involves three distinct stages.
 * <p>
 * The first stage is simply for buildup, and for introducing the player to the
 * {@link GameEntity} they control if needed ({@link MegaPlayer}). Involves a
 * small room containing a {@link Door} and a {@link Refractor}. The Door begins
 * closed, but opens upon collecting the Refractor. Touching the Door and
 * pressing down moves to the second area.
 * 
 * The second area involves climbing upwards on a series of {@link Platform}s
 * set up like stairs, as the visible area pans upwards and a
 * {@link SeeteufelFront} destroys Platforms in a semi-random manner. Platforms
 * should never be destroyed in such a way as to render the segment unbeatable.
 * Falling below the visible area of the screen results in a loss. Part of the
 * way through the segment, the rate at which the screen pans upwards will
 * increase.
 * <p>
 * The third segment begins upon reaching the top of the second area, and
 * involves a fight with a {@link SeeteufelSide}.
 * <p>
 * Upon clearing all three segments, a Door is opened through which the player
 * may enter to win.
 */
public class SeeteufelScreen implements GameScreen {
    
    private static final int SCREEN_TOP = MyGdxGame.SCREEN_HEIGHT / 2;
    private static final int SCREEN_BOTTOM = -MyGdxGame.SCREEN_HEIGHT / 2;
    private static final int SCREEN_LEFT = -MyGdxGame.SCREEN_WIDTH / 2;
    private static final int ENEMY_HEALTHBAR_MOVE_SPEED = 40;
    private static final Vector2 PLAYER_HEALTH_POS = new Vector2(SCREEN_LEFT + 10, SCREEN_BOTTOM + 10);
    private static final int ENEMY_HEALTH_TARGET_X = (int) (SeeteufelScreen.PLAYER_HEALTH_POS.x);
    private static final int MAP1_CAM_Y = SeeteufelScreen.SCREEN_BOTTOM / 5;
    private static final float MAP2_CAM_SPEED_1 = 25;
    private static final float MAP2_CAM_SPEED_2 = 35;
    private static final float MAP2_CAM_X = (SecondMap.GROUND_DIM * SecondMap.GROUND_WIDTH) / 2.0f;
    private static final Vector3 MAP2_SEETEUFEL_INIT_POS = new Vector3(MAP2_CAM_X, SeeteufelScreen.MAP2_PIXEL_HEIGHT / 2, 0);
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
    private static final int MAP2_WATERFALL_OFFSET = 200;
    private static final int CAMERA_SHAKE = 15;
    private static final float CAMERA_SHAKE_FALLOFF = 0.5f;
    private static final float MAP2_STAIR_STEP_HEIGHT = 0.5f;
    private static final float MAP2_STAIR_FLIGHT_HEIGHT = 4.5f;
    private static final int MAP2_STAIR_FLIGHT_X_OFFSET = 5;
    private static final int MAP2_STAIR_STEP_LENGTH = 3;
    private static final int MAP2_STAIR_IGNORE_LAST_TARGETS = 2;
    private static final int MAP2_WATER_LATENT_RISE_RATE = 90;
    private static final int MAP2_WATER_WIDTH = SecondMap.GROUND_WIDTH * SecondMap.GROUND_DIM;
    private static final int MAP3_WATER_BASE_X = MAP2_PIXEL_HEIGHT - 500;
    private static final int MAP3_CAM_Y = 1700;
    private static final float MAP3_CAM_MIN_X = SecondMap.GROUND_ORIGIN_X - 
            (SecondMap.ARENA_WIDTH * SecondMap.GROUND_DIM) - SecondMap.GROUND_DIM + MyGdxGame.SCREEN_WIDTH / 2.0f;
    private static final float MAP3_CAM_MAX_X = SecondMap.GROUND_ORIGIN_X + 
            (SecondMap.GROUND_WIDTH * SecondMap.GROUND_DIM) + SecondMap.GROUND_DIM - MyGdxGame.SCREEN_WIDTH / 2.0f;
    private static final float SFX_VOLUME = 0.5f;
    private static final float MAP2_PLATFORM_DESTRUCTION_DELAY = 0.3f;
    
    private static final String WIN_MESSAGE = "Thank you for playing!";
    
    private static final int PAUSE_KEY = Keys.SHIFT_LEFT;
    private static final int FPS_KEY = Keys.BACKSLASH;
    
    // State.
    private GameState state = GameState.Running;
        
    // Each of the maps and a holder for the current map
    private FirstMap map1;
    private SecondMap map2;
    private int currentMap;
    
    // Resource files.
    private Texture t_tiles1;
    private Texture waterfall;
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
    
    // Containers for managing entities generically. Keep a pointer to any
    // entities that need specialized management.
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
    private Door room3Exit;
    private WatchNadia bonus;
    private SeeteufelFront seeFront;
    private SeeteufelSide seeSide;
    private Renderer hudRenderer;
    private InfinityWaterfall room2Fall1;
    private InfinityWaterfall room2Fall2;
    private TextureRegion missionComplete;
    
    private LinkedList<GameEntity> obstacles = new LinkedList<GameEntity>();
    private LinkedList<FallingPlatform> fallingBlocks = new LinkedList<FallingPlatform>();
    private LinkedList<FallingPlatform> fallenBlocks = new LinkedList<FallingPlatform>();
    private Collection<Damageable> ceilingTargets = new LinkedList<Damageable>();
    private LinkedList<Damageable> playerTargets = new LinkedList<Damageable>();
    private LinkedList<LinkedList<Damageable>> seeteufelTargets = new LinkedList<LinkedList<Damageable>>();
    private LinkedList<LinkedList<Damageable>> removedSeeteufelTargets = new LinkedList<LinkedList<Damageable>>();
    private LinkedList<Damageable> room2InitialStairs = new LinkedList<Damageable>();
    private LinkedList<Damageable> room2NonTargettedStairs = new LinkedList<Damageable>();
    private LinkedList<Integer> seeteufelTargetLevels = new LinkedList<Integer>();
    private Rectangle map2Ceiling = null;
    private Rectangle visibleRegion = new Rectangle();
    private LinkedList<Damageable> room2PlatformsToDestroy = new LinkedList<Damageable>();
    
    private boolean isMap2Flooding = false;
    private boolean displayFps = false;
    private boolean displayFpsButtonTrigger = false;
    private boolean firstWaterfallFell = false;
    private boolean secondWaterfallFell = false;
    private boolean isPaused = false;
    private boolean pauseButtonTrigger = false;
    private boolean bypassedSeeteufel = false;
    private boolean reachedArenaBeforeCamera = false;
    private boolean isMusicPaused = false;
    private Music currentMusic = null;
    private float map2Y = MAP2_INITIAL_CAM_Y;
    private float map2WaterY = MAP2_INITIAL_CAM_Y - MAP2_WATER_Y_OFFSET;
    private float cameraShake = 0;
    private Vector2 map3CamPos = new Vector2();
    private float map2PlatformDestuctionTimer = 0;
    private float map3ExitBridgeFallTimer = 0;
    
    // Cheat Code Support variables
    private GameCheatListener cheatEngine;
    private InputProcessor defaultProcessor;
	private GameCheat unlockedCheat;
    
    /** Container class for textures used by the SeeteufelScreen maps.*/
    public static class MapTiles {
        public Texture rockTex = null;
        public Texture wallTex = null;
        public Texture smallMazeTex = null;
        public Texture largeMazeTex = null;
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
                
                this.largeMazeTex = new Texture("img/tile6.png");
                this.largeMazeTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                
                this.pillarTex = new Texture("img/tile3.png");
                this.pillarTex.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
                
                this.greyBlockTex = new Texture("img/tile5.png");
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
        this.t_tiles1 = new Texture("img/seeteufelScreen.png");
        this.waterfall = new Texture("img/waterfall1.png");
        this.waterfall.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        this.waterfall.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        this.playerResources.load();
        
        this.explosion = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-grenade-explode1.ogg"));
        this.splash = Gdx.audio.newSound(Gdx.files.internal("sound/splash.ogg"));
        this.seeSplash = Gdx.audio.newSound(Gdx.files.internal("sound/see_crash.ogg"));
        this.bombShoot = Gdx.audio.newSound(Gdx.files.internal("sound/bomb_fire.ogg"));
        this.music1 = Gdx.audio.newMusic(Gdx.files.internal("sound/Seeteufel_the_Mighty_Intro.ogg"));
        this.music2 = Gdx.audio.newMusic(Gdx.files.internal("sound/Seeteufel_the_Mighty_Loop.ogg"));
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
        enemyDamage.play();
        enemyDamage.stop();
        
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
        this.waterfall.dispose();
        
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
        this.enemyDamage.dispose();
    }

    @Override
    public void render(float deltaTime, int difficulty, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {   

        // Determine if paused.
        if (!Gdx.input.isKeyPressed(PAUSE_KEY)) {
            this.pauseButtonTrigger = true;
        }
        if (this.pauseButtonTrigger && Gdx.input.isKeyPressed(PAUSE_KEY)) {
            this.isPaused = !this.isPaused;
            this.pauseButtonTrigger = false;
            
            // Toggle the Cheat Listener whenever paused state changes
            // Also, pause and restart music as needed.
            if (this.isPaused) { 
                Gdx.input.setInputProcessor(this.cheatEngine);
                if (this.currentMusic.isPlaying()) {
                    this.currentMusic.pause();
                    this.isMusicPaused = true;
                }
            } else {
                Gdx.input.setInputProcessor(this.defaultProcessor);
                if (this.isMusicPaused) {
                    this.currentMusic.play();
                    this.isMusicPaused = false;
                }
            }
        }
        
        // Determine if fps display is enabled.
        if (!Gdx.input.isKeyPressed(FPS_KEY)) {
            this.displayFpsButtonTrigger = true;
        }
        if (this.displayFpsButtonTrigger && Gdx.input.isKeyPressed(FPS_KEY)) {
            this.displayFps = !this.displayFps;
            this.displayFpsButtonTrigger = false;
        }
        
        // Update logic based on current map.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        if (this.currentMap == 1) {
            if (this.isPaused) {
                this.renderMap1(0, perspCam, orthoCam);
            }
            else {
                this.renderMap1(deltaTime, perspCam, orthoCam);
                this.updateMap1(deltaTime, difficulty);
            }
        } else if (this.currentMap == 2) {
            if (this.isPaused) {
                this.renderMap2(0, perspCam, orthoCam);
            }
            else {
                this.renderMap2(deltaTime, perspCam, orthoCam);
                this.updateMap2(deltaTime, difficulty);
            }
        } else if (this.currentMap == 3) {
            if (this.isPaused) {
                this.renderMap3(0, perspCam, orthoCam);
            }
            else {
                this.renderMap3(deltaTime, perspCam, orthoCam);
                this.updateMap3(deltaTime, difficulty);
            }
        } else {
            if (this.isPaused) {
                this.renderMap4(0, perspCam, orthoCam);
            }
            else {
                this.renderMap4(deltaTime, perspCam, orthoCam);
                this.updateMap4(deltaTime, difficulty);
            }
        }
        
        // Draw paused overlay or fps display, if needed.
        int halfWidth = Gdx.graphics.getWidth() / 2;
        int halfHeight = Gdx.graphics.getHeight() / 2;
        int cheatTextX = -halfWidth;
        int cheatTextY = halfHeight;
        float cheatTextHeight = this.font.getBounds("TEST").height;
        if (this.isPaused) {
            // Stop music and draw black overlay.
            this.music1.pause();
            this.music2.pause();
            this.hudRenderer.drawRect(ShapeType.Filled, new Color(0, 0, 0, 0.5f),
                    -halfWidth, -halfHeight, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        if (this.displayFps) {
            this.font.setScale(1, 1);
            String fpsString = Integer.toString(Gdx.graphics.getFramesPerSecond());
            this.hudRenderer.drawText(this.font, fpsString, cheatTextX, cheatTextY);
            cheatTextY -= cheatTextHeight;
        }
        if (this.isPaused) {        	
            // Draw text indicating all active cheats.
            for (GameCheat cheat : this.cheatEngine.getAllCheats()) {
                if (cheat.isEnabled()) {
                    this.hudRenderer.drawText(this.font, cheat.getDescription(), cheatTextX, cheatTextY);
                    cheatTextY -= cheatTextHeight;
                }
            }
            // Draw pause message.
            this.font.setScale(1.5f, 1.5f);
            int pauseOffsetX = (int)(this.font.getBounds("Paused").width / 2);
            int pauseOffsetY = (int)(this.font.getBounds("Paused").height / 2);
            this.hudRenderer.drawText(this.font, "Paused", -pauseOffsetX, -pauseOffsetY);
        }
        // Reset fonst size.
        this.font.setScale(1, 1);
        
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
        this.room2PlatformsToDestroy.clear();
        this.isMap2Flooding = false;
        this.firstWaterfallFell = false;
        this.secondWaterfallFell = false;
        this.state = GameState.Running;
        this.map2Y = SeeteufelScreen.MAP2_INITIAL_CAM_Y;
        this.map2WaterY = SeeteufelScreen.MAP2_INITIAL_CAM_Y - SeeteufelScreen.MAP2_WATER_Y_OFFSET;
        this.seeteufelTargets.clear();
        this.seeteufelTargetLevels.clear();
        this.removedSeeteufelTargets.clear();
        this.room2InitialStairs.clear();
        this.room2NonTargettedStairs.clear();
        this.cameraShake = 0;
        this.map2PlatformDestuctionTimer = 0;
        this.map3ExitBridgeFallTimer = 0;
        this.music1.stop();
        this.music2.stop();
        this.seeFront = null;
        
        // Set the intro music callback, which starts the main body of the music.
        this.music1.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                SeeteufelScreen.this.music2.play();
                SeeteufelScreen.this.music2.setLooping(true);
                SeeteufelScreen.this.currentMusic = SeeteufelScreen.this.music2;
            }});
        this.currentMusic = this.music1;
        this.isMusicPaused = false;
        
        for (GameEntity e : this.entities) {
            e.destroy();
        }
        this.entities.clear();
        
        // Create fresh instances of vital objects.
        this.player = new MegaPlayer(this.t_tiles1, this.playerResources,
                this.map1.getInitialPosition(),
                Collections.unmodifiableCollection(this.obstacles),
                Collections.unmodifiableCollection(this.playerTargets));
        this.playerHealth = new MegaHealthBar(this.t_tiles1,
                (int) SeeteufelScreen.PLAYER_HEALTH_POS.x,
                (int) SeeteufelScreen.PLAYER_HEALTH_POS.y);
        this.enemyHealth = new BonneHealthBar(this.t_tiles1, 0, 0);
        this.refractor = new Refractor(this.t_tiles1, this.itemGet, this.font,                
                (int) Math.ceil(FirstMap.PLATFORM_START_X + FirstMap.GROUND_DIM
                        * 1.5 - Refractor.REFRACTOR_W / 2),
                (int) Math.ceil(FirstMap.GROUND_START_Y + FirstMap.GROUND_DIM * 2
                        + 22));
        this.room1Exit = new Door(this.t_tiles1, this.doorOpen, this.doorClose,
                FirstMap.GROUND_END_X - (int) (FirstMap.GROUND_DIM * 1.5),
                FirstMap.GROUND_START_Y);
        
        // Setup cheatcode engine
        this.cheatEngine = new GameCheatListener(10, this.itemGet);
        this.defaultProcessor = this.player.getComboListener();
        
        this.cheatEngine.addCheat(new BusterMaxCheat(this.player));
        this.cheatEngine.addCheat(new JumpSpringsCheat(this.player));
        this.cheatEngine.addCheat(new GeminiShotCheat(this.player));
        this.cheatEngine.addCheat(new KevlarOmegaArmorCheat(this.player));
        this.cheatEngine.addCheat(new HadoukenCheat(this.player));
        
        // Set up first map.
        this.entities.add(this.refractor);
        this.entities.add(this.room1Exit);
        this.obstacles.add(this.map1.getObstacles());
        
        this.bypassedSeeteufel = false;
        this.reachedArenaBeforeCamera = false;        
    }

    @Override
    public GameState getState() {
        return this.state;
    }
    
    private void updateMap1(float deltaTime, int difficulty) {
        // Update and draw all generic entities.
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.addFirst(e);              
            }
            if (e.hasCreatedEntities()) {
                this.toAdd.addFirst(e.getCreatedEntities());
            }
        }
        
        this.map1.update(deltaTime);
        
        // Player.
        this.player.update(deltaTime);
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
            if (refractor.getHitArea()[0].overlaps(box)) {
                refractor.onTake();
            }
            
            // Exit the room. Do setup for room 2.
            if (room1Exit.getDoorState() == Door.DoorState.OPEN) {
                if (room1Exit.getHitBox().overlaps(box) &&
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
                        Gdx.input.isKeyPressed(Input.Keys.S))) {
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
    
    private void renderMap1(float deltaTime, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        
        // Update camera. Clamp position at edges of room.
        orthoCam.position.x = this.player.getPosition().x;
        float halfWidth = MyGdxGame.SCREEN_WIDTH / 2.0f;
        float minVisibleX = orthoCam.position.x - halfWidth;
        float minVisibleY = orthoCam.position.y - MyGdxGame.SCREEN_HEIGHT / 2.0f;
        float maxVisibleX = orthoCam.position.x + halfWidth;
        if (minVisibleX <= FirstMap.GROUND_ORIGIN_X - FirstMap.GROUND_DIM) {
            orthoCam.position.x = (FirstMap.GROUND_ORIGIN_X - FirstMap.GROUND_DIM) + halfWidth;
        } else if (maxVisibleX >= FirstMap.GROUND_END_X + FirstMap.GROUND_DIM) {
            orthoCam.position.x = (FirstMap.GROUND_END_X + FirstMap.GROUND_DIM) - halfWidth;
        }
        
        orthoCam.position.y = SeeteufelScreen.MAP1_CAM_Y;
        orthoCam.update();
        
        // Make renderer.
        Renderer renderer = new Renderer(orthoCam.combined);
        
        // Draw the map
        this.visibleRegion.set(minVisibleX, minVisibleY, MyGdxGame.SCREEN_WIDTH, MyGdxGame.SCREEN_HEIGHT);
        this.map1.render(deltaTime, this.visibleRegion, renderer);
        
        // Update and draw all generic entities.
        for (GameEntity e : this.entities) {
            e.draw(renderer);            
        }
        
        // Player.
        this.player.draw(renderer);
        this.playerHealth.draw(this.hudRenderer);
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
        this.obstacles.add(this.map2.getObstacles());
        
        // Generate stairs of destructable platforms and load them into lists.
        this.generateMap2Stairs();
        
        // Add the bonus tile
        this.bonus = this.makeBonusTile();
        this.entities.add(this.bonus);
        this.playerTargets.add(this.bonus);
        
        // Add moving ceiling obstacle to obstacle list.
        this.map2Ceiling = new Rectangle(0, 0, Gdx.graphics.getWidth(), 50);
        this.obstacles.add(new GenericEntity(Arrays.asList(new Rectangle[] {this.map2Ceiling})));
        
        // Set player state and initial position.
        Vector3 map2InitPos = this.map2.getInitialPosition();
        this.player.setPosition(map2InitPos);

        // Add doors.
        this.room2Entrance = new Door(this.t_tiles1, this.doorOpen,
                this.doorClose, SecondMap.GROUND_DIM, SecondMap.GROUND_DIM * (SecondMap.ENTRANCE_PLAT_HEIGHT + 1));
        this.room2Entrance.setIsOpen(DoorState.SHUT, false);
        this.room3Exit = new Door(this.t_tiles1, this.doorOpen,
                this.doorClose, (SecondMap.GROUND_WIDTH - 2) * SecondMap.GROUND_DIM - this.room2Entrance.getWidth() / 2,
                (SeeteufelScreen.MAP2_HEIGHT + 1) * SecondMap.GROUND_DIM);
        this.room3Exit.setIsOpen(DoorState.OPEN, false);
        this.entities.add(this.room2Entrance);
        this.entities.add(this.room3Exit);
        
        // Create decorative waterfall.
        this.room2Fall1 = new InfinityWaterfall(
                this.waterfall, this.t_tiles1, 100, SeeteufelScreen.MAP2_PIXEL_HEIGHT + MAP2_WATERFALL_OFFSET, 1);
        this.room2Fall2 = new InfinityWaterfall(
                this.waterfall, this.t_tiles1, 510, SeeteufelScreen.MAP2_PIXEL_HEIGHT + MAP2_WATERFALL_OFFSET, 1);
    }
    
    /** Add player dynamic obstacles. Also generates Seeteufel target list(s).*/
    private void generateMap2Stairs() {
        
        // First flight of stairs. Do not add these to seeteufelTargets, no need to destroy them.
        int currentTileX = SecondMap.GROUND_WIDTH / 2;
        int currentTileY = (int) (1.0f / SeeteufelScreen.MAP2_STAIR_STEP_HEIGHT);
        int maxTileX = SecondMap.GROUND_WIDTH - 1;
        Platform destructableTile = null;
        for (currentTileX = SecondMap.GROUND_WIDTH / 2; currentTileX < maxTileX; currentTileX++) {
            destructableTile = new Platform(this.t_tiles1, this.mapTiles, currentTileX * SecondMap.GROUND_DIM, 
                    (int) (currentTileY * SecondMap.GROUND_DIM * SeeteufelScreen.MAP2_STAIR_STEP_HEIGHT));
            this.obstacles.add(destructableTile);
            this.entities.add(destructableTile);
            this.room2InitialStairs.add(destructableTile);
            
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
        
        // Remove the second and third staircases (the first two "real" ones)
        // from being targeted, to give the player a moment to adapt.
        LinkedList<Damageable> removedList = this.seeteufelTargets.removeLast();
        this.obstacles.addAll(removedList);
        this.entities.addAll(removedList);
        this.room2NonTargettedStairs.addAll(removedList);
        removedList = this.seeteufelTargets.removeLast();
        this.obstacles.addAll(removedList);
        this.entities.addAll(removedList);
        this.room2NonTargettedStairs.addAll(removedList);
        this.seeteufelTargetLevels.removeLast();
        this.seeteufelTargetLevels.removeLast();
        
        this.seeteufelTargets.removeFirst();
        this.seeteufelTargetLevels.removeFirst();
        
        // Keep the last staircase, but prevent it from being targeted.
        // This will allow the player the chance to jump to the room 2 exit early.
        this.seeteufelTargetLevels.removeFirst();

        // At this point, only the non-targeted stairs are in the obstacle and
        // entity lists. To reduce the number of collision checks entities will
        // need to do, platforms will be added from the target list into the
        // obstacle list as the player moves up through the level. For now, add
        // only the first stairs set to be targeted.
        removedList = this.seeteufelTargets.getLast();
        this.obstacles.addAll(removedList);
        this.entities.addAll(removedList);
    }
    
    private int[] getSeeteufelTargets(Damageable[] currentTargets, boolean isUpperArea) {
        
        // Target an extra platform when you reach the upper half of the room.
        int[] targets = null;
        if (isUpperArea) {
            targets = new int[4];
        }
        else {
            targets = new int[3];
        }

        // First target, random platform that isn't the first one on the
        // stairs (so as not to create a ledge that can't be jumped up).
        // Also, leave the last MAP2_STAIR_IGNORE_LAST_TARGETS alone.
        // Destroying them has little effect on difficulty, and the
        // door goes there on the last flight.
        targets[0] = Math.max(1, (int) (Math.random() * 
                currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));

        // Second target, random platform that isn't first target or the
        // first block of a staircase. Also, don't destroy both 8 and 9,
        // as this results in an overly tricky jump.
        do {
            targets[1] = Math.max(1, (int) (Math.random() * 
                    currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));
        }
        while (targets[1] == targets[0] ||
                (targets[0] == 8 && targets[1] == 9) ||
                (targets[0] == 9 && targets[1] == 8));

        // Third target. Random platform that isn't first or second
        // target, isn't step 0, and isn't adjacent to both 0 and 1.
        do {
            targets[2] = Math.max(1, (int) (Math.random() * 
                    currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));
        }
        while (targets[2] == targets[1] ||
                targets[2] == targets[0] ||
                Math.abs(targets[2] - targets[0]) == 1 ||
                Math.abs(targets[2] - targets[1]) == 1);

        // Fourth target. Random platform that isn't first or second or third
        // target, isn't step 0, and isn't adjacent to both 0 and 1 and 2. Target
        // only if you are in the upper area of the shaft.
        if (isUpperArea) {
            do {
                targets[3] = Math.max(1, (int) (Math.random() * 
                        currentTargets.length - SeeteufelScreen.MAP2_STAIR_IGNORE_LAST_TARGETS));
            }
            while (targets[3] == targets[0] ||
                    targets[3] == targets[1] ||
                    targets[3] == targets[2] ||
                    Math.abs(targets[3] - targets[0]) == 1 ||
                    Math.abs(targets[3] - targets[1]) == 1 ||
                    Math.abs(targets[3] - targets[2]) == 1);
        }

        return targets;
    }
    
    private WatchNadia makeBonusTile() {        
        /* Randomly places the tile between four and eight flights of stairs above.
         * the bottom. Alternates left and right based on stair placement / direction.
         * Gets slightly harder to reach the higher it goes.
         */
        int bonusYOffset = (int)(System.currentTimeMillis() % 5) + 4;
        boolean leftSide = (bonusYOffset % 2 == 0);
        int bonusX = leftSide ? 0 : SecondMap.GROUND_DIM * (SecondMap.GROUND_WIDTH - 1);                
        int bonusY = 9 + (int) (MAP2_STAIR_FLIGHT_HEIGHT * bonusYOffset * SecondMap.GROUND_DIM);
        bonusY -= bonusY % SecondMap.GROUND_DIM; // Ensure Y is an even tile height
        
        WatchNadia seriouslyWtfMlt = new WatchNadia(
                this.mapTiles.bonusTex, 
                this.player, 
                this.itemGet, SeeteufelScreen.SFX_VOLUME, 
                bonusX, bonusY
        );        
        return seriouslyWtfMlt;
    }
    
    private void updateMap2(float deltaTime, int difficulty) {
        
        Vector3 playerPos = this.player.getPosition();
        
        // Activate flooding once player moves beyond a certain x.
        int targetWaterfalHeight = SeeteufelScreen.MAP2_PIXEL_HEIGHT - (int)this.map2WaterY + MAP2_WATERFALL_OFFSET;
        if (this.isMap2Flooding) {
            // Raise water only after waterfall has reached bottom.
            if (this.firstWaterfallFell) {
                if (this.map2Y < SeeteufelScreen.MAP2_CAM_MAX_Y) {
                    // Move camera faster once you reach a certain point.
                    if (this.map2Y > SeeteufelScreen.MAP2_CAM_INCREASE_SPEED_TIRGGER_Y && this.secondWaterfallFell) {
                        float movement = SeeteufelScreen.MAP2_CAM_SPEED_2 * deltaTime;
                        this.map2Y += movement;
                    } else {
                        float movement = SeeteufelScreen.MAP2_CAM_SPEED_1 * deltaTime;
                        this.map2Y += movement;
                    }
                    this.map2WaterY = this.map2Y - SeeteufelScreen.MAP2_WATER_Y_OFFSET;
                } else if (this.map2WaterY < SeeteufelScreen.MAP2_PIXEL_HEIGHT){
                    // Camera has reached the top of the shaft. Continue filling water until it is full.
                    this.map2WaterY += SeeteufelScreen.MAP2_WATER_LATENT_RISE_RATE * deltaTime;
                    if (this.map2WaterY > SeeteufelScreen.MAP2_PIXEL_HEIGHT) {
                        this.map2WaterY = SeeteufelScreen.MAP2_PIXEL_HEIGHT;
                    }
                }
            }
        } else if (playerPos.x > SeeteufelScreen.MAP2_ACTIVATION_X) {
            // Activate chase sequence.
            this.isMap2Flooding = true;
            this.music1.play();
            this.currentMusic = this.music1;
            this.seeSplash.play(SFX_VOLUME);
            this.cameraShake = SeeteufelScreen.CAMERA_SHAKE;
            this.playerHealth.setInDanger(true);
            this.seeFront = new SeeteufelFront(this.t_tiles1, this.t_tiles1,
                    this.explosion, this.seeSplash, this.bombShoot,
                    SeeteufelScreen.MAP2_SEETEUFEL_INIT_POS);
        }
        
        // Update map.
        this.map2.update(deltaTime);
        
        // Update moving barrier at the top of the screen.
        float screenTop = this.map2Y + Gdx.graphics.getHeight() / 2;
        this.map2Ceiling.setPosition(0, screenTop);
        
        // Update Seeteufel and draw waterfalls only after water starts rising.
        if (this.isMap2Flooding) {
            
            // Update falls.
            if (this.firstWaterfallFell) {
                this.room2Fall1.setHeight(targetWaterfalHeight);
            }
            else {
                this.room2Fall1.setHeight((int) (this.room2Fall1.getHeight() + 750.0f * deltaTime));
                if (this.room2Fall1.getHeight() >= targetWaterfalHeight) {
                    this.firstWaterfallFell = true;
                    this.room2Fall1.setHeight(targetWaterfalHeight);
                }
            }
            if (this.secondWaterfallFell) {
                this.room2Fall2.setHeight(targetWaterfalHeight);
            }
            else if (this.map2Y > SeeteufelScreen.MAP2_CAM_INCREASE_SPEED_TIRGGER_Y) {
                this.room2Fall2.setHeight((int) (this.room2Fall2.getHeight() + 750.0f * deltaTime));
                if (this.room2Fall2.getHeight() >= targetWaterfalHeight) {
                    this.secondWaterfallFell = true;
                    this.room2Fall2.setHeight(targetWaterfalHeight);
                }
            }
            this.room2Fall1.update(deltaTime);
            this.room2Fall2.update(deltaTime);
            
            // Update Seeteufel.
            this.seeFront.setTargetY((int)this.map2WaterY);
            this.seeFront.update(deltaTime);
            
            // Attack if water level is one block below the next staircase.
            if (!this.seeteufelTargetLevels.isEmpty()) {
                
                // Select and attack targets.
                Integer currentLevel = this.seeteufelTargetLevels.removeLast();
                if (this.map2WaterY >= currentLevel - SeeteufelScreen.MAP2_ENEMY_ATTACK_OFFSET) {
                    LinkedList<Damageable> currentTargetList = this.seeteufelTargets.removeLast();
                    Damageable[] currentTargets = new Damageable[currentTargetList.size()];
                    currentTargets = currentTargetList.toArray(currentTargets);
                    int[] targets = this.getSeeteufelTargets(currentTargets, this.secondWaterfallFell);
                    
                    // Add/remove obstacles and entities based on what is in
                    // attack range, determined by the current targets. Platforms that
                    // fall below the active area will be added to a list to be destroyed.
                    LinkedList<Damageable> removedList = null;
                    this.removedSeeteufelTargets.add(currentTargetList);
                    if (this.removedSeeteufelTargets.size() > 2) {
                        removedList = this.removedSeeteufelTargets.removeFirst();
                        this.room2PlatformsToDestroy.addAll(removedList);
                        
                        // Remove untargeted platforms near the bottom of the room.
                        // Only needs to be done once, the first time targets start getting removed.
                        if (!this.room2NonTargettedStairs.isEmpty()) {
                            GenericDamager damager = new GenericDamager(1, 0);
                            for (Damageable platform : this.room2NonTargettedStairs) {
                                platform.damage(damager);
                            }
                            this.room2NonTargettedStairs.clear();
                            this.explosion.stop();this.explosion.play();
                        }
                    }
                    
                    // Add next flight of stairs as obstacles.
                    if (!this.seeteufelTargets.isEmpty()) {
                        removedList = this.seeteufelTargets.getLast();
                        this.obstacles.addAll(removedList);
                        this.entities.addAll(removedList);
                    }
                    
                    // Attack targets.
                    for (int x : targets) {
                        this.seeFront.attack(currentTargets[x]);
                    }
                    
                    // If Seeteufel has nothing left to target, move remaining
                    // platforms to waiting list to be destroyed.
                    if (this.seeteufelTargetLevels.isEmpty()) {
                        while (!this.removedSeeteufelTargets.isEmpty()) {
                            this.room2PlatformsToDestroy.addAll(this.removedSeeteufelTargets.removeFirst());
                        }
                        while (!this.seeteufelTargets.isEmpty()) {
                            this.room2PlatformsToDestroy.addAll(this.seeteufelTargets.removeFirst());
                        }
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
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            // TODO hackish
            if (e instanceof Rubble) {
                ((Rubble) e).setWaterLevel(this.map2WaterY, this.splash);
            }
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.addFirst(e);              
            }
            if (e.hasCreatedEntities()) {
                this.toAdd.addFirst(e.getCreatedEntities());
            }
        }
        
        // Slowly destroy old platforms from the bottom up.
        if (!this.room2PlatformsToDestroy.isEmpty()) {
            this.map2PlatformDestuctionTimer += deltaTime;
            if (this.map2PlatformDestuctionTimer >= MAP2_PLATFORM_DESTRUCTION_DELAY) {
                Damageable target = this.room2PlatformsToDestroy.removeFirst();
                while (!this.room2PlatformsToDestroy.isEmpty() &&
                        target.getState() == EntityState.Destroyed) {
                    target = this.room2PlatformsToDestroy.removeFirst();
                }
                target.damage(new GenericDamager(1, 0));
                this.map2PlatformDestuctionTimer = 0;
                this.explosion.stop();
                this.explosion.play();
            }
        }
        
        // Player.
        // Kill if player falls below screen.
        if (playerPos.y + SeeteufelScreen.MAP2_PLAYER_DROWN_OFFSET < this.map2Y - Gdx.graphics.getHeight() / 2) {
            this.player.destroy();
        }
        // Set under water state on/off as needed.
        if (playerPos.y < this.map2WaterY) {
            if (!this.player.getIsUnderwater()) {
                this.splash.play(SFX_VOLUME);
                this.player.setIsUnderwater(true, true);
            }
        } else if (this.player.getIsUnderwater()) {
            //this.splash.play(SFX_VOLUME);
            this.player.setIsUnderwater(false, false);
        }
        // Update internal logic and draw.
        this.player.update(deltaTime);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.addFirst(this.player.getCreatedEntities());
        }
        
        // Health bar.
        this.playerHealth.setValue((float)this.player.getHealth() / this.player.getMaxHealth());
        
        // Exit the room when player reaches the arena. Do setup for room 3.
        if (!this.player.getIsInAir() &&
                playerPos.y >= (SeeteufelScreen.MAP2_HEIGHT + 0.5) * SecondMap.GROUND_DIM &&
                playerPos.x <= SecondMap.GROUND_DIM) {
            this.currentMap = 3;
            this.setupMap3();
            
            // If player reaches top before the second-to-last staircase gets
            // destroyed, set flag to unlock jump springs.
            if (this.seeteufelTargetLevels.isEmpty() && 
                    this.room2PlatformsToDestroy.size() >= SecondMap.GROUND_WIDTH - MAP2_STAIR_FLIGHT_X_OFFSET) {
                this.reachedArenaBeforeCamera = true;
                System.out.println(this.room2PlatformsToDestroy.size());
            }
        }
        
        // Remove or add to generic entity list as needed.
        this.entities.removeAll(this.toRemove);
        for (GameEntity e : this.toRemove) {
            // Remove from obstacle list if this was one of the platforms.
            // TODO Hackish, fix eventually.
            if (e instanceof Platform) {
                this.obstacles.remove(e);
            }
        }
        for (GameEntity[] newEntities : this.toAdd) {
            this.entities.addAll(Arrays.asList(newEntities));
        }
        this.toAdd.clear();
        this.toRemove.clear();
        
        // Check if player has reached the final door and is exiting, bypassing the fight.
        // Possible with the jump springs.
        if (!this.player.getIsInAir() && 
                this.player.getHitArea()[0].overlaps(this.room3Exit.getHitArea()[0]) &&
                (Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
                Gdx.input.isKeyPressed(Input.Keys.S))) {
            
            this.bypassedSeeteufel = true;
            
            setupMap4();
            this.currentMap = 4;
        }
    }
    
    private void renderMap2(float deltaTime, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        // Update camera according to current map state.
        // Rises slowly as room floods. Stays static prior.
        orthoCam.position.x = SeeteufelScreen.MAP2_CAM_X;
        orthoCam.position.y = (float) Math.floor(this.map2Y + this.cameraShake);
        orthoCam.update();
        
        // Update camera shake.
        if (this.cameraShake > 0) {
            this.cameraShake = -this.cameraShake + SeeteufelScreen.CAMERA_SHAKE_FALLOFF;
        } else if (this.cameraShake < 0) {
            this.cameraShake = -this.cameraShake - SeeteufelScreen.CAMERA_SHAKE_FALLOFF;
        }
        
        // Create Renderer.
        Renderer renderer = new Renderer(orthoCam.combined);
        
        // Draw the map
        float minVisibleX = orthoCam.position.x - MyGdxGame.SCREEN_WIDTH / 2.0f;
        float minVisibleY = orthoCam.position.y - MyGdxGame.SCREEN_HEIGHT / 2.0f;
        this.visibleRegion.set(minVisibleX, minVisibleY, MyGdxGame.SCREEN_WIDTH, MyGdxGame.SCREEN_HEIGHT);
        this.map2.render(deltaTime, this.visibleRegion, renderer);
        
        this.room2Fall1.draw(renderer);
        this.room2Fall2.draw(renderer);
        if (this.seeFront != null) {
            this.seeFront.draw(renderer);
        }
        
        // Update and draw all generic entities in range.
        float entityY = 0;
        float screenBottom = orthoCam.position.y - (Gdx.graphics.getHeight() / 2) - Door.DOOR_H;
        float screenTop = orthoCam.position.y + Gdx.graphics.getHeight() / 2;
        for (GameEntity e : this.entities) {
            entityY = e.getPosition().y;
            if (entityY < screenTop && entityY > screenBottom) {
                e.draw(renderer); 
            }
        }
        
        // Player.
        this.player.draw(renderer);
        
        // Water, which follows just below the camera.
        renderer.drawRect(ShapeType.Filled, SeeteufelScreen.WATER_COLOR,
                0, 0, SeeteufelScreen.MAP2_WATER_WIDTH, this.map2WaterY);
        
        // Health bar.
        this.playerHealth.draw(this.hudRenderer);
    }
    
    private void setupMap3() {
        // Match the third area camera position with the second area cam.
        map3CamPos.x = SeeteufelScreen.MAP2_CAM_X;
        map3CamPos.y = this.map2Y;
        
        // Remove moving ceiling and all room 2 platforms.
        this.obstacles.clear();
        this.obstacles.add(this.map2.getObstacles());
        
        // Destroy all remaining platforms from room 2.
        GenericDamager damager = new GenericDamager(1, 0);
        for (GameEntity entity : this.entities) {
            // TODO hackish. Get rid of this instanceof.
            if (entity instanceof Platform) {
                ((Platform) entity).damage(damager);
            }
        }
        this.explosion.play(SFX_VOLUME);
        
        int enemyHealthY = SCREEN_TOP - this.enemyHealth.getHeight() - 10;
        this.enemyHealth.setPosition(SeeteufelScreen.SCREEN_LEFT - 1000, enemyHealthY);
        
        // Set player as the main target for enemy.
        this.seeteufelTargets.clear();
        this.seeteufelTargets.offer(new LinkedList<Damageable>());
        this.seeteufelTargets.get(0).add(this.player);
        
        // Set up a bunch of ceiling tiles, attacked as part of the battle sequence.
        ceilingTargets.clear();
        for (int x = 0; x < SecondMap.ARENA_WIDTH; x++) {
            this.ceilingTargets.add(new DamagingPlatform(this.t_tiles1, this.mapTiles, -x * SecondMap.GROUND_DIM,
                    (int) (SecondMap.GROUND_DIM * (SeeteufelScreen.MAP2_HEIGHT + SecondMap.ARENA_HEIGHT - 1)),
                    this.seeteufelTargets.get(0)));
        }
        
        // Create SeeteufelSide as the same position as SeeteufelFront.
        Vector3 seeSideStartPos = new Vector3(MAP2_SEETEUFEL_INIT_POS);
        seeSideStartPos.y = this.seeFront.getTargetY();
        this.seeSide = new SeeteufelSide(this.t_tiles1, this.explosion,
                this.bombShoot, this.enemyDamage, this.playerResources,
                seeSideStartPos, this.seeteufelTargets.get(0), this.obstacles,
                this.ceilingTargets);

        // Set SeeteufelSide as the player's sole target.
        this.playerTargets.add(this.seeSide);
        
        // Set up falling blocks.
        this.fallingBlocks.clear();
        this.fallenBlocks.clear();
        int fallingBlockY = (SecondMap.ARENA_HEIGHT + SeeteufelScreen.MAP2_HEIGHT - 1) * SecondMap.GROUND_DIM;
        int fallingBlockTargetY = SeeteufelScreen.MAP2_HEIGHT * SecondMap.GROUND_DIM;
        for (int x = 1; x < SecondMap.GROUND_WIDTH - 3; x++) {
            fallingBlocks.add(new FallingPlatform(this.t_tiles1, this.mapTiles, this.seeSplash,
                    x * SecondMap.GROUND_DIM, fallingBlockY, fallingBlockTargetY));
            this.obstacles.add(fallingBlocks.getLast());
        }
    }
    
    private void updateMap3(float deltaTime, int difficulty) {
        
        // Have camera track player. Clamp at edges of room.
        Vector3 playerPos = this.player.getPosition();
        if (map3CamPos.x > playerPos.x) {
            map3CamPos.x -= Math.min(MegaPlayer.MAX_SPEED * deltaTime, map3CamPos.x - playerPos.x);
        } else if (map3CamPos.x < playerPos.x) {
            map3CamPos.x += Math.min(MegaPlayer.MAX_SPEED * deltaTime, playerPos.x - map3CamPos.x);
        }
        if (map3CamPos.y < MAP3_CAM_Y) {
            map3CamPos.y += Math.min(MegaPlayer.MAX_SPEED * deltaTime, MAP3_CAM_Y - map3CamPos.y);
        }
        if (map3CamPos.x < MAP3_CAM_MIN_X) {
            map3CamPos.x = MAP3_CAM_MIN_X;
        }
        else if (map3CamPos.x > MAP3_CAM_MAX_X) {
            map3CamPos.x = MAP3_CAM_MAX_X;
        }
        
        // If water has not yet reached the top of the shaft, keep raising it.
        if (this.map2WaterY < SeeteufelScreen.MAP2_PIXEL_HEIGHT) {
            this.map2WaterY += SeeteufelScreen.MAP2_WATER_LATENT_RISE_RATE * deltaTime;
            if (this.map2WaterY > SeeteufelScreen.MAP2_PIXEL_HEIGHT) {
                this.map2WaterY = SeeteufelScreen.MAP2_PIXEL_HEIGHT;
            }
            this.seeFront.setTargetY((int)map2WaterY);
        }
        
        // Update map.
        this.map2.update(deltaTime);
        
        // Update falls.
        this.room2Fall1.update(deltaTime);
        this.room2Fall1.setHeight(SeeteufelScreen.MAP2_PIXEL_HEIGHT - (int)this.map2WaterY + MAP2_WATERFALL_OFFSET);
        this.room2Fall2.update(deltaTime);
        this.room2Fall2.setHeight(SeeteufelScreen.MAP2_PIXEL_HEIGHT - (int)this.map2WaterY + MAP2_WATERFALL_OFFSET);
        
        // Update the falling blocks as needed.
        for (FallingPlatform platform : this.fallingBlocks) {
            platform.update(deltaTime);
        }
        for (FallingPlatform platform : this.fallenBlocks) {
            platform.update(deltaTime);
        }
        for (Damageable platform : this.ceilingTargets) {
            platform.update(deltaTime);
            if (platform.getState() == EntityState.Destroyed) {
                this.toRemove.addFirst(platform);              
            }
            if (platform.hasCreatedEntities()) {
                this.toAdd.addFirst(platform.getCreatedEntities());
            }
        }
        
        // Update and draw all other generic entities.
        for (GameEntity e : this.entities) {
            e.update(deltaTime);
            if (e.getState() == EntityState.Destroyed) {
                this.toRemove.addFirst(e);              
            }
            else if (e instanceof Rubble) {
                // TODO hackish. Set water level for new rubble.
                ((Rubble) e).setWaterLevel(this.map2WaterY, this.splash);
            }
            if (e.hasCreatedEntities()) {
                this.toAdd.addFirst(e.getCreatedEntities());
            }
        }
        
        // Update/Draw player.
        if (playerPos.y + SeeteufelScreen.MAP2_PLAYER_DROWN_OFFSET < this.map2Y - Gdx.graphics.getHeight() / 2) {
            this.player.destroy();
        }
        if (playerPos.y < this.map2WaterY) {
            if (!this.player.getIsUnderwater()) {
                this.splash.play(SFX_VOLUME);
                this.player.setIsUnderwater(true, true);
            }
        } else if (this.player.getIsUnderwater()) {
//            this.splash.play(SFX_VOLUME);
            this.player.setIsUnderwater(false, true);
        }
        this.player.update(deltaTime);
        if (this.player.hasCreatedEntities()) {
            this.toAdd.addFirst(this.player.getCreatedEntities());
        }
        
        // Update/Draw Seeteufel.
        if (this.seeSide.getState() != EntityState.Destroyed) {
            this.seeSide.setTargetY((int)this.map2WaterY);
            this.seeSide.update(deltaTime);
            if (this.seeSide.hasCreatedEntities()) {
                this.toAdd.addFirst(this.seeSide.getCreatedEntities());
            }
        }
        
        // Health bars.
        this.playerHealth.setValue((float)this.player.getHealth() / this.player.getMaxHealth());
        this.playerHealth.draw(this.hudRenderer);
        if (this.enemyHealth.getX() < ENEMY_HEALTH_TARGET_X) {
            int moveAmount = (int) Math.min(ENEMY_HEALTHBAR_MOVE_SPEED,
                    ENEMY_HEALTH_TARGET_X - this.enemyHealth.getX());
            this.enemyHealth.setPosition(this.enemyHealth.getX() + moveAmount, this.enemyHealth.getY());
        }
        this.enemyHealth.setValue((float)this.seeSide.getHealth() / this.seeSide.getMaxHealth());
        if (this.enemyHealth.getValue() > 0) {
            this.enemyHealth.draw(this.hudRenderer);
        }
        
        // Remove or add to generic entity list as needed.
        this.entities.removeAll(this.toRemove);
        for (GameEntity[] newEntities : this.toAdd) {
            this.entities.addAll(Arrays.asList(newEntities));
        }
        this.toAdd.clear();
        this.toRemove.clear();
        
        // Ending sequence and victory conditions.
        if (this.seeSide.getHealth() <= 0) {
            if (this.seeSide.getState() == EntityState.Destroyed) {
                
                this.playerHealth.setInDanger(false);
                
                if (!this.fallingBlocks.isEmpty()) {
                    this.map3ExitBridgeFallTimer += deltaTime;
                    if (this.map3ExitBridgeFallTimer >= 0.1f) {
                        FallingPlatform currentBlock = this.fallingBlocks.removeFirst();
                        currentBlock.fall();
                        this.fallenBlocks.add(currentBlock);
                        this.map3ExitBridgeFallTimer = 0;
                    }
                }
                
                this.music1.stop();
                this.music2.stop();
                
                if (!this.player.getIsInAir() && 
                        this.player.getHitArea()[0].overlaps(this.room3Exit.getHitArea()[0]) &&
                        (Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
                        Gdx.input.isKeyPressed(Input.Keys.S))) {
                    
                    setupMap4();
                    this.currentMap = 4;
                }
            }
            else {
                if (this.cameraShake == 0) {
                    this.cameraShake = SeeteufelScreen.CAMERA_SHAKE;
                }
            }
        }
    }
    
    private void renderMap3(float deltaTime, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        
        // Camera.
        orthoCam.position.x = map3CamPos.x;
        orthoCam.position.y = map3CamPos.y + this.cameraShake;
        orthoCam.update();
        
        // Camera shake.
        if (this.cameraShake > 0) {
            this.cameraShake = -this.cameraShake + SeeteufelScreen.CAMERA_SHAKE_FALLOFF;
        } else if (this.cameraShake < 0) {
            this.cameraShake = -this.cameraShake - SeeteufelScreen.CAMERA_SHAKE_FALLOFF;
        }
        
        // Create Renderer.
        Renderer renderer = new Renderer(orthoCam.combined);
        
        // Draw the map.
        float halfWidth = MyGdxGame.SCREEN_WIDTH / 2.0f;
        float minVisibleX = orthoCam.position.x - halfWidth;
        float minVisibleY = orthoCam.position.y - MyGdxGame.SCREEN_HEIGHT / 2.0f;
        this.visibleRegion.set(minVisibleX, minVisibleY, MyGdxGame.SCREEN_WIDTH, MyGdxGame.SCREEN_HEIGHT);
        this.map2.render(deltaTime, this.visibleRegion, renderer);
        
        // Update falls.
        this.room2Fall1.draw(renderer);
        this.room2Fall2.draw(renderer);
        
        // Update the falling blocks as needed.
        for (FallingPlatform platform : this.fallingBlocks) {
            platform.draw(renderer);
        }
        for (FallingPlatform platform : this.fallenBlocks) {
            platform.draw(renderer);
        }
        for (Damageable platform : this.ceilingTargets) {
            platform.draw(renderer);
        }
        
        // Update and draw all other generic entities.
        for (GameEntity e : this.entities) {
            e.draw(renderer); 
        }
        
        // Draw player.
        this.player.draw(renderer);
        
        // Update/Draw Seeteufel.
        if (this.seeSide.getState() != EntityState.Destroyed) {
            this.seeSide.draw(renderer);
        }
        
        // Draw water.
        renderer.drawRect(ShapeType.Filled, SeeteufelScreen.WATER_COLOR,
                0, SeeteufelScreen.MAP3_WATER_BASE_X, SeeteufelScreen.MAP2_WATER_WIDTH,
                this.map2WaterY - SeeteufelScreen.MAP3_WATER_BASE_X);
        
        // Health bars.
        this.playerHealth.draw(this.hudRenderer);
        if (this.enemyHealth.getValue() > 0) {
            this.enemyHealth.draw(this.hudRenderer);
        }
    }
    
    private void setupMap4() {
        this.missionComplete = new TextureRegion(this.t_tiles1, 0, 594, 208, 24);
        
        this.music1.stop();
        this.music2.stop();
        
        this.unlockedCheat = this.getUnlockedCheat();
    }
    
    private void updateMap4(float deltaTime, int difficulty) {
        // Play again.
        if (Gdx.input.isKeyPressed(Keys.ENTER)) {
            this.state = GameState.Win;
        }
    }
    
    private void renderMap4(float deltaTime, PerspectiveCamera perspCam, OrthographicCamera orthoCam) {
        // Clear screen.
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        float baseY = 3*this.missionComplete.getRegionHeight();
        
        // Congratulations.
        this.hudRenderer.drawRegion(this.missionComplete, -this.missionComplete.getRegionWidth() / 2.0f, baseY);
        font.setScale(1);
        TextBounds bounds = font.getBounds(WIN_MESSAGE);
        this.hudRenderer.drawText(font, WIN_MESSAGE, 
                -bounds.width / 2.0f, baseY-this.missionComplete.getRegionHeight() / 2.0f);
        bounds = font.getBounds("Press Enter to Play Again");
        this.hudRenderer.drawText(font, "Press Enter to Play Again", -bounds.width / 2.0f, baseY-this.missionComplete.getRegionHeight()/2.0f-bounds.height);
        
        final String [] CREDITS = {
				"",
				"Created by:",
				"Emeltee, Pitch and Canticleer Blues",
				"",
				"Based on original concept \"Seeteufel\" by MegaMan Legends Station",
				"",
				"Special Thanks:",
				"Dashe, Raijin, Inclover, General Specific, Trege,", 
				"Buster Cannon, Rockxas, Rockman Striker and Hirovoid",
				"",
				"Uses Resources from Sky Pirate Arcade",
				"Music by Dash Myoku" 
				};

		for (int i = 1; i<=CREDITS.length; i++) {
			String current  = CREDITS[i-1];
			TextBounds temp = font.getBounds(current);
			this.hudRenderer.drawText(font,  current, -temp.width / 2.0f, 
					baseY - (2 * this.missionComplete.getRegionHeight()) - (i+1.0f) * temp.height);
			
		}
        
        
        
        showUnlockedCheat(this.unlockedCheat);
    }
    
    private void showUnlockedCheat(GameCheat cheat) {
    	// Default message for no cheats unlocked
        String cheatHead = "";
        String cheatName = "Not all hidden treasures are refractors";
        String cheatSeq = "try again";

        // Unlocked Cheat Code
        if (this.unlockedCheat != null) {
        	// Unlocked cheat details
        	cheatHead = "! New Ability Unlocked !";
        	cheatName = "GET EQUIPPED WITH: " + this.unlockedCheat.getDescription();
        	cheatSeq = this.unlockedCheat.getSequenceString();
        } else {
        	if (this.player.isGeminiEnabled()) {
        		// No cheats unlocked, but Gemini Buster Discovered
        		cheatHead = "! CONGRATULATIONS !";
        		cheatName = "You discovered the secret weapon 'Gemini Buster'.";
        		cheatSeq = "There are more hidden secrets for stylish players. Keep playing!";
        	}
        }
                
        TextBounds bounds = font.getBounds(cheatHead);
        this.hudRenderer.drawText(font, cheatHead, -bounds.width / 2.0f, SCREEN_BOTTOM + 4.5f * bounds.height);
        bounds = font.getBounds(cheatName);
        this.hudRenderer.drawText(font, cheatName, -bounds.width / 2.0f, SCREEN_BOTTOM + 3.0f * bounds.height);
        bounds = font.getBounds(cheatSeq);
        this.hudRenderer.drawText(font, cheatSeq,  -bounds.width / 2.0f, SCREEN_BOTTOM + 1.5f * bounds.height);
    }
    
    private GameCheat getUnlockedCheat() {
    	// Give up the JumpSprings cheat if player reaches third area before camera stops rising.
    	// BusterMax if they think to use the jump springs to bypass the Seeteufel.
    	// Armor if they finished with more than 2/3 health.
    	// GeminiShot code if everything else is enabled.
        // Having the BusterMax equipped automatically precluded receiving any more codes (other than gemini).
        
    	GameCheat tempBusterMax = new BusterMaxCheat(player),
    			  tempGeminiShot = new GeminiShotCheat(player),
    			  tempJumpSprings = new JumpSpringsCheat(player),
    			  tempKevlarOmega = new KevlarOmegaArmorCheat(player),
    			  tempHadouken = new HadoukenCheat(player);
    	
    	if (this.cheatEngine.getEnabledCheats().size() == this.cheatEngine.getAllCheats().size()
    	    || (this.cheatEngine.getEnabledCheats().size() == this.cheatEngine.getAllCheats().size() - 1
    	        && !this.cheatEngine.hasEnabledCheat(tempHadouken))) {
    		// All cheats unlocked, show cheat code for GeminiBuster.
    		return tempGeminiShot;
    	} else if (this.bypassedSeeteufel &&
    	        !this.cheatEngine.hasEnabledCheat(tempBusterMax)) {
    		// Skipped Seeteufel with jump springs. Show BusterMax cheat.
    		return tempBusterMax;
    	} else if (!this.bypassedSeeteufel &&
    	            this.player.getHealth() >= (2.0f / 3.0f * (float)this.player.getMaxHealth())
    			    && !this.cheatEngine.hasEnabledCheat(tempBusterMax)
    			    && !this.cheatEngine.hasEnabledCheat(tempKevlarOmega)) {
    		// Won with with 2/3 health and MegaBuster, show KevlarOmega cheat.
    		return tempKevlarOmega;
    	} else if (this.reachedArenaBeforeCamera && 
    			   !this.cheatEngine.hasEnabledCheat(tempJumpSprings)) {
    		// Reached top of shaft before the camera, show Jump Springs Cheat
    		return tempJumpSprings;
    	} else {    	
    		// None of the above, return nothing
    		return null;
    	}
    }
    
}
