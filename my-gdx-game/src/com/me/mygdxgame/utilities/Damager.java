package com.me.mygdxgame.utilities;

/**
 * Interface for objects that can deal damage.
 */
public interface Damager extends GameEntity {
    
    /**
     * @return The amount of power this object should do. May be zero or
     *         negative.
     */
    public int getPower();
    
    /**
     * @return The amount of force this object should apply.
     */
    public int getKnockback();
    
    //public DamageType getDamageType();
}
