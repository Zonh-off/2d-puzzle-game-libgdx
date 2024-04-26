package com.mygdx.game.manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Player;
import com.mygdx.game.objects.*;
import com.mygdx.game.utils.TileMapHelper;

import java.util.HashSet;
import java.util.Random;

public class MapManager {
    private HashSet<InteractableBaseComponent> interactableObjectsList = new HashSet<>();
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TiledMap map;
    private TileMapHelper tileMapHelper;
    private World world;
    private boolean isSpawner = false;
    private Projectile projectile = null;
    private Spawner spawner = null;
    private Destroyer destroyer = null;

    public void createLevelMap(World world, TiledMap level) {
        this.world = world;
        map = level;
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(map);
        tileMapHelper = new TileMapHelper(world, map);
        isSpawner = false;
        removeInteractableObjects();
        if (Projectile.Instance != null)
            Projectile.Instance.destroyProjectile();
        TiledMapTileLayer objectsPos = (TiledMapTileLayer) map.getLayers().get("objects");
        TiledMapTileLayer spawnerPos = (TiledMapTileLayer) map.getLayers().get("spawner");
        for (int x = 0; x < map.getProperties().get("width", Integer.class); x++) {
            for (int y = 0; y < map.getProperties().get("height", Integer.class); y++) {
                if (objectsPos.getCell(x, y) == null) continue;
                addInteractableObjects(x + 0.5f, y + 0.5f);
//                objects.setCell(x, y, null);
            }
        }
        for (int x = 0; x < map.getProperties().get("width", Integer.class); x++) {
            for (int y = 0; y < map.getProperties().get("height", Integer.class); y++) {
                if (spawnerPos.getCell(x, y) == null) continue;
                if (!isSpawner) {
                    isSpawner = true;
                    spawner = new Spawner(world, x + 0.5f, y + 0.5f);
                    interactableObjectsList.add(spawner);
                } else {
                    destroyer = new Destroyer(world, x + 0.5f, y + 0.5f);
                    interactableObjectsList.add(destroyer);
                }

//                objects.setCell(x, y, null);
            }
        }
        int count = 0;
        for (InteractableBaseComponent v : interactableObjectsList) {
            count++;
//            System.out.println(count + " Bodies: " + v.getBody());
        }
        Player.Instance.setPosition(map.getProperties().get("width", Integer.class) / 2 + 0.5f, map.getProperties().get("height", Integer.class) / 2 + 0.5f);
    }

    public OrthogonalTiledMapRenderer orthogonalTiledMapRenderer() {
        return orthogonalTiledMapRenderer;
    }

    public void addInteractableObjects(float posX, float posY) {
        Random rand = new Random();
        Arrow newInteractableObject = new Arrow(world, posX, posY, rand.nextInt(0, 3));

        interactableObjectsList.add(newInteractableObject);
    }

    public void removeInteractableObjects() {
        for (InteractableBaseComponent v : interactableObjectsList) {
            world.destroyBody(v.getBody());
//            System.out.println("Bodies: " + v.getBody());
        }
        interactableObjectsList.clear();
    }

    public void drawInteractableObjects(SpriteBatch batch) {
        for (InteractableBaseComponent v : interactableObjectsList) {
            v.setBatchTexture(batch);
        }
    }
}
