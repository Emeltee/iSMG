package com.me.mygdxgame.entities;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.entities.projectiles.BusterShot;
import com.me.mygdxgame.entities.projectiles.BusterShot.ShotDirection;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class MegaPlayer implements GameEntity, Damageable {

    private static final int MAX_HEALTH = 100;
    private static final int HITBOX_WIDTH = 28;
    private static final int HITBOX_HEIGHT = 32;
    private static final int SPRITE_WIDTH = 38;
    private static final int SPRITE_HEIGHT = 45;
    private static final int HITBOX_OFFSET_X = 5;
    private static final int HITBOX_OFFSET_Y = 5;
    private static final float MAX_BUSTER_COOLDOWN = 0.3f;
    private static final float MAX_FLINCH_TIME = 0.4f;
    private static final float FLINCH_ANIMATION_THRESHOLD = 0.3f;
    private static final float MAX_SPEED = 4.0f;
    private static final float MAX_JUMP_THRUST_TIME = 0.25f;
    private static final float ACCELERATION = 80.0f;
    private static final float DECELERATION = 40.0f;
    private static final short RUN_FRAMERATE = 10;
    private static final short MAX_RUN_FRAMES = 4;
    private static int BASE_SHOT_SPEED = 300;
    private static int BASE_SHOT_POWER = 1;
    private static float SHOT_OFFSET_Y = 16;
    private static float SHOT_OFFSET_X = 16;
    private static float BASE_SHOT_RANGE = 300;
    
    private Vector3 position = new Vector3();
    private Vector3 velocity = new Vector3();
    private Vector3 shotOrigin = new Vector3();
    private int health = MegaPlayer.MAX_HEALTH;
    private Rectangle[] obstacles = null;
    private Damageable[] targets = null;
    private Rectangle hitBox = new Rectangle(0, 0, MegaPlayer.HITBOX_WIDTH, MegaPlayer.HITBOX_HEIGHT);
    private int animationTimer = 0;
    private int prevFrame = 0;
    private boolean isInAir = false;
    private boolean isJumping = false;
    private boolean canJump = true;
    private float busterCooldown = 0;
    private float flinchTimer = 0;
    private float jumpThrustTimer = 0;
    private boolean isFacingRight = true;
    
    MegaPlayerResources resources = new MegaPlayerResources();
    
    private TextureRegion[] runRight = new TextureRegion[4];
    private TextureRegion[] runLeft = new TextureRegion[4];
    private TextureRegion[] runShootRight = new TextureRegion[4];
    private TextureRegion[] runShootLeft = new TextureRegion[4];
    private TextureRegion standRight = null;
    private TextureRegion standLeft = null;
    private TextureRegion standShootRight = null;
    private TextureRegion standShootLeft = null;
    private TextureRegion[] damageRight = new TextureRegion[2];
    private TextureRegion[] damageLeft = new TextureRegion[2];
    private TextureRegion[] jumpLeft = new TextureRegion[2];
    private TextureRegion[] jumpRight = new TextureRegion[2];
    private TextureRegion[] jumpShootLeft = new TextureRegion[2];
    private TextureRegion[] jumpShootRight = new TextureRegion[2];


    private ArrayDeque<BusterShot> newShots = new ArrayDeque<BusterShot>();
    
    /**
     * Simple storage class to manage the resources required by MegaPlayer.
     */
    public static class MegaPlayerResources {
        public Texture playerTexture = null;
        public Texture busterTexture = null;
        public Sound shootSound = null;
        public Sound shotMissSound = null;
        public Sound footstepSound = null;
        public Sound jumpSound = null;
        public Sound landSound = null;
        public Sound hurtSound = null;
        
        private boolean isLoaded = false;
        
        public void load() {
            if (!this.isLoaded) {
                this.busterTexture = new Texture(Gdx.files.internal("img/seeTiles1.png"));
                this.playerTexture = new Texture(Gdx.files.internal("img/mmd.png"));
                this.footstepSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-walk1.ogg"));
                this.hurtSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-hurt1.ogg"));
                this.jumpSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-jump1.ogg"));
                this.landSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-land1.ogg"));
                this.shootSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-buster-fire1.ogg"));
                this.shotMissSound = Gdx.audio.newSound(Gdx.files.internal("sound/sfx-buster-miss1.ogg"));
                
                this.isLoaded = true;
            }
        }
        
        public void unload() {
            if (this.isLoaded) {
                this.busterTexture.dispose();
                this.playerTexture.dispose();
                this.footstepSound.dispose();
                this.hurtSound.dispose();
                this.jumpSound.dispose();
                this.landSound.dispose();
                this.shootSound.dispose();
                this.shotMissSound.dispose();
                
                this.isLoaded = false;
            }
        }
    }
    
    public MegaPlayer(MegaPlayerResources resources, Vector3 initialPosition,
            Rectangle[] obstacles, Damageable[] targets) {
        this.resources = resources;
        this.position.set(initialPosition);
        this.obstacles = obstacles;
        this.targets = targets;
        
        this.runRight[0] = new TextureRegion(resources.playerTexture, 0, 0,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.runRight[1] = new TextureRegion(resources.playerTexture,
                this.runRight[0].getRegionX() + MegaPlayer.SPRITE_WIDTH, 0,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.runRight[2] = new TextureRegion(resources.playerTexture,
                this.runRight[1].getRegionX() + MegaPlayer.SPRITE_WIDTH, 0,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.runRight[3] = new TextureRegion(resources.playerTexture,
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
        
        this.runShootRight[0] = new TextureRegion(resources.playerTexture, 0,
                MegaPlayer.SPRITE_HEIGHT + 1, MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.runShootRight[1] = new TextureRegion(resources.playerTexture,
                this.runRight[1].getRegionX(),
                this.runShootRight[0].getRegionY(), MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.runShootRight[2] = new TextureRegion(resources.playerTexture,
                this.runRight[2].getRegionX(),
                this.runShootRight[0].getRegionY(), MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.runShootRight[3] = new TextureRegion(resources.playerTexture,
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
        
        this.standRight = new TextureRegion(resources.playerTexture, 0,
                this.runShootRight[0].getRegionY() + MegaPlayer.SPRITE_HEIGHT,
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.standLeft = new TextureRegion(this.standRight);
        this.standLeft.flip(true, false);
        
        this.standShootRight = new TextureRegion(resources.playerTexture,
                this.runShootRight[1].getRegionX(),
                this.standRight.getRegionY(), MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.standShootLeft = new TextureRegion(this.standShootRight);
        this.standShootLeft.flip(true, false);

        this.damageRight[0] = new TextureRegion(resources.playerTexture, this.runShootRight[2].getRegionX(),
                this.standShootRight.getRegionY(),
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.damageRight[1] = new TextureRegion(resources.playerTexture, this.runShootRight[3].getRegionX(),
                this.standShootRight.getRegionY(),
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        this.damageLeft[0] = new TextureRegion(this.damageRight[0]);
        this.damageLeft[0].flip(true, false);
        this.damageLeft[1] = new TextureRegion(this.damageRight[1]);
        this.damageLeft[1].flip(true, false);
        
        this.jumpRight[0] = new TextureRegion(resources.playerTexture,
                this.runRight[0].getRegionX(), this.standRight.getRegionY()
                        + MegaPlayer.SPRITE_HEIGHT, MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.jumpRight[1] = new TextureRegion(resources.playerTexture,
                this.runRight[2].getRegionX(), this.jumpRight[0].getRegionY(),
                MegaPlayer.SPRITE_WIDTH, MegaPlayer.SPRITE_HEIGHT);
        
        this.jumpLeft[0] = new TextureRegion(this.jumpRight[0]);
        this.jumpLeft[0].flip(true, false);
        this.jumpLeft[1] = new TextureRegion(this.jumpRight[1]);
        this.jumpLeft[1].flip(true, false);
        
        this.jumpShootRight[0] = new TextureRegion(resources.playerTexture,
                this.runRight[1].getRegionX(), this.standRight.getRegionY()
                        + MegaPlayer.SPRITE_HEIGHT, MegaPlayer.SPRITE_WIDTH,
                MegaPlayer.SPRITE_HEIGHT);
        this.jumpShootRight[1] = new TextureRegion(resources.playerTexture, this.runRight[3].getRegionX(),
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
    
    public Vector3 getPosition() {
        return this.position;
    }
    
    @Override
    public void damage(int damage) {
        this.health -= damage;
        this.flinchTimer = MegaPlayer.MAX_FLINCH_TIME;
        this.isJumping = false;
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

        // If not flinched, apply user controls. If flinched, just reduce flinch timer.
        if (this.flinchTimer == 0) {
            this.handleInput(deltaTime);
        } else {
            this.flinchTimer = Math.max(this.flinchTimer - deltaTime, 0);
        }
        
        // Move according to velocity, and check for obstacle collisions.
        this.position.y += this.velocity.y;
        this.checkCollisionsY();
        this.position.x += this.velocity.x;
        this.checkCollionsX();

        // Apply constant forces.
        this.handlePhysics(deltaTime);
    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        
        float drawPositionX = this.position.x - MegaPlayer.HITBOX_OFFSET_X;
        float drawPositionY = this.position.y - MegaPlayer.HITBOX_OFFSET_Y;
        
        MyGdxGame.currentGame.spriteBatch.setProjectionMatrix(transformMatrix);
        MyGdxGame.currentGame.spriteBatch.begin();
        
        // Facing right.
        if (this.isFacingRight) {
            if (this.flinchTimer > 0) {
                if (this.flinchTimer > MegaPlayer.FLINCH_ANIMATION_THRESHOLD) {
                    MyGdxGame.currentGame.spriteBatch.draw(this.damageRight[0],
                            drawPositionX, drawPositionY);
                } else {
                    MyGdxGame.currentGame.spriteBatch.draw(this.damageRight[1],
                            drawPositionX, drawPositionY);
                }
            } else if (this.isInAir) {
                if (this.velocity.y > 0) {
                    if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                            MyGdxGame.currentGame.spriteBatch.draw(this.jumpShootRight[0],
                                    drawPositionX, drawPositionY);
                    } else {
                        MyGdxGame.currentGame.spriteBatch.draw(this.jumpRight[0],
                                drawPositionX, drawPositionY);
                    }
                } else if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                    MyGdxGame.currentGame.spriteBatch.draw(this.jumpShootRight[1],
                            drawPositionX, drawPositionY);
                } else {
                    MyGdxGame.currentGame.spriteBatch.draw(this.jumpRight[1],
                            drawPositionX, drawPositionY);
                }
            } else if (this.velocity.x == 0) {
                if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                    MyGdxGame.currentGame.spriteBatch.draw(this.standShootRight,
                            drawPositionX, drawPositionY);
                } else {
                    MyGdxGame.currentGame.spriteBatch.draw(this.standRight,
                            drawPositionX, drawPositionY);
                }
            } else {
                
                // Update animation timer/frame for running.
                this.animationTimer++;
                int currentFrame = this.prevFrame;
                if (this.animationTimer > MegaPlayer.RUN_FRAMERATE) {
                    this.animationTimer = 0;
                    currentFrame = (currentFrame + 1) % MegaPlayer.MAX_RUN_FRAMES;
                    
                    if (currentFrame == 1 || currentFrame == 3) {
                        this.resources.footstepSound.play();
                    }
                }
                
                if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                    MyGdxGame.currentGame.spriteBatch.draw(this.runShootRight[currentFrame],
                            drawPositionX, drawPositionY);
                } else {
                    MyGdxGame.currentGame.spriteBatch.draw(this.runRight[currentFrame],
                            drawPositionX, drawPositionY);
                }
                
                this.prevFrame = currentFrame;
            }
        // Facing left.
        } else if (this.flinchTimer > 0) {
            if (this.flinchTimer > MegaPlayer.FLINCH_ANIMATION_THRESHOLD) {
                MyGdxGame.currentGame.spriteBatch.draw(this.damageLeft[0],
                        drawPositionX, drawPositionY);
            } else {
                MyGdxGame.currentGame.spriteBatch.draw(this.damageLeft[1],
                        drawPositionX, drawPositionY);
            }
        } else if (this.isInAir) {
            if (this.velocity.y > 0) {
                if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                        MyGdxGame.currentGame.spriteBatch.draw(this.jumpShootLeft[0],
                                drawPositionX, drawPositionY);
                } else {
                    MyGdxGame.currentGame.spriteBatch.draw(this.jumpLeft[0],
                            drawPositionX, drawPositionY);
                }
            } else if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                MyGdxGame.currentGame.spriteBatch.draw(this.jumpShootLeft[1],
                        drawPositionX, drawPositionY);
            } else {
                MyGdxGame.currentGame.spriteBatch.draw(this.jumpLeft[1],
                        drawPositionX, drawPositionY);
            }
        } else if (this.velocity.x == 0) {
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                MyGdxGame.currentGame.spriteBatch.draw(this.standShootLeft,
                        drawPositionX, drawPositionY);
            } else {
                MyGdxGame.currentGame.spriteBatch.draw(this.standLeft,
                        drawPositionX, drawPositionY);
            }
        } else {
            
            // Update animation timer/frame for running.
            this.animationTimer++;
            int currentFrame = this.prevFrame;
            if (this.animationTimer > MegaPlayer.RUN_FRAMERATE) {
                this.animationTimer = 0;
                currentFrame =  (currentFrame + 1) % MegaPlayer.MAX_RUN_FRAMES;
                
                if (currentFrame == 1 || currentFrame == 3) {
                    this.resources.footstepSound.play();
                }
            }
            
            if (Gdx.input.isKeyPressed(Keys.SPACE)) {
                MyGdxGame.currentGame.spriteBatch.draw(this.runShootLeft[currentFrame],
                        drawPositionX, drawPositionY);
            } else {
                MyGdxGame.currentGame.spriteBatch.draw(this.runLeft[currentFrame],
                        drawPositionX, drawPositionY);
            }
            
            this.prevFrame = currentFrame;
        }
        
        MyGdxGame.currentGame.spriteBatch.end();
        
//        // Test code. Draws hitbox as overlay.
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        MyGdxGame.currentGame.shapeRenderer.begin(ShapeType.Filled);
//        MyGdxGame.currentGame.shapeRenderer.setColor(new Color(0, 0, 1, 0.5f));
//        MyGdxGame.currentGame.shapeRenderer.setProjectionMatrix(MyGdxGame.currentGame.perspectiveCamera.combined);
//        MyGdxGame.currentGame.shapeRenderer.rect(this.hitBox.x, this.hitBox.y, this.hitBox.width, this.hitBox.height);
//        MyGdxGame.currentGame.shapeRenderer.end();
    }

    @Override
    public EntityState getState() {
        if (this.health > 0) {
            return EntityState.Running;
        } else {
            return EntityState.Destroyed;
        }
    }

    @Override
    public boolean hasCreatedEntities() {
        return !this.newShots.isEmpty();
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        
        GameEntity[] returnList = new GameEntity[this.newShots.size()];
        this.newShots.toArray(returnList);
        this.newShots.clear();
        return returnList;
    }
    
    /**
     * Check for collisions with obstacles in obstacles[] on the y axis.
     * Alters position as appropriate.
     */
    private void checkCollisionsY() {
        // Check collisions with obstacles on y. Move box down by one to ensure you
        // detect collisions with the floor you may be standing on.
        this.hitBox.x = this.position.x;
        this.hitBox.y = this.position.y - 1;
        
        float obstacleTop = 0;
        float hitBoxTop = this.hitBox.y + MegaPlayer.HITBOX_HEIGHT;
        boolean floorCollision = false;
        
        // Brute force it. We've got a deadline here.
        for (Rectangle obstacle : this.obstacles) {
            if (obstacle.overlaps(this.hitBox)) {
                
                obstacleTop = obstacle.y + obstacle.height;
                // Check if it's a floor collision.
                if (hitBoxTop > obstacleTop && this.hitBox.y <= obstacleTop) {
                    
                    // Play landing sound if in the air.
                    if (this.isInAir) {
                        this.resources.landSound.play();
                    }
                    
                    // Set position to top of obstacle. Set y velocity to 0. Reset air flag.
                    this.position.y = obstacle.y + obstacle.height;
                    this.velocity.y = 0;
                    this.isInAir = false;
                    floorCollision = true;
                    break;
                }
                // Check if it's a ceiling collision.
                else if(hitBoxTop < obstacleTop && hitBoxTop > obstacle.y)
                {
                    // Set position to bottom of obstacle. Set y velocity to 0. Reset jump values.
                    this.hitBox.y = obstacle.y - this.hitBox.height - 1;
                    this.velocity.y = 0;
                    this.isJumping = false;
                    this.jumpThrustTimer = 0;
                    break;
                }
            }
        }

        // If there were no y collisions, assume you're in the air.
        if (!floorCollision) {
            this.isInAir = true;
        }
    }
    
    /**
     * Check for collisions with obstacles in obstacles[] on the x axis.
     * Alters position as appropriate.
     */
    private void checkCollionsX() {
        // Check collisions with obstacles on x.
        this.hitBox.x = this.position.x;
        this.hitBox.y = this.position.y;
        
        float obstacleRightEdge = 0;
        float hitBoxRightEdge = this.hitBox.x + MegaPlayer.HITBOX_WIDTH;
        
        // O(n) like a charlatan.
        for (Rectangle obstacle : this.obstacles) {
            if (obstacle.overlaps(this.hitBox)) {
                
                obstacleRightEdge = obstacle.x + obstacle.width;
                
                // Collision on right side of obstacle.
                if (this.hitBox.x < obstacleRightEdge && hitBoxRightEdge > obstacleRightEdge) {
                    // Set position to obstacle's right edge. Drop velocity to 0 if you were moving left.
                    this.position.x = obstacleRightEdge;
                    this.hitBox.x = this.position.x;
                    if(this.velocity.x < 0)
                    {
                        this.velocity.x = 0;
                    }
                    break;
                }
                // Collision on left side of obstacle.
                else if (hitBoxRightEdge > obstacle.x && this.hitBox.x < obstacle.x) {
                    // Set position to obstacle's left edge. Drop velocity to 0 if you were moving right.
                    this.position.x = obstacle.x - this.hitBox.width;
                    this.hitBox.x = this.position.x;
                    if(this.velocity.x > 0)
                    {
                        this.velocity.x = 0;
                    }
                    break;
                }
            }
        }
    }
    
    /**
     * Updates velocity according to user input.
     * 
     * @param deltaTime This update's time delta.
     */
    private void handleInput(float deltaTime) {
        
        // Acceleration based on key states.
        if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
            this.isFacingRight = false;
            this.velocity.x = Math.max(this.velocity.x
                    - (MegaPlayer.ACCELERATION * deltaTime),
                    -MegaPlayer.MAX_SPEED);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            this.isFacingRight = true;
            this.velocity.x = Math.min(this.velocity.x
                    + (MegaPlayer.ACCELERATION * deltaTime),
                    MegaPlayer.MAX_SPEED);
        }
        
        // Jumping.
        if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W))
        {
            // Jump if you're on the ground and can jump. Set flags as needed.
            if (!this.isInAir && this.canJump) {
                this.isJumping = true;
                this.canJump = false;
                
                // Play jump sound.
                this.resources.jumpSound.play();
            }
        } else {
            // If not pressing key and in the middle of a jump, stop ascending.
            // Also keep the canJump flag updated. Ensures you need to let go
            // of the key before you can jump again. Reset jump timer, which
            // determines how long you ascend upwards.
            this.canJump = !this.isInAir;
            this.isJumping = false;
            this.jumpThrustTimer = 0;
        }
        
        // If already jumping, ascend upwards for as long as indicated by the
        // timer.
        if (this.isJumping) {
            this.jumpThrustTimer += deltaTime;
            if (this.jumpThrustTimer < MegaPlayer.MAX_JUMP_THRUST_TIME) {
                this.velocity.y = Math.max(this.velocity.y
                        + (MegaPlayer.ACCELERATION * deltaTime),
                        MegaPlayer.MAX_SPEED);
            } else {
                this.isJumping = false;
                this.jumpThrustTimer = 0;
            }
        }
        
        // Shooting.
        if (this.busterCooldown > 0) {
            this.busterCooldown = Math.max(this.busterCooldown - deltaTime, 0);
        } else if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            
            this.resources.shootSound.play(0.75f);

            this.shotOrigin.set(this.position);
            this.shotOrigin.y += MegaPlayer.SHOT_OFFSET_Y;

            this.busterCooldown = MegaPlayer.MAX_BUSTER_COOLDOWN;
            if (this.isFacingRight) {
                this.shotOrigin.x += MegaPlayer.SHOT_OFFSET_X;
                this.newShots.push(new BusterShot(this.resources.busterTexture,
                        this.resources.shotMissSound, this.shotOrigin,
                        MegaPlayer.BASE_SHOT_SPEED, ShotDirection.RIGHT,
                        MegaPlayer.BASE_SHOT_POWER, MegaPlayer.BASE_SHOT_RANGE,
                        this.obstacles, this.targets));
            } else {
                this.newShots.push(new BusterShot(this.resources.busterTexture,
                        this.resources.shotMissSound, this.shotOrigin,
                        MegaPlayer.BASE_SHOT_SPEED, ShotDirection.LEFT,
                        MegaPlayer.BASE_SHOT_POWER, MegaPlayer.BASE_SHOT_RANGE,
                        this.obstacles, this.targets));
            }
        }
    }
    
    /**
     * Apply constant forces such as deceleration and gravity to velocity.
     * @param deltaTime The time delta for the current update.
     */
    private void handlePhysics(float deltaTime) {
        // Applies deceleration to current velocity. Player decelerated such then they
        // slow to a stop over several frames when not accelerating (pushing a
        // movement button).
        if (this.velocity.x < 0) {
            this.velocity.x = Math.min(this.velocity.x
                    + (MegaPlayer.DECELERATION * deltaTime), 0);
        } else if (this.velocity.x > 0) {
            this.velocity.x = Math.max(this.velocity.x
                    - (MegaPlayer.DECELERATION * deltaTime), 0);
        }
        
        // Apply gravity. Accelerate downwards the same as you accelerate
        // upwards (acceleration - deceleration). It looks like it should be
        // more intuitive to control this way.
        if (this.isInAir) {
            this.velocity.y = Math.min(this.velocity.y -
                    ((MegaPlayer.ACCELERATION - MegaPlayer.DECELERATION) * deltaTime),
                    MegaPlayer.MAX_SPEED);
        }
    }
}
