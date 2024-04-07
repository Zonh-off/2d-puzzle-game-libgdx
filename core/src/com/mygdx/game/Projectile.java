package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.*;

public class Projectile {
    private float posX = 0;
    private float posY = 0;
    private TextureRegion texture;
    private World world;
    private Body body;

    public Projectile(World world, float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        this.world = world;
        body = createBody(posX, posY, 32, 32, false, BIT_PLAYER, BIT_WALL, (short) 0);
    }

    public void update(float deltaTime) {

    }

    public void updateAnimation(SpriteBatch batch, float delta) {
        setBatchTexture(batch, Assets.projectile);
    }

    private void setBatchTexture(SpriteBatch batch, TextureRegion texture) {
        batch.draw(
                texture,
                body.getPosition().x * PPM - ((float) Assets.idle_down_sheet.getRegionWidth() / 2),
                body.getPosition().y * PPM - ((float) Assets.idle_down_sheet.getRegionHeight() / 2));
    }

    private Body createBody(float x, float y, int width, int height, boolean isStatic, short cBits, short mBits, short gIndex) {
        Body pBody;
        BodyDef def = new BodyDef();

        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.2f);

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
