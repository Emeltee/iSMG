package com.me.mygdxgame.entities.projectiles;

import java.util.Collection;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.GameEntity;
import com.me.mygdxgame.utilities.Renderer;

public class Hadouken extends BusterShot {
	
	private static final int HADOUKEN_1_X = 211;
	private static final int HADOUKEN_1_Y = 211;
	private static final int HADOUKEN_1_W = 16;
	private static final int HADOUKEN_1_H = 19;
	
	private static final int HADOUKEN_2_X = 228;
	private static final int HADOUKEN_2_Y = 211;
	private static final int HADOUKEN_2_W = 19;
	private static final int HADOUKEN_2_H = 17;
	
	private static final float TIME_TO_FULLSIZE = 0.10f;
	
	private TextureRegion hadouken1;
	private TextureRegion hadouken2;
	private float fullSizeTimer = TIME_TO_FULLSIZE;
	private boolean fullSize = false;	
	
	public Hadouken(Texture spriteSheet, Sound missSound, Vector3 position, int speed,
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
	
	

}
