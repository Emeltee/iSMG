package com.me.mygdxgame.utilities;

/** 
 * General interface for augmenting a {@link MegaBuster} object with stat boosts.  
 * The {@link MegaBuster} class determines how each stat level should be adjusted
 * when creating {@link BusterShot} objects.
 * 
 * Note: Ideally each stat boost should range be in the range [0, 5] inclusive, where
 * 0 is None and 5 is Max. TODO clamp those values automatically somehow?
 */

public interface BusterPart {
    
	/** Accessor for part name as string for (hopefully unique) names of BusterParts. 
     * @return String name of BusterPart
     */
    public String getPartName();
    
    /** Accessor for Attack stat modification factor.  
     * @return Integer measure of attack stat modification
     */
    public int getAttack();
    
    /** Accessor for Energy stat modification factor.  
     * @return Integer measure of energy stat modification
     */
    public int getEnergy();
    
    /** Accessor for Range stat modification factor.  
     * @return Integer measure of range stat modification
     */
    public int getRange();
    
    /** Accessor for Rapid stat modification factor.  
     * @return Integer measure of rapid stat modification
     */
    public int getRapid();
    
    // TODO Add a field for Special; applies to weapons other than Buster?

}
