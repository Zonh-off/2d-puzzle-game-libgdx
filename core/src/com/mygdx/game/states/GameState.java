package com.mygdx.game.states;

import com.mygdx.game.Application;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.manager.GameStateManager;

public abstract class GameState {

    // References
    protected GameStateManager gsm;
    protected Application app;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    protected GameState(GameStateManager gsm) {
        this.gsm = gsm;
        this.app = (Application) gsm.application();
        batch = app.getBatch();
        camera = app.getCamera();
    }

    public void resize(int w, int h) {
        camera.setToOrtho(false, w, h);
    }

    public abstract void update(float delta);
    public abstract void render(float delta);
    public abstract void dispose();
}
