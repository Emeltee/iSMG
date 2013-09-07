package com.me.mygdxgame.client;

import com.me.mygdxgame.MyGdxGame;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(MyGdxGame.SCREEN_WIDTH, MyGdxGame.SCREEN_HEIGHT);
		cfg.fps = 30;
		
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return new MyGdxGame();
	}
}