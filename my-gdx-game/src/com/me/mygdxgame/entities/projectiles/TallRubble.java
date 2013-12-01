package com.me.mygdxgame.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class TallRubble extends Rubble {
    // Texture control constants, get passed up into super-class instance vars
    public static final int RUBBLE_X = 165;
    public static final int RUBBLE_Y = 211;
    public static final int RUBBLE_W = 15;
    public static final int RUBBLE_H = 29;
    
    public TallRubble(Texture spriteSheet, Vector3 position, Vector3 velocity, 
            int damage, GameEntity[] obstacles, Damageable[] targets, float scale) {
        super((int)position.x, (int)position.y, RUBBLE_W, RUBBLE_H);
        this.rubble = new TextureRegion(spriteSheet, RUBBLE_X, RUBBLE_Y, RUBBLE_W, RUBBLE_H);
        this.y = RUBBLE_Y;
        this.x = RUBBLE_X;
        this.h = RUBBLE_H;
        this.w = RUBBLE_W;
        this.position = position;
        this.velocity = velocity;
        this.obstacles = obstacles;
        this.targets = targets;
        this.power = damage;
        this.state = EntityState.Running;
        this.scale = scale;
    }
}
