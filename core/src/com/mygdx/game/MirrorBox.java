package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.*;

public class MirrorBox {
    private float posX = 0;
    private float posY = 0;
    private TextureRegion texture;
    private World world;
    private Body body;
    private int dir = 0;

    public MirrorBox(World world, float posX, float posY, int given_dir) {
        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.dir = given_dir;
        texture = Assets.mirror_ne;
        body = createBody(posX * PPM, posY * PPM, 48, 48, true, BIT_WALL, BIT_PLAYER, (short) 0);
        setState(dir);
    }

    public void setState(int given_dir) {
        if (given_dir > 3) {
            dir = 0;
        } else {
            dir = given_dir;
        }
        switch (dir) {
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

    public int getRotation_id() {
        return dir;
    }

    public Body getBody() {
        return body;
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

        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;
        pBody = world.createBody(def);

//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.4f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = cBits; // Is a
        fixtureDef.filter.maskBits = mBits; // Collide with
        fixtureDef.filter.groupIndex = gIndex;
        pBody.setUserData(this);
        pBody.createFixture(fixtureDef).getBody();
        shape.dispose();

        return pBody;
    }
}
