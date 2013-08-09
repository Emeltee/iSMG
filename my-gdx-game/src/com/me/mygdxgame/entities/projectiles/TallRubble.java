package com.me.mygdxgame.entities.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;

public class TallRubble extends Rubble {
    // Texture control constants, get passed up into super-class instance vars
    public static final int RUBBLE_X = 165;
    public static final int RUBBLE_Y = 211;
    public static final int RUBBLE_W = 15;
    public static final int RUBBLE_H = 29;
    
    public TallRubble(Texture spriteSheet, Vector3 position, Vector3 velocity, int damage, Rectangle[] obstacles, Damageable[] targets) {
        this.rubble = new TextureRegion(spriteSheet, RUBBLE_X, RUBBLE_Y, RUBBLE_W, RUBBLE_H);
        this.y = RUBBLE_Y;
        this.x = RUBBLE_X;
        this.h = RUBBLE_H;
        this.w = RUBBLE_W;
        this.position = position;
        this.velocity = velocity;
        this.obstacles = obstacles;
        this.targets = targets;
        this.damage = damage;
        this.status = EntityState.Running;
    }
}