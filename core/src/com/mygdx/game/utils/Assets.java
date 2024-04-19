package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    // Character
    public static Texture character;
    public static Texture baseTiles;
    public static Texture logo;
    public static Texture background;
    public static TextureRegion idle_down_sheet;
    public static Animation idle_right_anim;
    public static Animation idle_left_anim;
    public static Animation walk_right_anim;
    public static Animation walk_left_anim;
    public static Animation die_anim;
    public static Animation shadow_anim;

    // Mirrors
    public static TextureRegion arrow_right;
    public static TextureRegion arrow_down;
    public static TextureRegion arrow_left;
    public static TextureRegion arrow_up;

    // Projectile
    public static TextureRegion projectile;
    public static TextureRegion spawner;
    public static TextureRegion destroyer;

    public static TextureRegion splashLogo;
    public static TextureRegion backgroundMenu;

    // Particles
    public static ParticleEffect rainEffect;

    // Map
    public static TiledMap level0;
    public static TiledMap level1;

    public static Skin menuSkin;

    public static Texture loadTexture(String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static Skin loadSkin(String file) {
        return new Skin(Gdx.files.internal(file));
    }

    public static void load() {
        character = loadTexture("AnimationSheet_Character.png");
        baseTiles = loadTexture("maps/basetiles.png");
        logo = loadTexture("logo.png");
        background = loadTexture("skin/background.png");
        menuSkin = loadSkin("skin/skin.json");

        //rainAtlas = new TextureAtlas().load(Gdx.files.internal("particles/rain.png"));
        rainEffect = new ParticleEffect();
        rainEffect.load(Gdx.files.internal("particles/rain.p"), Gdx.files.internal("particles/"));

        splashLogo = new TextureRegion(logo, 0, 0, 1024, 1024);
        backgroundMenu = new TextureRegion(background, 0, 0, 300, 164);

        idle_down_sheet = new TextureRegion(character, 0, 0, 32, 32);

        projectile = new TextureRegion(baseTiles, 96, 96, 32, 32);
        spawner = new TextureRegion(baseTiles, 96, 160, 32, 32);
        destroyer = new TextureRegion(baseTiles, 64, 192, 32, 32);

        idle_right_anim = new Animation(0.25f,
                new TextureRegion(character, 0, 0, 32, 32),
                new TextureRegion(character, 32, 0, 32, 32));
        idle_left_anim = new Animation(0.25f,
                new TextureRegion(character, 64, 0, 32, 32),
                new TextureRegion(character, 96, 0, 32, 32));
        walk_right_anim = new Animation(0.1f,
                new TextureRegion(character, 0, 64, 32, 32),
                new TextureRegion(character, 32, 64, 32, 32),
                new TextureRegion(character, 64, 64, 32, 32),
                new TextureRegion(character, 96, 64, 32, 32));
        walk_left_anim = new Animation(0.1f,
                new TextureRegion(character, 0, 96, 32, 32),
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

        arrow_right = new TextureRegion(baseTiles, 32, 96, 32, 32);
        arrow_down = new TextureRegion(baseTiles, 32, 128, 32, 32);
        arrow_left = new TextureRegion(baseTiles, 32, 160, 32, 32);
        arrow_up = new TextureRegion(baseTiles, 32, 192, 32, 32);


        TmxMapLoader.Parameters params = new TmxMapLoader.Parameters();
        params.textureMinFilter = Texture.TextureFilter.Nearest;
        params.textureMagFilter = Texture.TextureFilter.Nearest;
        params.generateMipMaps = true;
        level0 = new TmxMapLoader().load("maps/map0.tmx", params);
        level1 = new TmxMapLoader().load("maps/map1.tmx", params);
    }
}
