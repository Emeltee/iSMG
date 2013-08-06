package com.me.mygdxgame.utilities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.math.Matrix4;

/**
 * Interface to be followed by dynamic entities managed by {@link GameScreen}.
 * Allows for a uniform way to access some necessary state data, and for
 * entities to request things such as object creation from the presiding Screen.
 */
public interface GameEntity extends Updatable {

    /**
     * Performs update logic. Should be called once per frame for as long as
     * this GameEntity is in the Running {@link EntityState} state. No rendering
     * should take place in this method, as it may interfere with the current
     * Screen's rendering. All rendering should instead be performed in
     * {@link #draw}.
     * 
     * Although a call to draw should generally follow a call to update earlier
     * in the same frame, no guarantee is made. The draw method may be called at
     * any time, or not at all. The timing will be whatever is most convenient
     * for the managing {@link GameScreen}. As such, all critical logic should
     * take place in update.
     * 
     * @param deltaTime
     *            The time that has passed since the last update call, in
     *            seconds.
     */
    @Override
    public void update(float deltaTime);
    
    /**
     * Renders this GameEntity as needed. A call to this method generally
     * follows a call to this GameEntity's {@link #update} method earlier in the
     * same frame, but no such guarantee is made. The draw method may be called
     * at any during a frame, or not at all. As such, all critical logic should
     * go in update.
     * <p>
     * The matrix passed to this method should be prepared beforehand.
     * 
     * @param transformMatrix
     *            The matrix this object should use to render. This should be
     *            the same argument that would be passed down to a SpriteBatch
     *            or ShapeRenderer.
     */
    @Override
    public void draw(Matrix4 transformMatrix);

    /**
     * Retrieves this GameEntity's current state. The state returned will
     * communicate to the caller whether it requires further updates.
     * 
     * @return An {@link EntityState} indicating the entity's current state.
     */
    public EntityState getState();
    
    /**
     * Checks whether or not this GameEntity has other GameEntities it would
     * like to pass back to the caller. Generally, this would be used to allow
     * GameEntities to create new GameEntities (such as particles) and return
     * them to the {@link GameScreen} running above them to be managed. If there
     * are entities to be retrieved, one can do so by calling
     * {@link #getCreatedEntities}.
     * 
     * @return True if there are GameEntities to be retrieved via
     *         getCreatedEntities, false otherwise.
     */
    public boolean hasCreatedEntities();
    
    /**
     * Retrieves any new GameEntities created by this GameEntity which have yet
     * to be retrieved. Whether or not there are any entities to retrieve is
     * indicated by {@link #hasCreatedEntities}. Calling this method when there
     * is nothing to retrieve will result in an exception.
     * 
     * @return An array of new GameEntities created by this GameEntity.
     * @throws NoSuchElementException
     *             If there is nothing to be retrieved.
     */
    public GameEntity[] getCreatedEntities() throws NoSuchElementException;
}
