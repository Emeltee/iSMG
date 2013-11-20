package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class HadoukenCheat extends GameCheat {

	private MegaPlayer player;
	
	public HadoukenCheat(MegaPlayer player) {
		super();
		this.player = player;
	}
	
	
	@Override
	public boolean isEnabled() {
		if (this.player != null) {
			return this.player.isHadoukenEnabled();
		} else {
			return false;
		}
	}

	@Override
	public void enableCheat() {
		if (this.player != null) {
			this.player.setHadoukenEnabled(true);
		}
 	}

	@Override
	public void disableCheat() {
		if (this.player != null) {
			this.player.setHadoukenEnabled(false);
		}
	}

	@Override
	public List<Integer> getSequence() {
		return Arrays.asList(new Integer[] {Input.Keys.DOWN, Input.Keys.RIGHT, Input.Keys.DOWN,Input.Keys.RIGHT,Input.Keys.SPACE});
	}

	@Override
	public String getDescription() {
		return "Hadouken";
	}

}
