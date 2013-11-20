package com.me.mygdxgame.utilities;

import java.util.List;

public abstract class SpecialCombo {

    public abstract List<Integer> getSequence();
    public abstract String getDescription();
    
    public boolean equals(Object o) {
    	if (o instanceof SpecialCombo) {
    		return this.getSequence().equals(((SpecialCombo)o).getSequence());
    	} else {
    		return false;
    	}
    }

}
