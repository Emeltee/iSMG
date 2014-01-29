package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class BlasterUnitR implements BusterPart {

    @Override
    public String getPartName() {
        return "Blaster Unit R";
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
        return 0;
    }

    @Override
    public int getRapid() {
        return 2;
    }

}