package com.chivalry.game.screens;

/*
 * Chivalry class allows switching between screens.
 * Also is used for sound throughout the game.
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;

public class Chivalry extends Game {
	public SpriteBatch batch;
	// Creates an object game
	private Game game;
	// creates a sound object
	Sound sound;

	public Chivalry() {
		game = this;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		// sets sound equal to the audio file
		sound = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/GameSong.mp3"));
		// This loops the sound so that it does not stop once its finished
		sound.loop(0.05f);
		// plays the sound at a certain volume
		sound.play(0.05f);
		// Sets the screen to the main menu
		setScreen(new MainMenu(game));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

}