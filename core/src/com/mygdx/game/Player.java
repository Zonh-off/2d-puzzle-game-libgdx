package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Assets;

import java.util.Objects;

import static com.mygdx.game.utils.Constants.*;
import static com.mygdx.game.utils.Constants.PPM;

public class Player {
    private World world;
    private Body bPlayer;

    private float speed = 5f;
    private Vector2 vel = new Vector2(0, 0);
    private Vector2 lastVel = new Vector2(1, 0);
    private float idleStateTime = 1;
    private float walkStateTime = 4;
    private float zSpeed = 0;
    private float jumpSpeed = -.1f;
    private float gameGravity = .2f;
    private float z = 0;
    private float zFloor = 0;

    private float jumpZ;
    private boolean isJumping;

    public Player(Vector2 startPosition, World world) {
        super();
        this.world = world;

        bPlayer = createBody(startPosition.x, startPosition.y, 32, 32, false, BIT_PLAYER, BIT_WALL, (short) 0);
    }

    public void update(float deltaTime) {
        handleInput(deltaTime);
    }

    public void updateAnimation(float delta, SpriteBatch batch) {
        walkStateTime -= delta;
        if(walkStateTime < 0)
            walkStateTime = 4;

        idleStateTime -= delta;
        if(idleStateTime < 0)
            idleStateTime = 1;

        if(vel.x > 0) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_right_anim.getKeyFrame(walkStateTime, true));
        }
        if(vel.x < 0){
            setBatchTexture(batch, (TextureRegion) Assets.walk_left_anim.getKeyFrame(walkStateTime, true));
        }
        if(vel.y > 0 && lastVel.x > 0 ) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_right_anim.getKeyFrame(walkStateTime, true));
        }
        if(vel.y > 0 && lastVel.x < 0 ) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_left_anim.getKeyFrame(walkStateTime, true));
        }
        if(vel.y < 0 && lastVel.x > 0 ) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_right_anim.getKeyFrame(walkStateTime, true));
        }
        if(vel.y < 0 && lastVel.x < 0 ) {
            setBatchTexture(batch, (TextureRegion) Assets.walk_left_anim.getKeyFrame(walkStateTime, true));
        }
        if(lastVel.x > 0 && vel.isZero()) {
            setBatchTexture(batch, (TextureRegion) Assets.idle_right_anim.getKeyFrame(walkStateTime, true));
        }
        if(lastVel.x < 0 && vel.isZero()) {
            setBatchTexture(batch, (TextureRegion) Assets.idle_left_anim.getKeyFrame(walkStateTime, true));
        }
    }

    private void setBatchTexture(SpriteBatch batch, TextureRegion texture) {
        batch.draw(
                texture,
                GetPosition().x * PPM - ((float) Assets.idle_down_sheet.getRegionWidth() / 2),
                GetPosition().y * PPM - ((float) Assets.idle_down_sheet.getRegionHeight() / 2));
    }

    public Vector2 GetPosition() {
        return bPlayer.getPosition();
    }

    private void handleInput(float deltaTime) {
        vel.x = 0;
        vel.y = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            vel.x += 1;
            lastVel.x = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            vel.x -= 1;
            lastVel.x = -1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            vel.y += 1;
            lastVel.y = 1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            vel.y -= 1;
            lastVel.y = -1;
        }

        if(Objects.equals(vel, new Vector2(1, 1)))
            vel = new Vector2(ONE_ON_ROOT_TWO, ONE_ON_ROOT_TWO);
        if(Objects.equals(vel, new Vector2(-1, -1)))
            vel = new Vector2(-ONE_ON_ROOT_TWO, -ONE_ON_ROOT_TWO);
        if(Objects.equals(vel, new Vector2(-1, 1)))
            vel = new Vector2(-ONE_ON_ROOT_TWO, ONE_ON_ROOT_TWO);
        if(Objects.equals(vel, new Vector2(1, -1)))
            vel = new Vector2(ONE_ON_ROOT_TWO, -ONE_ON_ROOT_TWO);

/*        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && zSpeed == 0){
            isJumping = true;
            jumpZ = bPlayer.getPosition().y;
            zSpeed = jumpSpeed;
        }
        if(z != zFloor){
            zSpeed += gameGravity * deltaTime;
        }
        if(z + zSpeed > zFloor){
            zSpeed = 0;
            z = zFloor;
            isJumping = false;
        }
        z += zSpeed;

        if(isJumping){
            bPlayer.setTransform(bPlayer.getPosition().x,  jumpZ - z,0);
        } else {
            bPlayer.setLinearVelocity(vel.x * speed, vel.y * speed);
        }
*/
        bPlayer.setLinearVelocity(vel.x * speed, vel.y * speed);

        System.out.println(bPlayer.getPosition() + " : " + z);
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

/*    private void handleInput(float deltaTime) {
        int horizontalForce = 0;
        int verticalForce = 0;

        if(Gdx.input.isKeyPressed(Input.Keys.L)){
            horizontalForce += 1;
            posX += playerSpeed * deltaTime;
            characterSheet = new Texture("IdleRight.png");
            characterFrame = new TextureRegion(characterSheet, 32, 32);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.J)){
            horizontalForce -= 1;
            posX -= playerSpeed * deltaTime;
            characterSheet = new Texture("IdleLeft.png");
            characterFrame = new TextureRegion(characterSheet, 32, 32);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.I)){
            verticalForce += 1;
            posY += playerSpeed * deltaTime;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.K)){
            verticalForce -= 1;
            posY -= playerSpeed * deltaTime;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && zSpeed == 0){
            zSpeed = jumpSpeed;
        }
        if(z != zFloor){
            zSpeed += gameGravity * deltaTime;
        }
        if(z + zSpeed > zFloor){
            zSpeed = 0;
            z = zFloor;
        }
        z += zSpeed;

        System.out.println(z);
        //player.setLinearVelocity(horizontalForce * playerSpeed, (verticalForce) * playerSpeed);
        player.setTransform(posX,posY - z,0);
        shadow.setTransform(posX, posY + zFloor, 0);
    }*/
