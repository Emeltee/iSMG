package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;


public class Refractor implements GameEntity {

    // Texture extraction constants 
    public static final int REFRACTOR_X = 136;
    public static final int REFRACTOR_Y = 210;
    public static final int REFRACTOR_W = 18;
    public static final int REFRACTOR_H = 46;
    public static final Color BOX_COLOR = new Color(0, 0, 1, 0.35f);
    public static final Color LINE_COLOR = new Color(1, 1, 1, 0.5f);

    private int x, y; // Coordinates
    private EntityState status; // Running/Destroyed
    private TextureRegion refractor;
    private boolean taken; // Whether or not player has taken refractor
    private static final float MESG_DELAY = 1.5f; // Time for message to be on-screen
    private float mesgDuration; // 
    private static final Sound ITEM_GET_SFX = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-item-get.ogg"));
    
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
    public void draw(Renderer renderer) {        
        if (this.status == EntityState.Running && !this.taken) {
            // Refractor is neither taken nor destroyed, show refractor.
            renderer.drawRegion(this.refractor, this.x, this.y);
        } else if (this.taken && (this.mesgDuration < MESG_DELAY)) {
            // Using emulogic, an imitation of the original NES font
            BitmapFont nesFont = new BitmapFont(Gdx.files.internal("data/emulogic.fnt"), Gdx.files.internal("data/emulogic.png"), false);
            CharSequence mesg = "YOU GOT BLUE REFRACTOR";
            
            // Once refractor is taken, show mesg until destroyed
            float posX = this.x - (int)((mesg.length() + 2) / 2 * 8);
            float posY = this.y + REFRACTOR_H + 16;
            float width = (mesg.length() + 5) * 8;
            float height = 24;
            renderer.drawRect(ShapeType.Filled, BOX_COLOR, posX, posY, width, height);
            renderer.drawRect(ShapeType.Line, LINE_COLOR, posX, posY, width, height);
            
            // This centers the text above wherever the refractor is.
            // TODO Maybe make this a fixed coord, but I like it this way.
            renderer.drawText(nesFont, mesg, this.x - (int)(mesg.length() / 2 * 8), this.y + REFRACTOR_H + 32);           
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
    
    public Rectangle getHitBox() {
        return new Rectangle(this.x, this.y, REFRACTOR_W, REFRACTOR_H);
    }
    
    public void onTake() {
        // Originally this was going to have more logic, but it makes more sense
        // to put it in update().
        if (!this.taken) {
            ITEM_GET_SFX.play();
            this.taken = true;
        }
    }

}
