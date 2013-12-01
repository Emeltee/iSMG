package com.me.mygdxgame.buster;

import com.me.mygdxgame.utilities.BusterPart;


public class BusterMax implements BusterPart {

    @Override
    public String getPartName() {
        return "Buster Max";
    }

    @Override
    public int getAttack() {
        return 5;
    }

    @Override
    public int getEnergy() {
        return 5;
    }

    @Override
    public int getRange() {
        return 5;
    }

    @Override
    public int getRapid() {
        return 5;
    }

}
