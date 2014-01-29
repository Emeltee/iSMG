package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class SniperRange implements BusterPart {

    @Override
    public String getPartName() {
        return "Sniper Range";
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
        return 4;
    }

    @Override
    public int getRapid() {
        return 0;
    }

}