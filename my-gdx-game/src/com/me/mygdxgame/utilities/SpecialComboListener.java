package com.me.mygdxgame.utilities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.InputProcessor;

/**
 * A special type of InputProcessor that logs keystrokes and has a fixed
 * timeout after which the log is flushed. This is used to match key combos
 * for simple fighting game-like input sequences.
 * 
 * This is neither perfect nor complete. It will match simple command sequences,
 * but has no support for things like charged inputs or simultaneous inputs (e.g.,
 * down+right). This also lacks any notion of "forward" and "backward", with commands
 * matched only by left and right. -- TODO Add distinction for forward/back.
 */

public class SpecialComboListener implements InputProcessor {

	/** Number of seconds used for the combo input timeout. */
	private static final float MAX_COMBO_TIME = 0.200f;
	
	/** Timer variable used to keep track of time between inputs. */
	private float comboTimer = 0.0f;
	/** Current sequence of key inputs. */
	private List<Integer> comboSequence;
	/** List of {@link SpecialCombo} objects to track for matches. */
	private List<SpecialCombo> combos;
	
	public SpecialComboListener(){
		this.comboSequence = new ArrayList<Integer>();
		this.combos = new ArrayList<SpecialCombo>();
	}
	
	/** Updates the combo timer by specified dt. 
	 * Flushes keylog after MAX_COMBO_TIME threshold is passed. 
	 * @param deltaTime Amount of time since last update in seconds
	 */
	public void updateComboTimer(float deltaTime) {
		this.comboTimer += deltaTime;
		if (this.comboTimer >= MAX_COMBO_TIME) {
			this.resetComboTimer();
			this.comboSequence.clear();
		}
	}
	
	/** Reset the combo timer to 0. (Unnecessary?) */
	private void resetComboTimer() {
		this.comboTimer = 0.0f;
	}
	
	/** Translate a combo sequence (List of Integers) to a String for simple matching.
	 * @param seq Integer key input sequence to be translated to a String
	 * @return String representation of key sequence
	 */
	private static String sequenceStr(List<Integer> seq) {
		StringBuilder builder = new StringBuilder();
		Iterator<Integer> iter = seq.iterator();
		while (iter.hasNext()) {
			builder.append(iter.next());
		}
		return builder.toString();		
	}
	
	/** Checks the list of {@link SpecialCombo} objects for a matched combo. 	 * 
	 * @return First {@link SpecialCombo} object matched or null.
	 */
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
	
	/** Adds a {@link SpecialCombo} object to the list of combos. Rejects all duplicates.	 * 
	 * @param sc {@link SpecialCombo} object to be added
	 * @return Boolean indicating whether combo was accepted
	 */
	public boolean addCombo(SpecialCombo sc) {
		if (!this.combos.contains(sc)) {
			this.combos.add(sc);
			return true;
		} else {
			return false;
		}
	}
	
	/** Removes a {@link SpecialCombo} object from the list of combos.
	 * @param sc {@link SpecialCombo} object to be removed
	 * @return Boolean indicating whether object was removed (i.e., if it was in the list in the first place)
	 */
	public boolean removeCombo(SpecialCombo sc) {
		if (this.combos.contains(sc)) {
			this.combos.remove(sc);
			return true;
		} else {
			return false;
		}
	}
	
    // The methods below need to be overridden for the InputProcessor implementation,
    // but they don't actually do anything, since this is only concerned with keyDown().
	
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
