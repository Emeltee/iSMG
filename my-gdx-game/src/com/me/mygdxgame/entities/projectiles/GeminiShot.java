package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;

public class GeminiShot extends BusterShot {

    public static final int GEMINI_X = 234;
    public static final int GEMINI_Y = 175;
    public static final int GEMINI_W = 7;
    public static final int GEMINI_H = 5;

    public static final int MAX_DEFLECTS = 6;

    public static enum ShotDeflection {
        UP, DOWN
    };

    private boolean isDeflected;
    private ShotDeflection dir2;
    private int numDeflects;

    public GeminiShot(Texture spriteSheet, Sound missSound, Vector3 position,
            int speed, BusterShot.ShotDirection dir, int power, float range,
            Collection<Rectangle> obstacles, Collection<Damageable> targets) {
        super(spriteSheet, missSound, position, speed, dir, 2 * power,
                5 * range, obstacles, targets);
        super.bullet = new TextureRegion(spriteSheet, GEMINI_X, GEMINI_Y,
                GEMINI_W, GEMINI_H);
        this.isDeflected = false;
        this.numDeflects = 0;
    }

    @Override
    public void update(float deltaTime) {
        
        Vector2 bulletA = new Vector2(this.position.x + GEMINI_W / 2, this.position.y + GEMINI_H / 2); // Trajectory Start Point
        
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
                if (this.dir2 == ShotDeflection.UP) {
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
            
            Vector2 bulletB = new Vector2(this.position.x + GEMINI_W / 2, this.position.y + GEMINI_H / 2); // Trajectory End point;
            if (this.dir == BusterShot.ShotDirection.LEFT)  { bulletA.x += GEMINI_W / 2; bulletB.x -= GEMINI_W / 2; }
            if (this.dir == BusterShot.ShotDirection.RIGHT) { bulletA.x -= GEMINI_W / 2; bulletB.x += GEMINI_W / 2; }
            if (this.dir2 == GeminiShot.ShotDeflection.UP)   { bulletA.y -= GEMINI_H / 2; bulletB.y += GEMINI_H / 2; }
            if (this.dir2 == GeminiShot.ShotDeflection.DOWN)   { bulletA.y += GEMINI_H / 2; bulletB.y -= GEMINI_H / 2; }
            
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
                    
                    if (numDeflects >= MAX_DEFLECTS) { return; }
                    else { numDeflects++; }                    
                    
                    if (!this.isDeflected) {
                        this.isDeflected = true;
                        this.dir2 = ShotDeflection.UP;
                        this.dir = (this.dir == BusterShot.ShotDirection.LEFT) ? BusterShot.ShotDirection.RIGHT : BusterShot.ShotDirection.LEFT;
                        continue;
                    } else {
                        
                        // These are really just lines for each edge
                        Rectangle rightEdge = new Rectangle(r.getX()+r.getWidth(), r.getY(), 1, r.getHeight());
                        Rectangle leftEdge = new Rectangle(r.getX(), r.getY(), 1, r.getHeight());
                        Rectangle topEdge = new Rectangle(r.getX(), r.getY() + r.getHeight(), r.getWidth(), 1);
                        Rectangle bottomEdge = new Rectangle(r.getX(), r.getY(), r.getWidth(), 1);
                        
                        // Find the intersection with the shortest distance from original point
                        Rectangle hitEdge = null;
                        Vector2 intersection = new Vector2();
                        float prevDistance = -1;
                        
                        /* This plots the trajectory of the bullet, and checks for any intersections
                         * with the edges, then determines which edge is closest to the bullet's original
                         * point in space. If the bullet overlaps two edges, it can determine which edge
                         * the bullet overlapped first. TODO Replace regular collision detection with this test?
                         */

                        for(Rectangle edge: new Rectangle [] {rightEdge, leftEdge, topEdge, bottomEdge}) {
                            Vector2 edgeA = new Vector2(edge.getX(), edge.getY());
                            Vector2 edgeB = new Vector2(edge.getX() + edge.getWidth(), edge.getY() + edge.getHeight());
                            // Check if bullet's path crosses edge's path
                            boolean crossed = Intersector.intersectSegments(bulletA, bulletB, edgeA, edgeB, intersection);
                            float tempDistance = (crossed) ? Intersector.distanceLinePoint(edgeA, edgeB, bulletA) : -1;                            
                            if (hitEdge == null && crossed) {
                                hitEdge = edge;
                                prevDistance = tempDistance;
                            } else {
                                if (crossed && tempDistance < prevDistance) {
                                    hitEdge = edge;
                                    prevDistance = tempDistance;                                    
                                }
                            }

                        }
                        
                        if (hitEdge == null) {
                            /* Since this is based on a single line through the middle of the shot
                             * there's a chance shots may overlap as a rectangle, while this doesn't
                             * detect an edge. So it's possible (however remotely) for shots to graze
                             * an edge. Maybe. In any case, it'll throw up NullPointerException unless
                             * I catch it here.
                             */
                            return;
                        } else if (hitEdge.equals(leftEdge)) {
                            this.dir = BusterShot.ShotDirection.LEFT;
                            this.position.x = leftEdge.getX() - GeminiShot.GEMINI_W;
                        } else if (hitEdge.equals(rightEdge)) {
                            this.dir = BusterShot.ShotDirection.RIGHT;
                            this.position.x = rightEdge.getX() + GeminiShot.GEMINI_W;
                        } else if (hitEdge.equals(topEdge)) {
                            this.dir2 = GeminiShot.ShotDeflection.UP;
                            this.position.y = topEdge.getY() + GeminiShot.GEMINI_W;
                        } else if (hitEdge.equals(bottomEdge)) {
                            this.dir2 = ShotDeflection.DOWN;
                            this.position.y = bottomEdge.getY() - GeminiShot.GEMINI_W;
                        }
                        
                        /*
                         * Method below is tried and true, but a little less precise
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
                        */
                        
                    }                    
                    
                    return;
                }
            }
        }

    }
}
