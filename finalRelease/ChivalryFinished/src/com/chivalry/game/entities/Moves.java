package com.chivalry.game.entities;

/*
 * Moves class used to store move methods for the AI.
 * Has attack and heal methods for all monsters.
 * Also has special methods for boss.
 * Uses randomizer to randomize AI choices when fighting.
 */

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.chivalry.game.screens.BattleScreen;
import java.util.Random;

public class Moves {
	// delay used for the animation
	static float delay = 1;

	// Method used for AI attacks
	public static void attack() {
		// Sets the aiAttackBoolean to true which enables the AI to attack
		BattleAI.aiAttackBoolean = true;
		// When this method is called for, the AI is attacking the player and the player
		// loses its health
		BattleScreen.battleHealth -= BattleAI.aiDamage;
		Timer.schedule(new Task() {
			@Override
			// This method is used for the animation, to delay the AIs move
			public void run() {
				BattleAI.aiAttackBoolean = false;
			}
		}, delay);
	}

	// This method is used to heal the AI
	public static void heal() {
		// If statement that ensures that the AI does not heal past its maximum health
		if (BattleScreen.aiBattleHealth < (BattleScreen.tempHealth - BattleAI.aiHeal)) {
			// Adding health to the AIs health
			BattleScreen.aiBattleHealth += BattleAI.aiHeal;
		}
		// Boolean for the animation
		BattleAI.aiDefendBoolean = true;
		Timer.schedule(new Task() {
			@Override

			public void run() {
				BattleAI.aiDefendBoolean = false;
			}
		}, delay);
	}

	// This method is for the bosses attacks
	public static void bossHit() {
		// Making the aiAttackBoolean equal to true
		BattleAI.aiAttackBoolean = true;
		// this is deducting the players health by 75
		BattleScreen.battleHealth -= 75;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				BattleAI.aiAttackBoolean = false;
			}
		}, delay);
	}

	// This method is used to determine the AIs next move. It randomizes between
	// Attack and heal
	public static void random() {
		// Generates a random number
		Random rn = new Random();
		// Generates a number between 1 and 3
		int answer = rn.nextInt(3) + 1;
		// If the number is 1 or 2 then attack (creates a 2/3 percent chance of
		// attacking)
		if (answer == 1 || answer == 2) {
			Moves.attack();
		}
		// if the number is 3 then heal ( 1/3 percent chance of healing)
		if (answer == 3) {
			Moves.heal();
		}

	}

	// Similar to the random method above but this is for the boss
	public static void bossRandom() {
		// Generates a random number
		Random rn = new Random();
		// Generates a number between 1 and 5
		int answer = rn.nextInt(5) + 1;
		// if the number is 1 or two then attack
		if (answer == 1 || answer == 2) {
			Moves.attack();
		}
		// if the number is 3 or 4 then heal
		if (answer == 3 || answer == 4) {
			Moves.heal();
		}
		// if the number is 5 then do the bosses special attack
		if (answer == 5) {
			Moves.bossHit();
		}

	}

}