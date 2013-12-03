package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;
import java.util.NoSuchElementException;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.particles.Explosion;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.EntityState;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

/**
 * A variety of {@link BusterShot} with a high minimum power value. Color and
 * scale cannot be altered.
 */
public class Hadouken extends BusterShot {
	
	private static final int HADOUKEN_1_X = 211;
	private static final int HADOUKEN_1_Y = 211;
	private static final int HADOUKEN_1_W = 16;
	private static final int HADOUKEN_1_H = 19;
	
	private static final int HADOUKEN_2_X = 228;
	private static final int HADOUKEN_2_Y = 211;
	private static final int HADOUKEN_2_W = 19;
	private static final int HADOUKEN_2_H = 17;
	
	private static final float SFX_VOLUME = 0.5f;
	
	private static final float TIME_TO_FULLSIZE = 0.10f;
	
	private TextureRegion hadouken1;
	private TextureRegion hadouken2;
	private float fullSizeTimer = TIME_TO_FULLSIZE;
	private boolean fullSize = false;	
	
    private Sound explosion = null;    
    private Explosion[] explosions;
    private boolean exploded = false;
	
	public Hadouken(Texture spriteSheet, Sound missSound, Sound explosion, Vector3 position, int speed,
            ShotDirection dir, int power, float range, Collection<GameEntity> obstacles,
            Collection<Damageable> targets) {
		super(spriteSheet, missSound, position, speed, dir, Math.max(9001, power), Math.max(1200, range), obstacles, targets);
		this.hitBox = new Rectangle(position.x, position.y, Hadouken.HADOUKEN_1_W, Hadouken.HADOUKEN_1_H);
		this.hadouken1 = new TextureRegion(spriteSheet, HADOUKEN_1_X, HADOUKEN_1_Y, HADOUKEN_1_W, HADOUKEN_1_H);
		this.hadouken2 = new TextureRegion(spriteSheet, HADOUKEN_2_X, HADOUKEN_2_Y, HADOUKEN_2_W, HADOUKEN_2_H);
		if (dir == ShotDirection.LEFT) {
			this.hadouken1.flip(true, false);
			this.hadouken2.flip(true, false);
		}
		this.explosion = explosion;
		//this.bullet = this.hadouken1;
	}
	
	@Override
	public void draw(Renderer renderer) {
		if (this.fullSize) {
			renderer.drawRegion(this.hadouken2, this.position.x, this.position.y);
		} else {
			renderer.drawRegion(this.hadouken1, this.position.x, this.position.y);
		}
	}

	@Override
	public void update(float deltaTime) {		
		if (!this.fullSize) {
			this.fullSizeTimer = Math.max(0, this.fullSizeTimer - deltaTime);
			if (this.fullSizeTimer == 0) {
				this.fullSize = true;
				this.hitBox = new Rectangle(position.x, position.y, Hadouken.HADOUKEN_2_W, Hadouken.HADOUKEN_2_H);
				this.bullet = this.hadouken2;
				this.position.y += 2; // Fullsize shot is shorter by 2px
			}
		}
		super.update(deltaTime);
		
		if (super.state == EntityState.Destroyed && !this.exploded) {
			this.explode();
		}
	}
	
	@Override
	public int getHeight() {
		if (this.fullSize) {
			return Hadouken.HADOUKEN_2_H;
		} else {
			return Hadouken.HADOUKEN_1_H;
		}
	}
	
	@Override
	public int getWidth() {
		if (this.fullSize) {
			return Hadouken.HADOUKEN_2_W;
		} else {
			return Hadouken.HADOUKEN_1_W;
		}
	}
	
	@Override
	public void setShotColor(Color shotColor) {
		// Can't make this private, but I can make it do nothing
	}
	@Override
	public void setShotScale(float shotScale) {
		// Can't make this private, but I can make it do nothing
	}
	
	private void explode() {
        if (!this.exploded) {
            // Adjust position vector to center of sprite.
            this.position.x += Hadouken.HADOUKEN_2_W / 2;
            this.position.y += Hadouken.HADOUKEN_2_H / 2;
            
            
            // Create explosions.
            this.explosions = new Explosion[4];
            this.explosions[0] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x+12, this.position.y+12, this.position.z));
            this.explosions[1] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x-12, this.position.y+12, this.position.z));
            this.explosions[2] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x+12, this.position.y-12, this.position.z));
            this.explosions[3] = new Explosion(this.spriteSheet, 
                    new Vector3(this.position.x-12, this.position.y-12, this.position.z));
            
            // Play sound.
            this.explosion.stop();
            this.explosion.play(SFX_VOLUME);
            
            // Set state.
            this.exploded = true;
            
        }

    }
	
	@Override
    public boolean hasCreatedEntities() {
        return this.explosions != null;
    }

    @Override
    public GameEntity[] getCreatedEntities() throws NoSuchElementException {
        if (this.explosions == null) {
            throw new NoSuchElementException();
        }
        
        GameEntity[] returnList = this.explosions;
        this.explosions = null;
        
        return returnList;
    }

}
