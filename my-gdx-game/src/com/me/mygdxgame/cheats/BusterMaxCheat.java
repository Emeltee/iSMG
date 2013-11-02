package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.buster.BusterMax;

public class BusterMaxCheat extends MegaCheat {
    
    /* Enables the BusterMax upgrade. */
    
    private BusterMax busterUpgrade;
    
    public BusterMaxCheat(){
        this.busterUpgrade = new BusterMax();
    }
    
    @Override
    public String getDescription() {
        return "'Buster Max' Enhancement";
    }
    
    @Override
    public void enableCheat() {
        super.enableCheat();
        MegaCheat.player.getMegaBuster().attachBusterPart(this.busterUpgrade);
    }
    
    public void disableCheat() {
        super.disableCheat();
        MegaCheat.player.getMegaBuster().removeBusterPart(this.busterUpgrade);
    }

    @Override
    public List<Integer> getSequence() {
        return Arrays.asList(new Integer[] { Keys.UP, Keys.UP, Keys.DOWN, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, Keys.B, Keys.A });
    }

}
