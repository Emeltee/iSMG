package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Refractor implements GameEntity {

    // Texture extraction constants 
    public static final int REFRACTOR_X = 138;
    public static final int REFRACTOR_Y = 210;
    public static final int REFRACTOR_W = 18;
    public static final int REFRACTOR_H = 46;

    private int x, y; // Coordinates
    private EntityState status; // Running/Destroyed
    private TextureRegion refractor;
    private boolean taken; // Whether or not player has taken refractor
    private static final float MESG_DELAY = 3.0f; // Time for message to be on-screen
    private float mesgDuration; // 
    
    public Refractor(Texture spriteSheet, int x, int y) {
        this.refractor = new TextureRegion(spriteSheet, REFRACTOR_X, REFRACTOR_Y, REFRACTOR_W, REFRACTOR_H);
        this.x = x; 
        this.y = y;
        this.taken = false;
        this.status = EntityState.Running;
        this.mesgDuration = 0.0f;
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.status == EntityState.Running) {
            if (this.taken) { 
                if (this.mesgDuration < MESG_DELAY) {
                    // Count time by accumulating deltas
                    // until mesg delay exceeded
                    this.mesgDuration += deltaTime;
                } else {
                    // Destroy after done showing mesg
                    this.status = EntityState.Destroyed;
                }
            }
        }
    }

    @Override
    public void draw() {        
        if (this.status == EntityState.Running && !this.taken) {
            // Refractor is neither taken nor destroyed, show refractor
            // Prepare the game's spriteBatch for drawing.
            MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
            MyGdxGame.currentGame.spriteBatch.begin();
            MyGdxGame.currentGame.spriteBatch.draw(this.refractor, this.x, this.y);
            MyGdxGame.currentGame.spriteBatch.end();
        } else if (this.taken) {
            // Once refractor is taken, show mesg until destroyed
            MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
            MyGdxGame.currentGame.spriteBatch.begin();
            
            // Using emulogic, an imitation of the original NES font
            BitmapFont nesFont = new BitmapFont(Gdx.files.internal("data/emulogic.fnt"),
         Gdx.files.internal("data/emulogic.png"), false);
            CharSequence mesg = "YOU GOT BLUE REFRACTOR";
            
            // This centers the text above wherever the refractor is.
            // TODO Maybe make this a fixed coord, but I like it this way.
            nesFont.draw(MyGdxGame.currentGame.spriteBatch, mesg, this.x - (int)(mesg.length() / 2 * 8), this.y + this.refractor.getRegionWidth() + 32);            
            MyGdxGame.currentGame.spriteBatch.end();
        }
    }

    @Override
    public EntityState getState() {
        // TODO Auto-generated method stub
        return this.status;
    }

    @Override
    public boolean hasCreatedEntities() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        // TODO Auto-generated method stub
        return null;
    }
    
    public void onTake() {
        // Originally this was going to have more logic, but it makes more sense
        // to put it in update(). 
        // TODO add the sound effect here
        this.taken = true;
    }

}
