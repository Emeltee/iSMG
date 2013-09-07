package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class WatchNadia implements Damageable, GameEntity {

    /* WATCH NADIA */
    public static final int WATCH_NADIA_X = 165;
    public static final int WATCH_NADIA_Y = 165;
    public static final int WATCH_NADIA_W = 45;
    public static final int WATCH_NADIA_H = 45;
    
    private TextureRegion watchNadia;
    public static final int MAX_HEALTH = 1;
    private int health;
    private int x, y;
    
    public WatchNadia(Texture spriteSheet, int x, int y) {
        this.watchNadia = new TextureRegion(spriteSheet, WATCH_NADIA_X, WATCH_NADIA_Y, WATCH_NADIA_W, WATCH_NADIA_H);
        this.health = MAX_HEALTH;
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void damage(int damage) {
        this.health = 0;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    @Override
    public Rectangle[] getHitArea() {
        return new Rectangle [] { new Rectangle(this.x, this.y, WATCH_NADIA_W, WATCH_NADIA_H) };
    }

    @Override
    public void update(float deltaTime) {
        
    }

    @Override
    public void draw(Renderer renderer) {
        if (this.getState() == EntityState.Running) {
            renderer.drawRegion(this.watchNadia, this.x, this.y);
        }
    }

    @Override
    public EntityState getState() {
        return (this.health <= 0) ? EntityState.Destroyed : EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        return false;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public void applyForce(Vector3 force) {
        // Do nothing.
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }

}
