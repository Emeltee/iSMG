package com.me.mygdxgame.entities.obstacles;

import java.util.Collection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.projectiles.FatRubble;
import com.me.mygdxgame.entities.projectiles.Rubble;
import com.me.mygdxgame.entities.projectiles.SmallRubble;
import com.me.mygdxgame.entities.projectiles.TallRubble;
import com.me.mygdxgame.screens.seeteufelscreen.SeeteufelScreen;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.GameEntity;

/**
 * An obstacles that, when damaged, creates several rubble objects that fall 
 * downwards and damage targets on contact.
 */
public class DamagingPlatform extends Platform {

    protected static final int PLATFORM_X = 0;
    protected static final int PLATFORM_Y = 0;
    protected static final int PLATFORM_W = 32;
    protected static final int PLATFORM_H = 32;
    
    protected static final int POWER = 8;
    protected static final int RUBBLE_LIFE = 500;
    private Collection<Damageable> targets;
    
    public DamagingPlatform(Texture spriteSheet, SeeteufelScreen.MapTiles tiles, 
            int x, int y, Collection<Damageable> targets) {
        super(spriteSheet, tiles, x, y);
        this.targets = targets;
    }
    
    @Override
    public void update(float deltaTime) {
        // Do nothing.
    }
    
    @Override
    public void damage(Damager damager) {
        explode();
    }
    
    @Override
    protected void explode() {
        Damageable[] targets = new Damageable[this.targets.size()];
        targets = this.targets.toArray(targets);
        GameEntity obstacles[] = new GameEntity[0];
        this.rubble = new Rubble [] {
                new FatRubble(this.spriteSheet, new Vector3(this.x-5, this.y + 5, 0), 
                        new Vector3((75 + ((float)Math.random() * 100)) * (float)Math.signum(Math.random() - 0.5), (float)Math.random() * 300, 0), 
                        POWER, obstacles, targets, 1),
                new SmallRubble(this.spriteSheet, new Vector3(this.x+10, this.y + 15, 0),
                        new Vector3((75 + ((float)Math.random() * 100)) * (float)Math.signum(Math.random() - 0.5), (float)Math.random() * 300, 0),
                        POWER, obstacles, targets, 1),
                new TallRubble(this.spriteSheet, new Vector3(this.x+0, this.y + 10, 0),
                        new Vector3((75 + ((float)Math.random() * 100)) * (float)Math.signum(Math.random() - 0.5), (float)Math.random() * 300, 0),
                        POWER, obstacles, targets, 1),
        };
    }
}
