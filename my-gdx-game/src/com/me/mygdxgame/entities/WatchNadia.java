package com.me.mygdxgame.entities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class WatchNadia implements Damageable {
    
    private Sprite watchNadia;
    private static final int MAX_HEALTH = 1;
    private static final float DEFAULT_VOLUME = 0.5f;
    private int health;
    private MegaPlayer player;
    private Sound itemGetSound = null;
    private float volume;
    
    private Rectangle hitBox = new Rectangle(0, 0, 0, 0);
    private Rectangle[] hitAreas = new Rectangle[] {this.hitBox};
    private Vector3 position = new Vector3();

    
    public WatchNadia(Texture sprite, MegaPlayer player, Sound itemGetSound, float volume, int x, int y) {
        this.watchNadia = new Sprite(sprite);
        this.player = player;
        this.itemGetSound = itemGetSound;
        this.volume = volume;
        this.health = MAX_HEALTH;
        this.hitBox.set(x, y, this.getWidth(), this.getHeight());
        this.position.x = x;
        this.position.y = y;
    }
    
    public WatchNadia(Texture sprite, MegaPlayer player, Sound itemGetSound, int x, int y) {
        this(sprite, player, itemGetSound, WatchNadia.DEFAULT_VOLUME, x, y);
    }
    
    @Override
    public void damage(Damager damager) {
        if (this.getState() != EntityState.Destroyed) {
            itemGetSound.play(this.volume);
            this.health = 0;
            this.player.setGeminiEnabled(true);
        }
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
        return this.hitAreas;
    }

    @Override
    public void update(float deltaTime) {
        // Nothing.
    }

    @Override
    public void draw(Renderer renderer) {
        if (this.getState() == EntityState.Running) {
            renderer.drawRegion(this.watchNadia, this.hitBox.x, this.hitBox.y);
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
    public void destroy() {
        // Nothing.
    }

    @Override
    public Vector3 getPosition() {
        return this.position;
    }

    @Override
    public int getWidth() {
        return this.watchNadia.getRegionWidth();
    }

    @Override
    public int getHeight() {
        return this.watchNadia.getRegionHeight();
    }

}
