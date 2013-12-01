package com.me.mygdxgame.armor;

import com.me.mygdxgame.utilities.ArmorJacket;

/**
 * Flak Jacket stats:
 * Damage reduced by 25%
 * 
 */

public class FlakJacket implements ArmorJacket {

	private static final float DEFENSE = .75f;
	
	@Override
	public float getDefenseFactor() {
		return DEFENSE;
	}

}
