package com.me.mygdxgame.utilities;

import com.badlogic.gdx.math.Matrix4;

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
     * Renders object to screen. The matrix passed to this method should be
     * prepared beforehand.
     * 
     * @param transformMatrix
     *            The matrix this object should use to render. This should be
     *            the same argument that would be passed down to a SpriteBatch
     *            or ShapeRenderer.
     */
    public void draw(Matrix4 transformMatrix);
}
