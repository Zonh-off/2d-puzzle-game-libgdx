package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.manager.CameraManager;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.SCALE;

public class Application extends ApplicationAdapter {

    public static Engine ashley;

    private CameraManager camera;
    private SpriteBatch batch;

    private GameStateManager gsm;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        Assets.load();

        ashley = new Engine();

        camera = new CameraManager();
        camera.getCamera().setToOrtho(false, w / SCALE, h / SCALE);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.getCamera().combined);

        gsm = new GameStateManager(this);
    }

    @Override
    public void render() {
        gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(Gdx.graphics.getDeltaTime());

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) gsm.setState(GameStateManager.State.SPLASH);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) gsm.setState(GameStateManager.State.MENU);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) gsm.setState(GameStateManager.State.PLAY);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
    }

    @Override
    public void resize(int width, int height) {
        gsm.resize((int) (1280 / SCALE), (int) (720 / SCALE));
    }

    @Override
    public void dispose() {
        gsm.dispose();
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
