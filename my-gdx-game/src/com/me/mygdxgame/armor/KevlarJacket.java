package com.me.mygdxgame.armor;

import com.me.mygdxgame.utilities.ArmorJacket;

/**
 * Kevlar Jacket stats:
 * Damage reduced by 50%
 *
 */

public class KevlarJacket implements ArmorJacket {

	private static final float DEFENSE = 0.5f;
	
	@Override
	public float getDefenseFactor() {
		return DEFENSE;
	}

}
