package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;

public class JumpSpringsCheat extends MegaCheat {

    public void enableCheat() {
        super.enableCheat();
        MegaCheat.player.setJumpSpringsEnabled(true);        
    }
    
    public void disableCheat() {
        super.disableCheat();
        MegaCheat.player.setJumpSpringsEnabled(false);
    }
    
    @Override
    public List<Integer> getSequence() {
        return Arrays.asList(new Integer[]{ Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP, Keys.UP });
    }

    @Override
    public String getDescription() {
        return "Jump Springs";
    }

}
