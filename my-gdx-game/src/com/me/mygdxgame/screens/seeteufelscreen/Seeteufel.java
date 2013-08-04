package com.me.mygdxgame.screens.seeteufelscreen;

import java.util.NoSuchElementException;

import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class Seeteufel implements GameEntity, Damageable {

    @Override
    public void damage(int damage) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMaxHealth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Rectangle[] getHitArea() {
        // TODO
        return new Rectangle[] {new Rectangle(1, 1, 1, 1)};
    }

    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public EntityState getState() {
        // TODO Auto-generated method stub
        return EntityState.Running;
    }

    @Override
    public boolean hasCreatedEntities() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        // TODO Auto-generated method stub
        return null;
    }


}
