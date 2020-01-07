package com.chivalry.game.entities;

/*
 * BattlePlayer class used to create the BattlePlayer objects for BattleScreen.
 * Stores animations, player health, damage and heal.
 * Also stores inputs that BattlePlayer uses in BattleScreen.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class BattlePlayer extends Sprite implements InputProcessor {

	//Float for BattlePlayer animation
	private float animation = 0;
	
	//Floats for player health, damage and heal
	public static float playerHealth = 100 + (3 * (Player.lvl - 1));
	public static float damage = 10 + (5 * Player.lvl/2);
	public static float heal = 5 + (10 * Player.lvl/5);
	
	//Float for animation delay
	float delay = 1;
	
	//Animation variables for BattlePlayer
	private Animation stationary, attack, defend;
	
	//Booleans for BattlePlayer animation cases
	boolean attackBoolean = false, defendBoolean = false, stationaryBoolean = true;

	//BattlePlayer Object to be used in BattleScreen
	public BattlePlayer(Animation stationary, Animation attack, Animation defend) 
	{
		this.stationary = stationary;
		this.attack = attack;
		this.defend = defend;
		setSize(450, 440);
	}

	//Draw method to call update
	@Override
	public void draw(SpriteBatch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	//Update method to update animations with boolean cases
	public void update(float delta) {

		animation += delta;
		setRegion(attackBoolean == true ? attack.getKeyFrame(animation)
				: defendBoolean == true ? defend.getKeyFrame(animation): stationary.getKeyFrame(animation));
	}

	//When these keys are pressed, call these cases
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		//When J is pressed, attackBoolean is equal to true and animation is equal to 0
		case Keys.J:
			attackBoolean = true;
			animation = 0;
			break;
		//When K is pressed, defendBoolean is equal to true and animation is equal to 0	
		case Keys.K:
			defendBoolean = true;
			animation = 0;
			break;
		}
		return true;
	}

	//When these keys are let go of, do these cases
	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		//When J is up, set attackBoolean to false after a delay and set animation to 0
		case Keys.J:
			Timer.schedule(new Task(){
			    @Override
			    public void run() {
			        attackBoolean = false;
			    }
			}, delay);
			animation = 0;
		//When J is up, set defendBoolean to false after a delay and set animation to 0	
		case Keys.K:
			defendBoolean = false;
			animation = 0;
		}
		return true;
	}

	public void delay(long delayMillis) {
		
	}
	
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
