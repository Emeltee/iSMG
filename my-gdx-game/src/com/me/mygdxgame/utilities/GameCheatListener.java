package com.me.mygdxgame.utilities;


import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/** 
 * Acts as an InputProcessor to monitor keystrokes and enable/disable 
 * GameCheats by watching for matching key sequences.   
 */

public class GameCheatListener implements InputProcessor {
    
	/** Volume adjuster. TODO Manage SFX, BGMs and volume through a separate class. */
    private static final float SFX_VOLUME = 0.5f;    
    /** List of {@link GameCheat} objects that GameCheatListener will listen for. */
    protected List<GameCheat> cheats;    
    /** List of Integer key codes currently input by the user. */
    protected List<Integer> keySequence;   
    
    /** Size limit imposed on the keySequence. */
    protected int maxSequenceSize;
    /** SFX played when a cheat is entered successfully. 
     * (Note: successSound plays regardless of whether cheat is enabled or disabled, as
     * long as the sequence was correctly matched. TODO: Change this?)
     */
    private Sound successSound;
    
    public GameCheatListener(int maxSequenceSize, Sound successSound) {
        this.cheats = new ArrayList<GameCheat>();
        this.keySequence = new ArrayList<Integer>();
        this.maxSequenceSize = maxSequenceSize;
        this.successSound = successSound;
    }
    
    @Override
    public boolean keyUp(int keycode) {
        
        if (this.keySequence.size() == this.maxSequenceSize) {
            this.keySequence.remove((int)0);
        }
        this.keySequence.add(keycode);
        
        this.checkCheats();
        
        return false;
    }    
    
	/** Adds a cheat to be tracked to the listener. Rejects duplicate cheats.
	 * (TODO Remove rejecting duplicates? This is no longer really necessary..)
	 * @param cheat The GameCheat object to be added to the list
	 * @return Boolean indicating whether the cheat was added
	 */
    public boolean addCheat(GameCheat cheat) {
        if (!this.cheats.contains(cheat)) {
            this.cheats.add(cheat);
            return true;
        } else {
            return false;
        }
    }
    
    /** Checks each {@link GameCheat} object for a match in the current input sequence.  
     * Enables the first matched cheat it finds, whereupon it flushes the input list
     * and plays the success sound.
     */
    private void checkCheats() {
        boolean success = false;
        for (GameCheat gc: this.cheats) {
            if (this.keyLogContainsSequence(gc.getSequence())) {
                // Toggle cheat enabled for matched cheat code
                if (!gc.isEnabled()) { 
                    gc.enableCheat();
                } else {
                    gc.disableCheat();
                }                
                
                success = true;
                this.keySequence.clear();
            }
        }
        
        if (success) {
            // Let player know a cheat is enabled
            successSound.play(SFX_VOLUME);
        }
        
    }
    
    /** Determines whether the current keylog contains a specified sequence of keys.
     * @param seq The sequence of keys (List of Integers) to be matched
     * @return Boolean indicating whether a match was found
     */
    private boolean keyLogContainsSequence(List<Integer> seq) {
        /* This converts sequences into a string and checks if 
         * the code is matched using indexOf(). This way cheats
         * can be any length and occur at any space in the keylog.
         */
        return sequenceAsText(this.keySequence).indexOf(sequenceAsText(seq)) != -1;
    }
    
    /** Translates a key sequence into a String for simple matching.
     * (Note: This string is not a necessarily human-readable string.)
     * @param seq The sequence of keys (List of Integers) to be translated
     * @return String representation of specified key sequence
     */
    private String sequenceAsText(List<Integer> seq) {
        // Converts a key sequence to a string for easy code matching
        StringBuilder builder = new StringBuilder();
        for(Integer key: seq) {
            builder.append(sanitizeWASD(key));            
        }
        return builder.toString();
    }
    
    /** Converts input codes for directional buttons and WASD to a common value,
     * allowing cheats to match codes with directions either way. (Note: Codes with
     * both directions and literal letters [W,A,S,D] will result in potential overlaps;
     * forward any complaints to any WASD-purists you may know.
     * @param keyCode int key code to be sanitized 
     * @return Sanitized int key code
     */
    public int sanitizeWASD(int keyCode) {
    	// Converts Up/Down/Left/Right and W/A/S/D to common numerics to appease PC gaming purists
    	// This may lead to ambiguities in code sequences that require both letters and directions.
    	if (keyCode == Input.Keys.UP || keyCode == Input.Keys.W) return -2;
    	if (keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) return -3;
    	if (keyCode == Input.Keys.LEFT || keyCode == Input.Keys.A) return -4;
    	if (keyCode == Input.Keys.RIGHT || keyCode == Input.Keys.D) return -5;
    	return keyCode;
    }
    
    /** Accessor for all {@link GameCheat} objects currently in the listener.     * 
     * @return List of {@link GameCheat} objects in the listener
     */
    public List<GameCheat> getAllCheats() {
        return this.cheats;
    }
    
    /** Accessor for only enabled {@link GameCheat} objects in the listener.
     * @return List of enabled {@link GameCheat} objects in the listener
     */
    public List<GameCheat> getEnabledCheats() {
        List<GameCheat> result = new ArrayList<GameCheat>();
        
        for (GameCheat gc: this.cheats) {
            if(gc.isEnabled()) {
                result.add(gc);
            }
        
        }
        
        return result;
    }
    
    /** Accessor for only enabled {@link GameCheat} objects in the listener.
     * @return List of enabled {@link GameCheat} objects in the listener
     */ 
    public List<GameCheat> getDisabledCheats() {
    	List <GameCheat> result = new ArrayList<GameCheat> ();
    	
    	for (GameCheat gc: this.cheats) {
    		if (!gc.isEnabled()) {
    			result.add(gc);
    		}
    	}
    	
    	return result;
    }
    
    /** Simple method for checking if the list of {@link GameCheat} objects has a 
     * specific {@link GameCheat} object.
     * @param gc {@link GameCheat} object to be found
     * @return Boolean indicating whether gc was found
     */
    public boolean containsCheat(GameCheat gc) {
    	return this.getAllCheats().contains(gc);
    }
    
    /** Simple method for checking if the list of {@link GameCheat} objects has a 
     * specific {@link GameCheat} object AND that that {@link GameCheat} is enabled.
     * @param gc {@link GameCheat} object to be found
     * @return Boolean indicating whether gc was found
     */
    public boolean hasEnabledCheat(GameCheat gc) {
    	return this.getEnabledCheats().contains(gc);
    }
    
    /** Varargs wrapped over containsCheat to match multiple cheats at once.
     * (Note: Checks for ALL cheats in the list. Partial matches return false.)
     * @param gcs List of {@link GameCheat} objects to be found
     * @return Boolean indicating whether all objects were found
     */
    public boolean containsCheats(GameCheat... gcs) {
    	for (GameCheat gc: gcs) {
    		if (!this.containsCheat(gc)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /** Varargs wrapped over containsEnabledCheat to match multiple cheats at once.
     * (Note: Checks for ALL cheats in the list and enabled. Partial matches return false.)
     * @param gcs List of {@link GameCheat} objects to be found
     * @return Boolean indicating whether all objects were found and enabled
     */
    public boolean hasEnabledCheats(GameCheat... gcs) {
    	for (GameCheat gc: gcs) {
    		if (!this.hasEnabledCheat(gc)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /** Disables all cheats at once. 
     */
    public void disableAllCheats() {
        for (GameCheat gc: this.cheats) {
            gc.disableCheat();
        }
    }

    // The methods below need to be overridden for the InputProcessor implementation,
    // but they don't actually do anything, since this is only concerned with keyDown().
    
    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
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
