package com.mygdx.game.manager;

import com.mygdx.game.Application;
import com.mygdx.game.states.GameState;
import com.mygdx.game.states.MenuState;
import com.mygdx.game.states.PlayState;
import com.mygdx.game.states.SplashState;

import java.util.Stack;

public class GameStateManager {

    // Application Reference
    private final Application app;

    private Stack<GameState> states;

    public enum State {
        SPLASH,
        PLAY,
        MENU
    }

    public GameStateManager(final Application app) {
        this.app = app;
        this.states = new Stack<GameState>();
        this.setState(State.SPLASH);
    }

    public Application application() {
        return app;
    }

    public void update(float delta) {
        states.peek().update(delta);
    }

    public void render(float delta) {
        states.peek().render(delta);
    }

    public void dispose() {
        for(GameState gs : states) {
            gs.dispose();
        }
        states.clear();
    }

    public void resize(int w, int h) {
        states.peek().resize(w, h);
    }

    public void setState(State state) {
        if(states.size() >= 1) {
            states.pop().dispose();
        }
        states.push(getState(state));
    }

    private GameState getState(State state) {
        switch(state) {
            case SPLASH: return new SplashState(this);
            case PLAY: return new PlayState(this);
            case MENU: return new MenuState(this);
        }
        return null;
    }
}
