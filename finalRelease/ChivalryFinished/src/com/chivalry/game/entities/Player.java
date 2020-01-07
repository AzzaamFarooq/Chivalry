package com.chivalry.game.entities;

/*
 * Player class used to create Player Object in OpenWorld. 
 * Has floats for xp, level.
 * Stores animations for player object.
 * Has methods and boolean checking collisions and interactions of player with other objects and parts of the map.
 * Uses input processor to allow player to move in OpenWorld.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.chivalry.game.screens.OpenWorld;

public class Player extends Sprite implements InputProcessor {

	//Vector 2 variable for velocity
	private Vector2 velocity = new Vector2();

	//Floats for speed, animation and change
	private float speed = 95, animation = 0, change;

	//Float and int for level and XP
	public static float xp = 0;
	public static int lvl = 30;

	//Animation variables for player
	private Animation stationary, left, right, up, down;
	//Tilemap layer variables for collisions and interactions
	private TiledMapTileLayer collisions;
	private TiledMapTileLayer interactions;

	//Strings for specific tiles to change booleans to true when player activates them
	private String blockedKey = "blocked";
	private String enterKey = "enterShop";
	private String exitKey = "exitShop";
	private String bossCaveKey = "bossCave";
	private String batKey = "bat";
	private String slimeKey = "slime";
	private String bossKey = "boss";

	//Boolean to check if player is fighting bat, slime or boss
	public static boolean batReady = false, slimeReady = false, bossReady = false;

	//Player object to be used in OpenWorld
	public Player(Animation stationary, Animation left, Animation right, Animation up, Animation down,
			TiledMapTileLayer collisions, TiledMapTileLayer interactions) {
		super(stationary.getKeyFrame(0));
		this.stationary = stationary;
		this.left = left;
		this.right = right;
		this.up = up;
		this.down = down;
		this.collisions = collisions;
		this.interactions = interactions;
		setSize(30, 40);
	}

	//Draw method which calls update 
	@Override
	public void draw(SpriteBatch spriteBatch) {
		update(Gdx.graphics.getDeltaTime());
		super.draw(spriteBatch);
	}

	//Update method
	public void update(float delta) {

		//Clamps speed so player does not go over it
		if (velocity.y > speed)
			velocity.y = speed;
		else if (velocity.y < -speed)
			velocity.y = -speed;
		else if (velocity.x < -speed)
			velocity.x = -speed;
		else if (velocity.x > speed)
			velocity.x = speed;

		//Floats to keep temporary X and Y for player collisions
		float originalX = getX(), originalY = getY();
		//Booleans to teleport player and for collisions
		boolean collisionX = false, collisionY = false, enteringShop = false, exitingShop = false, enteringCave = false;

		//Player moving on X values
		setX(getX() + velocity.x * delta);

		//calculates the steps for collisionsLeft() and collisionsRight()
		change = collisions.getTileWidth();
		change = getWidth() < change ? getWidth() / 2 : change / 2;

		//If player going left, collisionX boolean is equal to collsionsLeft()
		if (velocity.x < 0)
			collisionX = collisionsLeft();
		//Else iff player going right, collisionX boolean is equal to collsionsRight()
		else if (velocity.x > 0)
			collisionX = collisionsRight();

		//If collisionX is true, set player X to tempX and X-velocity to 0
		if (collisionX) {
			setX(originalX);
			velocity.x = 0;
		}

		//Player moving on Y values
		setY(getY() + velocity.y * delta);

		//calculates the steps for collisionsUp() and collisionsDown()
		change = collisions.getTileHeight();
		change = getHeight() < change ? getHeight() / 2 : change / 2;

		//If player going down, collisionY boolean is equal to collsionsBottom()
		if (velocity.y < 0)
			collisionY = collisionsBottom();
		//Else if player going up, collisionY boolean is equal to collsionsTop()
		else if (velocity.y > 0)
			collisionY = collisionsTop();

		//If collisionY is true, set player Y to tempY and Y-velocity to 0
		if (collisionY) {
			setY(originalY);
			velocity.y = 0;
		}

		//When player going down, exitingShop boolean is equal to exit();
		if (velocity.y < 0)
			exitingShop = exit();

		//When player going up, enteringShop is equal to enter();
		if (velocity.y > 0)
			enteringShop = enter();

		//When enteringShop is true, set position of player to these
		if (enteringShop) {
			setPosition(1265, 1038);
		}

		//When exitingShop is true, set position of player to these
		if (exitingShop) {
			setPosition(481, 1340);
		}

		//EnteringCave is equal to caveEnter(), when enteringCave is true, set player position to these
		enteringCave = caveEnter();
		if (enteringCave) {
			setPosition(1270, 50);
		}
		
		//These booleans are equal to their respective methods to see when they become true to be used in OpenWorld to change screen
		batReady = batBattle();
		slimeReady = slimeBattle();
		bossReady = bossBattle();

		//Update the animation when it meets these conditions
		animation += delta;
		setRegion(velocity.x < 0 ? left.getKeyFrame(animation)
				: velocity.x > 0 ? right.getKeyFrame(animation)
						: velocity.y > 0 ? up.getKeyFrame(animation)
								: velocity.y < 0 ? down.getKeyFrame(animation) : stationary.getKeyFrame(animation));
	}

	//Check collisions layer to find the cell that contains the blockedKey
	private boolean checkCollisions(float x, float y) {
		Cell cell = collisions.getCell((int) (x / collisions.getTileWidth()), (int) (y / collisions.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(blockedKey);
	}

	//Check interactions layer to find the cell that contains the enterKey
	private boolean checkEnter(float x, float y) {
		Cell cell2 = interactions.getCell((int) (x / interactions.getTileWidth()),
				(int) (y / interactions.getTileHeight()));
		return cell2 != null && cell2.getTile() != null && cell2.getTile().getProperties().containsKey(enterKey);
	}

	//Check interactions layer to find the cell that contains the exitKey
	private boolean checkExit(float x, float y) {
		Cell cell3 = interactions.getCell((int) (x / interactions.getTileWidth()),
				(int) (y / interactions.getTileHeight()));
		return cell3 != null && cell3.getTile() != null && cell3.getTile().getProperties().containsKey(exitKey);
	}

	//Check interactions layer to find the cell that contains the bossCaveKey
	private boolean checkCaveEntrance(float x, float y) {
		Cell cell3 = interactions.getCell((int) (x / interactions.getTileWidth()),
				(int) (y / interactions.getTileHeight()));
		return cell3 != null && cell3.getTile() != null && cell3.getTile().getProperties().containsKey(bossCaveKey);
	}
	
	//Check interactions layer to find the cell that contains the batKey
	private boolean checkBatBattle(float x, float y) {
		Cell cell4 = interactions.getCell((int) (x / interactions.getTileWidth()),
				(int) (y / interactions.getTileHeight()));
		return cell4 != null && cell4.getTile() != null && cell4.getTile().getProperties().containsKey(batKey);
	}

	//Check interactions layer to find the cell that contains the slimeKey
	private boolean checkSlimeBattle(float x, float y) {
		Cell cell5 = interactions.getCell((int) (x / interactions.getTileWidth()),
				(int) (y / interactions.getTileHeight()));
		return cell5 != null && cell5.getTile() != null && cell5.getTile().getProperties().containsKey(slimeKey);
	}
	
	//Check interactions layer to find the cell that contains the bossKey
	private boolean checkBossBattle(float x, float y) {
		Cell cell6 = interactions.getCell((int) (x / interactions.getTileWidth()),
				(int) (y / interactions.getTileHeight()));
		return cell6 != null && cell6.getTile() != null && cell6.getTile().getProperties().containsKey(bossKey);
	}
	
	//When the blocked cell is on the right of the player, return true
	public boolean collisionsRight() {
		for (float step = 0; step <= getHeight(); step += change)
			if (checkCollisions(getX() + getWidth(), getY() + step))
				return true;
		return false;
	}

	//When the blocked cell is on the left of the player, return true
	public boolean collisionsLeft() {
		for (float step = 0; step <= getHeight(); step += change)
			if (checkCollisions(getX(), getY() + step))
				return true;
		return false;
	}

	//When the blocked cell is on top of the player, return true
	public boolean collisionsTop() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkCollisions(getX() + step, getY() + getHeight()))
				return true;
		return false;

	}

	//When the blocked cell is on the bottom of the player, return true
	public boolean collisionsBottom() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkCollisions(getX() + step, getY()))
				return true;
		return false;
	}

	//When the enterShop cell is the same X and Y as the player, return true
	public boolean enter() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkEnter(getX(), getY())) {
				return true;
			}
		return false;

	}

	//When the exitShop cell is the same X and Y as the player, return true
	public boolean exit() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkExit(getX(), getY())) {
				return true;
			}
		return false;
	}

	//When the caveEnter cell is the same X and Y as the player, return true
	public boolean caveEnter() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkCaveEntrance(getX(), getY())) {
				return true;
			}
		return false;
	}
	
	//When the batBattle cell is the same X and Y as the player, return true
	public boolean batBattle() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkBatBattle(getX(), getY())) {
				return true;
			}
		return false;
	}
	
	//When the slimeBattle cell is the same X and Y as the player, return true
	public boolean slimeBattle() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkSlimeBattle(getX(), getY())) {
				return true;
			}
		return false;
	}

	//When the bossBattle cell is the same X and Y as the player, return true
	public boolean bossBattle() {
		for (float step = 0; step <= getWidth(); step += change)
			if (checkBossBattle(getX(), getY())) {
				return true;
			}
		return false;
	}

	//When these keys are pressed, call these cases
	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		//When W is down, set velocity on Y to positive speed and animation to 0
		case Keys.W:
			velocity.y = speed;
			animation = 0;
			break;
		//When S is down, set velocity on Y to negative speed and animation to 0	
		case Keys.S:
			velocity.y = -speed;
			animation = 0;
			break;
		//When A is down, set velocity on X to negative speed and animation to 0
		case Keys.A:
			velocity.x = -speed;
			animation = 0;
			break;
		//When D is down, set velocity on X to positive speed and animation to 0	
		case Keys.D:
			velocity.x = speed;
			animation = 0;
			break;
		//When H is down, set velocity on X and Y to 0 and make instructions appear in the middle of the screen
		case Keys.H:
			OpenWorld.instructions.setPosition(OpenWorld.cam.position.x - 40, OpenWorld.cam.position.y - 60);
			velocity.x = 0;
			velocity.y = 0;
		}
		return true;
	}

	//When these keys are let go of, do these cases
	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		//When W is up, set velocity on Y to 0 and animation to 0
		case Keys.W:
			velocity.y = 0;
			animation = 0;
		//When S is up, set velocity on Y to 0 and animation to 0		
		case Keys.S:
			velocity.y = 0;
			animation = 0;
		//When A is up, set velocity on X to 0 and animation to 0	
		case Keys.A:
			velocity.x = 0;
			animation = 0;
		//When D is up, set velocity on X to 0 and animation to 0			
		case Keys.D:
			velocity.x = 0;
			animation = 0;
		//When H is up, set the instructions position to off the screen	
		case Keys.H:
			OpenWorld.instructions.setPosition(3000, 3000);	
		}
		return true;
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
