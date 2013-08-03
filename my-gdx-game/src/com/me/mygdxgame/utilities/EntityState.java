package com.me.mygdxgame.utilities;

/**
 * Different possible states that GameEntities may declare themselves to be in
 * to their Screen.
 */
public enum EntityState {

    /**
     * Indicates that the entity is still performing its logic. Further updating
     * is still required.
     */
    Running,
    /**
     * Indicates that the entity has finished doing whatever it does, and should
     * be removed from the Screen. No further updates are required.
     */
    Destroyed
}
