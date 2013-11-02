package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class GeminiShotCheat extends GameCheat {

    private MegaPlayer player;
    
    public GeminiShotCheat(MegaPlayer player) {
        super();
        this.player = player;
    }
    
    public void enableCheat() {
        super.enableCheat();
        this.player.setGeminiEnabled(true);        
    }
    
    public void disableCheat() {
        super.disableCheat();
        this.player.setGeminiEnabled(false);
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
