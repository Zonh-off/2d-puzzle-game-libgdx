package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class TileMapHelper {
    private TiledMap tiledMap;
    private World world;

    public TileMapHelper(World world, TiledMap tiledMap) {
        this.world = world;
        this.tiledMap = tiledMap;
        parseTiledObjectLayer(tiledMap.getLayers().get("objects").getObjects());
    }

    public void parseTiledObjectLayer(MapObjects mapObjects){
        for(MapObject mapObject : mapObjects){
            if(mapObject instanceof PolygonMapObject){
                createStaticBody(world, (PolygonMapObject) mapObject);
            }
        }
    }

    private void createStaticBody(World world, PolygonMapObject polygonMapObject) {
        Body body;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1000f);
        shape.dispose();
    }

    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        for(int i = 0; i < vertices.length / 2; i++){
            worldVertices[i] = new Vector2(vertices[i * 2] / Constants.PPM, vertices[i * 2 + 1] / Constants.PPM);
        }
        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);
        return shape;
    }
}
