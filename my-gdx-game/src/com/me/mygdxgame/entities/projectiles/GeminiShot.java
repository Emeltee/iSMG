package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;

public class GeminiShot extends BusterShot {

    public static final int MAX_DEFLECTS = 4;
    public static enum Deflection { North, South };
    private boolean isDeflected;
    private Deflection dir2;
    private int numDeflects;
    
    public GeminiShot(Texture spriteSheet, Sound missSound, Vector3 position, int speed, BusterShot.ShotDirection dir, int power, float range, Collection<Rectangle> obstacles, Collection<Damageable> targets) {
        super(spriteSheet, missSound, position, speed, dir, 2 * power, 3 * range, obstacles, targets);
        this.isDeflected = false;
        this.numDeflects = 0;
    }
    
    @Override
    public void update(float deltaTime) {
        
        if (this.status == EntityState.Running) {
            // Move. Assumes speed is always positive.
            float toTravel = this.speed * deltaTime;
            if (this.dir == ShotDirection.LEFT) {
                this.position.x -= toTravel;            
            } else {
                this.position.x += toTravel;
            }
            
            // Check deflections
            if (this.isDeflected) {
                if (this.dir2 == Deflection.North) {
                    this.position.y += toTravel;
                } else {
                    this.position.y -= toTravel;
                }
            }
            
            // Check if range has been traveled. If so, destroy.
            this.distanceTraveled += toTravel;
            if (this.distanceTraveled >= this.range) {
                this.status = EntityState.Destroyed;
                this.missSound.play();
                return;
            }
            
            // Update hitbox.
            this.hitBox.x = this.position.x;
            this.hitBox.y = this.position.y;
            
            // Check collisions with potential targets.
            for (Damageable target : this.targets) {
                Rectangle[] hitAreas = target.getHitArea();
                for (Rectangle hitBox : hitAreas) {
                    if (hitBox.overlaps(this.hitBox)) {
                        target.damage(this.power);
                        this.status = EntityState.Destroyed;
                        return;
                    }
                }
            }
            
            // Check for collisions with obstacles.
            for (Rectangle r: this.watchOut) {
                if (r.overlaps(this.hitBox)) {
                    
                    if (numDeflects >= MAX_DEFLECTS) {
                        return;
                    } else {
                        numDeflects++;
                    }
                    
                    /*
                    float obstacleBottom = r.getY();
                    float obstacleLeft = r.getX();
                    float obstacleRight = r.getX() + r.getWidth();
                    float obstacleTop = r.getY() + r.getHeight();
                    */
                    
                    if (!this.isDeflected) {
                        this.isDeflected = true;
                        this.dir2 = Deflection.North;
                        this.dir = (this.dir == BusterShot.ShotDirection.LEFT) ? BusterShot.ShotDirection.RIGHT : BusterShot.ShotDirection.LEFT;
                        continue;
                    } else {
                        
                        Rectangle rightEdge = new Rectangle(r.getX()+r.getWidth(), r.getY(), 1, r.getHeight());
                        Rectangle leftEdge = new Rectangle(r.getX(), r.getY(), 1, r.getHeight());
                        Rectangle topEdge = new Rectangle(r.getX(), r.getY() + r.getHeight(), r.getWidth(), 1);
                        Rectangle bottomEdge = new Rectangle(r.getX(), r.getY(), r.getWidth(), 1);
                        
                        if (this.hitBox.overlaps(leftEdge) && this.dir == BusterShot.ShotDirection.RIGHT) {
                            this.dir = BusterShot.ShotDirection.LEFT;
                            this.position.x = leftEdge.getX() - BusterShot.BULLET_W;
                        } else if (this.hitBox.overlaps(rightEdge) && this.dir == BusterShot.ShotDirection.LEFT) {
                            this.dir = BusterShot.ShotDirection.RIGHT;
                            this.position.x = rightEdge.getX() + BusterShot.BULLET_W;
                        } else if (this.hitBox.overlaps(bottomEdge) && this.dir2 == Deflection.North) {
                            this.dir2 = Deflection.South;
                            this.position.y = bottomEdge.getY() - BusterShot.BULLET_W;
                        } else if (this.hitBox.overlaps(topEdge) && this.dir2 == Deflection.South) {
                            this.dir2 = Deflection.North;
                            this.position.y = topEdge.getY() + BusterShot.BULLET_W;
                        }
                        
                        /*
                        if (this.dir == BusterShot.ShotDirection.LEFT 
                            && this.position.x < obstacleRight) {
                            this.dir = BusterShot.ShotDirection.RIGHT;
                            this.position.x = obstacleRight + (BusterShot.BULLET_W + 2);
                            continue;
                        } else if (this.dir == BusterShot.ShotDirection.RIGHT
                                   && this.position.x > obstacleLeft) {
                            this.dir = BusterShot.ShotDirection.LEFT;
                            this.position.x = obstacleLeft - (BusterShot.BULLET_W + 2);
                            continue;
                        }
                        
                        if (this.dir2 == Deflection.North && this.position.y > obstacleBottom) {
                            this.dir2 = Deflection.South;
                            this.position.y = obstacleBottom - (BusterShot.BULLET_W + 2);
                            continue;
                        } else if (this.dir2 == Deflection.South && this.position.y < obstacleTop) {
                            this.dir2 = Deflection.North;
                            this.position.y = obstacleTop + (BusterShot.BULLET_W + 2);
                            continue;
                        }
                        */
                        
                    }
                    
                    /*
                    if ((this.dir == BusterShot.ShotDirection.RIGHT && (obstacleLeft < this.position.x))
                         || (this.dir == BusterShot.ShotDirection.LEFT && (obstacleRight > this.position.x))) { 
                        // Switch the X direction if collision on side
                        this.dir = (this.dir == BusterShot.ShotDirection.LEFT) ? BusterShot.ShotDirection.RIGHT : BusterShot.ShotDirection.LEFT;
                        System.out.println("Deflecting " + this.dir.name());
                    }                    
                    
                    
                    if ((this.dir2 == Deflection.North && (obstacleBottom < this.position.y))
                            || (this.dir2 == Deflection.South && (obstacleTop > this.position.y))) {                        
                        // Switch the Y direction
                        this.position.y += (this.dir2 == Deflection.South) ? BusterShot.BULLET_H : -BusterShot.BULLET_H;
                        this.dir2 = (this.dir2 == Deflection.North) ? Deflection.South : Deflection.North;
                        System.out.println("Deflecting " + this.dir2.name());
                    }*/
                    
                    
                    return;
                }
            }
        }

    }
    
}
