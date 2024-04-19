package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.*;

public class Arrow extends InteractableBaseComponent {
    private float posX = 0;
    private float posY = 0;
    private TextureRegion texture = Assets.arrow_right;
    private World world;
    private int dir = 0;
    private Vector2 vecotrDir = new Vector2(0, 0);

    public Arrow(World world, float posX, float posY, int given_dir) {
        super(world, posX, posY);

        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.dir = given_dir;
        this.createBody(posX * PPM, posY * PPM, 48, 48, true, BIT_ARROW, (short) (BIT_PLAYER | BIT_PROJECTILE), (short) 0);
        setDir(dir);
    }

    public void setDir(int given_dir) {
        if (given_dir > 3) {
            dir = 0;
        } else {
            dir = given_dir;
        }
        switch (dir) {
            case 0:
                texture = Assets.arrow_right;
                break;
            case 1:
                texture = Assets.arrow_down;
                break;
            case 2:
                texture = Assets.arrow_left;
                break;
            case 3:
                texture = Assets.arrow_up;
                break;
        }
        setTexture(texture);
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getDir() {
        return dir;
    }

    @Override
    public Body getBody() {
        return super.getBody();
    }

    @Override
    public void setTexture(TextureRegion texture) {
        super.setTexture(this.texture);
    }

    @Override
    public void setBatchTexture(SpriteBatch batch) {
        super.setBatchTexture(batch);
    }

    @Override
    public void createBody(float x, float y, int width, int height, boolean isStatic, short cBits, short mBits, short gIndex) {
        super.createBody(x, y, width, height, isStatic, cBits, mBits, gIndex);
    }

    @Override
    public void Interact() {
        System.out.println("Interact with mirror");
        setDir(Math.max(0, Math.min(4, dir + 1)));
        System.out.println("Dir: " + dir);
    }
}
