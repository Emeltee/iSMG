package com.me.mygdxgame.utilities;

import com.badlogic.gdx.math.Rectangle;

/**
 * Interface for objects that have some health value.
 */
public interface Damageable {

    /**
     * Inflict some amount of damage on this object. May be negative; hoe this
     * is handled may be determined by the implementation.
     * 
     * @param damage
     *            The amount of damage to deal. May be negative.
     */
    public void damage(int damage);
    
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
    
    /**
     * Retrieves this objects vulnerable areas.
     * 
     * @return An array of Rectangles indicating areas attacks may affect.
     */
    public Rectangle[] getHitArea();
}
