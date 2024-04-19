package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.utils.Assets;

public class SplashState extends GameState {
    private Stage stage;
    private Image splashLogo;

    float acc = 0f;
    TextureRegion tex;

    public SplashState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage(new ScreenViewport());
        tex = Assets.splashLogo;
        splashLogo = new Image(tex);
        splashLogo.setSize(256, 256);
        splashLogo.setPosition(Gdx.graphics.getWidth() / 2 - splashLogo.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - splashLogo.getHeight() / 2);
        stage.addActor(splashLogo);
    }

    @Override
    public void update(float delta) {
        acc += delta;
        splashLogo.setSize(256 + 10 * acc, 256 + 10 * acc);
        splashLogo.setPosition(Gdx.graphics.getWidth() / 2 - splashLogo.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - splashLogo.getHeight() / 2);
        if (acc >= 4) {
            tex = null;
            splashLogo = new Image(tex);
            gsm.setState(GameStateManager.State.MENU);
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.act(delta);
        stage.draw();

        batch.begin();

        batch.end();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
