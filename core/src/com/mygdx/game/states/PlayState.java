package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Player;
import com.mygdx.game.manager.CameraManager;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.objects.Projectile;
import com.mygdx.game.ui.Hud;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.LevelLoader;
import com.mygdx.game.utils.MyContactListener;

import static com.mygdx.game.utils.Constants.PPM;

public class PlayState extends GameState {

    private final World world;
    private final Box2DDebugRenderer box2DDebugRenderer;
    private final Player player;
    private final Hud hud;
    private final OrthographicCamera camera;
    private final LevelLoader levelLoader;
    private Integer levelId = 0;
    private boolean isFullscreen = false;
    private boolean debugMode = false;
    private boolean projspawned = false;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, 0), false);
        box2DDebugRenderer = new Box2DDebugRenderer();

        player = new Player(new Vector2(Assets.level0.getProperties().get("width", Integer.class) * 32 / 2,
                Assets.level0.getProperties().get("height", Integer.class) * 32 / 2), world);
        camera = CameraManager.Instance.getCamera();

//        world.setContactListener(player);
        levelLoader = new LevelLoader(world);

        world.setContactListener(new MyContactListener());

        hud = new Hud(camera);
    }

    @Override
    public void update(float delta) {
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            levelId = 0;
            levelLoader.setLevel(0);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            levelId++;
            System.out.println("LevelID: " + levelId);
            levelLoader.setLevel(levelId);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F4)) {

        }

        if (Projectile.Instance != null && !projspawned) {
            projspawned = true;
            Projectile.Instance.setDir(Player.Instance.getLookingDir());
        }

        player.update(delta);
        handleCamera(delta);

//        System.out.println(camera.getCamera().position + " : " + player.getPosition());

        setFullscreenMode();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.09f, 0.08f, 0.15f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0.09f, 0.08f, 0.15f, 1);

        levelLoader.getMapManager().orthogonalTiledMapRenderer().setView(camera);
        levelLoader.getMapManager().orthogonalTiledMapRenderer().render();
        batch.setProjectionMatrix(camera.combined);


        if (debugMode) {
            box2DDebugRenderer.render(world, camera.combined.scl(PPM));
        }

        player.GetRaycastHandler().updateAndRender();
        player.GetRaycastHandler().setCombinedMatrix(camera.combined.cpy().scl(PPM));

        //System.out.println(player.GetPosition().x + " : " + player.GetPosition().y);

        hud.getStage().draw();
        hud.setCounter(Gdx.graphics.getFramesPerSecond());

        batch.begin();
        levelLoader.getMapManager().drawInteractableObjects(batch);
        player.updateAnimation(delta, batch);
        player.drawRainEffect(batch, delta);
        if (Projectile.Instance != null)
            Projectile.Instance.updateAnimation(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        levelLoader.getMapManager().orthogonalTiledMapRenderer().dispose();
        box2DDebugRenderer.dispose();
        levelLoader.getMap().dispose();
    }

    private void setFullscreenMode() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            isFullscreen = !isFullscreen;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debugMode = !debugMode;
        }

        if (isFullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode(1280, 720);
        }
    }

    private void handleCamera(float delta) {
        Vector3 position = camera.position;
        if (debugMode) {
            position.x = camera.position.x + (Player.Instance.getPosition().x * PPM - camera.position.x) * .2f;
            position.y = camera.position.y + (Player.Instance.getPosition().y * PPM - camera.position.y) * .2f;
        } else {
            position.x = Assets.level0.getProperties().get("width", Integer.class) * 32 / 2 + (Player.Instance.getPosition().x * PPM - camera.position.x) * .05f;
            position.y = Assets.level0.getProperties().get("height", Integer.class) * 32 / 2 + (Player.Instance.getPosition().y * PPM - camera.position.y) * .025f;
        }

        camera.position.set(new Vector3(position.x, position.y, camera.position.z));
        camera.update();
    }
}