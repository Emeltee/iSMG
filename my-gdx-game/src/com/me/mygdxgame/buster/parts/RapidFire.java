package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class RapidFire implements BusterPart {

    @Override
    public String getPartName() {
        return "Rapid Fire";
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
        return 0;
    }

    @Override
    public int getRapid() {
        return 1;
    }

}