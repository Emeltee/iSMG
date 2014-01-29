package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class OmniUnit implements BusterPart {

    @Override
    public String getPartName() {
        return "Omni-Unit";
    }

    @Override
    public int getAttack() {
        return 1;
    }

    @Override
    public int getEnergy() {
        return 1;
    }

    @Override
    public int getRange() {
        return 1;
    }

    @Override
    public int getRapid() {
        return 1;
    }

}