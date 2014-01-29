package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class BusterUnitOmega implements BusterPart {

    @Override
    public String getPartName() {
        return "Buster Unit Omega";
    }

    @Override
    public int getAttack() {
        return 3;
    }

    @Override
    public int getEnergy() {
        return 0;
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