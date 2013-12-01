package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class JumpSpringsCheat extends GameCheat {

	/** {@link MegaPlayer} object to toggle Jump Springs on and off. */
    private MegaPlayer player;
    
    public JumpSpringsCheat(MegaPlayer player) {
        super();
        this.player = player;
    }
    
    
    
    @Override
    /** Checks whether provided {@link MegaPlayer} object has JumpSprings mode enabled.
	 * (Note: Does not rely on the enabled flag from abstract super class {@link GameCheat}.)
	 * @return Boolean indicating whether or not JumpSprings mode is on.
	 */
	public boolean isEnabled() {
    	if (this.player != null) {
    		return this.player.isJumpSpringsEnabled();
    	} else {
    		return super.isEnabled();
    	}
	}

	@Override
    /**
     * Enables the JumpSprings mode state for provided {@link MegaPlayer} object.
     */
    public void enableCheat() {
        super.enableCheat();
        this.player.setJumpSpringsEnabled(true);        
    }
    
    @Override
    /**
     * Disables the JumpSprings mode state for provided {@link MegaPlayer} object.
     */
    public void disableCheat() {
        super.disableCheat();
        this.player.setJumpSpringsEnabled(false);
    }
    
    @Override
    public List<Integer> getSequence() {
        return Arrays.asList(new Integer[]{ Keys.UP, Keys.UP, Keys.UP, 
                Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP });
    }

    @Override
    public String getDescription() {
        return "Jump Springs";
    }

}
