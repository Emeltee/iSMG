package com.me.mygdxgame;

import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.me.mygdxgame.maps.FirstMap;
import com.me.mygdxgame.screens.GameScreen;
import com.me.mygdxgame.screens.maptestscreen.MapTestScreen;

/**
 * Some game. Uses libGDX.
 */
public class MyGdxGame implements ApplicationListener {

    /**
     * Static link to the last created game, set upon create(). Probably bad
     * practice, but convenient.
     */
    public static MyGdxGame currentGame = null;
    /** Width of the screen.*/
    public static final int SCREEN_WIDTH = 700;
    /** Height of the screen.*/
    public static final int SCREEN_HEIGHT = 376;
    /** Initial number of player lives. */
    public static final int MAX_LIVES = 3;

    // Public communal resources. Probably bad practice, but works for now.
    /** Communal SpriteBatch for drawing. */
    public SpriteBatch spriteBatch;
    /** Communal ShapeRenderer for primitives.*/
    public ShapeRenderer shapeRenderer;
    /** Communal OrthographicCamera for adjusting view. */
    public OrthographicCamera orthoCamera;
    /** Communal PerspectiveCamera for adjusting view. */
    public PerspectiveCamera perspectiveCamera;

    private GameScreen currentGameScreen = null;
    /** List of GameScreens containing regular games. */
    private ArrayList<GameScreen> games = new ArrayList<GameScreen>();
    /** List of miniboss-level GameScreens. */
    private ArrayList<GameScreen> miniBossGames = new ArrayList<GameScreen>();
    /** List of boss-level GameScreens. */
    private ArrayList<GameScreen> bossGames = new ArrayList<GameScreen>();
    /** Number of player lives remaining.*/
    private int lives = MyGdxGame.MAX_LIVES;
    /** Current difficulty level.*/
    private int difficulty = 0;

    /** Constructor */
    public MyGdxGame() {

    }

    @Override
    public void create() {
        try {
            // Set static link to this Game for easy access to communal
            // resources.
            MyGdxGame.currentGame = this;

            // Load GameScreens and all resources.
            // TODO May want to load resources lazily, and pull out common
            // resources for storage in a public place. Also, can do this with
            // reflection.
            this.games.add(new MapTestScreen(new FirstMap(true)));
            //this.games.add(new MapTestScreen(null));

            for (GameScreen game : this.games) {
                game.load();
            }

            // Set and initialize first screen.
            this.currentGameScreen = this.games.get((int)(Math.random() * this.games.size()));

            // Set up Cameras.
            Gdx.graphics.setDisplayMode(1, 1, true);
            this.orthoCamera = new OrthographicCamera(Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight());
            this.orthoCamera.update();
            this.perspectiveCamera = new PerspectiveCamera(60,
                    Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.perspectiveCamera.far = 5000.0f;
            this.perspectiveCamera.position.z = 256.0f;
            this.perspectiveCamera.update();

            // Set up SpriteBatch and shapeRenderer.
            this.spriteBatch = new SpriteBatch();
            this.shapeRenderer = new ShapeRenderer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resize(int width, int height) {

        // Enforce a constant screen size.
        Gdx.graphics.setDisplayMode(MyGdxGame.SCREEN_WIDTH, MyGdxGame.SCREEN_HEIGHT,
                Gdx.graphics.isFullscreen());

        // Update cameras.
        this.orthoCamera.viewportWidth = Gdx.graphics.getWidth();
        this.orthoCamera.viewportHeight = Gdx.graphics.getHeight();
        this.orthoCamera.update();
        this.perspectiveCamera.viewportWidth = Gdx.graphics.getWidth();
        this.perspectiveCamera.viewportHeight = Gdx.graphics.getHeight();
        this.perspectiveCamera.update();
    }

    @Override
    public void render() {

        //        // TODO Should choose boss/miniboss screens as appropriate.
        //        // TODO Need to to proper screen transitions.
        //        //TODO If lives == 0, transition to Game Over.
        //
        //        // If current screen has not reached a win or lose state, keep running.
        //        if (this.currentGameScreen.getState() == GameState.Running) {
        //            this.currentGameScreen.render(Gdx.graphics.getDeltaTime(), this.difficulty);
        //        } else {
        //            // If current screen has reached either the win or lose state, time to pick a new screen.
        //
        //            // If screen has reached the lose state, reduce lives by one.
        //            if (this.currentGameScreen.getState() == GameState.Lose) {
        //                this.lives--;
        //            }
        //
        //            // Pick the next screen. Ensure that it is not the same as the current screen.
        //            GameScreen lastScreen = this.currentGameScreen;
        //            while (lastScreen == this.currentGameScreen) {
        //                this.currentGameScreen = this.games.get((int)(Math.random() * this.games.size()));
        //            }
        //
        //            // Initialize new screen.
        //            this.currentGameScreen.initialize();
        //        }

        // TEMP. Just run current screen. Change once we have enough to rotate between.
        this.currentGameScreen.render(Gdx.graphics.getDeltaTime(), this.difficulty);
    }

    @Override
    public void pause() {
        // Not applicable.
    }

    @Override
    public void resume() {
        // Not applicable.
    }

    @Override
    public void dispose() {
        for (GameScreen game : this.games) {
            game.unload();
        }
        for (GameScreen game : this.miniBossGames) {
            game.unload();
        }
        for (GameScreen game : this.bossGames) {
            game.unload();
        }
    }
}