package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {

    // Character
    public static Texture character;
    public static TextureRegion idle_up_sheet;
    public static TextureRegion idle_down_sheet;
    public static TextureRegion idle_left_sheet;
    public static TextureRegion idle_right_sheet;

    public static Animation idle_right_anim;
    public static Animation idle_left_anim;
    public static Animation walk_right_anim;
    public static Animation walk_left_anim;

    public static TextureRegion walk_up_sheet;
    public static TextureRegion walk_down_sheet;
    public static TextureRegion walk_left_sheet;
    public static TextureRegion walk_right_sheet;
    public static TextureRegion walk_up_frames;
    public static TextureRegion walk_down_frames;
    public static TextureRegion walk_left_frames;
    public static TextureRegion walk_right_frames;
    public static TextureRegion shadow_sheet;

    // Map
    public static TiledMap level0;

    public static Texture loadTeture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load() {
        character = loadTeture("AnimationSheet_Character.png");

        idle_down_sheet = new TextureRegion(character, 0, 0, 32, 32);

        idle_right_anim = new Animation(0.25f,
                new TextureRegion(character,0, 0, 32, 32),
                new TextureRegion(character, 32, 0, 32, 32));
        idle_left_anim = new Animation(0.25f,
                new TextureRegion(character,64, 0, 32, 32),
                new TextureRegion(character, 96, 0, 32, 32));
        walk_right_anim = new Animation(0.1f,
                new TextureRegion(character,0, 64, 32, 32),
                new TextureRegion(character, 32, 64, 32, 32),
                new TextureRegion(character, 64, 64, 32, 32),
                new TextureRegion(character, 96, 64, 32, 32));
        walk_left_anim = new Animation(0.1f,
                new TextureRegion(character,0, 96, 32, 32),
                new TextureRegion(character, 32, 96, 32, 32),
                new TextureRegion(character, 64, 96, 32, 32),
                new TextureRegion(character, 96, 96, 32, 32));

        level0 = new TmxMapLoader().load("maps/map0.tmx");
    }
}
