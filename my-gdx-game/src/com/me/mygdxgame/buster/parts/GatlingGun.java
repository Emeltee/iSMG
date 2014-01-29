package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class GatlingGun implements BusterPart {

    @Override
    public String getPartName() {
        return "Gatling Gun";
    }

    @Override
    public int getAttack() {
        return 1;
    }

    @Override
    public int getEnergy() {
        return 4;
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