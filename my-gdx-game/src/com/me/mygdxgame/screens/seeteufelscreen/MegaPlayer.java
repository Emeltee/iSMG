package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.BusterShot;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class MegaPlayer implements GameEntity, Damageable {

    private static int MAX_HEALTH = 100;
    private static int HITBOX_WIDTH = 26;
    private static int HITBOX_HEIGHT = 32;
    private static int SPRITE_WIDTH = 38;
    private static int SPRITE_HEIGHT = 46;
    
    private Vector3 positon = new Vector3();
    private Vector3 velocity = new Vector3();
    private int health = MegaPlayer.MAX_HEALTH;
    private Rectangle[] obstacles = null;
    private Seeteufel seeteufel = null;
    private Rectangle hitBox = new Rectangle(0, 0, MegaPlayer.HITBOX_WIDTH, MegaPlayer.HITBOX_HEIGHT);
    
    private TextureRegion[] runRight = new TextureRegion[4];
    private TextureRegion[] runLeft = new TextureRegion[4];
    private TextureRegion[] runShootRight = new TextureRegion[4];
    private TextureRegion[] runShootLeft = new TextureRegion[4];
    private TextureRegion standRight = new TextureRegion();
    private TextureRegion standLeft = new TextureRegion();
    private TextureRegion standShootRight = new TextureRegion();
    private TextureRegion standShootLeft = new TextureRegion();
    private TextureRegion[] damageRight = new TextureRegion[2];
    private TextureRegion[] damageLeft = new TextureRegion[2];
    private TextureRegion[] jumpLeft = new TextureRegion[2];
    private TextureRegion[] jumpRight = new TextureRegion[2];
    private TextureRegion[] jumpShootLeft = new TextureRegion[2];
    private TextureRegion[] jumpShootRight = new TextureRegion[2];
    
    private ArrayDeque<BusterShot> newShots = new ArrayDeque<BusterShot>();
    
    public MegaPlayer(Texture texture, Seeteufel seeteufel, Vector3 initialPosition, Rectangle[] obstacles) {
        this.seeteufel = seeteufel;
        this.positon.set(initialPosition);
    }
    
    public void setPosition(Vector3 position) {
        this.positon.set(position);
    }
    
    public void setObstacles(Rectangle[] obstacles) {
        this.obstacles = obstacles;
    }
    
    @Override
    public void damage(int damage) {
        this.health -= damage;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMaxHealth() {
        return MegaPlayer.MAX_HEALTH;
    }

    @Override
    public Rectangle[] getHitArea() {
        return new Rectangle[] {this.hitBox};
    }

    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public EntityState getState() {
        // Always running, until the end of the screen.
        return EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        return !this.newShots.isEmpty();
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        GameEntity[] returnList = (GameEntity[]) this.newShots.toArray();
        this.newShots.clear();
        return returnList;
    }

}
