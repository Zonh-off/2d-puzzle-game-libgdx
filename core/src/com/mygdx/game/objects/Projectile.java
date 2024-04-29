package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Assets;
import com.mygdx.game.utils.MyContactListener;

import static com.mygdx.game.utils.Constants.*;

public class Projectile extends MyContactListener {
    private final TextureRegion texture = Assets.projectile;
    private final World world;
    public static Projectile Instance;
    private float posX = 0;
    private float posY = 0;
    private final float velPower = 2;
    private Body body;
    private Arrow arrow = null;

    public Projectile(World world, float posX, float posY) {
        Instance = this;
        this.posX = posX;
        this.posY = posY;
        this.world = world;
        body = createBody(posX, posY, 32, 32, false, BIT_PROJECTILE, (short) (BIT_ARROW | BIT_DESTROYER | BIT_WALL), (short) 0);
    }

    public void update(float deltaTime) {

    }

    public void updateAnimation(SpriteBatch batch) {
        setBatchTexture(batch);
    }

    public void setDir(int dir) {
        switch (dir) {
            case 0:
                body.setLinearVelocity(1 * velPower, 0 * velPower);
                break;
            case 1:
                body.setLinearVelocity(0 * velPower, -1 * velPower);
                break;
            case 2:
                body.setLinearVelocity(-1 * velPower, 0 * velPower);
                break;
            case 3:
                body.setLinearVelocity(0 * velPower, 1 * velPower);
                break;
        }
    }

    public void destroyProjectile() {
        if (Instance != null) {
            Instance = null;
            System.out.println("destory");
        }
    }

    public void setBatchTexture(SpriteBatch batch) {
        batch.draw(
                texture,
                body.getPosition().x * PPM - ((float) Assets.idle_down_sheet.getRegionWidth() / 2),
                body.getPosition().y * PPM - ((float) Assets.idle_down_sheet.getRegionHeight() / 2));
    }

    private Body createBody(float x, float y, int width, int height, boolean isStatic, short cBits, short mBits, short gIndex) {
        BodyDef def = new BodyDef();

        if (isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        body = world.createBody(def);

        //        PolygonShape shape = new PolygonShape();
        //        shape.setAsBox((float) width / 2 / PPM, (float) height / 2 / PPM);

        CircleShape shape = new CircleShape();
        shape.setRadius(0.05f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.filter.categoryBits = cBits; // Is a
        fixtureDef.filter.maskBits = mBits; // Collide with
        fixtureDef.filter.groupIndex = gIndex;

        body.setUserData("Projectile");
        body.createFixture(fixtureDef).getBody();
        shape.dispose();

        return body;
    }

    public Arrow getArrow() {
        return arrow;
    }

    public void setArrow(Arrow arrow) {
        this.arrow = arrow;
    }
}
