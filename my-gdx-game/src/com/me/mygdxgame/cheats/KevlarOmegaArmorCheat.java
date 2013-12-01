package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.armor.KevlarOmegaJacket;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.ArmorJacket;
import com.me.mygdxgame.utilities.GameCheat;

public class KevlarOmegaArmorCheat extends GameCheat {

	/** {@link KevlarOmegaJacket} object to be attached to provided {@link MegaPlayer} object. */
	private KevlarOmegaJacket armorJacket;
	/** Reference to {@link MegaPlayer}'s original {@link ArmorJacket} for toggling on and off.  */
	private ArmorJacket oldArmor;
	/** {@link MegaPlayer} object to receive #{KevlarOmegaJacket} object */
	private MegaPlayer player;
	
	public KevlarOmegaArmorCheat(MegaPlayer player) {
		super();
		this.player = player;
		this.armorJacket = new KevlarOmegaJacket();
	}
	
	@Override
	/** Checks whether provided {@link MegaPlayer} is wearing a {@link KevlarOmegaJacket} object.
	 * (Note: Does not rely on the enabled flag from abstract super class {@link GameCheat}.)
	 * @return Boolean indicating whether or not {@link KevlarOmegaJacket} is on.
	 */
	public boolean isEnabled() {
		if (this.player != null) {
			return this.player.getArmor().equals(this.armorJacket);
		} else {
			return super.isEnabled();
		}
	}

	@Override
	/** Replace the provided {@link MegaPlayer} object's current armor with a #{KevlarOmegaJacket} object. 
	 * (Note: Stores the original {@link ArmorJacket} to be restored when cheat toggled off.)
	 */
	public void enableCheat() {
		super.enableCheat();
		this.oldArmor = player.getArmor();
		this.player.setArmor(this.armorJacket);
	}

	@Override
	/** Replace the provided {@link MegaPlayer} object's {@link KevlarOmegaJacket} object with its original
	 * {@link ArmorJacket} object. Only applies if {@link MegaPlayer} object has swapped armors before.
	 */
	public void disableCheat() {
		super.disableCheat();
		if (this.oldArmor != null) {
			this.player.setArmor(this.oldArmor);
		} 
	}

	@Override
	public List<Integer> getSequence() {
		return Arrays.asList(new Integer[] { Keys.RIGHT, Keys.DOWN, Keys.RIGHT, Keys.DOWN, 
		        Keys.RIGHT, Keys.DOWN, Keys.RIGHT, Keys.DOWN, Keys.RIGHT, Keys.DOWN });
	}

	@Override
	public String getDescription() {
		return "Kevlar Omega Armor";
	}

}
