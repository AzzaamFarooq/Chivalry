package com.chivalry.game.screens;

/*
 * WinScreen class is the screen that the game finishes on when the boss is defeated.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WinScreen extends Game implements Screen {
	// Creates texture, spritebatch and game
	private Game game;
	private Texture background;
	private SpriteBatch batch;

	public WinScreen(Game game) {
		this.game = game;
		// creates a new texture and gives it a picture
		background = new Texture("assets/maps/winScreen.png");
		batch = new SpriteBatch();
	}

	public void create() {
		// sets the screen to a new mainmenu
		setScreen(new WinScreen(game));
	}

	public void render(float delta) {
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