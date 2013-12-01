package com.me.mygdxgame.utilities;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Input;

/** GameCheat class enables/disables an effect to the game 
  * through void methods. Effects toggled by entering a key
  * sequence. Key sequence is matched by {@link GameCheatListener}.
  */

public abstract class GameCheat {
    
	/** Flag indicating whether cheat is enabled. (Note: Not all cheats actually use this.) */
    protected boolean enabled;
    
    public GameCheat() {
        this.enabled = false;
    }    
    
    /** Accessor for command sequence, ensures GameCheat objects provide key sequences for codes.
     *  (Note: Integers should correspond to codes from the constants in Input.Keys.)
     * @return A list of Integer key codes for command input sequence
     */
    public abstract List<Integer> getSequence();
    
    /** Accessor for cheat description, guarantees GameCheat objects provide a description.
     * @return A string name or description of the cheat's effect
     */
    public abstract String getDescription();
    
    /** Determines the enabled state of the cheat. May rely on the enabled instance var, but doesn't have to.
     * @return A boolean indicating whether or not the cheat is enabled.
     */
    public boolean isEnabled() {
        return this.enabled;        
    };    
    
    /** Applies the effect of the cheat to the game. This may depend on any instance vars included
     * per implementation of GameCheat. (Note: It may be necessary to track enabled status in this
     * method, which the abstract GameCheat super-class provides by default.)
     */
    public void enableCheat() {
        this.enabled = true; 
    };
    
    /** Removes the effect of the cheat from the game. This may depend on any instance vars
     * included per implementation of GameCheat. (Note: It may be necessary to track enabled 
     * status in this method, which the abstract GameCheat super-class provides by default.)
     */
    public void disableCheat() {
        this.enabled = false; 
    };    
    
    /** Translates the sequence of Integers into a String.
     * @return A String representation of the command input sequence
     */
    public String getSequenceString() {
    	Iterator<Integer> sequence = this.getSequence().iterator();
    	StringBuilder builder = new StringBuilder("[" + GameCheat.translateKeyCode(sequence.next()) + "]");
    	while (sequence.hasNext()) {
    		builder.append(" [" + GameCheat.translateKeyCode(sequence.next()) + "]");
    	}
    	return builder.toString();
    	
    }
        
    /** Determines whether two cheats are equal based on their key input sequences.
     * (Note: Matching cheats by command sequence should be sufficient for most cases, which
     * the GameCheat abstract super-class provides by default, but additional logic may be
     * necessary in some implementations of GameCheat.)
     */
    @Override
	public boolean equals(Object obj) {
    	// Equality based on same key-sequence; this can avoid overlapping cheat-codes
		if (obj instanceof GameCheat) {
			return ((GameCheat)obj).getSequence().equals(this.getSequence());
		} else {
			return false;
		}
	}

    /** Maps Integer key codes from the Input.Keys set of constants to String values.
     * @param keyCode
     * @return A String representation of the provided keycode
     */
	protected static String translateKeyCode (int keyCode) {
    	// (Note: I Used a regex on a function that translated awt constants to Input.Keys constants to make this. ^_^)
    	if (keyCode == Input.Keys.PLUS) return "+";
    	if (keyCode == Input.Keys.MINUS) return "-";
    	if (keyCode == Input.Keys.NUM_0) return "0";
    	if (keyCode == Input.Keys.NUM_1) return "1";
    	if (keyCode == Input.Keys.NUM_2) return "2";
    	if (keyCode == Input.Keys.NUM_3) return "3";
    	if (keyCode == Input.Keys.NUM_4) return "4";
    	if (keyCode == Input.Keys.NUM_5) return "5";
    	if (keyCode == Input.Keys.NUM_6) return "6";
    	if (keyCode == Input.Keys.NUM_7) return "7";
    	if (keyCode == Input.Keys.NUM_8) return "8";
    	if (keyCode == Input.Keys.NUM_9) return "9";
    	if (keyCode == Input.Keys.A) return "A";
    	if (keyCode == Input.Keys.B) return "B";
    	if (keyCode == Input.Keys.C) return "C";
    	if (keyCode == Input.Keys.D) return "D";
    	if (keyCode == Input.Keys.E) return "E";
    	if (keyCode == Input.Keys.F) return "F";
    	if (keyCode == Input.Keys.G) return "G";
    	if (keyCode == Input.Keys.H) return "H";
    	if (keyCode == Input.Keys.I) return "I";
    	if (keyCode == Input.Keys.J) return "J";
    	if (keyCode == Input.Keys.K) return "K";
    	if (keyCode == Input.Keys.L) return "L";
    	if (keyCode == Input.Keys.M) return "M";
    	if (keyCode == Input.Keys.N) return "N";
    	if (keyCode == Input.Keys.O) return "O";
    	if (keyCode == Input.Keys.P) return "P";
    	if (keyCode == Input.Keys.Q) return "Q";
    	if (keyCode == Input.Keys.R) return "R";
    	if (keyCode == Input.Keys.S) return "S";
    	if (keyCode == Input.Keys.T) return "T";
    	if (keyCode == Input.Keys.U) return "U";
    	if (keyCode == Input.Keys.V) return "V";
    	if (keyCode == Input.Keys.W) return "W";
    	if (keyCode == Input.Keys.X) return "X";
    	if (keyCode == Input.Keys.Y) return "Y";
    	if (keyCode == Input.Keys.Z) return "Z";
    	if (keyCode == Input.Keys.ALT_LEFT) return "ALT_LEFT";
    	if (keyCode == Input.Keys.ALT_RIGHT) return "ALT_RIGHT";
    	if (keyCode == Input.Keys.BACKSLASH) return "\\";
    	if (keyCode == Input.Keys.COMMA) return ",";
    	if (keyCode == Input.Keys.DPAD_LEFT) return "LEFT";
    	if (keyCode == Input.Keys.DPAD_RIGHT) return "RIGHT";
    	if (keyCode == Input.Keys.DPAD_UP) return "UP";
    	if (keyCode == Input.Keys.DPAD_DOWN) return "DOWN";
    	if (keyCode == Input.Keys.ENTER) return "ENTER";
    	if (keyCode == Input.Keys.HOME) return "HOME";
    	if (keyCode == Input.Keys.PERIOD) return ".";
    	if (keyCode == Input.Keys.SEMICOLON) return ";";
    	if (keyCode == Input.Keys.SHIFT_LEFT) return "SHIFT_LEFT";
    	if (keyCode == Input.Keys.SLASH) return "/";
    	if (keyCode == Input.Keys.SPACE) return "SPACE";
    	if (keyCode == Input.Keys.TAB) return "TAB";
    	if (keyCode == Input.Keys.DEL) return "DEL";
    	if (keyCode == Input.Keys.CONTROL_LEFT) return "CONTROL_LEFT";
    	if (keyCode == Input.Keys.ESCAPE) return "ESCAPE";
    	if (keyCode == Input.Keys.END) return "END";
    	if (keyCode == Input.Keys.INSERT) return "INSERT";
    	if (keyCode == Input.Keys.DPAD_CENTER) return "DPAD_CENTER";
    	if (keyCode == Input.Keys.PAGE_UP) return "PAGE_UP";
    	if (keyCode == Input.Keys.PAGE_DOWN) return "PAGE_DOWN";
    	if (keyCode == Input.Keys.F1) return "F1";
    	if (keyCode == Input.Keys.F2) return "F2";
    	if (keyCode == Input.Keys.F3) return "F3";
    	if (keyCode == Input.Keys.F4) return "F4";
    	if (keyCode == Input.Keys.F5) return "F5";
    	if (keyCode == Input.Keys.F6) return "F6";
    	if (keyCode == Input.Keys.F7) return "F7";
    	if (keyCode == Input.Keys.F8) return "F8";
    	if (keyCode == Input.Keys.F9) return "F9";
    	if (keyCode == Input.Keys.F10) return "F10";
    	if (keyCode == Input.Keys.F11) return "F11";
    	if (keyCode == Input.Keys.F12) return "F12";
    	if (keyCode == Input.Keys.COLON) return ":";

    	return "UNKNOWN";
    }
    
}
