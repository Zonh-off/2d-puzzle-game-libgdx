package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.utils.Constants.*;

public class TileMapHelper {
    private TiledMap tiledMap;
    private World world;

    public TileMapHelper(World world, TiledMap tiledMap) {
        this.world = world;
        this.tiledMap = tiledMap;
        parseTiledObjectLayer(tiledMap.getLayers().get("colliders").getObjects());
    }

    public void parseTiledObjectLayer(MapObjects mapObjects){
        for(MapObject mapObject : mapObjects){
            if(mapObject instanceof PolygonMapObject){
                createStaticBody(world, (PolygonMapObject) mapObject, BIT_WALL, BIT_PLAYER, (short) 0);
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

    private void createStaticBody(World world, PolygonMapObject polygonMapObject, short cBits, short mBits, short gIndex) {
        Body body;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);
        Shape shape = createPolygonShape(polygonMapObject);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.filter.categoryBits = cBits; // Is a
        fixtureDef.filter.maskBits = mBits; // Collide with
        fixtureDef.filter.groupIndex = gIndex;

        body.createFixture(fixtureDef).getBody();
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
