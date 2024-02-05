package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.utils.TileMapHelper;

import static com.mygdx.game.utils.Constants.*;

public class MyGdxGame extends ApplicationAdapter {
	private final float SCALE = 2f;
	private float playerSpeed = 3f;

	private float posX = 0, posY = 0;
	private float zSpeed = 0;
	private float jumpSpeed = -.035f;
	private float gameGravity = .1f;
	private float z = 0;
	private float zHeight = (float) 32 / 2;
	private float zFloor = 0;

	private OrthographicCamera camera;
	private World world;
	private Body player, platform, shadow;
	private Box2DDebugRenderer box2DDebugRenderer;
	private SpriteBatch batch;
	private Texture characterSheet, playerShadow;
	private TextureRegion characterFrame;
	private Sprite sprite;


	private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
	private TiledMap map;
	private TileMapHelper tileMapHelper;

	@Override
	public void create () {
		batch = new SpriteBatch();
		characterSheet = new Texture("IdleRight.png");
		characterFrame = new TextureRegion(characterSheet, 32, 32);
		playerShadow = new Texture("shadow.png");
		sprite = new Sprite();

		world = new World(new Vector2(0, 0), false);
		box2DDebugRenderer = new Box2DDebugRenderer();
		player = createBox(200, 100, 32, 32, false, BIT_PLAYER, BIT_WALL, (short) 0);
		platform = createBox(200, 100, 64, 32, true, BIT_WALL, BIT_WALL, (short) 0);
		shadow = createBox(200, 100, 32, 32, false);

		posX = player.getPosition().x;
		posY = player.getPosition().y;

		camera = new OrthographicCamera();
		camera.setToOrtho(false);

		map = new TmxMapLoader().load("maps/map0.tmx");
		orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map);

		tileMapHelper = new TileMapHelper(world, map);
	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ScreenUtils.clear(0, 0, 0, 1);
		orthogonalTiledMapRenderer.render();

		box2DDebugRenderer.render(world, camera.combined.scl(PPM));

		batch.begin();
		batch.draw(
				playerShadow,
				shadow.getPosition().x * PPM - ((float) characterFrame.getRegionWidth() / 2),
				shadow.getPosition().y * PPM - ((float) characterFrame.getRegionHeight()));
		batch.draw(
				characterFrame,
				player.getPosition().x * PPM - ((float) characterFrame.getRegionWidth() / 2),
				player.getPosition().y * PPM - ((float) characterFrame.getRegionHeight() / 2));
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(
				false,
				Gdx.graphics.getWidth() / SCALE,
				Gdx.graphics.getHeight() / SCALE);
	}

	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		orthogonalTiledMapRenderer.dispose();
		box2DDebugRenderer.dispose();
		map.dispose();
	}

	private void update(float deltaTime) {
		world.step(1 / 60f, 6, 2);

		handleInput(deltaTime);
		handleCamera(deltaTime);
		orthogonalTiledMapRenderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
	}

	private Body createBox(int x, int y, int width, int height, boolean isStatic) {
		Body pBody;
		BodyDef def = new BodyDef();

		if(isStatic)
			def.type = BodyDef.BodyType.StaticBody;
		else
			def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(x / PPM, y / PPM);
		def.fixedRotation = true;
		pBody = world.createBody(def);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

		pBody.createFixture(shape, 2f);
		shape.dispose();

		return pBody;
	}

	private Body createBox(int x, int y, int width, int height, boolean isStatic, short cBits, short mBits, short gIndex) {
		Body pBody;
		BodyDef def = new BodyDef();

		if(isStatic)
			def.type = BodyDef.BodyType.StaticBody;
		else
			def.type = BodyDef.BodyType.DynamicBody;

		def.position.set(x / PPM, y / PPM);
		def.fixedRotation = true;
		pBody = world.createBody(def);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 2f;
		fixtureDef.filter.categoryBits = cBits; // Is a
		fixtureDef.filter.maskBits = mBits; // Collide with
		fixtureDef.filter.groupIndex = gIndex;

		pBody.createFixture(fixtureDef).getBody();
		shape.dispose();

		return pBody;
	}

	private void handleInput(float deltaTime) {
		int horizontalForce = 0;
		int verticalForce = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.D)){
			horizontalForce += 1;
			posX += playerSpeed * deltaTime;
			characterSheet = new Texture("IdleRight.png");
			characterFrame = new TextureRegion(characterSheet, 32, 32);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			horizontalForce -= 1;
			posX -= playerSpeed * deltaTime;
			characterSheet = new Texture("IdleLeft.png");
			characterFrame = new TextureRegion(characterSheet, 32, 32);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			verticalForce += 1;
			posY += playerSpeed * deltaTime;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.S)){
			verticalForce -= 1;
			posY -= playerSpeed * deltaTime;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && zSpeed == 0){
			zSpeed = jumpSpeed;
		}
		if(z != zFloor){
			zSpeed += gameGravity * deltaTime;
		}
		if(z + zSpeed > zFloor){
			zSpeed = 0;
			z = zFloor;
		}
		z += zSpeed;

		System.out.println(z);
		//player.setLinearVelocity(horizontalForce * playerSpeed, (verticalForce) * playerSpeed);
		player.setTransform(posX,posY - z,0);
		shadow.setTransform(posX, posY + zFloor, 0);
	}

	private void handleCamera(float deltaTime) {
		Vector3 position = camera.position;
		position.x = camera.position.x + (player.getPosition().x * PPM - camera.position.x) * .2f;
		position.y = camera.position.y + (player.getPosition().y * PPM - camera.position.y) * .2f;
		camera.position.set(position);

		camera.update();
	}
}