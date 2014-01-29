package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class SniperUnit implements BusterPart {

    @Override
    public String getPartName() {
        return "Sniper Unit";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getEnergy() {
        return 1;
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