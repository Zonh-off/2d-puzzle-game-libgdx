package com.mygdx.game.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MirrorBox;
import com.mygdx.game.Player;
import com.mygdx.game.utils.TileMapHelper;

import java.util.HashSet;
import java.util.Random;

public class MapManager {
    private HashSet<MirrorBox> mirrorBoxesList = new HashSet<MirrorBox>();
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMap map;
    private TileMapHelper tileMapHelper;
    private World world;

    public void createLevelMap(World world, TiledMap level) {
        this.world = world;
        map = level;
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        tileMapHelper = new TileMapHelper(world, map);
        removeMirrorObjects();
        TiledMapTileLayer objects = (TiledMapTileLayer) map.getLayers().get("objects");
        TiledMapTileLayer startEndPoints = (TiledMapTileLayer) map.getLayers().get("startEndPoints");
        for (int x = 0; x < map.getProperties().get("width", Integer.class); x++) {
            for (int y = 0; y < map.getProperties().get("height", Integer.class); y++) {
                if (objects.getCell(x, y) == null) continue;
                System.out.println(objects.getCell(x, y));
                addMirrorObjects(x + 0.5f, y + 0.5f);
//                objects.setCell(x, y, null);
            }
        }
        Player.Instance.setPosition(map.getProperties().get("width", Integer.class) / 2, map.getProperties().get("height", Integer.class) / 2);
    }

    public OrthogonalTiledMapRenderer orthogonalTiledMapRenderer() {
        return orthogonalTiledMapRenderer;
    }

    public void addMirrorObjects(float posX, float posY) {
        Random rand = new Random();
        MirrorBox newMirrorObject = new MirrorBox(world, posX, posY, rand.nextInt(0, 3));

        mirrorBoxesList.add(newMirrorObject);
    }

    public void removeMirrorObjects() {
        for (MirrorBox v : mirrorBoxesList) {
            world.destroyBody(v.getBody());
            System.out.println("Bodies: " + v.getBody());
        }
        mirrorBoxesList.clear();
    }

    public void drawMirrorObjects(SpriteBatch batch) {
        for (MirrorBox v : mirrorBoxesList) {
            v.setBatchTexture(batch);
        }
    }
}
