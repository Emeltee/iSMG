package com.me.mygdxgame.entities;

import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.projectiles.Bomb;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class SeeteufelSide implements GameEntity, Damageable {

    public static final int TARGET_Y_OFFSET = 20;
    
    private static final int MAX_HEALTH = 200;
    private static final int FRONT_ARM_FRAMERATE = 6;
    private static final int SIDE_ARM_FRAMERATE = 10;
    private static final int OBSTACLE_HITBOX_WIDTH = 140;
    private static final float MOVE_SPEED = 2f;
    private static final float ATTACK_DELAY = 0.6f;
    private static final int ROCKET_POWER = 10;
    private static final int ROCKET_KNOCKBACK = 15;
    
    private Vector3 position;
    
    private Collection<Damageable> targets;
    private Collection<Rectangle> obstacles;
    
    private TextureRegion front = null;
    private TextureRegion[] frontArm = new TextureRegion[6];
    private TextureRegion[] sideArmBack = new TextureRegion[3];
    private TextureRegion[] sideArmFront = new TextureRegion[3];
    private Texture rocketSpritesheet = null;
    
    private Sound explosion = null;
    private Sound shoot = null;
    private Sound damage = null;
    
    private boolean frontArmAnimDir = true;
    private int frontArmFrame = 0;
    private int sideArmFrame = 0;
    private int frontArmTimer = 0;
    private int sideArmTimer = 0;
    private int targetY = 0;
    private EntityState state = EntityState.Running;
    private int health = MAX_HEALTH;
    private boolean moving = true;
    
    private float attackDelayTimer = 0;
    private LinkedList<GameEntity> createdEntities = new LinkedList<GameEntity>();
    
    private Rectangle[] hitArea = new Rectangle[1];
    private Rectangle movementHitArea = new Rectangle(0, 0, 0, 0);
    
    public SeeteufelSide(Texture spritesheet, Texture rocketSpritesheet,
            Sound explosion, Sound shoot, Sound damage, Vector3 position, Collection<Damageable> targets, Collection<Rectangle> obstacles) {
        this.position = new Vector3(position);
        this.targets = targets;
        this.obstacles = obstacles;
        
        this.hitArea[0] = new Rectangle(position.x, position.y, 72, 115);
        this.movementHitArea = new Rectangle(0, 0, OBSTACLE_HITBOX_WIDTH, 115);
        
        this.explosion = explosion;
        this.shoot = shoot;
        this.damage = damage;
        
        this.front = new TextureRegion(spritesheet, 0, 211, 72, 115);
        
        this.rocketSpritesheet = rocketSpritesheet;
        
        this.frontArm[0] = new TextureRegion(spritesheet, 19, 20, 101, 77);
        this.frontArm[1] = new TextureRegion(spritesheet, 132, 14, 111, 84);
        this.frontArm[2] = new TextureRegion(spritesheet, 244, 25, 122, 73);
        this.frontArm[3] = new TextureRegion(spritesheet, 22, 103, 100, 103);
        this.frontArm[4] = new TextureRegion(spritesheet, 290, 98, 76, 113);
        this.frontArm[5] = new TextureRegion(spritesheet, 185, 98, 59, 113);
        for (TextureRegion region : this.frontArm) {
            region.flip(true, false);
        }
        
        this.sideArmFront[0] = new TextureRegion(spritesheet, 0, 334, 113, 28);
        this.sideArmFront[1] = new TextureRegion(spritesheet, 114, 334, 113, 24);
        this.sideArmFront[2] = new TextureRegion(spritesheet, 228, 325, 113, 29);
        this.sideArmBack[0] = new TextureRegion(spritesheet, 0, 370, 113, 28);
        this.sideArmBack[1] = new TextureRegion(spritesheet, 114, 370, 113, 24);
        this.sideArmBack[2] = new TextureRegion(spritesheet, 228, 362, 113, 29);
        for (TextureRegion region : this.sideArmFront) {
            region.flip(true, false);
        }
        for (TextureRegion region : this.sideArmBack) {
            region.flip(true, false);
        }
    }
    
    
    @Override
    public void update(float deltaTime) {
        // Unlike SeeteufelFront, don't fall. Just snap to target position.
        this.position.y = this.targetY - SeeteufelFront.TARGET_Y_OFFSET;
        
        if (this.state == EntityState.Running) {
            if (this.health <= 0) {
                this.state = EntityState.Destroyed;
                return;
            }
            
            if (this.moving) {
                // Move left until you hit an obstacle.
                this.position.x -= SeeteufelSide.MOVE_SPEED;
                this.movementHitArea.x = this.position.x - OBSTACLE_HITBOX_WIDTH / 2;
                this.movementHitArea.y = this.position.y;
                for (Rectangle rect : this.obstacles) {
                    if (this.movementHitArea.overlaps(rect)) {
                        this.moving = false;
                        break;
                    }
                }
                this.hitArea[0].x = this.position.x;
                this.hitArea[0].y = this.position.y;
            } else {
                // Attack.
                this.attackDelayTimer += deltaTime;
                if (this.attackDelayTimer > ATTACK_DELAY) {
                    this.shoot.play();
                    Vector3 rocketVel = new Vector3((float) (-Math.random() * 200 - 50), 250.0f, 0);
                    Damageable[] currentTargets = new Damageable[this.targets.size()];
                    currentTargets = this.targets.toArray(currentTargets);
                    Rectangle[] currentObstacles= new Rectangle[this.obstacles.size()];
                    currentObstacles = this.obstacles.toArray(currentObstacles);
                    this.createdEntities.offer(
                            new Bomb(this.rocketSpritesheet, this.explosion, this.position,
                                    rocketVel, ROCKET_POWER, ROCKET_KNOCKBACK,
                                    currentObstacles, currentTargets));
                    this.attackDelayTimer = 0;
                }
            }
        }
    }

    @Override
    public void draw(Renderer renderer) {
        
        // Determine frames.
        this.sideArmTimer++;
        if (this.sideArmTimer >= SIDE_ARM_FRAMERATE) {
            this.sideArmFrame = (this.sideArmFrame + 1) % 3;
            this.sideArmTimer = 0;
        }
        this.frontArmTimer++;
        if (this.frontArmTimer >= FRONT_ARM_FRAMERATE) {
            if (frontArmAnimDir) {
                this.frontArmFrame++;
                if (frontArmFrame == 5) {
                    frontArmAnimDir = false;
                }
            } else {
                this.frontArmFrame--;
                if (frontArmFrame == 0) {
                    frontArmAnimDir = true;
                }
            }
            this.frontArmTimer = 0;
        }
        
        // Draw back tentacle.
        if (this.frontArmFrame == 0) {
            this.frontArm[0].flip(true, false);
            renderer.drawRegion(this.frontArm[0], this.position.x - 62, this.position.y + 32);
            this.frontArm[0].flip(true, false);
            renderer.drawRegion(this.frontArm[0], this.position.x + 50, this.position.y + 32);
        } else if (this.frontArmFrame == 1){
            this.frontArm[1].flip(true, false);
            renderer.drawRegion(this.frontArm[1], this.position.x - 73, this.position.y + 28);
            this.frontArm[1].flip(true, false);
            renderer.drawRegion(this.frontArm[1], this.position.x + 50, this.position.y + 28);
        } else if (this.frontArmFrame == 2){
            this.frontArm[2].flip(true, false);
            renderer.drawRegion(this.frontArm[2], this.position.x - 85, this.position.y + 31);
            this.frontArm[2].flip(true, false);
            renderer.drawRegion(this.frontArm[2], this.position.x + 50, this.position.y + 31);
        } else if (this.frontArmFrame == 3){
            this.frontArm[3].flip(true, false);
            renderer.drawRegion(this.frontArm[3], this.position.x - 60, this.position.y + 18);
            this.frontArm[3].flip(true, false);
            renderer.drawRegion(this.frontArm[3], this.position.x + 50, this.position.y + 18);
        } else if (this.frontArmFrame == 4){
            this.frontArm[4].flip(true, false);
            renderer.drawRegion(this.frontArm[4], this.position.x - 37, this.position.y + 16);
            this.frontArm[4].flip(true, false);
            renderer.drawRegion(this.frontArm[4], this.position.x + 50, this.position.y + 16);
        } else {
            this.frontArm[5].flip(true, false);
            renderer.drawRegion(this.frontArm[5], this.position.x - 18, this.position.y + 10);
            this.frontArm[5].flip(true, false);
            renderer.drawRegion(this.frontArm[5], this.position.x + 50, this.position.y + 10);
        }
        if (this.sideArmFrame == 0) {
            renderer.drawRegion(this.sideArmBack[2], this.position.x - 78, this.position.y + 14);
        } else if (this.sideArmFrame == 1) {
            renderer.drawRegion(this.sideArmBack[0], this.position.x - 78, this.position.y + 18);
        } else {
            renderer.drawRegion(this.sideArmBack[1], this.position.x - 78, this.position.y + 22);
        }
        
        // Draw the body.
        renderer.drawRegion(this.front, this.position.x, this.position.y);
        
        // Draw front tentacles.
        if (this.sideArmFrame == 0) {
            renderer.drawRegion(this.sideArmFront[0], this.position.x - 66, this.position.y + 14);
        } else if (this.sideArmFrame == 1) {
            renderer.drawRegion(this.sideArmFront[1], this.position.x - 66, this.position.y + 18);
        } else {
            renderer.drawRegion(this.sideArmFront[2], this.position.x - 66, this.position.y + 22);
        }
    }
    
    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
    
    public int getTargetY() {
        return this.targetY;
    }

    @Override
    public EntityState getState() {
        return this.state;
    }

    @Override
    public boolean hasCreatedEntities() {
        return !this.createdEntities.isEmpty();
    }

    @Override
    public Rectangle[] getHitArea() {
        return this.hitArea;
    }

    @Override
    public void destroy() {
        this.state = EntityState.Destroyed;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        if (this.createdEntities.isEmpty()) {
            throw new NoSuchElementException("No entities to return.");
        }
        GameEntity[] newEntities = new GameEntity[this.createdEntities.size()];
        newEntities = this.createdEntities.toArray(newEntities);
        this.createdEntities.clear();
        return newEntities;
    }


    @Override
    public void damage(int damage) {
        if (damage > 0) {
            this.health -= damage;
            this.damage.play();
        }
    }


    @Override
    public void applyForce(Vector3 force) {
        // Do nothing.
    }


    @Override
    public int getHealth() {
        return this.health;
    }


    @Override
    public int getMaxHealth() {
        return SeeteufelSide.MAX_HEALTH;
    }

}
