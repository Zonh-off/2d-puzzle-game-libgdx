package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.*;

public class MirrorBox extends InteractableBaseComponent {
    private float posX = 0;
    private float posY = 0;
    private TextureRegion texture = Assets.mirror_ne;
    private World world;
    private int dir = 0;

    public MirrorBox(World world, float posX, float posY, int given_dir) {
        super(world, posX, posY);

        this.world = world;
        this.posX = posX;
        this.posY = posY;
        this.dir = given_dir;
        createBody(posX * PPM, posY * PPM, 48, 48, true, BIT_WALL, BIT_PLAYER, (short) 0);
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

    @Override
    public Body getBody() {
        return super.getBody();
    }

    @Override
    public void setTexture(TextureRegion texture) {
        super.setTexture(this.texture);
    }
    public void setBatchTexture(SpriteBatch batch) {
        if(texture != null) {
            batch.draw(texture, posX * PPM - ((float) Assets.mirror_ne.getRegionWidth() / 2), posY * PPM - ((float) Assets.mirror_ne.getRegionHeight() / 2));
        }
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
