package com.chivalry.game;

//By Abdullah Khan & Azzaam Farooq

/*
 * Launcher class which runs the game.
 */


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.chivalry.game.screens.Chivalry;

public class Launcher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Giving the window a name and giving the window a size
		config.title = "Chivalry";
		config.useGL20 = true;
		//Dont allow screen to be resized
		config.resizable = false;
		config.width = 1024;
		config.height = 650;

		new LwjglApplication(new Chivalry(), config);
	}
}