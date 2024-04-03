package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Assets;
import com.sun.jdi.Mirror;

import static com.mygdx.game.utils.Constants.*;

public class MirrorBox {
    private float posX = 0;
    private float posY = 0;
    private TextureRegion texture;
    private World world;
    private Body body;

    public MirrorBox(World world, float posX, float posY, int rotation_id) {
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        setState(rotation_id);
        createBody(posX * PPM, posY * PPM, 64, 64, true, BIT_WALL, BIT_WALL, (short) 0);
    }

    public void setState(int rotation_id) {
        switch(rotation_id) {
            case 0:
                texture = Assets.mirror_ne;
                break;
            case 1:
                texture = Assets.mirror_es;
                break;
            case 2:
                texture = Assets.mirror_sw;
                break;
            case 3:
                texture = Assets.mirror_wn;
                break;
        }
    }

    public Body getBody(){
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setBatchTexture(SpriteBatch batch) {
        batch.draw(
                texture,
                posX * PPM - ((float) Assets.mirror_ne.getRegionWidth() / 2),
                posY * PPM - ((float) Assets.mirror_ne.getRegionHeight() / 2)
        );
    }

    private Body createBody(float x, float y, int width, int height, boolean isStatic, short cBits, short mBits, short gIndex) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.filter.categoryBits = cBits; // Is a
        fixtureDef.filter.maskBits = mBits; // Collide with
        fixtureDef.filter.groupIndex = gIndex;

        pBody.createFixture(fixtureDef).getBody();
        shape.dispose();

        return pBody;
    }
}
