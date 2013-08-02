package com.me.mygdxgame.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.GameMap;

public class FirstMap implements GameMap {

	private static final String TEXTURE_PATH = "img/seeTiles1.png";
	private Texture spriteSheet;	
	private boolean resourcesLoaded;
	private int animationFrame;	
	
	private TextureRegion rockSprite;
	private TextureRegion wallSprite;
	private TextureRegion smallMazeSprite;
	private TextureRegion largeMazeSprite;
	private TextureRegion idkSprite;
	private TextureRegion goalSprite;
	private TextureRegion nadiaSprite;
	
	private TextureRegion[] borderFrames;
	
	private TextureRegion grate;
	private TextureRegion[] waterfall;
	
	// Since apparently this is a 3D space, which contains a sprawling infinity of
	// floating point nonsense, and no precise integer pixel coordinates that one might
	// use to find, say, the lower left corner of the screen ("field of view", whatever),
	// I'm making everything on screen relative to the ground, where the ground starts,
	// what size the ground tile is and how much it repeats. Because apparently as long
	// as everything makes sense relative to everything else, all you have to do is put
	// the camera in the right spot, and you can kind of get it to look the way you want..
	
	private final int GROUND_DIM = 44; // Width of ground tile
	private final int OFFSET = 5 * GROUND_DIM; // Extra padding tiles, since this is so imprecise
	private final int GROUND_ORIGIN_X = (-1 * MyGdxGame.SCREEN_WIDTH / 2) - OFFSET,  // Origin X point, off-screen to the left 
					  GROUND_ORIGIN_Y = (-1 * MyGdxGame.SCREEN_HEIGHT / 2) - OFFSET, // Origin Y point, off-screen below					  
					  GROUND_HEIGHT = 7, // Rows of ground tile
					  GROUND_WIDTH = 20; // Columns of ground tile
	
	// Opposites of the Origins (GROUND_START_X is GROUND ORIGIN_X; GROUND_END_Y is GROUND_ORIGIN_Y) 
	private final int GROUND_END_X = GROUND_ORIGIN_X + (GROUND_WIDTH * GROUND_DIM), // Where ground stops horizontally
					  GROUND_START_Y = GROUND_ORIGIN_Y + (GROUND_HEIGHT * GROUND_DIM); // Where ground begins vertically
	
	private final int PLATFORM_START_X = GROUND_ORIGIN_X + (6 * GROUND_DIM);
	
	@Override
	public void load() {
		if (!this.resourcesLoaded) { 
			this.spriteSheet = new Texture(Gdx.files.internal(TEXTURE_PATH));
			this.spriteSheet.setFilter(TextureFilter.Linear, TextureFilter.Nearest);
			this.rockSprite = new TextureRegion(spriteSheet, 0, 57, 44, 44);
			this.wallSprite = new TextureRegion(spriteSheet, 209, 17, 44, 44);
			this.smallMazeSprite = new TextureRegion(spriteSheet, 165, 17, 44, 44);
			this.largeMazeSprite = new TextureRegion(spriteSheet, 45, 0, 120, 120);
			this.idkSprite = new TextureRegion(spriteSheet, 165, 120, 44, 44);
			this.goalSprite = new TextureRegion(spriteSheet, 165, 0, 55, 17);
			this.nadiaSprite = new TextureRegion(spriteSheet, 165, 165, 44, 44); // :P
			
			
			this.borderFrames = new TextureRegion[5];
			this.borderFrames[0] = new TextureRegion(spriteSheet, 35, 3, 10, 10);
			this.borderFrames[1] = new TextureRegion(spriteSheet, 35, 14, 10, 10);
			this.borderFrames[2] = new TextureRegion(spriteSheet, 35, 25, 10, 10);
			this.borderFrames[3] = new TextureRegion(spriteSheet, 35, 36, 10, 10);
			this.borderFrames[4] = new TextureRegion(spriteSheet, 35, 47, 10, 10);
			
			this.grate = new TextureRegion(spriteSheet, 22, 102, 23, 23);
			this.waterfall = new TextureRegion[5];
			this.waterfall[0] = new TextureRegion(spriteSheet, 0, 125, 32, 84);
			this.waterfall[1] = new TextureRegion(spriteSheet, 31, 125, 32, 84);
			this.waterfall[2] = new TextureRegion(spriteSheet, 61, 125, 32, 84);
			this.waterfall[3] = new TextureRegion(spriteSheet, 91, 125, 32, 84);
			this.waterfall[4] = new TextureRegion(spriteSheet, 121, 125, 34, 84);
					
			this.animationFrame = 0;
			
			this.resourcesLoaded = true;
		}
	}

	@Override
	public void unload() {
		if (this.resourcesLoaded) {
			this.spriteSheet.dispose();
			this.resourcesLoaded = false;
		}

	}

	@Override
	public Vector3 getInitialPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Rectangle[] getObstacles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(float deltaTime) {
        // Prepare the game's spriteBatch for drawing.
        MyGdxGame.currentGame.spriteBatch
                .setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
        MyGdxGame.currentGame.spriteBatch.begin();
        
        // Draw the lower wall in columns, alternating between decorative tiles by refractor
        tileXY(this.smallMazeSprite, GROUND_ORIGIN_X, GROUND_START_Y, 30, 6);
        for (int i = 0; i < 5; i++) { checkerX(this.wallSprite, GROUND_ORIGIN_X, GROUND_START_Y + (i * GROUND_DIM), 15); }
        tileXY(this.smallMazeSprite, PLATFORM_START_X - (2 * GROUND_DIM), GROUND_START_Y, 7, 5);
        diagonalRight(this.idkSprite, PLATFORM_START_X - (2 * GROUND_DIM), GROUND_START_Y, 4);
        diagonalLeft(this.idkSprite, PLATFORM_START_X + (4 * GROUND_DIM), GROUND_START_Y, 3);
        tileX(this.rockSprite, GROUND_ORIGIN_X, GROUND_START_Y + (5 * GROUND_DIM), 30);
        
        // Draw the upper wall, using one repeating pattern
        tileX(this.borderFrames[animationFrame / 3 % 5], GROUND_ORIGIN_X, GROUND_START_Y + (6 * GROUND_DIM), 132);
        tileXY(this.largeMazeSprite, GROUND_ORIGIN_X, GROUND_START_Y + (GROUND_DIM * 6) + 10, (int) Math.ceil(30 * (this.rockSprite.getRegionWidth()/(double)this.largeMazeSprite.getRegionWidth())), 2);
        
        // Create the refractor platform
        tileX(this.rockSprite, PLATFORM_START_X, GROUND_START_Y, 3);
        MyGdxGame.currentGame.spriteBatch.draw(this.idkSprite, PLATFORM_START_X + (1 * GROUND_DIM), GROUND_START_Y + (1 * GROUND_DIM));
        MyGdxGame.currentGame.spriteBatch.draw(this.goalSprite, PLATFORM_START_X + (1 * GROUND_DIM - 5), GROUND_START_Y + (1* GROUND_DIM));
        MyGdxGame.currentGame.spriteBatch.draw(this.nadiaSprite, PLATFORM_START_X - (4 * GROUND_DIM), GROUND_START_Y);
       
        // Draw the Waterfall
       MyGdxGame.currentGame.spriteBatch.draw(this.grate, PLATFORM_START_X - (3 * GROUND_DIM) + 5, GROUND_START_Y + 64);
       MyGdxGame.currentGame.spriteBatch.draw(this.grate, PLATFORM_START_X + (5 * GROUND_DIM) + 18, GROUND_START_Y + 64);
       MyGdxGame.currentGame.spriteBatch.draw(this.waterfall[animationFrame / 4 % 5], PLATFORM_START_X - (3 * GROUND_DIM), GROUND_START_Y);
       MyGdxGame.currentGame.spriteBatch.draw(this.waterfall[animationFrame / 4 % 5], PLATFORM_START_X + (5 * GROUND_DIM) + 13, GROUND_START_Y);
        
        
        // Draw the ground (last so it goes on top of everything.
        tileXY(this.rockSprite, GROUND_ORIGIN_X, GROUND_ORIGIN_Y, 30, 7);
        
        animationFrame = (animationFrame < 60) ? animationFrame + 1 : 0;
        
        // Finalize drawing.
        MyGdxGame.currentGame.spriteBatch.end();

	}
	
	private static void diagonalRight(TextureRegion pattern, int x, int y, int count) {
		for (int i = 0; i < count; i++) {
			MyGdxGame.currentGame.spriteBatch.draw(pattern, x + (i * pattern.getRegionWidth()), y + (i * pattern.getRegionHeight()));
		}
	}
	
	private static void diagonalLeft(TextureRegion pattern, int x, int y, int count) {
		for (int i = 0; i < count; i++) {
			MyGdxGame.currentGame.spriteBatch.draw(pattern, x - (i * pattern.getRegionWidth()), y + (i * pattern.getRegionHeight()));
		}
	}
	
	/** Automatically tile a Texture region horizontally
	 * @param pattern Region to be tiled
	 * @param x initial X coordinate (tiles to the right)
	 * @param y initial Y coordinate (fixed)
	 * @param count number of iterations for tile
	 */
	private static void tileX(TextureRegion pattern, int x, int y, int count) {
		for (int i = 0; i < count; i++) {
			MyGdxGame.currentGame.spriteBatch.draw(pattern, x + (i * pattern.getRegionWidth()), y);
		}
	}
	
	/** Automatically tile a TextureRegion vertically
	 * @param pattern Region to be tiled
	 * @param x initial X coordinate (fixed)
	 * @param y initial Y coordinate (tiles upward)
	 * @param count number of iterations for tile
	 */
	private static void tileY(TextureRegion pattern, int x, int y, int count) {
		for (int i = 0; i < count; i++) {
			MyGdxGame.currentGame.spriteBatch.draw(pattern, x, y + (i * pattern.getRegionHeight()));
		}
	}
	
	/** Automatically tile a TextureRegion horizontally and vertically
	 * @param pattern Region to be tiled
	 * @param x initial X coordinate (tiles to the right)
	 * @param y initial Y coordinate (tiles upward)
	 * @param columns number of columns (Xs to repeat pattern)
	 * @param rows number of rows (Ys to repeat pattern)
	 */
	private static void tileXY(TextureRegion pattern, int x, int y, int columns, int rows) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				MyGdxGame.currentGame.spriteBatch.draw(
						pattern, 
						(x + j * pattern.getRegionWidth()), 
						(y + i * pattern.getRegionHeight())
				);
			}
		}
	}

	/** Tiles horizontally, alternating between pattern and same-width nothing
	 * TODO maybe have a double pattern checkerer
	 * @param pattern Pattern to be checkered
	 * @param x initial X coordinate (tiles to the right, skipping one tile each iteration) 
	 * @param y initial Y coordinate (fixed, or tiles upward based on rows)
	 * @param count number of iterations pattern appears per row, not including blank spaces
	 * @param rows number of rows to apply the pattern
	 */
	private static void checkerX(TextureRegion pattern, int x, int y, int count, int rows) {
		for (int i = 0; i < rows; i++) {
			for (int j = (i % 2 == 0) ? 0 : 1; j < 2*count; j+=2) {
				MyGdxGame.currentGame.spriteBatch.draw(pattern, x + (j * pattern.getRegionWidth()), (y + i*pattern.getRegionHeight()));	
			}
		}
	}
	
	/** Tiles vertically, aternating between pattern and same-height nothing
	 * TODO again, make a double checkerer
	 * @param pattern Pattern to be checkered
	 * @param x initial X coordinate (fixed, or tiles right based on columns)
	 * @param y initial Y coordinate (tiles upward, skipping one tile each iteration)
	 * @param count number of iterations pattern appears per column, not including blank spaces
	 * @param columns number of columns to apply the pattern
	 */
	private static void checkerY(TextureRegion pattern, int x, int y, int count, int columns) {
		for (int i = 0; i < columns; i++) {
			for (int j = (i % 2 == 0) ? 0 : 1; j < 2*count; j+=2) {
				MyGdxGame.currentGame.spriteBatch.draw(pattern, x + (i * pattern.getRegionWidth()), y+(j*pattern.getRegionHeight()));	
			}
		}
	}
	
	/** Wraps checkerX() for a single row
	 * @param pattern sic
	 * @param x sic
	 * @param y sic
	 * @param count sic
	 */
	private static void checkerX(TextureRegion pattern, int x, int y, int count) {
		checkerX(pattern,x,y,count,1);
	}
	
	/** Wraps checkerY for a single column
	 * @param pattern sic
	 * @param x sic
	 * @param y sic
	 * @param count sic
	 */
	private static void checkerY(TextureRegion pattern, int x, int y, int count) {
		checkerY(pattern,x,y,count,1);
	}
}
