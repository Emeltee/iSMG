package com.me.mygdxgame.utilities;

/**
 * Interface for objects that have some health value.
 */
public interface Damageable extends GameEntity {

    /**
     * Inflict power upon this object using the given Damager. The effect of
     * this call is determined by the Damageable; the Damager simply provides
     * accessors to properties of interest.
     * 
     * @param power
     *            The Damager to use on this Damageable.
     */
    public void damage(Damager damager);
    
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
