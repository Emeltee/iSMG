package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class OmniUnitOmega implements BusterPart {

    @Override
    public String getPartName() {
        return "Omni-Unit Omega";
    }

    @Override
    public int getAttack() {
        return 2;
    }

    @Override
    public int getEnergy() {
        return 2;
    }

    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public int getRapid() {
        return 1;
    }

}