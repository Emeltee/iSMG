package com.me.mygdxgame.utilities;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.InputProcessor;

public class GameCheatListener implements InputProcessor {
    
    /* Listens for key sequences to apply cheats.  */
    
    protected static final int DEFAULT_MAX = 10;
    
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
            successSound.play();
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
            builder.append(key);            
        }
        return builder.toString();
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
