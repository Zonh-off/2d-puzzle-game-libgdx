package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.SCALE;

public class SplashState extends GameState {

    float acc = 0f;
    TextureRegion tex;

    public SplashState(GameStateManager gsm) {
        super(gsm);
        tex = Assets.mirror_es;
    }

    @Override
    public void update(float delta) {
        acc += delta;
        if(acc >= 3) {
            gsm.setState(GameStateManager.State.MENU);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        camera.update();

        batch.begin();
        batch.draw(tex, Gdx.graphics.getWidth() / (SCALE * 2) - tex.getRegionHeight() / 2, Gdx.graphics.getHeight() / (SCALE * 2)  - tex.getRegionWidth() / 2);
        batch.end();
    }

    @Override
    public void dispose() {

    }
}
