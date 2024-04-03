package com.mygdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.manager.GameStateManager;
import com.mygdx.game.utils.Assets;


public class MenuState extends GameState {
    private Texture btn;
    private Stage stage;
    Image btnImage;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        stage = new Stage(new ScreenViewport());

        btn = Assets.loadTexture("button.png");
        btnImage = new Image(btn);
        btnImage.setScale(5f);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.add(btnImage).fillX().uniformX();
        table.row().pad(0, 0, 0, 0);

        btnImage.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                System.out.println("Clicked");
                gsm.setState(GameStateManager.State.PLAY);
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
