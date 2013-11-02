package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class JumpSpringsCheat extends GameCheat {

    private MegaPlayer player;
    
    public JumpSpringsCheat(MegaPlayer player) {
        super();
        this.player = player;
    }
    
    public void enableCheat() {
        super.enableCheat();
        this.player.setJumpSpringsEnabled(true);        
    }
    
    public void disableCheat() {
        super.disableCheat();
        this.player.setJumpSpringsEnabled(false);
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
