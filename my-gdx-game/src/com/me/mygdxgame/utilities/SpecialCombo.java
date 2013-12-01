package com.me.mygdxgame.utilities;

import java.util.List;

/** Represents a sequence of inputs to be used as a special combo for a
 * {@link SpecialComboListener} object to use for fighting-game style combos.
 * Exactly similar to {@link GameCheat} class as far as sequences and descriptions
 * go; does not have any need for enable/disable features.
 */

public abstract class SpecialCombo {

	/** Accessor for key input sequence; ensures each SpecialCombo has one.
	 * (Note: Integers in sequence should correspond to constants from Input.Keys) 
	 * @return List of Integer key codes for combo sequence
	 */ 
    public abstract List<Integer> getSequence();
    
    /** Accessor for String name for each combo; ensures each SpecialCombo has one.
     * TODO This may be unnecessary. Remove? 
     * @return String name/description for combo
     */
    public abstract String getDescription();
    
    /** Determines whether two combos are equal based on their input sequences.
     * (Note: Unlike with {@link GameCheat#equals}, duplicate sequences for attack combos
     * can be easily problematic, so matching based on their input sequences seems ideal.
     * Additional logic may still be necessary for certain implementations.) 
     */
    public boolean equals(Object o) {
    	if (o instanceof SpecialCombo) {
    		return this.getSequence().equals(((SpecialCombo)o).getSequence());
    	} else {
    		return false;
    	}
    }

}
