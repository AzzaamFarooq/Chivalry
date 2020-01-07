package com.chivalry.game.screens;

/*
 * BattleScreen class which is the main screen where battles between player and AI occur.
 * Has values for player and ai health.
 * Uses tiled map and orthographic camera and rendering.
 * Uses textureatlases for animations for players and AI.
 * Uses sprite batches to create UI.
 * Uses a turn based system where AI only attacks once player has attacked.
 */

import com.chivalry.game.entities.Player;
import com.chivalry.game.entities.BattleAI;
import com.chivalry.game.entities.BattlePlayer;
import com.chivalry.game.entities.Moves;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class BattleScreen extends Game implements Screen {
	
	//Variables for tiled map, renderer and camera
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera cam;

	//Variables for player health, ai health and a temporary ai health
	public static float battleHealth = BattlePlayer.playerHealth;
	public static float aiBattleHealth;
	public static float tempHealth;

	//Variable for shape renderer
	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	//Variables for BattlePlayer and battle player texture atlas
	private TextureAtlas battlePlayerAtlas;
	private BattlePlayer battlePlayer;

	//Variables for BattleAI for bat and battle bat texture atlas
	private TextureAtlas battleBatAtlas;
	private BattleAI battleBat;
	
	//Variables for BattleAI for slime and battle slime texture atlas
	private TextureAtlas battleSlimeAtlas;
	private BattleAI battleSlime;
	
	//Variables for BattleAI for boss and battle boss texture atlas
	private TextureAtlas battleBossAtlas;
	private BattleAI battleBoss;

	//Integer array for the buttonpile
	private Array<Integer> buttonPile;

	//booleans for checking which ai is fighting
	boolean batBattle = false;
	boolean slimeBattle = false;
	boolean bossBattle = false;
	
	//Background layer for tiled map
	private int[] background = new int[] { 0 };

	//Used to change screens
	private Game game;
	
	//Variables for UI
	private SpriteBatch batch;
	private Sprite playerUI;
	private Sprite batUI;
	private Sprite slimeUI;
	private Sprite bossUI;
	private Sprite battleUI;

	//Used to switch screens
	public BattleScreen(Game game) {
		this.game = game;
		buttonPile = new Array<Integer>();
	}

	//Render method
	@Override
	public void render(float delta) {

		//Clears screen before rendering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Sets camera position to be centered and to update
		cam.position.set(513, 335, 3);
		cam.update();

		//Sets renderer view on camera
		renderer.setView(cam);

		//Renders the background layer for tiled map
		renderer.render(background);

		//Draws battle player
		renderer.getSpriteBatch().begin();
		battlePlayer.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();

		//If batReady boolean is true, draw battle bat
		if (Player.batReady) {
			batBattle = true;
			renderer.getSpriteBatch().begin();
			battleBat.draw(renderer.getSpriteBatch());
			renderer.getSpriteBatch().end();
		}
		
		//If slimeReady boolean is true, draw battle slime
		if (Player.slimeReady) {
			slimeBattle = true;
			renderer.getSpriteBatch().begin();
			battleSlime.draw(renderer.getSpriteBatch());
			renderer.getSpriteBatch().end();
		}
		
		//If bossReady boolean is true, draw battle boss
		if (Player.bossReady) {
			bossBattle = true;
			renderer.getSpriteBatch().begin();
			battleBoss.draw(renderer.getSpriteBatch());
			renderer.getSpriteBatch().end();
		}
		
		//Updates camera and makes sure batches created are visible in camera
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		
		//Draws battle UI
		batch.begin();
		battleUI.draw(batch);
		batch.end();
		
		//Draws player UI
		batch.begin();
		playerUI.draw(batch);
		batch.end();
		
		//If batReady boolean is true, draw bat UI
		if (Player.batReady) {
			batch.begin();
			batUI.draw(batch);
			batch.end();
		}
		
		//If slimeReady boolean is true, draw slime UI
		if (Player.slimeReady) {
			batch.begin();
			slimeUI.draw(batch);
			batch.end();
		}
		
		//If bossReady boolean is true, draw boss UI
		if (Player.bossReady) {
			batch.begin();
			bossUI.draw(batch);
			batch.end();
		}
		
		//Makes sure shapes rendered are visible by camera
		shapeRenderer.setProjectionMatrix(cam.combined);

		//Draws rectangle for AI health bar
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0);
		shapeRenderer.rect(94, 575, tempHealth/2 + 2, 25);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(95, 576, tempHealth/2, 23);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 128, 0, 0);
		shapeRenderer.rect(95, 576, aiBattleHealth/2, 23);
		shapeRenderer.end();

		//Draws rectangle for player health bar
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0);
		shapeRenderer.rect(874, 575, BattlePlayer.playerHealth/2 + 2, 25);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.rect(875, 576, BattlePlayer.playerHealth/2, 23);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 128, 0, 0);
		shapeRenderer.rect(875, 576, battleHealth/2, 23);
		shapeRenderer.end();

		//Keeps checking the update method whenever render is called
		update(Gdx.graphics.getDeltaTime());

		super.render();

	}
	
	//Method to make sure you cannot hold down attack and heal
	private boolean singlePress(int keycode) {
		boolean rc = true;

		// Check if a button is pressed
		if (Gdx.input.isKeyPressed(keycode)) {
			// if in the buttonpile area, return false
			if (buttonPile.contains(keycode, true)) {
				rc = false;
			}
			// if not, return true and add it to the buttonpile
			else {
				rc = true;
				buttonPile.add(keycode);
			}
		}
		// if a button is not pressed, remove it from buttonpile array
		else {
			rc = false;
			if (buttonPile.contains(keycode, true)) {
				buttonPile.removeIndex(buttonPile.lastIndexOf(keycode, true));
			}
		}

		return rc;
	}

	//Update method
	public void update(float delta) {

		//If J is pressed, ai's health goes down by the players damage
		if (singlePress(Input.Keys.J)) {
			aiBattleHealth -= BattlePlayer.damage;
			
			//if batBattle or slimeBattle is true, run random method from Moves class
			if (batBattle || slimeBattle) {
				Moves.random();
			}
			//if bossBattle is true, run bossRandom method from Moves class
			if (bossBattle) {
				Moves.bossRandom();
			}
		}
		
		//If K is pressed, players health goes up by the players heal if the players health is below a certain threshold
		if (singlePress(Input.Keys.K)) {
			if (battleHealth < (BattlePlayer.playerHealth - BattlePlayer.heal)) {
				battleHealth += BattlePlayer.heal;
			}
			
			//if batBattle or slimeBattle is true, run random method from Moves class
			if (batBattle || slimeBattle) {
				Moves.random();
			}
			
			//if bossBattle is true, run bossRandom method from Moves class
			if (bossBattle) {
				Moves.bossRandom();
			}
		}
		
		//When ai's health goes to or below 0, call playerWon method
		if (aiBattleHealth <= 0) {
			playerWon();
		}

		//When player's health goes to or below 0, call playerLost method
		if (battleHealth <= 0) {
			playerLost();
		}

	}

	//playerWon method
	public void playerWon() {
		//If batBattle boolean is true, call reset method, set batBattle to false, increase player xp by 50 
		//Set Screen back to OpenWorld and set players position to temp X and Y values
		if (batBattle) {
			reset();
			batBattle = false;
			Player.xp += 50;
			game.setScreen(new OpenWorld(game));
			OpenWorld.player.setPosition(OpenWorld.tempX, OpenWorld.tempY + 50);
		}
		//If slimeBattle boolean is true, call reset method, set slimeBattle to false, increase player xp by 25
		//Set Screen back to OpenWorld and set players position to temp X and Y values
		if (slimeBattle) {
			reset();
			slimeBattle = false;
			Player.xp += 25;
			game.setScreen(new OpenWorld(game));
			OpenWorld.player.setPosition(OpenWorld.tempX, OpenWorld.tempY - 50);
		}
		//If bossBattle boolean is true, call reset method, set bossBattle to false
		//Set Screen back to WinScreen and set players position to temp X and Y values
		if (bossBattle) {
			bossBattle = false;
			game.setScreen(new WinScreen(game));
			OpenWorld.player.setPosition(OpenWorld.tempX - 100, OpenWorld.tempY + 100);
		}
	}

	//playerLost method
	public void playerLost() {
		//Call reset method, set screen back to OpenWorld and set player position to inside the house
		reset();
		game.setScreen(new OpenWorld(game));
		OpenWorld.player.setPosition(1265, 1038);
	}

	//Reset method
	public void reset() {
		//Set battleHealth back to full and same with ai battle health
		battleHealth = BattlePlayer.playerHealth;
		aiBattleHealth = tempHealth;
	}
	
	//Resize camera view
	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width;
		cam.viewportHeight = height;
	}

	//Show method
	@Override
	public void show() {
		//if batReady or slimeReady booleans are true, set background to this
		if (Player.batReady || Player.slimeReady) {
			map = new TmxMapLoader().load("assets/maps/battleBackground.tmx");
		}
		
		//if bossReady boolean is true, set background to this
		if (Player.bossReady) {
			map = new TmxMapLoader().load("assets/maps/caveBattleBackground.tmx");
		}
		
		renderer = new OrthogonalTiledMapRenderer(map);
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		
		//Set texture for player UI and set its position
		playerUI = new Sprite(new Texture("assets/img/ui/playerUI.png"));
		playerUI.setPosition(800, 550);
		
		//Set texture for battle UI and set its position
		battleUI = new Sprite(new Texture("assets/img/ui/BattleUI.png"));
		battleUI.setPosition(30, 50);
		
		//If batReady boolean is true, set texture for bat UI and set its position
		if (Player.batReady) {
			batUI = new Sprite(new Texture("assets/img/ui/batBattleUI.png"));
			batUI.setPosition(20, 550);
		}
		
		//If slimeReady boolean is true, set texture for slime UI and set its position
		if (Player.slimeReady) {
			slimeUI = new Sprite(new Texture("assets/img/ui/slimeBattleUI.png"));
			slimeUI.setPosition(20, 550);
		}
		
		//If bossReady boolean is true, set texture for boss UI and set its position
		if (Player.bossReady) {
			bossUI = new Sprite(new Texture("assets/img/ui/bossBattleUI.png"));
			bossUI.setPosition(20, 550);
		}
		
		//Set texture for battle player atlas and the animations
		battlePlayerAtlas = new TextureAtlas("assets/img/battleSprites/battlePlayerSprites.pack");
		Animation stationary, attack, defend;
		stationary = new Animation(1 / 3f, battlePlayerAtlas.findRegions("stationary"));
		attack = new Animation(1 / 6f, battlePlayerAtlas.findRegions("attack"));
		defend = new Animation(1 / 4f, battlePlayerAtlas.findRegions("defend"));
		stationary.setPlayMode(Animation.LOOP);
		attack.setPlayMode(Animation.NORMAL);
		defend.setPlayMode(Animation.NORMAL);

		//Set texture for battle bat atlas and the animations
		battleBatAtlas = new TextureAtlas("assets/img/battleSprites/battleBatSprites.pack");
		Animation batStationary, batAttack, batDefend;
		batStationary = new Animation(1 / 2f, battleBatAtlas.findRegions("stationary"));
		batAttack = new Animation(1 / 4f, battleBatAtlas.findRegions("attack"));
		batDefend = new Animation(1 / 4f, battleBatAtlas.findRegions("defend"));
		batStationary.setPlayMode(Animation.LOOP);
		batAttack.setPlayMode(Animation.NORMAL);
		batDefend.setPlayMode(Animation.NORMAL);
		
		//Set texture for battle slime atlas and the animations
		battleSlimeAtlas = new TextureAtlas("assets/img/battleSprites/battleSlimeSprites.pack");
		Animation slimeStationary, slimeAttack, slimeDefend;
		slimeStationary = new Animation(1 / 2f, battleSlimeAtlas.findRegions("stationary"));
		slimeAttack = new Animation(1 / 4f, battleSlimeAtlas.findRegions("attack"));
		slimeDefend = new Animation(1 / 4f, battleSlimeAtlas.findRegions("defend"));
		slimeStationary.setPlayMode(Animation.LOOP);
		slimeAttack.setPlayMode(Animation.NORMAL);
		slimeDefend.setPlayMode(Animation.NORMAL);
		
		//Set texture for battle boss atlas and the animations
		battleBossAtlas = new TextureAtlas("assets/img/battleSprites/battleBossSprites.pack");
		Animation bossStationary, bossAttack, bossDefend;
		bossStationary = new Animation(1 / 2f, battleBossAtlas.findRegions("stationary"));
		bossAttack = new Animation(1 / 4f, battleBossAtlas.findRegions("attack"));
		bossDefend = new Animation(1 / 4f, battleBossAtlas.findRegions("defend"));
		bossStationary.setPlayMode(Animation.LOOP);
		bossAttack.setPlayMode(Animation.NORMAL);
		bossDefend.setPlayMode(Animation.NORMAL);

		//Create battle player object and its animations and set its position
		battlePlayer = new BattlePlayer(stationary, attack, defend);
		battlePlayer.setPosition(575, 25);

		//If batReady boolean is true, create battle bat object with its animations and set its position/size
		if (Player.batReady) {
			battleBat = new BattleAI(batStationary, batAttack, batDefend, 30, 20);
			battleBat.setSize(310, 200);
			battleBat.setPosition(70, 350);
		}

		//If slimeReady boolean is true, create battle sime object with its animations and set its position/size
		if (Player.slimeReady) {
			battleSlime = new BattleAI(slimeStationary, slimeAttack, slimeDefend, 7, 10);
			battleSlime.setSize(200, 200);
			battleSlime.setPosition(120, 250);
		}
		
		//If bossReady boolean is true, create battle boss object with its animations and set its position/size
		if (Player.bossReady) {
			battleBoss = new BattleAI(bossStationary, bossAttack, bossDefend, 50, 50);
			battleBoss.setSize(400, 400);
			battleBoss.setPosition(120, 250);
		}
		
		//Allow player to input keys to attack or heal
		Gdx.input.setInputProcessor(battlePlayer);

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
		battlePlayerAtlas.dispose();
		battleBatAtlas.dispose();
		battleSlimeAtlas.dispose();
		battleBossAtlas.dispose();
	}

	@Override
	public void create() {

	}

}
