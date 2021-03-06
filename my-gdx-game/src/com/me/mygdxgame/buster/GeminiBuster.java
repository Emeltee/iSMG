package com.me.mygdxgame.buster;

import java.util.Collection;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.projectiles.BusterShot;
import com.me.mygdxgame.entities.projectiles.BusterShot.ShotDirection;
import com.me.mygdxgame.entities.projectiles.GeminiShot;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

/**
 * Slightly modified subclass of {@link MegaBuster}, this class manages creation of 
 * {@link GeminiShot} objects instead of plain old {@link BusterShot}s.
 *
 * Projectile-based special weapons could be generalized to some format like this.
 * TODO Implement a general framework for projectile type weapons based on MegaBuster/GeminiBuster relationship?
 */

public class GeminiBuster extends MegaBuster {

    public GeminiBuster(Texture spriteSheet, Sound shootSound, Sound missSound) {
        super(spriteSheet, shootSound, missSound);
    }

    @Override
    public BusterShot makeShot(Vector3 shotOrigin, ShotDirection dir,
            Collection<GameEntity> obstacles, Collection<Damageable> targets) {

        this.energyTimer = 1 / (float)this.energyStat();
        BusterShot shot = new GeminiShot(this.spritesheet,
                                  this.missSound, shotOrigin,
                                  this.rapidStat(), dir,
                                  this.attackStat(), this.rangeStat(),
                                  obstacles, targets);
        if (shot.getState() != EntityState.Destroyed) {
            shot.setShotScale(this.calcShotScale());
            this.shootSound.play(SFX_VOLUME);
        }
        
        return shot;
    }
    
    

}
