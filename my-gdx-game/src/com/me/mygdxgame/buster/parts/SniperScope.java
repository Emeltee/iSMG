package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class SniperScope implements BusterPart {

    @Override
    public String getPartName() {
        return "Sniper Scope";
    }

    @Override
    public int getAttack() {
        return 1;
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public int getRange() {
        return 5;
    }

    @Override
    public int getRapid() {
        return 0;
    }

}