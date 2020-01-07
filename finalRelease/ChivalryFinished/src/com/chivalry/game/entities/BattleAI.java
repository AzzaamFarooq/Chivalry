package com.chivalry.game.entities;

/*
 * BattleAI class used to create the BattleAI objects for BattleScreen.
 * Stores animations, and ai damage and heal floats.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BattleAI extends Sprite {

	//Float for animation
	private float animation = 0;
	
	//Battle AI animation variables
	private Animation stationary, attack, defend;
	
	//Float variables for ai Damage and ai Heal
	public static float aiDamage, aiHeal;

	//Booleans for ai animations
	boolean stationaryBoolean = true;	
	public static boolean aiAttackBoolean = false;
	public static boolean aiDefendBoolean = false; 

	//Battle AI object to be used in battle screen
	public BattleAI(Animation stationary, Animation attack, Animation defend, float aiDamage, float aiHeal) 
	{
		this.stationary = stationary;
		this.attack = attack;
		this.defend = defend;
		BattleAI.aiDamage = aiDamage;
		BattleAI.aiHeal = aiHeal;
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
		setRegion(aiAttackBoolean == true ? attack.getKeyFrame(animation)
				: aiDefendBoolean == true ? defend.getKeyFrame(animation): stationary.getKeyFrame(animation));
	}

}
