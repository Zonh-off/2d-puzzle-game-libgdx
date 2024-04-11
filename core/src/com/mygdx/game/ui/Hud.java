package com.mygdx.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
    private Stage stage;
    private Viewport viewport;
    private Integer counter;
    private Label countLabel;

    public Hud(OrthographicCamera camera) {
        counter = 699;

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        stage = new Stage();

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countLabel = new Label(String.format("FPS: %03d", counter), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        countLabel.setFontScale(2f);
        table.add(countLabel).expandX().padTop(10);
        stage.addActor(table);
    }

    public Stage getStage(){
        return stage;
    }

    public void setCounter(int i){
        counter = i;
        countLabel.setText(String.format("FPS: %03d", counter));
    }
}
