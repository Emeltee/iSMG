package com.me.mygdxgame.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class GameCheatListener implements InputProcessor {
    
    /* Listens for key sequences to apply cheats.  */
    
    private static final float SFX_VOLUME = 0.5f;
    
    protected List<GameCheat> cheats;    
    protected List<Integer> keySequence;
    private List<Integer> testList;
    protected int maxSequenceSize;
    private Sound successSound;
    
    public GameCheatListener(int maxSequenceSize, Sound successSound) {
        this.cheats = new ArrayList<GameCheat>();
        this.keySequence = new ArrayList<Integer>();
        this.testList = new ArrayList<Integer>();
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
    
    public boolean addCheat(GameCheat cheat) {
        if (!this.cheats.contains(cheat)) {
            this.cheats.add(cheat);
            return true;
        } else {
            return false;
        }
    }
    
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
    
    private boolean keyLogContainsSequence(List<Integer> seq) {
        /* This converts sequences into a string and checks if 
         * the code is matched using indexOf(). This way cheats
         * can be any length and occur at any space in the keylog.
         */
        return sequenceAsText(this.keySequence).indexOf(sequenceAsText(seq)) != -1;
    }
    
    private String sequenceAsText(List<Integer> seq) {
        // Converts a key sequence to a string for easy code matching
        StringBuilder builder = new StringBuilder();
        for(Integer key: seq) {
            builder.append(sanitizeWASD(key));            
        }
        return builder.toString();
    }
    
    public int sanitizeWASD(int keyCode) {
    	// Converts Up/Down/Left/Right and W/A/S/D to common numerics to appease PC gaming purists
    	// This may lead to ambiguities in code sequences that require both letters and directions.
    	if (keyCode == Input.Keys.UP || keyCode == Input.Keys.W) return -2;
    	if (keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) return -3;
    	if (keyCode == Input.Keys.LEFT || keyCode == Input.Keys.A) return -4;
    	if (keyCode == Input.Keys.RIGHT || keyCode == Input.Keys.D) return -5;
    	return keyCode;
    }
    
    public List<GameCheat> getAllCheats() {
        return this.cheats;
    }
    
    public List<GameCheat> getEnabledCheats() {
        List<GameCheat> result = new ArrayList<GameCheat>();
        
        for (GameCheat gc: this.cheats) {
            if(gc.isEnabled()) {
                result.add(gc);
            }
        
        }
        
        return result;
    }
    
    public List<GameCheat> getDisabledCheats() {
    	List <GameCheat> result = new ArrayList<GameCheat> ();
    	
    	for (GameCheat gc: this.cheats) {
    		if (!gc.isEnabled()) {
    			result.add(gc);
    		}
    	}
    	
    	return result;
    }
    
    public boolean containsCheat(GameCheat gc) {
    	return this.getAllCheats().contains(gc);
    }
    
    public boolean hasEnabledCheat(GameCheat gc) {
    	return this.getEnabledCheats().contains(gc);
    }
    
    public boolean containsCheats(GameCheat... gcs) {
    	for (GameCheat gc: gcs) {
    		if (!this.containsCheat(gc)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean hasEnabledCheats(GameCheat... gcs) {
    	for (GameCheat gc: gcs) {
    		if (!this.hasEnabledCheat(gc)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public void disableAllCheats() {
        for (GameCheat gc: this.cheats) {
            gc.disableCheat();
        }
    }

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
