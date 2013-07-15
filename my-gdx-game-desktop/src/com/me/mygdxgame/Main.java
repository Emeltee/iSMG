package com.me.mygdxgame;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "my-gdx-game";
		cfg.useGL20 = true;
		cfg.width = MyGdxGame.SCREEN_WIDTH;
		cfg.height = MyGdxGame.SCREEN_HEIGHT;
		cfg.foregroundFPS = 60;
		cfg.backgroundFPS = 60;
		cfg.resizable = false;
		
		new LwjglApplication(new MyGdxGame(), cfg);
	}
}
