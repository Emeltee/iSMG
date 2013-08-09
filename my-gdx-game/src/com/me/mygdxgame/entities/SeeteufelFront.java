package com.me.mygdxgame.entities;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.NoSuchElementException;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;

public class SeeteufelFront implements GameEntity {
    
    private ArrayDeque<Damageable> targets = null;
    
    private TextureRegion front = null;
    private TextureRegion frontArmRight = null;
    private TextureRegion frontArmLeft = null;
    private TextureRegion backArmRight = null;
    private TextureRegion backArmLeft = null;
    private TextureRegion upperArmRight = null;
    private TextureRegion upperArmLeft = null;
    
    public SeeteufelFront(Texture spritesheet, Collection<Damageable> targets) {
        
    }
    
    @Override
    public void update(float deltaTime) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(Matrix4 transformMatrix) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public EntityState getState() {
        // TODO Auto-generated method stub
        return null;
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
