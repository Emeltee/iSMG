package com.me.mygdxgame.armor;

import com.me.mygdxgame.utilities.ArmorJacket;

public class KevlarOmegaJacket implements ArmorJacket {

	private static final float DEFENSE = .25f;
	
	@Override
	public float getDefenseFactor() {
		return DEFENSE;
	}

}
