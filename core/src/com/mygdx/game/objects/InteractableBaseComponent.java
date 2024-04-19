package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.PPM;

public abstract class InteractableBaseComponent implements IInteractable {
    private float posX = 0;
    private float posY = 0;
    private World world;
    private Body body;
    private TextureRegion texture;

    InteractableBaseComponent(World world, float posX, float posY) {
        this.posX = posX;
        this.posY = posY;
        this.world = world;
    }

    @Override
    public abstract void Interact();

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    public void setBatchTexture(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, posX * PPM - ((float) Assets.arrow_right.getRegionWidth() / 2), posY * PPM - ((float) Assets.arrow_right.getRegionHeight() / 2));
        }
    }

    public void createBody(float x, float y, int width, int height, boolean isStatic, short cBits, short mBits, short gIndex) {
        BodyDef def = new BodyDef();

        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = false;
        body = world.createBody(def);

        //        PolygonShape shape = new PolygonShape();
        //        shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.001f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = cBits; // Is a
        fixtureDef.filter.maskBits = mBits; // Collide with
        fixtureDef.filter.groupIndex = gIndex;
        body.setUserData(this);
        body.createFixture(fixtureDef).getBody();
        shape.dispose();
    }

    public Body getBody() {
        return body;
    }
}
