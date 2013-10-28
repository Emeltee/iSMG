package com.me.mygdxgame.utilities;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Generic entity used to pass around static lists of obstacles.
 */
public class GenericEntity implements GameEntity {
    
    Collection<Rectangle> obstacles;
    Vector3 position = new Vector3();
    
    public GenericEntity(Collection<Rectangle> obstacles) {
        this.obstacles = obstacles;
    }
    
    @Override
    public Vector3 getPosition() {
        return this.position;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void update(float deltaTime) {
    }

    @Override
    public void draw(Renderer renderer) {
    }

    @Override
    public EntityState getState() {
        return EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        return false;
    }

    @Override
    public Rectangle[] getHitArea() {
        
        Rectangle[] returnList = new Rectangle[this.obstacles.size()];
        
        int x = 0;
        Iterator<Rectangle> itr = this.obstacles.iterator();
        while (itr.hasNext()) {
            returnList[x] = new Rectangle(itr.next());
            x++;
        }
        
        return returnList;
    }

    @Override
    public void destroy() {
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

}
