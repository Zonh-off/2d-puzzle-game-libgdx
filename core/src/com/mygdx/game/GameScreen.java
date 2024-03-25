package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.TileMapHelper;

import static com.mygdx.game.utils.Constants.*;
import static com.mygdx.game.utils.Constants.PPM;

public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private SpriteBatch batch;
    private Player player;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMap map;
    private TileMapHelper tileMapHelper;
    private Hud hud;
    private Integer counter = 0;
    private boolean isFullscreen = false;
    private boolean debugMode = false;
    private MapManager mapManager;

    public GameScreen(Game game) {
        super();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        world = new World(new Vector2(0, 0), false);
        box2DDebugRenderer = new Box2DDebugRenderer();

        player = new Player(new Vector2(Assets.level0.getProperties().get("width", Integer.class) * 32 / 2,
                Assets.level0.getProperties().get("height", Integer.class) * 32 / 2), world, camera);

        camera = new OrthographicCamera();
        camera.setToOrtho(false);

        map = Assets.level0;

        mapManager = new MapManager();
        mapManager.createLevelMap(world, map);

        hud = new Hud(camera);
    }

    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(0.09f, 0.08f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0.09f, 0.08f, 0.15f, 1);

        mapManager.orthogonalTiledMapRenderer().render();

        if(debugMode) {
            box2DDebugRenderer.render(world, camera.combined.scl(PPM));
        }

        player.GetRaycastHandler().updateAndRender();
        player.GetRaycastHandler().setCombinedMatrix(camera.combined.cpy().scl(PPM));

        //System.out.println(player.GetPosition().x + " : " + player.GetPosition().y);

        hud.getStage().draw();
        hud.setCounter(Gdx.graphics.getFramesPerSecond());
        counter++;

        batch.begin();
            mapManager.drawMirrorObjects(batch);
            player.updateAnimation(delta, batch);
            player.drawRainEffect(batch, delta);
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        mapManager.orthogonalTiledMapRenderer().dispose();
        box2DDebugRenderer.dispose();
        map.dispose();
    }

    private void update(float deltaTime) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            map = Assets.level0;
            mapManager.createLevelMap(world, map);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            map = Assets.level1;
            mapManager.createLevelMap(world, map);
        }

        player.update(deltaTime);
        handleCamera(deltaTime);
        setFullscreenMode();

        mapManager.orthogonalTiledMapRenderer().setView(camera);
        batch.setProjectionMatrix(camera.combined);
    }

    private void setFullscreenMode() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            isFullscreen = !isFullscreen;
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }

        if(isFullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(1280, 720);
        }
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

    // main scene camera
    private void handleCamera(float deltaTime) {
        Vector3 position = camera.position;
        if(debugMode){
            position.x = camera.position.x + (player.GetPosition().x * PPM - camera.position.x) * .2f;
            position.y = camera.position.y + (player.GetPosition().y * PPM - camera.position.y) * .2f;
        } else {
            position.x = Assets.level0.getProperties().get("width", Integer.class) * 32 / 2;
            position.y = Assets.level0.getProperties().get("height", Integer.class) * 32 / 2;
        }

        camera.position.set(position);
        camera.update();
    }
}
