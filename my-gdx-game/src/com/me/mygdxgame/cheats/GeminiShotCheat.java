package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class GeminiShotCheat extends GameCheat {

	/** MegaPlayer object to toggle gemini state */
    private MegaPlayer player;
    
    public GeminiShotCheat(MegaPlayer player) {
        super();
        this.player = player;
    }
    
    @Override
    /** Checks if the provided {@link MegaPlayer} object already has the GeminiShot equipped.
     * (Note: Does not rely on the enabled flag from abstract super class {@link GameCheat}.)
     * @return Boolean indicating whether provided {@link MegaPlayer} object has Gemini mode on
     */
    public boolean isEnabled() {
        if (this.player != null) {
            return this.player.isGeminiEnabled();
        } else {
            return super.isEnabled();
        }
    }

    @Override
    /** Enables the Gemini mode state for provided {@link MegaPlayer} object.
     */
    public void enableCheat() {
        super.enableCheat();
        this.player.setGeminiEnabled(true);        
    }
    
    @Override
    /** Disables the Gemini mode state for provided {@link MegaPlayer} object.
     */
    public void disableCheat() {
        super.disableCheat();
        this.player.setGeminiEnabled(false);
    }
    
    @Override
    public List<Integer> getSequence() {
        return Arrays.asList(new Integer []{ Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, 
                Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT });
    }

    @Override
    public String getDescription() {
        return "Gemini Buster";
    }

}
