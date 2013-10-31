package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class LemonShot extends BusterShot {
    
    private static final int MAX_SHOTS = 6;
    private static int currentShots = 0;

    public LemonShot(Texture spriteSheet, Sound missSound, Vector3 position,
            int speed, BusterShot.ShotDirection dir, int power, float range,
            Collection<GameEntity> obstacles, Collection<Damageable> targets) {
        
        super(spriteSheet, missSound, position, speed, dir, power, range, obstacles, targets);
        
        LemonShot.currentShots++;
        if (LemonShot.currentShots >= LemonShot.MAX_SHOTS) {
            this.range = 0;
        }
    }
    
    @Override
    public void update(float deltaTime) {
        if (this.state == EntityState.Running) {
            super.update(deltaTime);
            if (this.state == EntityState.Destroyed) {
                LemonShot.currentShots--;
            }
        }
    }
    
    @Override
    public void draw(Renderer renderer) {
        
        if (this.state == EntityState.Running) {
            // Every call, increment the animationTimer.
            this.animationTimer++;
            
            renderer.drawRegion(bullet, this.position.x, this.position.y, Color.YELLOW, 1, 1, 60 * this.animationTimer);
        }
    }
    
    @Override
    public void destroy() {
        if (this.state == EntityState.Running) {
            this.state = EntityState.Destroyed;
            LemonShot.currentShots--;
        }
    }
}
