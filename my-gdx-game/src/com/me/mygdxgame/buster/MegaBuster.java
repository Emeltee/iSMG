package com.me.mygdxgame.buster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.projectiles.BusterShot;
import com.me.mygdxgame.entities.projectiles.BusterShot.ShotDirection;
import com.me.mygdxgame.utilities.BusterPart;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.GameEntity;

/**
 * Used by {@link MegaPlayer} class to facilitate the creation of {@link BusterShot} objects.
 * This class is used to calculate the four basic stats of {@link BusterShot} objects:
 * attack, energy, range and rapid
 * 
 * The stats for each {@link BusterShot} may be influenced by a collection of {@link BusterPart}
 * objects. Based on any attached {@link BusterPart} objects, this calculates the stats for each
 * created {@link BusterShot}.
 */

public class MegaBuster {

	/** Limit on number of {@link BusterPart} objects attached. */
    protected static final int MAX_ATTACHMENTS = 2;
    /** List of {@link BusterPart} objects currently attached. */
    protected List<BusterPart> attachments;
    /** Flag for the adapter plug which allows an extra {@link BusterPart} to be attached */
    protected boolean adapterPlugEnabled;
    /** Texture reference which will be reused throughout life of MegaBuster object */
    protected Texture spritesheet;
    /** SFX to play on shot missed. */
    protected Sound missSound;
    /** SFX to play on shot fired. */
    protected Sound shootSound;
    
    // Stat calculation constants
    /** Default (minimum) Attack stat value. */
    protected static final int BASE_SHOT_ATTACK = 1;
    /** Default (minimum) Energy stat value. */
    protected static final int BASE_SHOT_ENERGY = 3;
    /** Default (minimum) Range stat value. */
    protected static final int BASE_SHOT_RANGE = 300;
    /** Default (minimum) Rapid stat value. */
    protected static final int BASE_SHOT_RAPID = 300;
    /** Maximum Attack stat value. */
    protected static final int MAX_SHOT_RAPID = 600;
    /** Maximum Energy stat value. */
    protected static final int MAX_SHOT_ATTACK = 5;
    /** Maximum Range stat value. */
    protected static final int MAX_SHOT_RANGE = 800;
    /** Maximum Rapid stat value. */
    protected static final int MAX_SHOT_ENERGY = 9;
   
    /** Used to manage sound volume. TODO Move this to a Sound Manager class. */
    protected static final float SFX_VOLUME = 0.5f;
        
    /** Buster Cooldown variable, used to limit number of shots fired at once. */
    protected float energyTimer;
       
    public MegaBuster(Texture spriteSheet, Sound shootSound, Sound missSound) {
        this.spritesheet = spriteSheet;
        this.missSound = missSound;        
        this.shootSound = shootSound;
        this.attachments = new ArrayList<BusterPart>();
    }
    
    /** Setter for shootSound. Note: This is mostly just for allowing {@link GeminiShot} to have
     * a custom sound, but this may be extended to other weapons in the future.
     * @param shootSound SFX to play on shooting
     */
    public void setShootSound(Sound shootSound) {
        this.shootSound = shootSound;
    }
    
    /** Calculates the Attack stat based on attached {@link BusterPart} objects.
     * @return Attack stat as integer
     */
    public int attackStat() {
        int boost = this.calcAttackBoost();
        return Math.min(MAX_SHOT_ATTACK, BASE_SHOT_ATTACK + boost);        
    }
    
    /** Calculates the Energy stat based on attached {@link BusterPart} objects.
     * @return Energy stat as integer
     */
    public int energyStat() {
        int boost = this.calcEnergyBoost();
        return Math.min(MAX_SHOT_ENERGY, (int)(BASE_SHOT_ENERGY + 1.25 * boost));
        
    }
    
    /** Calculates the Range stat based on attached {@link BusterPart} objects.
     * @return Range stat as integer
     */
    public int rangeStat() {
        int boost = this.calcRangeBoost();
        return Math.min(MAX_SHOT_RANGE, BASE_SHOT_RANGE + 100 * boost);
    }
    
    /** Calculates the Rapid stat based on attached {@link BusterPart} objects.
     * @return Rapid stat as integer
     */
    public int rapidStat() {
        int boost = this.calcRapidBoost();
        return Math.min(MAX_SHOT_RAPID, BASE_SHOT_RAPID + 60 * boost);
    }    
    
    /** Calculates the overall boost to Attack from attached {@link BusterPart} objects.
     * @return Boost factor to apply to Attack stat, as integer
     */
    public int calcAttackBoost() {  
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getAttack();
        }
        return sum;
    } 
    
    /** Calculates the overall boost to Energy from attached {@link BusterPart} objects.
     * @return Boost factor to apply to Energy stat, as integer
     */
    public int calcEnergyBoost() {
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getEnergy();
        }
        return sum;
    }
    
    /** Calculates the overall boost to Range from attached {@link BusterPart} objects.
     * @return Boost factor to apply to Range stat, as integer
     */
    public int calcRangeBoost() {
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getRange();
        }
        return sum;
    }
    
    /** Calculates the overall boost to Rapid from attached {@link BusterPart} objects
     * @return Boost factor to apply to Rapid stat, as integer
     */    
    public int calcRapidBoost() {
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getRapid();
        }
        return sum;
    }
    
    /** Convenience function, wraps over the contains() method for the attachments list.
     * @param attachment {@link BusterPart} object to be found
     * @return Boolean indicating whether part was found
     */
    public boolean hasBusterPart(BusterPart attachment) {
        return this.attachments.contains(attachment);
    }
    
    /** Convenience function, wraps over the add() method for the attachments list.
     * Will not allow more {@link BusterPart} objects than allowed.
     * @param attachment {@link BusterPart} object to be attached
     * @return Boolean indicating whether part was attached successfully
     */
    public boolean attachBusterPart(BusterPart attachment) {
        if (this.attachments.size() < this.attachmentLimit()) {
            this.attachments.add(attachment);
            return true;
        } else {
            return false;
        }
    }
    
    /** Convenience function, wraps over the remove() method for the attachments list.
     * @param attachment {@link BusterPart} object to be removed
     * @return Boolean indicating whether part was found and detached successfully
     */
    public boolean removeBusterPart(BusterPart attachment) {
        if (this.hasBusterPart(attachment)) {
            this.attachments.remove(attachment);
            return true;
        } else {
            return false;
        }
    }
    
    /** Convenience function, wraps over alternate remove() method for attachments list.
     * @param index Index of {@link BusterPart} object to be removed
     * @return Boolean indicating whether part was remoeved
     */
    public boolean removeBusterPart(int index) {
        if (this.attachments.size() > index) {
            this.attachments.remove(index);
            return true;
        } else {
            return false;
        }
    }
    
    /** Accessor for attachment list.
     * @return List of all {@link BusterPart} objects currently attached
     */
    public List<BusterPart> getAttachments() {
        return this.attachments;
    }
    
    /** Calculates the attachment limit based on fixed constant and adapterPlugEnabled instance var.
     * @return Integer total number of attachments possible
     */
    public int attachmentLimit() {
        return MegaBuster.MAX_ATTACHMENTS + ((this.adapterPlugEnabled) ? 1 : 0); 
    }
    
    /** Determines whether the MegaBuster is ready to fire another shot,
     * based th the Energy timer and Energy stat.
     * @param deltaTime Amount of time passed since last shot fired
     * @return Boolean indicating whether MegaBuster can fire again
     */
    public boolean canMakeShot(float deltaTime) {
        if (this.energyTimer > 0) {
            this.energyTimer = Math.max(0, this.energyTimer - deltaTime);
            return false;
        } else {
            return true;
        }
    }
    
    /** Creates a {@link BusterShot} object based on all the different stats of MegaBuster.
     * @param shotOrigin Coordinates of initial point of shot
     * @param dir Direction in which shot must travel
     * @param obstacles List of ostacles to use for collision detection
     * @param targets List of objects used for damaging
     * @return A {@link BusterShot} object 
     */
    public BusterShot makeShot(Vector3 shotOrigin, ShotDirection dir, 
            Collection<GameEntity> obstacles, Collection<Damageable> targets) {
        this.energyTimer = 1 / (float)this.energyStat();
//        this.shootSound.stop();
        this.shootSound.play(SFX_VOLUME);
        BusterShot shot = new BusterShot(this.spritesheet,
                                this.missSound, shotOrigin,
                                this.rapidStat(), dir,
                                this.attackStat(), this.rangeStat(),
                                obstacles, targets);
        shot.setShotColor(this.calcShotColor());
        shot.setShotScale(this.calcShotScale());
        return shot;        
    }
    
    /** Determines what color a {@link BusterShot} object should be based on the Attack stat.
     * @return A Color to be applied to a {@link BusterShot} object
     */
    protected Color calcShotColor() {
        Color result;
        switch(this.calcAttackBoost()) {
            case 1: result = new Color(8.0f, 0.6f, 0.8f, 1.0f); // Purple
                break;    
            case 2: result = new Color(.8f, .8f, 1.0f, 1.0f); // Blue
                break;
            case 3: result = new Color(0.6f, 1.0f, 0.6f, 1.0f); // Green
                break;            
            case 4: result = new Color(1.0f, 0.8f, 0.8f, 1.0f); // Red
                break;
            case 5: result = new Color(1.0f, 1.0f, 0.3f, 1.0f); // Yellow
                break;    
            case 0:
            default:
                result = Color.WHITE; // No colorization
        }
        return result;
    }
    
    /** Determines what size a {@link BusterShot} object should be based on the Attack stat.
     * @return A scale factor to be applied to a {@link BusterShot} object
     */
    protected float calcShotScale() {
        return Math.min(1.5f, 1.0f + 0.5f*(this.calcAttackBoost() / 5));        
    }
    
}
