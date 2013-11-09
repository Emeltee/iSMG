package com.me.mygdxgame.entities;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.MegaPlayer.MegaPlayerResources;
import com.me.mygdxgame.entities.particles.Explosion;
import com.me.mygdxgame.entities.projectiles.Bomb;
import com.me.mygdxgame.entities.projectiles.BusterShot.ShotDirection;
import com.me.mygdxgame.entities.projectiles.LemonShot;
import com.me.mygdxgame.entities.projectiles.Rocket;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.Damager;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class SeeteufelSide implements GameEntity, Damageable {

    private static final int TARGET_Y_OFFSET = 20;
    private static final int BASE_WIDTH = 72;
    
    private static final int MAX_HEALTH = 130;
    private static final int FRONT_ARM_FRAMERATE = 6;
    private static final int SIDE_ARM_FRAMERATE = 10;
    private static final int OBSTACLE_HITBOX_WIDTH = 140;
    private static final float MOVE_SPEED = 2f;
    private static final float BASE_ATTACK_DELAY = 1.0f;
    private static final float MIN_ATTACK_DELAY = 0.5f;
    private static final float ATTACK_DELAY_RAMP = 0.004f;
    private static final float CEILING_ATTACK_CHANCE = 0.2f;
    private static final float CEILING_ATTACK_COOLDOWN = 1f;
    private static final int ROCKET_POWER = 8;
    private static final int ROCKET_KNOCKBACK = 15;
    private static final float ROCKET_SPEED = 200.0f;
    private static final int SINK_SPEED = 50;
    private static final int SINK_DEPTH = 150;
    private static final float SINK_EXPLOSION_DELAY = 0.2f;
    private static final int[] SHOT_HEIGHTS = new int[] {60, 60, 60, 60, 
        60 + MegaPlayer.HITBOX_HEIGHT, 60 + MegaPlayer.HITBOX_HEIGHT * 2};
    private static final int BASE_SHOT_SPEED = 300;
    private static final int BASE_SHOT_POWER = 5;
    private static final float BASE_SHOT_RANGE = 1000;
    
    private static final float SFX_VOLUME = 0.5f;
    
    private Vector3 position;
    
    private Collection<Damageable> targets;
    private Collection<Damageable> ceilingTargets;
    private Collection<GameEntity> obstacles;
    
    private TextureRegion front = null;
    private TextureRegion[] frontArm = new TextureRegion[6];
    private TextureRegion[] sideArmBack = new TextureRegion[3];
    private TextureRegion[] sideArmFront = new TextureRegion[3];
    private Texture spritesheet = null;
    
    private Sound explosion = null;
    private Sound shoot = null;
    private Sound damage = null;
    private MegaPlayerResources otherSounds = null;
    
    private boolean frontArmAnimDir = true;
    private int frontArmFrame = 0;
    private int sideArmFrame = 0;
    private int frontArmTimer = 0;
    private int sideArmTimer = 0;
    private int targetY = 0;
    private EntityState state = EntityState.Running;
    private int health = MAX_HEALTH;
    private boolean moving = true;
    private boolean shootLemons = true;
    private float destroyedTargetY = 0;
    private LinkedList<Explosion> explosions = new LinkedList<Explosion>();
    
    private float attackDelayTimer = 0;
    private float cielingAttackDelayTimer = 0;
    private LinkedList<GameEntity> createdEntities = new LinkedList<GameEntity>();
    
    private Rectangle[] hitArea = new Rectangle[1];
    private Rectangle movementHitArea = new Rectangle(0, 0, 0, 0);
    
    public SeeteufelSide(Texture spritesheet, Sound explosion, Sound shoot,
            Sound damage, MegaPlayerResources otherSounds, Vector3 position,
            Collection<Damageable> targets, Collection<GameEntity> obstacles,
            Collection<Damageable> ceilingTargets) {
        this.position = new Vector3(position);
        this.position.x -= BASE_WIDTH / 2;
        this.position.y -= TARGET_Y_OFFSET;
        
        this.targets = targets;
        this.ceilingTargets = new LinkedList<Damageable>(ceilingTargets);
        this.obstacles = obstacles;
        
        this.hitArea[0] = new Rectangle(position.x, position.y, 72, 115);
        this.movementHitArea = new Rectangle(0, 0, OBSTACLE_HITBOX_WIDTH, 115);
        
        this.explosion = explosion;
        this.shoot = shoot;
        this.damage = damage;
        this.otherSounds = otherSounds;
        
        this.front = new TextureRegion(spritesheet, 256, 211, 72, 115);
        
        this.spritesheet = spritesheet;
        
        this.frontArm[0] = new TextureRegion(spritesheet, 275, 20, 101, 77);
        this.frontArm[1] = new TextureRegion(spritesheet, 388, 14, 111, 84);
        this.frontArm[2] = new TextureRegion(spritesheet, 500, 25, 122, 73);
        this.frontArm[3] = new TextureRegion(spritesheet, 278, 103, 100, 103);
        this.frontArm[4] = new TextureRegion(spritesheet, 546, 98, 76, 113);
        this.frontArm[5] = new TextureRegion(spritesheet, 441, 98, 59, 113);
        for (TextureRegion region : this.frontArm) {
            region.flip(true, false);
        }
        
        this.sideArmFront[0] = new TextureRegion(spritesheet, 256, 334, 113, 28);
        this.sideArmFront[1] = new TextureRegion(spritesheet, 370, 334, 113, 24);
        this.sideArmFront[2] = new TextureRegion(spritesheet, 484, 325, 113, 29);
        this.sideArmBack[0] = new TextureRegion(spritesheet, 256, 370, 113, 28);
        this.sideArmBack[1] = new TextureRegion(spritesheet, 370, 370, 113, 24);
        this.sideArmBack[2] = new TextureRegion(spritesheet, 484, 362, 113, 29);
        for (TextureRegion region : this.sideArmFront) {
            region.flip(true, false);
        }
        for (TextureRegion region : this.sideArmBack) {
            region.flip(true, false);
        }
    }
    
    
    @Override
    public void update(float deltaTime) {
        
        if (this.state == EntityState.Running) {
            // If out of health, sink and explode.
            // Reuse attack timer for explosion effects.
            if (this.health <= 0) {
                LinkedList<Explosion> finishedExplosions = new LinkedList<Explosion>();
                for (Explosion explosion : this.explosions) {
                    explosion.update(deltaTime);
                    if (explosion.getState() == EntityState.Destroyed) {
                        finishedExplosions.add(explosion);
                    }
                }
                this.explosions.removeAll(finishedExplosions);
                if (this.position.y > destroyedTargetY) {
                    this.position.y -= SINK_SPEED * deltaTime;
                    this.hitArea[0].y = this.position.y;
                    this.attackDelayTimer += deltaTime;
                    if (this.attackDelayTimer >= SINK_EXPLOSION_DELAY) {
                        explode();
                        this.attackDelayTimer = 0;
                    }
                }
                else {
                    this.state = EntityState.Destroyed;
                    this.explosions.clear();
                }
                return;
            }
            
            // Unlike SeeteufelFront, don't fall. Just snap to target position.
            this.position.y = this.targetY - TARGET_Y_OFFSET;
            this.hitArea[0].x = this.position.x;
            this.hitArea[0].y = this.position.y;
            
            if (this.moving) {
                // Move left until you hit an obstacle.
                this.position.x -= SeeteufelSide.MOVE_SPEED;
                this.movementHitArea.x = this.position.x - OBSTACLE_HITBOX_WIDTH / 2;
                this.movementHitArea.y = this.position.y;
                for (GameEntity entity : this.obstacles) {
                    for (Rectangle rect : entity.getHitArea()) {
                        if (this.movementHitArea.overlaps(rect)) {
                            this.moving = false;
                            return;
                        }
                    }
                }
            } else {
                // Attack.
                this.attackDelayTimer += deltaTime;
                this.cielingAttackDelayTimer += deltaTime;
                float attackDelay = Math.max(MIN_ATTACK_DELAY, BASE_ATTACK_DELAY - (MAX_HEALTH - this.health) * ATTACK_DELAY_RAMP);
                if (this.attackDelayTimer > attackDelay) {
                    
                    this.attackDelayTimer = 0;
                    this.shoot.play(SFX_VOLUME);
                    
                    // Decide whether to attack a ceiling tile in addition to the normal attack.
                    // Ramp up to +0.1 the normal rate as damage is done. Impose a minimum delay
                    // between ceiling attacks.
                    float ceilingAttackChance = 0;
                    if (this.cielingAttackDelayTimer >= CEILING_ATTACK_COOLDOWN) {
                        ceilingAttackChance = CEILING_ATTACK_CHANCE + 0.1f * ((MAX_HEALTH - this.health) / (float)MAX_HEALTH);
                        this.cielingAttackDelayTimer = 0;
                    }
                    if (Math.random() <= ceilingAttackChance) {
                        if (!this.ceilingTargets.isEmpty()) {
                            int index = (int) (Math.random() * this.ceilingTargets.size());
                            Damageable target = this.ceilingTargets.iterator().next();
                            Iterator<Damageable> itr = this.ceilingTargets.iterator();
                            for (int x = 0; x < index; x++) {
                                target = itr.next();
                            }
                            Vector3 targetPos = target.getPosition();
                            Vector3 rocketPosition = this.position.cpy();
                            rocketPosition.x += BASE_WIDTH / 2;
                            Vector3 targetPosition = new Vector3(targetPos.x
                                    - rocketPosition.x, targetPos.y - rocketPosition.y, 0);
    
                            this.createdEntities.add(new Rocket(this.spritesheet,
                                    this.explosion, rocketPosition, targetPosition.nor().scl(
                                            ROCKET_SPEED), 1, 0,
                                    new GameEntity[0], new Damageable[] { target }));
                        }
                    }
                    
                    // Decide on lemon or bomb attack based on health and random chance.
                    if (this.health < MAX_HEALTH / 3) {
                        // Below 1/3 health, swap between bombs and lemons with a 20% chance.
                        if (Math.random() <= 0.2) {
                            this.shootLemons = !this.shootLemons;
                        }
                    }
                    else if(this.health < (MAX_HEALTH / 3) * 2) {
                        // Below 2/3 health, switch from lemons to bombs.
                        this.shootLemons = false;
                    }
                    
                    if (this.shootLemons) {
                        // Shoot lemon at one of three random heights.
                        // Get random value up to 3, and wrap 4 back to 0.
                        // This way, lower height gets chosen more often.
                        int shotHeight = SHOT_HEIGHTS[(int) (Math.random() * SHOT_HEIGHTS.length)];
                        Vector3 shotPos = new Vector3(this.position.x, this.position.y + shotHeight, 0);
                        this.createdEntities.offer(new LemonShot(spritesheet,
                                this.otherSounds.shotMissSound, shotPos,
                                BASE_SHOT_SPEED, ShotDirection.LEFT,
                                BASE_SHOT_POWER, BASE_SHOT_RANGE, this.obstacles,
                                this.targets));
                    }
                    else {
                     // Shoot bomb with random trajectory.
                        Vector3 rocketVel = new Vector3((float) (-Math.random() * 200 - 60), 260.0f, 0);
                        Damageable[] currentTargets = new Damageable[this.targets.size()];
                        currentTargets = this.targets.toArray(currentTargets);
                        GameEntity[] currentObstacles= new GameEntity[this.obstacles.size()];
                        currentObstacles = this.obstacles.toArray(currentObstacles);
                        this.createdEntities.offer(
                                new Bomb(this.spritesheet, this.explosion, this.position,
                                        rocketVel, ROCKET_POWER, ROCKET_KNOCKBACK,
                                        currentObstacles, currentTargets));
                    }
                }
            }
            
            
            // Determine animation frames.
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
        }
    }

    @Override
    public void draw(Renderer renderer) {
        
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
        
        // Draw any explosions. Handled here instead of by the screen so we can render them in front.
        for (Explosion explosion : this.explosions) {
            explosion.draw(renderer);
        }
    }
    
    private void explode() {
        // Create explosions.
        this.explosions.add(new Explosion(this.spritesheet,
                new Vector3((float) (this.position.x + (Math.random() * this.front.getRegionWidth())),
                        (float) (this.position.y + (Math.random() * this.front.getRegionHeight())),
                        this.position.z)));
        
        // Play sound.
        this.explosion.play(SFX_VOLUME);
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
    public void damage(Damager damager) {
        
        boolean alive = this.health > 0;
        
        if (damager.getPower() > 0) {
            this.health = Math.max(0, this.health - damager.getPower());
            this.damage.play(SFX_VOLUME);
        }
        
        if (alive && this.health <= 0) {
            this.destroyedTargetY = this.position.y - SINK_DEPTH;
        }
    }

    @Override
    public int getHealth() {
        return this.health;
    }


    @Override
    public int getMaxHealth() {
        return SeeteufelSide.MAX_HEALTH;
    }


    @Override
    public Vector3 getPosition() {
        return new Vector3(this.position);
    }


    @Override
    public int getWidth() {
        // Rough estimate. Don't count arms.
        return this.front.getRegionWidth();
    }


    @Override
    public int getHeight() {
        // Rough estimate. Don't count arms.
        return this.front.getRegionHeight();
    }
}
