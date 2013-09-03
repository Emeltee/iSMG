package com.me.mygdxgame.entities;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.projectiles.Rocket;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class SeeteufelFront implements GameEntity {
    
    public static final int BASE_WIDTH = 98;
    public static final int TARGET_Y_OFFSET = 20;
    
    private static final int FRONT_ARM_FRAMERATE = 30;
    private static final int BACK_ARM_FRAMERATE = 15;
    private static final int GRAVITY_FACTOR = 400;
    private static final float ATTACK_DELAY = 0.5f;
    private static final float ROCKET_SPEED = 200.0f;
    
    private Vector3 position = new Vector3();
    
    private LinkedList<Damageable> targets = new LinkedList<Damageable>();
    
    private TextureRegion front = null;
    private TextureRegion[] frontArmRight = new TextureRegion[2];
    private TextureRegion[] frontArmLeft = new TextureRegion[2];
    private TextureRegion[] backArmRight = new TextureRegion[3];
    private TextureRegion[] backArmLeft = new TextureRegion[3];
    private Texture rocketSpritesheet = null;
    
    private Sound explosion = null;
    private Sound splash = null;
    private Sound shoot = null;
    
    private boolean frontArmFrame = true;
    private int backArmFrame = 0;
    private int frontArmTimer = 0;
    private int backArmTimer = 0;
    private int targetY = 0;
    private boolean isFalling = false;
    
    private float attackDelayTimer = 0;
    private LinkedList<GameEntity> createdEntities = new LinkedList<GameEntity>();
    
    public SeeteufelFront(Texture spritesheet, Texture rocketSpritesheet,
            Sound explosion, Sound splash, Sound shoot, Vector3 position) {
        this.position.set(position);
        
        this.explosion = explosion;
        this.splash = splash;
        this.shoot = shoot;
        
        this.front = new TextureRegion(spritesheet, 73, 211, 98, 115);
        
        this.rocketSpritesheet = rocketSpritesheet;
        
        this.frontArmLeft[0] = new TextureRegion(spritesheet, 175, 271, 88, 54);
        this.frontArmLeft[1] = new TextureRegion(spritesheet, 269, 267, 82, 55);
        this.frontArmRight[0] = new TextureRegion(spritesheet, 175, 271, 88, 54);
        this.frontArmRight[0].flip(true, false);
        this.frontArmRight[1] = new TextureRegion(spritesheet, 269, 267, 82, 55);
        this.frontArmRight[1].flip(true, false);
        
        this.backArmLeft[0] = new TextureRegion(spritesheet, 0, 370, 113, 28);
        this.backArmLeft[0].flip(true, false);
        this.backArmLeft[1] = new TextureRegion(spritesheet, 114, 370, 113, 24);
        this.backArmLeft[1].flip(true, false);
        this.backArmLeft[2] = new TextureRegion(spritesheet, 228, 362, 113, 29);
        this.backArmLeft[2].flip(true, false);
        this.backArmRight[0] = new TextureRegion(spritesheet, 0, 370, 113, 28);
        this.backArmRight[1] = new TextureRegion(spritesheet, 114, 370, 113, 24);
        this.backArmRight[2] = new TextureRegion(spritesheet, 228, 362, 113, 29);
    }
    
    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
    
    public int getTargetY() {
        return this.targetY;
    }
    
    public void attack(Damageable target) {
        this.targets.addFirst(target);
    }
    
    @Override
    public void update(float deltaTime) {
        
        // Adjust target position down slightly do sprite isn't sitting exactly
        // on the water level.
        int adjustedTargetY = this.targetY - SeeteufelFront.TARGET_Y_OFFSET;
        if (this.position.y > adjustedTargetY) {
            // If higher than the water level, fall.
            this.position.y -= SeeteufelFront.GRAVITY_FACTOR * deltaTime;
            this.isFalling = true;
        } 
        if (this.position.y <= adjustedTargetY) {
            this.position.y = adjustedTargetY;
            
            if (this.isFalling) {
                this.splash.play();
            }
            this.isFalling = false;
        }
        
        // Attack targets specified via attack method at intervals.
        if (this.attackDelayTimer < SeeteufelFront.ATTACK_DELAY) {
            this.attackDelayTimer += deltaTime;
        } else if (!this.targets.isEmpty()) {
            this.attackDelayTimer = 0;
            this.shoot.play(1);
            Damageable target = this.targets.removeLast();
            Rectangle targetHitArea = target.getHitArea()[0];
            Vector3 rocketPosition = this.position.cpy();
            rocketPosition.x += SeeteufelFront.BASE_WIDTH / 2;
            rocketPosition.x -= Rocket.ROCKET_W / 2;
            Vector3 targetPosition = new Vector3(targetHitArea.x
                    - rocketPosition.x, targetHitArea.y - rocketPosition.y, 0);
            this.createdEntities.addFirst(new Rocket(this.rocketSpritesheet,
                    this.explosion, rocketPosition, targetPosition.nor().scl(
                            SeeteufelFront.ROCKET_SPEED), 1, 0,
                    new Rectangle[0], new Damageable[] { target }));
        }
    }

    @Override
    public void draw(Renderer renderer) {
        
        // Draw the front. Always the same.
        renderer.drawRegion(this.front, this.position.x, this.position.y);
        
        // Draw the long tentacles on the sides.
        this.backArmTimer++;
        if (this.backArmTimer >= SeeteufelFront.BACK_ARM_FRAMERATE) {
            this.backArmFrame = (this.backArmFrame + 1) % 3;
            this.backArmTimer = 0;
        }
        if (this.backArmFrame == 0) {
            renderer.drawRegion(this.backArmLeft[0], this.position.x - 81, this.position.y + 20);
            renderer.drawRegion(this.backArmRight[0], this.position.x + 65, this.position.y + 20);
        } else if (this.backArmFrame == 1) {
            renderer.drawRegion(this.backArmLeft[1], this.position.x - 81, this.position.y + 24);
            renderer.drawRegion(this.backArmRight[1], this.position.x + 65, this.position.y + 24);
        } else {
            renderer.drawRegion(this.backArmLeft[2], this.position.x - 81, this.position.y + 27);
            renderer.drawRegion(this.backArmRight[2], this.position.x + 65, this.position.y + 27);
        }
        
        // Draw the front tentacles.
        this.frontArmTimer++;
        if (this.frontArmTimer >= SeeteufelFront.FRONT_ARM_FRAMERATE) {
            this.frontArmFrame = !this.frontArmFrame;
            this.frontArmTimer = 0;
        }
        if (this.frontArmFrame) {
            renderer.drawRegion(this.frontArmRight[0], this.position.x + 58, this.position.y + 8);
            renderer.drawRegion(this.frontArmLeft[0], this.position.x - 48, this.position.y + 8);
        } else {
            renderer.drawRegion(this.frontArmLeft[1], this.position.x - 42, this.position.y + 11);
            renderer.drawRegion(this.frontArmRight[1], this.position.x + 58, this.position.y + 11);
        }
    }

    @Override
    public EntityState getState() {
        return EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        return !this.createdEntities.isEmpty();
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        if (this.createdEntities.isEmpty()) {
            throw new NoSuchElementException();
        } else {
            GameEntity[] entities = this.createdEntities.toArray(
                    new GameEntity[this.createdEntities.size()]);
            this.createdEntities.clear();
            return entities;
        }
    }
}
