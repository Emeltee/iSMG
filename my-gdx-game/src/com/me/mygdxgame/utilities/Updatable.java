package com.me.mygdxgame.utilities;

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
}
