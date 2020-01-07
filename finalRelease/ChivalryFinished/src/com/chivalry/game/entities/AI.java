package com.chivalry.game.entities;

/*
 * AI class used to create the AI objects for OpenWorld and to store it's animations.
 */


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AI extends Sprite {

	//Float for animation
	private float animation = 0;

	//Stationary animation variable for AI
	private Animation stationary;

	//AI Object for OpenWorld to use
	public AI(Animation stationary) {
		super(stationary.getKeyFrame(0));
		this.stationary = stationary;
	}

	//Draw method which calls update
	@Override
	public void draw(SpriteBatch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	//Update method for animation
	public void update(float delta) {
		animation += delta;
		setRegion(stationary.getKeyFrame(animation));
	}

}
