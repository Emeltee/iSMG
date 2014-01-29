package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.buster.parts.BusterMax;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.GameCheat;

public class BusterMaxCheat extends GameCheat {
    
	/** {@link BusterMax} object to be attached. */
    private BusterMax busterUpgrade;    
    /** {@link MegaPlayer} object to receive {@link BusterMax} object */ 
    private MegaPlayer player;
    
    public BusterMaxCheat(MegaPlayer player) {
        super();
        this.player = player;
        this.busterUpgrade = new BusterMax();
    }
    
    
    
    @Override
    /** Checks the provided {@link MegaPlayer} object's {@link MegaBuster} to see if
     * it has a {@link BusterMax} object attached. (Note: does not use the 'enabled' flag
     * from the abstract super class {@link GameCheat}.)
     * @return Boolean indicating whether or not the part was found
     */
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
    /** 
     * Attaches a {@link BusterMax} object to the provided {@link MegaPlayer} object's {@link MegaBuster} object.
     */
    public void enableCheat() {
        super.enableCheat();
        this.player.getMegaBuster().attachBusterPart(this.busterUpgrade);
    }
    
    /** 
     * Detaches a {@link BusterMax} object from the provided {@link MegaPlayer} object's {@link MegaBuster} object.
     */
    @Override
    public void disableCheat() {
        super.disableCheat();
        this.player.getMegaBuster().removeBusterPart(this.busterUpgrade);
    }

    @Override
    public List<Integer> getSequence() {
        return Arrays.asList(new Integer[] { Keys.UP, Keys.UP, Keys.DOWN, Keys.DOWN, 
                Keys.LEFT, Keys.RIGHT, Keys.LEFT, Keys.RIGHT, Keys.B, Keys.A });
    }

}
