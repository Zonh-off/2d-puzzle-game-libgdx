package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.TileMapHelper;

import java.awt.*;
import java.util.HashSet;
import java.util.Random;

public class MapManager {
    private HashSet<MirrorBox> mirrorBoxesList = new HashSet<MirrorBox>();
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMap map;
    private TileMapHelper tileMapHelper;


    public void createLevelMap(World world, TiledMap level) {
        map = level;
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        tileMapHelper = new TileMapHelper(world, map);
        removeMirrorObjects();

        TiledMapTileLayer objects = (TiledMapTileLayer)map.getLayers().get("Objects");
        for(int x = 0; x < map.getProperties().get("width", Integer.class); x++) {
            for(int y = 0; y < map.getProperties().get("height", Integer.class); y++) {
                if(objects.getCell(x, y) == null) continue;
                System.out.println(objects.getCell(x, y));
                addMirrorObjects(x + 0.5f, y + 0.5f);
//                objects.setCell(x, y, null);
            }
        }
    }

    public OrthogonalTiledMapRenderer orthogonalTiledMapRenderer() {
        return orthogonalTiledMapRenderer;
    }

    public void addMirrorObjects(float posX, float posY) {
        Random rand = new Random();
        MirrorBox newMirrorObject = new MirrorBox(posX, posY, rand.nextInt(0, 3));
        mirrorBoxesList.add(newMirrorObject);
    }

    public void removeMirrorObjects() {
        mirrorBoxesList.clear();
    }

    public void drawMirrorObjects(SpriteBatch batch) {
        for(MirrorBox v : mirrorBoxesList) {
            v.setBatchTexture(batch);
        }
    }
}
