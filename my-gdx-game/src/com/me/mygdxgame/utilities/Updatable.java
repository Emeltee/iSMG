package com.me.mygdxgame.utilities;

import com.badlogic.gdx.math.Vector3;

/**
 * Simple interface indicating that an object can be updated and drawn by
 * {@link GameScreen}s, or other managing entities.
 */
public interface Updatable {

    /**
     * Performs update logic.
     * 
     * @param deltaTime
     *            The time that has passed since the last update call, in
     *            seconds.
     */
    public void update(float deltaTime);

    /**
     * Renders object to screen.
     * 
     * @param renderer
     *            The Renderer this object should use to render. This should
     *            already be properly set up with the desired transformation
     *            matrix.
     */
    public void draw(Renderer renderer);
    
    /**
     * Retrieves the position of this object. Generally should correspond to the
     * lower-left corner of the object's visible area, but this may vary,
     * particularly with compound objects.
     * 
     * The returned vector should be a copy. Modifying it should not alter the
     * state of the object from which the vector was retrieved.
     * 
     * As this is mostly a 2D deal, the Z coordinate will likely not have any
     * particular meaning for most objects.
     * 
     * @return A new Vector3 containing the declared position of this object.
     */
    public Vector3 getPosition();
    
    /**
     * Retrieves the width of this object. Generally should refer to the width
     * of the object's visible area, but this may vary, particularly with
     * compound objects. May not necessarily be indicative of the width of the
     * area that should be used for collision detection.
     * 
     * @return The object's declared width.
     */
    public int getWidth();
    
    /**
     * Retrieves the height of this object. Generally should refer to the height
     * of the object's visible area, but this may vary, particularly with
     * compound objects. May not necessarily be indicative of the height of the
     * area that should be used for collision detection.
     * 
     * @return The object's declared height.
     */
    public int getHeight();
}
