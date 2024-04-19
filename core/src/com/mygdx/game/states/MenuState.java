package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.utils.Assets;


public class MenuState extends GameState {
    private TextButton startBtn;
    private TextButton exitBtn;
    private Image background;
    private Stage stage;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage(new ScreenViewport());

        background = new Image(Assets.background);
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        background.setPosition(Gdx.graphics.getWidth() / 2 - background.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - background.getHeight() / 2);

        startBtn = new TextButton("Start", Assets.menuSkin, "default");
        startBtn.setSize(240, 85);
        startBtn.setPosition(Gdx.graphics.getWidth() / 2 - startBtn.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - startBtn.getHeight() / 2);

        exitBtn = new TextButton("Exit", Assets.menuSkin, "default");
        exitBtn.setSize(240, 85);
        exitBtn.setPosition(Gdx.graphics.getWidth() / 2 - startBtn.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - startBtn.getHeight() / 2 - 100);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(background);
        stage.addActor(startBtn);
        stage.addActor(exitBtn);

//        table.add().fillX().uniformX();
        table.row().pad(0, 0, 0, 0);

        startBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gsm.setState(GameStateManager.State.PLAY);
                return true;
            }
        });
        exitBtn.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
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
