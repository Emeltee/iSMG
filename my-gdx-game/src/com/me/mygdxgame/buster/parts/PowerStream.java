package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class PowerStream implements BusterPart {

    @Override
    public String getPartName() {
        return "Power Stream";
    }

    @Override
    public int getAttack() {
        return 5;
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
        return 0;
    }

}