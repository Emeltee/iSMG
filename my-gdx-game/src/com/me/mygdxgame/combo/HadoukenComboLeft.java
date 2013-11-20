package com.me.mygdxgame.combo;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input;
import com.me.mygdxgame.utilities.SpecialCombo;

public class HadoukenComboLeft extends SpecialCombo {

	@Override
	public List<Integer> getSequence() {
		return Arrays.asList(new Integer[] { Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.SPACE });		
	}

	@Override
	public String getDescription() {
		return "Hadouken";
	}

}
