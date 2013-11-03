package com.me.mygdxgame.buster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.entities.projectiles.BusterShot;
import com.me.mygdxgame.entities.projectiles.BusterShot.ShotDirection;
import com.me.mygdxgame.utilities.BusterPart;
import com.me.mygdxgame.utilities.Damageable;
import com.me.mygdxgame.utilities.GameEntity;

public class MegaBuster {

    private static final int MAX_ATTACHMENTS = 2;
    private List<BusterPart> attachments;
    private boolean adapterPlugEnabled;
    private Texture spritesheet;
    private Sound missSound;
    private Sound shootSound;
    
    private static final int BASE_SHOT_ATTACK = 1;
    private static final int BASE_SHOT_ENERGY = 3;
    private static final int BASE_SHOT_RANGE = 300;
    private static final int BASE_SHOT_RAPID = 300;
    private static final int MAX_SHOT_RAPID = 600;
    private static final int MAX_SHOT_ATTACK = 5;
    private static final int MAX_SHOT_RANGE = 800;
    private static final int MAX_SHOT_ENERGY = 9;
    private static final float SFX_VOLUME = 0.5f;
        
    private float energyTimer; // buster cooldown
       
    public MegaBuster(Texture spriteSheet, Sound shootSound, Sound missSound) {
        this.spritesheet = spriteSheet;
        this.missSound = missSound;        
        this.shootSound = shootSound;
        this.attachments = new ArrayList<BusterPart>();
    }
    
    public void setShootSound(Sound shootSound) {
        this.shootSound = shootSound;
    }
    
    public int attackStat() {
        int boost = this.calcAttackBoost();
        return Math.min(MAX_SHOT_ATTACK, BASE_SHOT_ATTACK + boost);        
    }
    
    public int energyStat() {
        int boost = this.calcEnergyBoost();
        return Math.min(MAX_SHOT_ENERGY, (int)(BASE_SHOT_ENERGY + 1.25 * boost));
        
    }
    
    public int rangeStat() {
        int boost = this.calcRangeBoost();
        return Math.min(MAX_SHOT_RANGE, BASE_SHOT_RANGE + 100 * boost);
    }
    
    public int rapidStat() {
        int boost = this.calcRapidBoost();
        return Math.min(MAX_SHOT_RAPID, BASE_SHOT_RAPID + 60 * boost);
    }    
    
    public int calcAttackBoost() {  
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getAttack();
        }
        return sum;
    } 
    
    public int calcEnergyBoost() {
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getEnergy();
        }
        return sum;
    }
    
    public int calcRangeBoost() {
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getRange();
        }
        return sum;
    }
    
    public int calcRapidBoost() {
        int sum = 0;
        for (BusterPart bp: this.attachments) {
            sum += bp.getRapid();
        }
        return sum;
    }
    
    public boolean attachBusterPart(BusterPart attachment) {
        if (this.attachments.size() < this.attachmentLimit()) {
            this.attachments.add(attachment);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeBusterPart(BusterPart attachment) {
        if (this.attachments.contains(attachment)) {
            this.attachments.remove(attachment);
            return true;
        } else {
            return false;
        }
    }
    
    public boolean removeBusterPart(int index) {
        if (this.attachments.size() > index) {
            this.attachments.remove(index);
            return true;
        } else {
            return false;
        }
    }
    
    public List<BusterPart> getAttachments() {
        return this.attachments;
    }
    
    public int attachmentLimit() {
        return MegaBuster.MAX_ATTACHMENTS + ((this.adapterPlugEnabled) ? 1 : 0); 
    }
    
    public boolean canMakeShot(float deltaTime) {
        if (this.energyTimer > 0) {
            this.energyTimer = Math.max(0, this.energyTimer - deltaTime);
            return false;
        } else {
            return true;
        }
    }
    
    public BusterShot makeShot(Vector3 shotOrigin, ShotDirection dir, Collection<GameEntity> obstacles, Collection<Damageable> targets) {
        this.energyTimer = 1 / (float)this.energyStat();
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
    
    private Color calcShotColor() {
        Color result;
        switch(this.calcAttackBoost()) {
            case 1: result = new Color(1.0f, 0.8f, 0.8f, 1.0f); // Red
                break;
            case 2: result = new Color(0.6f, 1.0f, 0.6f, 1.0f); // Green
                break;
            case 3: result = new Color(1.0f, 1.0f, 0.7f, 1.0f); // Yellow
                break;            
            case 4: result = new Color(8.0f, 0.6f, 0.8f, 1.0f); // Purple
                break;
            case 5: result = new Color(.8f, .8f, 1.0f, 1.0f); // Blue
                break;
            case 0:
            default:
                result = Color.WHITE; // No colorization
        }
        return result;
    }
    
    private float calcShotScale() {
        return Math.min(1.5f, 1.0f + 0.5f*(this.calcAttackBoost() / 5));        
    }
    
}
