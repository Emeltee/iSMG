package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.BusterShot;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class MegaPlayer implements GameEntity, Damageable {

    private static final int MAX_HEALTH = 100;
    private static final int HITBOX_WIDTH = 26;
    private static final int HITBOX_HEIGHT = 32;
    private static final int SPRITE_WIDTH = 38;
    private static final int SPRITE_HEIGHT = 46;
    private static final float MAX_BUSTER_COOLDOWN = 1.0f;
    private static final float MAX_FLINCH_TIME = 1.0f;
    private static final float MAX_SPEED = 5.0f;
    private static final float ACCELERATION = 120.0f;
    private static final float DECELERATION = 30.0f;
    private static final short RUN_FRAMERATE = 3;
    private static final short MAX_RUN_FRAMES = 4;
    
    private Vector3 position = new Vector3();
    private Vector3 velocity = new Vector3();
    private int health = MegaPlayer.MAX_HEALTH;
    private Rectangle[] obstacles = null;
    private Seeteufel seeteufel = null;
    private Rectangle hitBox = new Rectangle(0, 0, MegaPlayer.HITBOX_WIDTH, MegaPlayer.HITBOX_HEIGHT);
    private int animationFrame = 0;
    private boolean isInAir = false;
    private int busterCooldown = 0;
    private float flinchTimer = 0;
    
    private TextureRegion[] runRight = new TextureRegion[4];
    private TextureRegion[] runLeft = new TextureRegion[4];
    private TextureRegion[] runShootRight = new TextureRegion[4];
    private TextureRegion[] runShootLeft = new TextureRegion[4];
    private TextureRegion standRight = new TextureRegion();
    private TextureRegion standLeft = new TextureRegion();
    private TextureRegion standShootRight = new TextureRegion();
    private TextureRegion standShootLeft = new TextureRegion();
    private TextureRegion[] damageRight = new TextureRegion[2];
    private TextureRegion[] damageLeft = new TextureRegion[2];
    private TextureRegion[] jumpLeft = new TextureRegion[2];
    private TextureRegion[] jumpRight = new TextureRegion[2];
    private TextureRegion[] jumpShootLeft = new TextureRegion[2];
    private TextureRegion[] jumpShootRight = new TextureRegion[2];
    
    private ArrayDeque<BusterShot> newShots = new ArrayDeque<BusterShot>();
    
    public MegaPlayer(Texture texture, Seeteufel seeteufel, Vector3 initialPosition, Rectangle[] obstacles) {
        this.seeteufel = seeteufel;
        this.position.set(initialPosition);
        this.obstacles = obstacles;
        
        this.runRight[0] = new TextureRegion(texture, 0, 0,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.runRight[1] = new TextureRegion(texture,
                this.runRight[0].getRegionX() + MegaPlayer.SPRITE_WIDTH, 0,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.runRight[2] = new TextureRegion(texture,
                this.runRight[1].getRegionX() + MegaPlayer.SPRITE_WIDTH, 0,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.runRight[3] = new TextureRegion(texture,
                this.runRight[2].getRegionX() + MegaPlayer.SPRITE_WIDTH, 0,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        
        this.runLeft[0] = new TextureRegion(this.runRight[0]);
        this.runLeft[0].flip(true, false);
        this.runLeft[1] = new TextureRegion(this.runRight[1]);
        this.runLeft[1].flip(true, false);
        this.runLeft[2] = new TextureRegion(this.runRight[2]);
        this.runLeft[2].flip(true, false);
        this.runLeft[3] = new TextureRegion(this.runRight[3]);
        this.runLeft[3].flip(true, false);
        
        this.runShootRight[0] = new TextureRegion(texture, 0,
                MegaPlayer.SPRITE_HEIGHT + 1, MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.runShootRight[1] = new TextureRegion(texture,
                this.runRight[1].getRegionX(),
                this.runShootRight[0].getRegionY(), MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.runShootRight[2] = new TextureRegion(texture,
                this.runRight[2].getRegionX(),
                this.runShootRight[0].getRegionY(), MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.runShootRight[3] = new TextureRegion(texture,
                this.runRight[3].getRegionX(),
                this.runShootRight[0].getRegionY(), MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        
        this.runShootLeft[0] = new TextureRegion(this.runShootRight[0]);
        this.runShootLeft[0].flip(true, false);
        this.runShootLeft[1] = new TextureRegion(this.runShootRight[1]);
        this.runShootLeft[1].flip(true, false);
        this.runShootLeft[2] = new TextureRegion(this.runShootRight[2]);
        this.runShootLeft[2].flip(true, false);
        this.runShootLeft[3] = new TextureRegion(this.runShootRight[3]);
        this.runShootLeft[3].flip(true, false);
        
        this.standRight = new TextureRegion(texture, 0,
                this.runShootRight[0].getRegionY() + MegaPlayer.SPRITE_HEIGHT,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.standLeft = new TextureRegion(this.standRight);
        this.standLeft.flip(true, false);
        
        this.standShootRight = new TextureRegion(texture,
                this.runShootRight[1].getRegionX(),
                this.standRight.getRegionY(), MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.standShootLeft = new TextureRegion(this.standShootRight);
        this.standShootLeft.flip(true, false);

        this.damageRight[0] = new TextureRegion(texture, this.runShootRight[2].getRegionX(),
                this.standShootRight.getRegionY(),
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.damageRight[1] = new TextureRegion(texture, this.runShootRight[3].getRegionX(),
                this.standShootRight.getRegionY(),
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.damageLeft[0] = new TextureRegion(this.damageRight[0]);
        this.damageLeft[0].flip(true, false);
        this.damageLeft[1] = new TextureRegion(this.damageRight[1]);
        this.damageLeft[1].flip(true, false);
        
        this.jumpRight[0] = new TextureRegion(texture,
                this.runRight[0].getRegionX(), this.standRight.getRegionY()
                        + MegaPlayer.SPRITE_HEIGHT, MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.jumpRight[1] = new TextureRegion(texture,
                this.runRight[2].getRegionX(), this.jumpRight[0].getRegionY(),
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        
        this.jumpLeft[0] = new TextureRegion(this.jumpRight[0]);
        this.jumpLeft[0].flip(true, false);
        this.jumpLeft[1] = new TextureRegion(this.jumpRight[1]);
        this.jumpLeft[1].flip(true, false);
        
        this.jumpShootRight[0] = new TextureRegion(texture,
                this.runRight[1].getRegionX(), this.standRight.getRegionY()
                        + MegaPlayer.SPRITE_HEIGHT, MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.jumpShootRight[1] = new TextureRegion(texture, this.runRight[3].getRegionX(),
                this.standRight.getRegionY() + MegaPlayer.SPRITE_HEIGHT,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        
        this.jumpShootLeft[0] = new TextureRegion(this.jumpShootRight[0]);
        this.jumpShootLeft[0].flip(true, false);
        this.jumpShootLeft[1] = new TextureRegion(this.jumpShootRight[1]);
        this.jumpShootLeft[1].flip(true, false);
    }
    
    public void setPosition(Vector3 position) {
        this.position.set(position);
    }
    
    public void setObstacles(Rectangle[] obstacles) {
        this.obstacles = obstacles;
    }
    
    public void applyForce(Vector3 force) {
        this.velocity.add(force);
    }
    
    @Override
    public void damage(int damage) {
        this.health -= damage;
        this.flinchTimer = MegaPlayer.MAX_FLINCH_TIME;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public int getMaxHealth() {
        return MegaPlayer.MAX_HEALTH;
    }

    @Override
    public Rectangle[] getHitArea() {
        return new Rectangle[] {this.hitBox};
    }

    @Override
    public void update(float deltaTime) {
        
//        // Check collisions with obstacles on y. Move box up by one to avoid
//        // detecting collisions with the floor you may be standing on.
//        this.position.y += this.velocity.y;
//        this.hitBox.y += 1;
//        
//        // Brute force it. We've got a deadline here.
//        for (Rectangle obstacle : this.obstacles) {
//            if (obstacle.overlaps(this.hitBox)) {
//                
//                // Check if it's a floor collision.
//                if (this.position.y + this.hitBox.height > obstacle.y + obstacle.height &&
//                        this.position.y <= obstacle.y + obstacle.height) {
//                    this.position.y = obstacle.y + obstacle.height;
//                    this.velocity.y = 0;
//                    this.isInAir = false;
//                }
//                // Check if it's a ceiling collision.
//                else if(this.position.y + this.hitBox.height < obstacle.y + obstacle.height && this.position.y + this.hitBox.height > obstacle.y)
//                {
//                    this.position.y = (collide->at(x)->posY_ - hitbox_.height_);
//                    velocity->y = 0;
//                    isJumping_ = false;
//                    jumpThrustTime_ = 0;
//                }
//            }
//        }
    }

    @Override
    public void draw() {
        this.animationFrame++;
        
        MyGdxGame.currentGame.spriteBatch
        .setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
        MyGdxGame.currentGame.spriteBatch.begin();
        
//        MyGdxGame.currentGame.spriteBatch.draw(this.runRight[(animationFrame / 20) % 4],
//                this.position.x, this.position.y);
        MyGdxGame.currentGame.spriteBatch.draw(this.jumpShootRight[(animationFrame / 20) % 2],
                this.position.x, this.position.y);
        
        MyGdxGame.currentGame.spriteBatch.end();
    }

    @Override
    public EntityState getState() {
        // Always running, until the end of the screen.
        return EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        return !this.newShots.isEmpty();
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        GameEntity[] returnList = (GameEntity[]) this.newShots.toArray();
        this.newShots.clear();
        return returnList;
    }

}
