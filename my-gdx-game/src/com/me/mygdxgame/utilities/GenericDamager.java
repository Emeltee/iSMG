package com.me.mygdxgame.utilities;

import java.util.NoSuchElementException;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

/**
 * Simple damager that can be used to deal damage/knockback anonymously. Create
 * with the attributes desired and pass to a {@link Damageable}.
 */
public class GenericDamager implements Damager {

    private int power, knockback;
    private Vector3 position;
    
    public GenericDamager(int power, int knockback) {
        this.power = power;
        this.knockback = knockback;
        this.position = new Vector3(0, 0, 0);
    }
    
    public GenericDamager(int power, int knockback, Vector3 position) {
        this.power = power;
        this.knockback = knockback;
        this.position = new Vector3(position);
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
        return new Rectangle[0];
    }

    @Override
    public void destroy() {
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        throw new NoSuchElementException();
    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this.position);
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
    public int getPower() {
        return this.power;
    }

    @Override
    public int getKnockback() {
        return this.knockback;
    }

}
