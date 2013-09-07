package com.me.mygdxgame.utilities;

import com.badlogic.gdx.math.Vector3;

/**
 * Interface for objects that have some health value.
 */
public interface Damageable extends GameEntity {

    /**
     * Inflict some amount of damage on this object. May be negative; how this
     * is handled may be determined by the implementation.
     * 
     * @param damage
     *            The amount of damage to deal. May be negative.
     */
    public void damage(int damage);

    /**
     * Apply an external force to this object. How this is handled may be
     * determined by the implementation. Object may choose to ignore external
     * forces entirely, or act on it in some way unrelated to position.
     * 
     * @param damage
     *            A Vector3 indicating the direction and magnitude of force to
     *            apply.
     */
    public void applyForce(Vector3 force);
    
    /**
     * Retrieves the object's remaining health value.
     * 
     * @return Object's remaining health.
     */
    public int getHealth();
    
    /**
     * Retrieves the object's maximum health value.
     * 
     * @return Object's max health.
     */
    public int getMaxHealth();
}
