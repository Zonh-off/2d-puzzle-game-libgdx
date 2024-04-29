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
    private final HashSet<InteractableBaseComponent> interactableObjectsList = new HashSet<>();
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private World world;

    public void createLevelMap(World world, TiledMap level) {
        this.world = world;
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(level);
        TileMapHelper tileMapHelper = new TileMapHelper(world, level);

        boolean isSpawner = false;

        removeInteractableObjects();

        if (Projectile.Instance != null)
            Projectile.Instance.destroyProjectile();

        TiledMapTileLayer objectsPos = (TiledMapTileLayer) level.getLayers().get("objects");
        TiledMapTileLayer spawnerPos = (TiledMapTileLayer) level.getLayers().get("spawner");
        for (int x = 0; x < level.getProperties().get("width", Integer.class); x++) {
            for (int y = 0; y < level.getProperties().get("height", Integer.class); y++) {
                if (objectsPos.getCell(x, y) == null) continue;
                addInteractableObjects(x + 0.5f, y + 0.5f);
//                objects.setCell(x, y, null);
            }
        }

        for (int x = 0; x < level.getProperties().get("width", Integer.class); x++) {
            for (int y = 0; y < level.getProperties().get("height", Integer.class); y++) {
                if (spawnerPos.getCell(x, y) == null) continue;
                if (!isSpawner) {
                    isSpawner = true;

                    Spawner spawner = new Spawner(world, x + 0.5f, y + 0.5f);
                    interactableObjectsList.add(spawner);
                } else {
                    Destroyer destroyer = new Destroyer(world, x + 0.5f, y + 0.5f);
                    interactableObjectsList.add(destroyer);
                }

//                objects.setCell(x, y, null);
            }
        }
//        int count = 0;
//        for (InteractableBaseComponent v : interactableObjectsList) {
//            count++;
//            System.out.println(count + " Bodies: " + v.getBody());
//        }
        Player.Instance.setPosition(level.getProperties().get("width", Integer.class) / 2 + 0.5f, level.getProperties().get("height", Integer.class) / 2 + 0.5f);
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
