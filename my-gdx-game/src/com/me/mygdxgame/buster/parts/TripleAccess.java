package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class TripleAccess implements BusterPart {

    @Override
    public String getPartName() {
        return "Triple Access";
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
        return 1;
    }

    @Override
    public int getRapid() {
        return 1;
    }

}