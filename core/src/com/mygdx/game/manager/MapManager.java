package com.mygdx.game.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.objects.InteractableBaseComponent;
import com.mygdx.game.objects.MirrorBox;
import com.mygdx.game.Player;
import com.mygdx.game.objects.ProjectileSpawner;
import com.mygdx.game.utils.TileMapHelper;

import java.util.HashSet;
import java.util.Random;

public class MapManager {
    private HashSet<InteractableBaseComponent> interactableObjectsList = new HashSet<InteractableBaseComponent>();
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMap map;
    private TileMapHelper tileMapHelper;
    private World world;
    private boolean isSpawner = false;

    public void createLevelMap(World world, TiledMap level) {
        this.world = world;
        map = level;
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        tileMapHelper = new TileMapHelper(world, map);
        removeInteractableObjects();
        TiledMapTileLayer objects = (TiledMapTileLayer) map.getLayers().get("objects");
        TiledMapTileLayer startEndPoints = (TiledMapTileLayer) map.getLayers().get("startEndPoints");
        for (int x = 0; x < map.getProperties().get("width", Integer.class); x++) {
            for (int y = 0; y < map.getProperties().get("height", Integer.class); y++) {
                if (objects.getCell(x, y) == null) continue;
                System.out.println(objects.getCell(x, y));
                addInteractableObjects(x + 0.5f, y + 0.5f);
//                objects.setCell(x, y, null);
            }
        }
        for (int x = 0; x < map.getProperties().get("width", Integer.class); x++) {
            for (int y = 0; y < map.getProperties().get("height", Integer.class); y++) {
                if (startEndPoints.getCell(x, y) == null) continue;
                System.out.println(startEndPoints.getCell(x, y));
                InteractableBaseComponent newInteractableObject = new ProjectileSpawner(world, x + 0.5f, y + 0.5f);
                interactableObjectsList.add(newInteractableObject);
//                objects.setCell(x, y, null);
            }
        }
        Player.Instance.setPosition(map.getProperties().get("width", Integer.class) / 2, map.getProperties().get("height", Integer.class) / 2);
    }

    public OrthogonalTiledMapRenderer orthogonalTiledMapRenderer() {
        return orthogonalTiledMapRenderer;
    }

    public void addInteractableObjects(float posX, float posY) {
        Random rand = new Random();
        InteractableBaseComponent newInteractableObject = new MirrorBox(world, posX, posY, rand.nextInt(0, 3));

        interactableObjectsList.add(newInteractableObject);
    }

    public void removeInteractableObjects() {
            for (InteractableBaseComponent v : interactableObjectsList) {
                world.destroyBody(v.getBody());
                System.out.println("Bodies: " + v.getBody());
            }
            interactableObjectsList.clear();
    }

    public void drawInteractableObjects(SpriteBatch batch) {
        for (InteractableBaseComponent v : interactableObjectsList) {
            v.setBatchTexture(batch);
        }
    }
}
