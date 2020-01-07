package com.chivalry.game.screens;

/*
 * MainMenu class is a screen that the game starts on when ran.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainMenu extends Game implements Screen {
	// Creates texture, spritebatch and game
	private Game game;
	private Texture background;
	private SpriteBatch batch;

	public MainMenu(Game game) {
		
		this.game = game;
		// creates a new texture and gives it a picture
		background = new Texture("assets/maps/mainMenu.png");
		batch = new SpriteBatch();
	}

	public void create() {
		// sets the screen to a new mainmenu
		setScreen(new MainMenu(game));
	}

	public void render(float delta) {
		// if you click your mouse then it will set the screen to the openworld class
		if (Gdx.input.isButtonPressed(Keys.S))
			game.setScreen(new OpenWorld(game));

		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		// Draws the main menu screen
		batch.begin();
		batch.draw(background, -70, -100, 1180, 748);
		batch.end();

	}

	public void resize(int width, int height) {

	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
	}

	public void show() {
	}

	public void hide() {
	}
}