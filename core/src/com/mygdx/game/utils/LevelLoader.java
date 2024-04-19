package com.mygdx.game.utils;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.manager.MapManager;

public class LevelLoader {
    public static LevelLoader Instance;
    private MapManager mapManager;
    private TiledMap map;
    private World world;

    public LevelLoader(World world) {
        this.world = world;
        Instance = this;
        map = Assets.level0;
        mapManager = new MapManager();
        mapManager.createLevelMap(world, map);
    }

    public void setLevel(int level_id) {
        switch (level_id) {
            case 0:
                map = Assets.level0;
                break;
            case 1:
                map = Assets.level1;
                break;
        }
        mapManager.createLevelMap(world, map);
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public TiledMap getMap() {
        return map;
    }
}
