package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.mygdx.game.utils.Assets;

public class MyGdxGame extends Game {

	@Override
	public void create() {
		Assets.load();
		setScreen(new GameScreen(this));
	}
}