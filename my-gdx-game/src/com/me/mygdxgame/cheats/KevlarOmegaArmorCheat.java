package com.me.mygdxgame.cheats;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.me.mygdxgame.armor.KevlarOmegaJacket;
import com.me.mygdxgame.entities.MegaPlayer;
import com.me.mygdxgame.utilities.ArmorJacket;
import com.me.mygdxgame.utilities.GameCheat;

public class KevlarOmegaArmorCheat extends GameCheat {

	private KevlarOmegaJacket armorJacket;
	private ArmorJacket oldArmor;
	private MegaPlayer player;
	
	public KevlarOmegaArmorCheat(MegaPlayer player) {
		super();
		this.player = player;
		this.armorJacket = new KevlarOmegaJacket();
	}
	
	@Override
	public boolean isEnabled() {
		if (this.player != null) {
			return this.player.getArmor().equals(this.armorJacket);
		} else {
			return super.isEnabled();
		}
	}

	@Override
	public void enableCheat() {
		super.enableCheat();
		this.oldArmor = player.getArmor();
		this.player.setArmor(this.armorJacket);
	}

	@Override
	public void disableCheat() {
		super.disableCheat();
		if (this.oldArmor != null) {
			this.player.setArmor(this.oldArmor);
		} else {
			this.player.setArmor(null);
		}
	}

	@Override
	public List<Integer> getSequence() {
		return Arrays.asList(new Integer[] { Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.DOWN, Keys.DOWN });
	}

	@Override
	public String getDescription() {
		return "Kevlar Omega Armor";
	}

}
