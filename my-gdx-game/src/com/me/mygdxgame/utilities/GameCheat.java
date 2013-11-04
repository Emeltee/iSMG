package com.me.mygdxgame.utilities;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Input;

public abstract class GameCheat {    
    
    /* GameCheat class enables/disables an effect to the game 
     * through void methods. Effects toggled by entering a key
     * sequence. Key sequence is matched by GameCheatListener.
     */
    
    protected boolean enabled;
    
    public GameCheat() {
        this.enabled = false;
    }    
    
    public abstract List<Integer> getSequence();
    public abstract String getDescription();
    
    public boolean isEnabled() {
        return this.enabled;        
    };    
    
    public void enableCheat() {
        this.enabled = true; 
    };
    public void disableCheat() {
        this.enabled = false; 
    };    
    
    public String getSequenceString() {
    	Iterator<Integer> sequence = this.getSequence().iterator();
    	StringBuilder builder = new StringBuilder(GameCheat.translateKeyCode(sequence.next()));
    	while (sequence.hasNext()) {
    		builder.append("," + GameCheat.translateKeyCode(sequence.next()));
    	}
    	return builder.toString();
    	
    }

    protected static String translateKeyCode (int keyCode) {
    	// Translates Input.Keys constants into Strings
    	// (Used a regex on a function that translated awt constants
    	// to Input.Keys constants to make this.)
    	if (keyCode == Input.Keys.PLUS) return "PLUS";
    	if (keyCode == Input.Keys.MINUS) return "MINUS";
    	if (keyCode == Input.Keys.NUM_0) return "NUM_0";
    	if (keyCode == Input.Keys.NUM_1) return "NUM_1";
    	if (keyCode == Input.Keys.NUM_2) return "NUM_2";
    	if (keyCode == Input.Keys.NUM_3) return "NUM_3";
    	if (keyCode == Input.Keys.NUM_4) return "NUM_4";
    	if (keyCode == Input.Keys.NUM_5) return "NUM_5";
    	if (keyCode == Input.Keys.NUM_6) return "NUM_6";
    	if (keyCode == Input.Keys.NUM_7) return "NUM_7";
    	if (keyCode == Input.Keys.NUM_8) return "NUM_8";
    	if (keyCode == Input.Keys.NUM_9) return "NUM_9";
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
    	if (keyCode == Input.Keys.BACKSLASH) return "BACKSLASH";
    	if (keyCode == Input.Keys.COMMA) return "COMMA";
    	if (keyCode == Input.Keys.DEL) return "DEL";
    	if (keyCode == Input.Keys.DPAD_LEFT) return "LEFT";
    	if (keyCode == Input.Keys.DPAD_RIGHT) return "RIGHT";
    	if (keyCode == Input.Keys.DPAD_UP) return "UP";
    	if (keyCode == Input.Keys.DPAD_DOWN) return "DOWN";
    	if (keyCode == Input.Keys.ENTER) return "ENTER";
    	if (keyCode == Input.Keys.HOME) return "HOME";
    	if (keyCode == Input.Keys.MINUS) return "MINUS";
    	if (keyCode == Input.Keys.PERIOD) return "PERIOD";
    	if (keyCode == Input.Keys.PLUS) return "PLUS";
    	if (keyCode == Input.Keys.SEMICOLON) return "SEMICOLON";
    	if (keyCode == Input.Keys.SHIFT_LEFT) return "SHIFT_LEFT";
    	if (keyCode == Input.Keys.SLASH) return "SLASH";
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
    	if (keyCode == Input.Keys.COLON) return "COLON";

    	return "UNKNOWN";
    }
    
}
