package com.me.mygdxgame.entities.obstacles;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.projectiles.FatRubble;
import com.me.mygdxgame.entities.projectiles.Rubble;
import com.me.mygdxgame.entities.projectiles.SmallRubble;
import com.me.mygdxgame.entities.projectiles.TallRubble;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class DamagingPlatform implements Damageable {

    protected static final int PLATFORM_X = 0;
    protected static final int PLATFORM_Y = 0;
    protected static final int PLATFORM_W = 32;
    protected static final int PLATFORM_H = 32;
    
    protected static final int MAX_HEALTH = 1;
    protected static final int POWER = 8;
    
    protected Texture spriteSheet;
    protected TextureRegion platform;
    protected EntityState status;
    protected int x, y;
    protected int health;
    protected Rectangle hitbox;
    protected Rubble[] rubble = null;
    protected static final int RUBBLE_LIFE = 500;
    Collection<Damageable> targets;
    
    public DamagingPlatform(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, int x, int y, Collection<Damageable> targets) {
        this.spriteSheet = spriteSheet;
        this.platform = new TextureRegion(tiles.rockTex, PLATFORM_X, PLATFORM_Y, PLATFORM_H, PLATFORM_W);
        this.status = EntityState.Running;
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(this.x, this.y, PLATFORM_W, PLATFORM_H);
        this.health = MAX_HEALTH;
        this.targets = targets;
    }
    
    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void draw(Renderer renderer) {
        if (this.status == EntityState.Running) {
            renderer.drawRegion(this.platform, this.x, this.y);
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
        
        GameEntity[] returnList;
        
        if (this.rubble != null && this.rubble.length > 0) {
            returnList = this.rubble;
            this.rubble = null;
        } else {
            throw new NoSuchElementException();
        }
        
        return returnList;
    }

    @Override
    public void damage(Damager damager) {
        explode();
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
        Damageable[] targets = new Damageable[this.targets.size()];
        targets = this.targets.toArray(targets);
        this.rubble = new Rubble [] {
                new FatRubble(this.spriteSheet, new Vector3(this.x-5, this.y + 5, 0),
                        new Vector3((75 + ((float)Math.random() * 100)) * (float)Math.signum(Math.random() - 0.5), (float)Math.random() * 300, 0),
                        POWER, new GameEntity [0], targets, 1),
                new SmallRubble(this.spriteSheet, new Vector3(this.x+10, this.y + 15, 0),
                        new Vector3((75 + ((float)Math.random() * 100)) * (float)Math.signum(Math.random() - 0.5), (float)Math.random() * 300, 0),
                        POWER, new GameEntity [0], targets, 1),
                new TallRubble(this.spriteSheet, new Vector3(this.x+0, this.y + 10, 0),
                        new Vector3((75 + ((float)Math.random() * 100)) * (float)Math.signum(Math.random() - 0.5), (float)Math.random() * 300, 0),
                        POWER, new GameEntity [0], targets, 1),
        };
    }

    @Override
    public void destroy() {
        this.status = EntityState.Destroyed;
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.x, this.y, 0);
    }

    @Override
    public int getWidth() {
        return Platform.PLATFORM_W;
    }

    @Override
    public int getHeight() {
        return Platform.PLATFORM_H;
    }
}
