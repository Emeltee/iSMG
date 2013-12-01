package com.me.mygdxgame.armor;

import com.me.mygdxgame.utilities.ArmorJacket;

/**
 * Normal Jacket stats:
 * No change
 *
 */

public class NormalJacket implements ArmorJacket {

	private static final float DEFENSE = 1.0f;
	
	@Override
	public float getDefenseFactor() {
		return DEFENSE;
	}

}
