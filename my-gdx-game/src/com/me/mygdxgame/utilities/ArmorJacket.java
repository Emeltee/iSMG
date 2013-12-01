package com.me.mygdxgame.utilities;

/**
 * Interface for augmenting a {@link MegaPlayer} object's defense.
 */

public interface ArmorJacket {
	/** Accessor for defense factor, used to scale damage. */
	float getDefenseFactor();
	
	// Yes, this is all.
	// TODO Add additional features like knockback reduction?
}
