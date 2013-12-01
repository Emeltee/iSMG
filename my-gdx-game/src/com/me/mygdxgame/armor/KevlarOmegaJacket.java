package com.me.mygdxgame.armor;

import com.me.mygdxgame.utilities.ArmorJacket;

/**
 * Kevlar Omega Jacket stats:
 * Damage reduced by 75%
 *
 */

public class KevlarOmegaJacket implements ArmorJacket {

	private static final float DEFENSE = .25f;
	
	@Override
	public float getDefenseFactor() {
		return DEFENSE;
	}

}
