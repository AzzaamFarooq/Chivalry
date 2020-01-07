package com.chivalry.game.screens;

/*
 * OpenWorld class which is the main screen where Player walks around to find battles.
 * Uses tiled map and orthographic camera and rendering.
 * Uses textureatlases for animations for players and AI.
 * Uses sprite batches to create UI.
 */

import com.chivalry.game.entities.Player;
import com.chivalry.game.entities.AI;
import com.chivalry.game.entities.BattlePlayer;
import com.chivalry.game.screens.BattleScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class OpenWorld extends Game implements Screen {

	//Tiled map used as background for open world screen
	private TiledMap map = new TmxMapLoader().load("assets/maps/startTown.tmx");
	
	//Variables for renderer, camera and shape renderer
	private OrthogonalTiledMapRenderer renderer;
	public static OrthographicCamera cam;
	private ShapeRenderer shapeRenderer = new ShapeRenderer();

	//Texture atlas' for player and monsters
	private TextureAtlas playerAtlas;
	private TextureAtlas batAtlas;
	private TextureAtlas slimeAtlas;
	private TextureAtlas bossAtlas;

	//Variables for player and AI objects
	public static Player player;
	private AI bat1;
	private AI bat2;
	private AI bat3;
	private AI slime1;
	private AI slime2;
	private AI slime3;
	private AI boss;
	
	//Variables to store player X and Y positions to teleport him back after a battle
	public static float tempX, tempY;

	//Integer array to store the tiled map layers
	private int[] L1 = new int[] { 2 }, L2 = new int[] { 3 }, L3 = new int[] { 4 }, L4 = new int[] { 5 };

	//Used to change screens
	private Game game;
	
	//Variables for UI
	private SpriteBatch batch;
	private Sprite playerUI;
	private Sprite levelUI;
	private Sprite xpUI;
	public static Sprite instructions;
	
	//String format for level
	public String levelDisplay = "" + Player.lvl;
	
	//Bitmap font for player levels
	BitmapFont levelFont = new BitmapFont(Gdx.files.internal("assets/fonts/score.fnt"));;
	
	//Used to switch screens
	public OpenWorld(Game game) {
		this.game = game;
	}
	
	//Render method
	@Override
	public void render(float delta) {

		//clears the screen before rendering
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//Sets the camera onto the player to follow him and then updates it
		cam.position.set(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, 0);
		cam.update();

		//Sets view of the renderer onto the camera
		renderer.setView(cam);

		//Renders the first two layers of tiled map
		renderer.render(L1);
		renderer.render(L2);

		//Renders all bat monsters
		renderer.getSpriteBatch().begin();
		bat1.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		renderer.getSpriteBatch().begin();
		bat2.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		renderer.getSpriteBatch().begin();
		bat3.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		
		//Renders all slime monsters
		renderer.getSpriteBatch().begin();
		slime1.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		renderer.getSpriteBatch().begin();
		slime2.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		renderer.getSpriteBatch().begin();
		slime3.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		
		//Renders boss monster
		renderer.getSpriteBatch().begin();
		boss.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();

		//Renders player
		renderer.getSpriteBatch().begin();
		player.draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();

		//Renders last 2 layers of tiled map
		renderer.render(L3);
		renderer.render(L4);
		
		//Makes sure batches created after are visible by the camera
		batch.setProjectionMatrix(cam.combined);
		
		//Draws player UI
		batch.begin();
		playerUI.draw(batch);
		batch.end();
		
		//Sets player UI size to parameters and position to stay with camera
		playerUI.setSize(50, 50);
		playerUI.setPosition(cam.position.x - 200, cam.position.y + 75);
		
		//Draws level UI
		batch.begin();
		levelUI.draw(batch);
		batch.end();
		
		//Sets level UI size to parameters and position to stay with camera
		levelUI.setSize(80, 50);
		levelUI.setPosition(cam.position.x - 150, cam.position.y + 75);

		//Draws xp UI
		batch.begin();
		xpUI.draw(batch);
		batch.end();
		
		//Sets xp UI size to parameters and position to stay with camera
		xpUI.setSize(30, 30);
		xpUI.setPosition(cam.position.x - 180, cam.position.y + 55);
		
		//Draws instructions UI
		batch.begin();
		instructions.draw(batch);
		batch.end();
		
		//Sets instructions UI size to parameters
		instructions.setSize(110, 170);
		
		//Draws the levels and sets position to stay with the camera
		batch.begin();
		levelFont.draw(batch, levelDisplay, cam.position.x - 140, cam.position.y + 123);
		batch.end();
		
		//Updates camera and makes sure the shapes that get rendered are visible
		cam.update();
		shapeRenderer.setProjectionMatrix(cam.combined);

		//Renders a rectangle to show the XP 
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 0, 0, 0);
		shapeRenderer.rect(cam.position.x - 150, cam.position.y + 68, Player.xp + 2, 10);
		shapeRenderer.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(1, 128, 0, 1);
		shapeRenderer.rect(cam.position.x - 149, cam.position.y + 69, Player.xp, 8);
		shapeRenderer.end();

		//Keeps checking the update method whenever render is called
		update(Gdx.graphics.getDeltaTime());

		super.render();

	}

	//Update method
	public void update(float delta) {
		
		//When players XP goes over 100 or is equal to 100, player level goes up, player xp is set back to 0 and re calls player damage
		if (Player.xp >= 100) {
			Player.lvl++;
			BattlePlayer.damage = 10 * Player.lvl;
			BattlePlayer.playerHealth = 100 + (3 * (Player.lvl - 1));
			Player.xp = 0;
		}
		
		//If batReady boolean becomes true, set the ai health in battle screen to the given and call reset method
		if (Player.batReady) {
			BattleScreen.aiBattleHealth = 350;
			BattleScreen.tempHealth = 350;
			reset();
		}
		
		//If slimeReady boolean becomes true, set the ai health in battle screen to the given and call reset method
		if (Player.slimeReady) {
			BattleScreen.aiBattleHealth = 100;
			BattleScreen.tempHealth = 100;
			reset();
		}
		
		//If bossReady boolean becomes true, set the ai health in battle screen to the given and call reset method
		if (Player.bossReady) {
			BattleScreen.aiBattleHealth = 1000;
			BattleScreen.tempHealth = 1000;
			reset();
		}
	}

	//Reset method
	public void reset() {
		//Keeps temp X and Y for player to be used to teleport player back after battle
		tempX = player.getX();
		tempY = player.getY();
		//Moves player so it won't continuously create the battle screen
		player.setPosition(481, 1300);
		//Sets screen to battle screen
		game.setScreen(new BattleScreen(game));
	}
	
	//Resizes camera view
	@Override
	public void resize(int width, int height) {
		cam.viewportWidth = width / 2.5f;
		cam.viewportHeight = height / 2.5f;
	}

	//Show method
	@Override
	public void show() {
		
		
		renderer = new OrthogonalTiledMapRenderer(map);
		cam = new OrthographicCamera();
		batch = new SpriteBatch();
		
		//Gives textures for all UIs
		playerUI = new Sprite(new Texture("assets/img/ui/playerUI.png"));
		levelUI = new Sprite(new Texture("assets/img/ui/levelUI.png"));
		xpUI = new Sprite(new Texture("assets/img/ui/xpUI.png"));
		instructions = new Sprite(new Texture("assets/img/ui/instructions.png"));
		
		//Texture atlas and animations for player
		playerAtlas = new TextureAtlas("assets/img/sprites/playerSprites.pack");
		Animation stationary, left, right, up, down;
		stationary = new Animation(1 / 2f, playerAtlas.findRegions("stationary"));
		left = new Animation(1 / 4f, playerAtlas.findRegions("left"));
		right = new Animation(1 / 4f, playerAtlas.findRegions("right"));
		up = new Animation(1 / 4f, playerAtlas.findRegions("up"));
		down = new Animation(1 / 4f, playerAtlas.findRegions("down"));
		stationary.setPlayMode(Animation.LOOP);
		left.setPlayMode(Animation.LOOP);
		right.setPlayMode(Animation.LOOP);
		up.setPlayMode(Animation.LOOP);
		down.setPlayMode(Animation.LOOP);

		//Texture atlas and animations for bats
		batAtlas = new TextureAtlas("assets/img/sprites/batSprites.pack");
		Animation batStationary;
		batStationary = new Animation(1 / 4f, batAtlas.findRegions("stationary"));
		batStationary.setPlayMode(Animation.LOOP);

		//Texture atlas and animations for slimes
		slimeAtlas = new TextureAtlas("assets/img/sprites/slimeSprites.pack");
		Animation slimeStationary;
		slimeStationary = new Animation(1, slimeAtlas.findRegions("stationary"));
		slimeStationary.setPlayMode(Animation.LOOP);
	
		//Texture atlas and animations for bosses
		bossAtlas = new TextureAtlas("assets/img/sprites/bossSprites.pack");
		Animation bossStationary;
		bossStationary = new Animation(1 / 2f, bossAtlas.findRegions("stationary"));
		bossStationary.setPlayMode(Animation.LOOP);

		//Creates player object and gives its animations, and sets its position
		player = new Player(stationary, left, right, up, down, (TiledMapTileLayer) map.getLayers().get(0),
				(TiledMapTileLayer) map.getLayers().get(1));
		player.setPosition(481, 1300);

		//Creates bat objects, gives the animation and sets its positions
		bat1 = new AI(batStationary);
		bat1.setPosition(481, 1150);
		bat2 = new AI(batStationary);
		bat2.setPosition(200, 850);
		bat3 = new AI(batStationary);
		bat3.setPosition(700, 1400);

		//Creates slime objects, gives the animation and sets its positions
		slime1 = new AI(slimeStationary);
		slime1.setPosition(350, 1000);
		slime2 = new AI(slimeStationary);
		slime2.setPosition(300, 1500);
		slime3 = new AI(slimeStationary);
		slime3.setPosition(165, 1200);

		//Creates boss objects gives the animation and sets its position
		boss = new AI(bossStationary);
		boss.setPosition(1250, 300);
		
		//Allow player to input keys to move
		Gdx.input.setInputProcessor(player);

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
		playerAtlas.dispose();
		batAtlas.dispose();
		slimeAtlas.dispose();
		bossAtlas.dispose();
	}

	@Override
	public void create() {

	}

}