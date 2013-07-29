package com.me.mygdxgame.utilities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Manages static elements of a "level". This includes background cosmetics and
 * static obstacles.
 */
public interface GameMap {

    /**
     * Loads all resources (textures and sounds) specific to this game. Does
     * nothing if resources have already been loaded. TODO Probably a good idea
     * to pull out common resources and place them into some separate pool to
     * avoid having multiple copies of resources in memory at once.
     */
    public void load();
    
    /**
     * Unloads all resources (textures and sounds) that have been previously
     * loaded by this game through load(). Does nothing if no resources are
     * currently loaded.
     */
    public void unload();
    
    /** Retrieves the initial position of the player.
     * 
     * @return A Vector3 containing the desired initial player position.
     */
    public Vector3 getInitialPosition();
    
    /**
     * Creates and returns an array of the static obstacles in this map.
     * 
     * @return A new array of static obstacles associated with this map.
     */
    public Rectangle[] getObstacles();

    /**
     * Updates any logic and draws all entities to the screen. TODO, some
     * (enforced) form of frustum culling would eventually be nice.
     * 
     * @param deltaTime
     *            The amount of time in seconds that has passed since the
     *            previous render call.
     */
    public void render(float deltaTime);
}
