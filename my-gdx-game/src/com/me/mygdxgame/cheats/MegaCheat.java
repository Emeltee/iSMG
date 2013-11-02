package com.me.mygdxgame.cheats;

import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.entities.SeeteufelSide;
import com.me.mygdxgame.utilities.GameCheat;

public abstract class MegaCheat extends GameCheat {

    // All MegaCheats can have access to aspects of 
    // environment through static variables
    
    public static MegaPlayer player;
    public static SeeteufelSide enemy;    
    
    public MegaCheat() {}

}
