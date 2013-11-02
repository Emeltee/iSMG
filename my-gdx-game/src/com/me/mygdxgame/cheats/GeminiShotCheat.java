package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;

public class GeminiShotCheat extends MegaCheat {

    public void enableCheat() {
        super.enableCheat();
        MegaCheat.player.setGeminiEnabled(true);        
    }
    
    public void disableCheat() {
        super.disableCheat();
        MegaCheat.player.setGeminiEnabled(false);
    }
    
    @Override
    public List<Integer> getSequence() {
        return Arrays.asList(new Integer []{ Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT });
    }

    @Override
    public String getDescription() {
        return "Gemini Buster Shot";
    }

}
