package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.buster.BusterMax;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class BusterMaxCheat extends GameCheat {
    
    /* Enables the BusterMax upgrade. */
    
    private BusterMax busterUpgrade;
    private MegaPlayer player;
    
    public BusterMaxCheat(MegaPlayer player) {
        super();
        this.player = player;
        this.busterUpgrade = new BusterMax();
    }
    
    
    
    @Override
    public boolean isEnabled() {
        if (this.player != null) {
            return this.player.getMegaBuster().hasBusterPart(busterUpgrade);
        } else {
            return super.isEnabled();    
        }        
    }

    @Override
    public String getDescription() {
        return "Buster Max";
    }
    
    @Override
    public void enableCheat() {
        super.enableCheat();
        this.player.getMegaBuster().attachBusterPart(this.busterUpgrade);
    }
    
    @Override
    public void disableCheat() {
        super.disableCheat();
        this.player.getMegaBuster().removeBusterPart(this.busterUpgrade);
    }

    @Override
    public List<Integer> getSequence() {
        return Arrays.asList(new Integer[] { Keys.UP, Keys.UP, Keys.DOWN, Keys.DOWN, Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, Keys.B, Keys.A });
    }

}
