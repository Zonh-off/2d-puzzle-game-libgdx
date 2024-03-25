package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Assets {

    // Character
    public static Texture character;
    public static Texture baseTiles;
    public static TextureRegion idle_down_sheet;
    public static Animation idle_right_anim;
    public static Animation idle_left_anim;
    public static Animation walk_right_anim;
    public static Animation walk_left_anim;
    public static Animation die_anim;
    public static Animation shadow_anim;

    // Mirrors
    public static TextureRegion mirror_ne;
    public static TextureRegion mirror_es;
    public static TextureRegion mirror_sw;
    public static TextureRegion mirror_wn;

    //particles
    public static TextureAtlas rainAtlas;
    public static ParticleEffect rainEffect;

    // Map
    public static TiledMap level0;
    public static TiledMap level1;

    public static Texture loadTeture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load() {
        character = loadTeture("AnimationSheet_Character.png");
        baseTiles = loadTeture("maps/basetiles.png");

        //rainAtlas = new TextureAtlas().load(Gdx.files.internal("particles/rain.png"));
        rainEffect = new ParticleEffect();
        rainEffect.load(Gdx.files.internal("particles/rain.p"), Gdx.files.internal("particles/"));

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
        die_anim = new Animation(0.25f,
                new TextureRegion(character, 0, 224, 32, 32),
                new TextureRegion(character, 32, 224, 32, 32),
                new TextureRegion(character, 64, 224, 32, 32),
                new TextureRegion(character, 96, 224, 32, 32));
        shadow_anim = new Animation(0.25f,
                new TextureRegion(character, 192, 0, 32, 32),
                new TextureRegion(character, 224, 0, 32, 32));

        mirror_ne = new TextureRegion(baseTiles, 32, 96, 32, 32);
        mirror_es = new TextureRegion(baseTiles, 32, 128 , 32, 32);
        mirror_sw = new TextureRegion(baseTiles, 32, 160 , 32, 32);
        mirror_wn = new TextureRegion(baseTiles, 32, 192, 32, 32);


        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;
        level0 = new TmxMapLoader().load("maps/map0.tmx", params);
        level1 = new TmxMapLoader().load("maps/map1.tmx", params);
    }
}
