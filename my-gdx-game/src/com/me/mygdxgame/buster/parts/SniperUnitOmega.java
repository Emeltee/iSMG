package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class SniperUnitOmega implements BusterPart {

    @Override
    public String getPartName() {
        return "Sniper Unit Omega";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getEnergy() {
        return 2;
    }

    @Override
    public int getRange() {
        return 3;
    }

    @Override
    public int getRapid() {
        return 0;
    }

}