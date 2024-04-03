package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Hud;
import com.mygdx.game.MapManager;
import com.mygdx.game.Player;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.TileMapHelper;

import static com.mygdx.game.utils.Constants.PPM;

public class PlayState extends GameState {

    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Player player;
    private TiledMap map;
    private Hud hud;
    private Integer counter = 0;
    private boolean isFullscreen = false;
    private boolean debugMode = false;
    private MapManager mapManager;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, 0), false);
        box2DDebugRenderer = new Box2DDebugRenderer();

        player = new Player(new Vector2(Assets.level0.getProperties().get("width", Integer.class) * 32 / 2,
                Assets.level0.getProperties().get("height", Integer.class) * 32 / 2), world, camera);

        map = Assets.level0;

        mapManager = new MapManager();
        mapManager.createLevelMap(world, map);

        hud = new Hud(camera);
    }

    @Override
    public void update(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        if(Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            map = Assets.level0;
            mapManager.createLevelMap(world, map);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            map = Assets.level1;
            mapManager.createLevelMap(world, map);
        }

        player.update(delta);
        handleCamera(delta);
        setFullscreenMode();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.09f, 0.08f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0.09f, 0.08f, 0.15f, 1);


        mapManager.orthogonalTiledMapRenderer().setView(camera);
        batch.setProjectionMatrix(camera.combined);

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
    public void dispose() {
        batch.dispose();
        world.dispose();
        mapManager.orthogonalTiledMapRenderer().dispose();
        box2DDebugRenderer.dispose();
        map.dispose();
    }

    private void handleCamera(float delta) {
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
}