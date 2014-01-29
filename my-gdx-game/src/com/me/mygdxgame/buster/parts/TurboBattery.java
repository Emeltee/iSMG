package com.me.mygdxgame.buster.parts;

import com.me.mygdxgame.utilities.BusterPart;

public class TurboBattery implements BusterPart {

    @Override
    public String getPartName() {
        return "Turbo Battery";
    }

    @Override
    public int getAttack() {
        return 0;
    }

    @Override
    public int getEnergy() {
        return 4;
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