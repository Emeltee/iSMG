package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class PowerBlasterL implements BusterPart {

    @Override
    public String getPartName() {
        return "Power Blaster L";
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
        return 1;
    }

    @Override
    public int getRapid() {
        return 0;
    }

}