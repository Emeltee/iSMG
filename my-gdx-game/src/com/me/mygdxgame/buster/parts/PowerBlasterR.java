package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class PowerBlasterR implements BusterPart {

    @Override
    public String getPartName() {
        return "Power Blaster R";
    }

    @Override
    public int getAttack() {
        return 2;
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public int getRange() {
        return 0;
    }

    @Override
    public int getRapid() {
        return 1;
    }

}