package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "my-gdx-game";
		cfg.useGL20 = true;
		cfg.width = 500;
		cfg.height = 500;
		cfg.foregroundFPS = 60;
		cfg.backgroundFPS = 60;
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
