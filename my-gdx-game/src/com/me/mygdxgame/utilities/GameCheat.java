package com.me.mygdxgame.utilities;

import java.util.List;

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

}
