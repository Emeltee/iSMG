package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class RangeBoosterAlpha implements BusterPart {

    @Override
    public String getPartName() {
        return "Range Booster Alpha";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public int getRapid() {
        return 0;
    }

}