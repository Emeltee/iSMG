package com.me.mygdxgame.entities.obstacles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.projectiles.FatRubble;
import com.me.mygdxgame.entities.projectiles.Rubble;
import com.me.mygdxgame.entities.projectiles.SmallRubble;
import com.me.mygdxgame.entities.projectiles.TallRubble;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class Platform implements GameEntity, Damageable {

    public static final int PLATFORM_X = 0;
    public static final int PLATFORM_Y = 57;
    public static final int PLATFORM_W = 45;
    public static final int PLATFORM_H = 45;
    
    public static final int MAX_HEALTH = 1;
    
    protected Texture spriteSheet;
    protected TextureRegion platform;
    protected EntityState status;
    protected int x, y;
    protected int health;
    protected Rectangle hitbox;
    protected Rubble[] rubble;
    protected static final int RUBBLE_LIFE = 500;
    
    public Platform() {}
    
    public Platform(Texture spriteSheet, int x, int y) {
        this.spriteSheet = spriteSheet;
        this.platform = new TextureRegion(spriteSheet, PLATFORM_X, PLATFORM_Y, PLATFORM_H, PLATFORM_W);
        this.status = EntityState.Running;
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(this.x, this.y, this.x + PLATFORM_W, this.x + PLATFORM_H);
        this.health = MAX_HEALTH;
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.status == EntityState.Running) {
            if (this.health <= 0) {
                this.explode();
                this.status = EntityState.Destroyed;
            }
        }

    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        if (this.status == EntityState.Running) {
            MyGdxGame.currentGame.spriteBatch
            .setProjectionMatrix(transformMatrix);
            MyGdxGame.currentGame.spriteBatch.begin();
            MyGdxGame.currentGame.spriteBatch.draw(this.platform, this.x, this.y);
            MyGdxGame.currentGame.spriteBatch.end();
        }
    }

    @Override
    public EntityState getState() {
        return this.status;
    }

    @Override
    public boolean hasCreatedEntities() {
        return this.rubble != null && this.rubble.length > 0;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        return this.rubble;
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
        return MAX_HEALTH;
    }

    @Override
    public Rectangle[] getHitArea() {
        return new Rectangle [] { this.hitbox };
    }
    
    public void explode() {
        // Since rubble stops when it hits an obstacle, this rectangle acts as a trap to stop the rubble after a certain point.
        Rectangle rubbleStopper = new Rectangle(this.x - 500, this.y - RUBBLE_LIFE, 1000, 100);
        this.rubble = new Rubble [] {
                new FatRubble(this.spriteSheet, new Vector3(this.x-5, this.y + 5, 0), new Vector3(0, 0, 0), 0, new Rectangle [] { rubbleStopper }, new Damageable [] {}),
                new SmallRubble(this.spriteSheet, new Vector3(this.x+10, this.y + 15, 0), new Vector3(0, 0, 0), 0, new Rectangle [] { rubbleStopper }, new Damageable [] {}),
                new TallRubble(this.spriteSheet, new Vector3(this.x+0, this.y + 10, 0), new Vector3(0, 0, 0), 0, new Rectangle [] { rubbleStopper}, new Damageable [] {}),
        };
    }
}
