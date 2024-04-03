package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.utils.Assets;


public class MenuState extends GameState {
    private TextButton startBtn;
    private TextButton exitBtn;
    private Stage stage;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage(new ScreenViewport());

        startBtn = new TextButton("Start", Assets.menuSkin, "default");
        startBtn.setSize(200, 65);
        startBtn.setPosition(Gdx.graphics.getWidth() / 2 - startBtn.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - startBtn.getHeight() / 2);

        exitBtn = new TextButton("Exit", Assets.menuSkin, "default");
        exitBtn.setSize(200, 65);
        exitBtn.setPosition(Gdx.graphics.getWidth() / 2 - startBtn.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - startBtn.getHeight() / 2 - 100);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(startBtn);
        stage.addActor(exitBtn);

//        table.add().fillX().uniformX();
        table.row().pad(0, 0, 0, 0);

        startBtn.addListener(new InputListener() {
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {

            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                gsm.setState(GameStateManager.State.PLAY);
                return true;
            }
        });
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta) {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        Gdx.input.setInputProcessor(stage);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
