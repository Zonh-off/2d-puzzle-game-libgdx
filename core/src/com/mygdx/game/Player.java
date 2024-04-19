package com.mygdx.game;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.objects.IInteractable;
import com.mygdx.game.utils.Assets;

import java.util.Objects;

import static com.mygdx.game.utils.Constants.*;

public class Player {
    public static Player Instance;

    private World world;
    private Body bPlayer;

    private float speed = 5f; // 2.5
    private Vector2 vel = new Vector2(0, 0);
    private Vector2 lastVel = new Vector2(0, 0);
    private int lookingDir = 0;
    private float idleStateTime = 1;
    private float walkStateTime = 4;
    private RayHandler rayHandler;
    private PointLight pointLight;
    private IInteractable interactableObject = null;

    public Player(Vector2 startPosition, World world) {
        super();
        this.world = world;

        Instance = this;

        rayHandler = new RayHandler(world);
        pointLight = new PointLight(rayHandler, 12, new Color(1, 1, 1, 0.4f), 2, 0, 0);
        pointLight.setSoftnessLength(0f);
        rayHandler.setBlur(true);
        rayHandler.setBlurNum(10);
        rayHandler.setAmbientLight(.8f);
        pointLight.attachToBody(bPlayer);

        bPlayer = createBody(startPosition.x, startPosition.y, 32, 32, false, BIT_PLAYER, (short) (BIT_WALL | BIT_ARROW | BIT_SPAWNER), (short) 0);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
        handleInteract();
//        System.out.println("Last Velocity: " + lookingDir);
    }

    public void updateAnimation(float delta, SpriteBatch batch) {
        walkStateTime -= delta;
        if (walkStateTime < 0)
            walkStateTime = 4;

        idleStateTime -= delta;
        if (idleStateTime < 0)
            idleStateTime = 1;

        setBatchTexture(batch, (TextureRegion) Assets.shadow_anim.getKeyFrame(idleStateTime, true), true);

        if (vel.x > 0) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_right_anim.getKeyFrame(walkStateTime, true), false);
            lookingDir = 0;
        }
        if (vel.x < 0) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_left_anim.getKeyFrame(walkStateTime, true), false);
            lookingDir = 2;
        }
        if (vel.y > 0 && lastVel.x > 0) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_right_anim.getKeyFrame(walkStateTime, true), false);
            lookingDir = 3;
        }
        if (vel.y > 0 && lastVel.x < 0) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_left_anim.getKeyFrame(walkStateTime, true), false);
            lookingDir = 3;
        }
        if (vel.y < 0 && lastVel.x > 0) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_right_anim.getKeyFrame(walkStateTime, true), false);
            lookingDir = 1;
        }
        if (vel.y < 0 && lastVel.x < 0) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_left_anim.getKeyFrame(walkStateTime, true), false);
            lookingDir = 1;
        }
        if (lastVel.x > 0 && vel.isZero()) {
            setBatchTexture(batch, (TextureRegion) Assets.idle_right_anim.getKeyFrame(idleStateTime, true), false);
        }
        if (lastVel.x < 0 && vel.isZero()) {
            setBatchTexture(batch, (TextureRegion) Assets.idle_left_anim.getKeyFrame(idleStateTime, true), false);
        }
    }

    // set texture for player
    private void setBatchTexture(SpriteBatch batch, TextureRegion texture, boolean isBottomTexture) {
        if (!isBottomTexture) {
            batch.draw(
                    texture,
                    getPosition().x * PPM - ((float) Assets.idle_down_sheet.getRegionWidth() / 2),
                    getPosition().y * PPM - ((float) Assets.idle_down_sheet.getRegionHeight() / 2));
        } else {
            batch.draw(
                    texture,
                    getPosition().x * PPM - ((float) Assets.idle_down_sheet.getRegionWidth() / 2),
                    getPosition().y * PPM - ((float) Assets.idle_down_sheet.getRegionHeight()));
        }
    }

    // rain effect
    public void drawRainEffect(SpriteBatch batch, float delta) {
        Assets.rainEffect.start();
        Assets.rainEffect.setPosition(getPosition().x, getPosition().y + 1080f);
        Assets.rainEffect.draw(batch, delta);
    }

    public Vector2 getPosition() {
        return bPlayer.getPosition();
    }

    public int getLookingDir() {
        return lookingDir;
    }

    public RayHandler GetRaycastHandler() {
        return rayHandler;
    }

    public void setPosition(float posX, float posY) {
        bPlayer.setTransform(posX, posY, 0);
    }

    // player movement
    private void handleInput(float deltaTime) {
        vel.x = 0;
        vel.y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            vel.x += 1;
            lastVel.x = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            vel.x -= 1;
            lastVel.x = -1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            vel.y += 1;
            lastVel.y = 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            vel.y -= 1;
            lastVel.y = -1;
        }

        if (Objects.equals(vel, new Vector2(1, 1)))
            vel = new Vector2(ONE_ON_ROOT_TWO, ONE_ON_ROOT_TWO);
        if (Objects.equals(vel, new Vector2(-1, -1)))
            vel = new Vector2(-ONE_ON_ROOT_TWO, -ONE_ON_ROOT_TWO);
        if (Objects.equals(vel, new Vector2(-1, 1)))
            vel = new Vector2(-ONE_ON_ROOT_TWO, ONE_ON_ROOT_TWO);
        if (Objects.equals(vel, new Vector2(1, -1)))
            vel = new Vector2(ONE_ON_ROOT_TWO, -ONE_ON_ROOT_TWO);

        pointLight.setPosition(bPlayer.getPosition());
        bPlayer.setLinearVelocity(vel.x * speed, vel.y * speed);
    }

    // interacting with objects
    public void handleInteract() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && interactableObject != null) {
            System.out.println("Interact!");
            interactableObject.Interact();
        }
    }

    // create player body
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
        shape.setRadius(.7f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 2f;
        fixtureDef.filter.categoryBits = cBits; // Is a
        fixtureDef.filter.maskBits = mBits; // Collide with
        fixtureDef.filter.groupIndex = gIndex;

        pBody.setUserData("Player");
        pBody.createFixture(fixtureDef).getBody();
        shape.dispose();

        return pBody;
    }

    public IInteractable getInteractableObject() {
        return interactableObject;
    }

    public void setInteractableObject(IInteractable interactableObject) {
        this.interactableObject = interactableObject;
    }
}