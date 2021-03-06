package com.me.mygdxgame.entities.particles;

import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

/**
 * Simple water splash particle. Take the form of small circles that fly upwards
 * and then back down in an arch. Diameter of the circle shrinks over time, and
 * particle is destroyed when either a timer runs out or its size gets small
 * enough. Initial velocity/direction, size, and color can be specified.
 * <p>
 * No effects other than being visual sweetener.
 */
public class Splash implements GameEntity {
    
    // Minimum velocity values.
    public static final float MIN_INIT_Y_VEL = 50;
    public static final float MAX_INIT_Y_VEL = 150;
    public static final float MIN_INIT_X_VEL = -75;
    public static final float MAX_INIT_X_VEL = 75;
    public static final int DEFAULT_RADIUS = 3;
    
    private static final float GRAVITY_FACTOR = -400;
    private static final float RADIUS_FALLOFF = 1.5f;
    private static final Color DEFAULT_COLOR = new Color(0.5f, 0.5f, 1, 0.5f);
    
    private Vector2 position = new Vector2();
    private Vector2 velocity = new Vector2();
    private Rectangle hitbox = new Rectangle(0, 0, 1, 1);
    private Rectangle[] hitAreas = new Rectangle[] {this.hitbox};
    private float lifetime = 5;
    private Color color = DEFAULT_COLOR;
    
    public Splash(float x, float y) {
        this.position.set(x, y);
        this.velocity.y = (float) (Splash.MIN_INIT_Y_VEL + (Math.random() * Splash.MAX_INIT_Y_VEL));
        this.velocity.x = (float) (Math.random() * (Splash.MAX_INIT_X_VEL - Splash.MIN_INIT_X_VEL)) + Splash.MIN_INIT_X_VEL;
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.hitbox.width = Splash.DEFAULT_RADIUS;
        this.hitbox.height = Splash.DEFAULT_RADIUS;
    }
    
    public Splash(float x, float y, boolean moveLeft) {
        this.position.set(x, y);
        this.velocity.y = (float) (Splash.MIN_INIT_Y_VEL + (Math.random() * Splash.MAX_INIT_Y_VEL));
        if (moveLeft) {
            this.velocity.x = (float) (Math.random() * Splash.MAX_INIT_X_VEL);
        } else {
            this.velocity.x = (float) (Math.random() * Splash.MIN_INIT_X_VEL);
        }
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.hitbox.width = Splash.DEFAULT_RADIUS;
        this.hitbox.height = Splash.DEFAULT_RADIUS;
    }
    
    public Splash(float x, float y, boolean moveLeft, int radius) {
        this.position.set(x, y);
        this.velocity.y = (float) (Splash.MIN_INIT_Y_VEL + (Math.random() * Splash.MAX_INIT_Y_VEL));
        if (moveLeft) {
            this.velocity.x = (float) (Math.random() * Splash.MAX_INIT_X_VEL);
        } else {
            this.velocity.x = (float) (Math.random() * Splash.MIN_INIT_X_VEL);
        }
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.hitbox.width = radius;
        this.hitbox.height = radius;
    }
    
    public Splash(float x, float y, float xVel, float yVel, int radius) {
        this.position.set(x, y);
        this.velocity.set(xVel, yVel);
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.hitbox.width = radius;
        this.hitbox.height = radius;
    }
    
    public Splash(float x, float y, float xVel, float yVel, int radius, Color color) {
        this.position.set(x, y);
        this.velocity.set(xVel, yVel);
        this.hitbox.x = x;
        this.hitbox.y = y;
        this.hitbox.width = radius;
        this.hitbox.height = radius;
        this.color = color;
    }
    
    
    @Override
    public void update(float deltaTime) {
        this.lifetime -= deltaTime;
        this.position.x += this.velocity.x * deltaTime;
        this.position.y += this.velocity.y * deltaTime;
        this.velocity.y += Splash.GRAVITY_FACTOR * deltaTime;
        this.hitbox.x = this.position.x;
        this.hitbox.y = this.position.y;
        
        this.hitbox.width = Math.max(this.hitbox.width - Splash.RADIUS_FALLOFF * deltaTime, 0);
        this.hitbox.height = this.hitbox.width;
    }

    @Override
    public void draw(Renderer renderer) {
        renderer.drawCircle(ShapeType.Filled, this.color, this.position.x, this.position.y, (int) this.hitbox.width);
    }

    @Override
    public EntityState getState() {
        if (this.lifetime <= 0 || this.hitbox.width <= 0) {
            return EntityState.Destroyed;
        } else {
            return EntityState.Running;
        }
    }

    @Override
    public boolean hasCreatedEntities() {
        return false;
    }

    @Override
    public Rectangle[] getHitArea() {
        return this.hitAreas;
    }

    @Override
    public void destroy() {
        this.lifetime = 0;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.position.x, this.position.y, 0);
    }

    @Override
    public int getWidth() {
        return (int) this.hitbox.width;
    }

    @Override
    public int getHeight() {
        return (int) this.hitbox.height;
    }

}
