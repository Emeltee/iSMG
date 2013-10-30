package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class GeminiShot extends BusterShot {

    private static final int GEMINI_W = 7;
    private static final int GEMINI_H = 5;
    // TODO
    //private static final float MAX_MOVEMEMT = Math.min(GEMINI_W, GEMINI_H);
    
    private static final int MAX_SHOTS = 6;
    private static int currentShots = 0;

    public static final int MAX_DEFLECTS = 6;

    public static enum ShotDeflection {
        UP, DOWN
    };

    private boolean isDeflected;
    private ShotDeflection dir2;
    private int numDeflects;

    public GeminiShot(Texture spriteSheet, Sound missSound, Vector3 position,
            int speed, BusterShot.ShotDirection dir, int power, float range,
            Collection<GameEntity> obstacles, Collection<Damageable> targets) {
        
        super(spriteSheet, missSound, position, speed, dir, 2 * power,
                3 * range, obstacles, targets);
        
        // Re-map region.
        int regionWidth = this.bullet.getRegionWidth();
        this.bullet.setRegionX(this.bullet.getRegionX() + this.bullet.getRegionWidth());
        this.bullet.setRegionWidth(regionWidth);
        
        GeminiShot.currentShots++;
        if (GeminiShot.currentShots >= GeminiShot.MAX_SHOTS) {
            this.range = 0;
        }
        
        this.isDeflected = false;
        this.numDeflects = 0;
    }

    @Override
    public void update(float deltaTime) {
        
        if (this.state == EntityState.Running) {
            
            //Calculate distance to travel and check if range has been traveled.
            //float toTravel = Math.min(this.speed * deltaTime, GeminiShot.MAX_MOVEMEMT);
            float toTravel = this.speed * deltaTime;
            this.distanceTraveled += toTravel;
            if (this.distanceTraveled >= this.range) {
                this.state = EntityState.Destroyed;
                GeminiShot.currentShots--;
                this.missSound.play();
                return;
            }
            
            // Move on X.
            if (this.dir == ShotDirection.LEFT) {
                this.position.x -= toTravel;            
            } else {
                this.position.x += toTravel;
            }
            
            // Check collisions on X.
            this.hitBox.x = this.position.x;
            this.hitBox.y = this.position.y;
            this.checkObstacleCollisionsX();
            
            // Move on Y if reflected.
            if (this.isDeflected) {
                if (this.dir2 == ShotDeflection.UP) {
                    this.position.y += toTravel;
                } else {
                    this.position.y -= toTravel;
                }
                
                // Check collisions on Y.
                this.hitBox.x = this.position.x;
                this.hitBox.y = this.position.y;
                this.checkObstacleCollisionsY();
            }
            
            // Check collisions with potential targets.
            this.hitBox.x = this.position.x;
            this.hitBox.y = this.position.y;
            for (Damageable target : this.targets) {
                Rectangle[] hitAreas = target.getHitArea();
                for (Rectangle hitBox : hitAreas) {
                    if (hitBox.overlaps(this.hitBox)) {
                        target.damage(this);
                        this.state = EntityState.Destroyed;
                        GeminiShot.currentShots--;
                        return;
                    }
                }
            }
        }
    }
    
    private void checkObstacleCollisionsX() {
        if (numDeflects >= MAX_DEFLECTS) { 
            return; 
        }
        
        for (GameEntity entity : this.obstacles) {
            for (Rectangle r: entity.getHitArea()) {
                if (r.overlaps(this.hitBox)) {
                    numDeflects++;
                    
                    if (this.dir == ShotDirection.LEFT) {
                        this.dir = BusterShot.ShotDirection.RIGHT;
                        this.position.x = r.getX() + r.getWidth() + 1;
                    } else {
                        this.dir = BusterShot.ShotDirection.LEFT;
                        this.position.x = r.getX() - GeminiShot.GEMINI_W - 1;
                    }
                    
                    if (!this.isDeflected) {
                        this.isDeflected = true;
                        this.dir2 = ShotDeflection.UP;
                        this.dir = (this.dir == BusterShot.ShotDirection.LEFT) ? BusterShot.ShotDirection.RIGHT : BusterShot.ShotDirection.LEFT;
                    }
                    
                    return;
                }
            }
        }
    }
    
    private void checkObstacleCollisionsY() {
        if (numDeflects >= MAX_DEFLECTS) { 
            return; 
        }
        
        for (GameEntity entity : this.obstacles) {
            for (Rectangle r: entity.getHitArea()) {
                if (r.overlaps(this.hitBox)) {
                    numDeflects++;
                    
                    if (this.dir2 == ShotDeflection.UP) {
                        this.dir2 = ShotDeflection.DOWN;
                        this.position.y = r.getY() - GeminiShot.GEMINI_H - 1;
                    } else {
                        this.dir2 = GeminiShot.ShotDeflection.UP;
                        this.position.y = r.getY() + r.getHeight() + 1;
                    }
                    
                    return;
                }
            }
        }
    }
    
    @Override
    public void destroy() {
        GeminiShot.currentShots--;
    }
}
