package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.GameEntity;

public class LemonShot extends BusterShot {

    public LemonShot(Texture spriteSheet, Sound missSound, Vector3 position,
            int speed, BusterShot.ShotDirection dir, int power, float range,
            Collection<GameEntity> obstacles, Collection<Damageable> targets) {
        
        super(spriteSheet, missSound, position, speed, dir, power, range, obstacles, targets);
        super.setShotColor(Color.YELLOW);
    }
}
