package com.me.mygdxgame.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class SpecialComboListener implements InputProcessor {

	private static final float MAX_COMBO_TIME = 0.200f;
	
	private float comboTimer = 0.0f;
	private List<Integer> comboSequence;
	private List<SpecialCombo> combos;
	
	public SpecialComboListener(){
		this.comboSequence = new ArrayList<Integer>();
		this.combos = new ArrayList<SpecialCombo>();
	}
	
	
	public void updateComboTimer(float deltaTime) {
		this.comboTimer += deltaTime;
		if (this.comboTimer >= MAX_COMBO_TIME) {
			this.resetComboTimer();
			this.comboSequence.clear();
		}
	}
	
	private void resetComboTimer() {
		this.comboTimer = 0.0f;
	}
	
	private static String sequenceStr(List<Integer> seq) {
		StringBuilder builder = new StringBuilder();
		Iterator<Integer> iter = seq.iterator();
		while (iter.hasNext()) {
			builder.append(iter.next());
		}
		return builder.toString();		
	}
	
	public SpecialCombo getMatchedCombo() {
		for (SpecialCombo sc: this.combos) {
			String testCombo = sequenceStr(sc.getSequence());
			String thisCombo = sequenceStr(this.comboSequence);
			if (thisCombo.lastIndexOf(testCombo) == thisCombo.length()-testCombo.length()) {
				this.comboSequence.clear();
				return sc;
			}
		}
		return null;
	}
	
	public boolean addCombo(SpecialCombo sc) {
		if (!this.combos.contains(sc)) {
			this.combos.add(sc);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean removeCombo(SpecialCombo sc) {
		if (this.combos.contains(sc)) {
			this.combos.remove(sc);
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean keyUp(int keycode) {		
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		this.comboSequence.add(keycode);
		this.resetComboTimer();
		return false;
	}
	
	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
