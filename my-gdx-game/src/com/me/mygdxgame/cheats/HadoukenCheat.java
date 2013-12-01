package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class HadoukenCheat extends GameCheat {

	/** {link MegaPlayer} object to toggle Hadouken on and off */
	private MegaPlayer player;
	
	public HadoukenCheat(MegaPlayer player) {
		super();
		this.player = player;
	}
	
	
	@Override
	/** Checks whether provided {@link MegaPlayer} object has Hadouken mode enabled.
	 * (Note: Does not rely on the enabled flag from abstract super class {@link GameCheat}.)
	 * @return Boolean indicating whether or not Hadouken mode is on.
	 */
	public boolean isEnabled() {
		if (this.player != null) {
			return this.player.isHadoukenEnabled();
		} else {
			return false;
		}
	}

	@Override
	/** 
	 * Enables the Hadouken mode state for provided {@link MegaPlayer} object.
	 */
	public void enableCheat() {
		if (this.player != null) {
			this.player.setHadoukenEnabled(true);
		}
 	}

	@Override
	/**
	 * Disables the Hadouken mode state for provided {@link MegaPlayer} object.
	 */
	public void disableCheat() {
		if (this.player != null) {
			this.player.setHadoukenEnabled(false);
		}
	}

	@Override
	public List<Integer> getSequence() {
		return Arrays.asList(new Integer[] {Input.Keys.DOWN, Input.Keys.RIGHT,
		        Input.Keys.DOWN,Input.Keys.RIGHT,Input.Keys.SPACE});
	}

	@Override
	public String getDescription() {
		return "Hadouken";
	}

}
