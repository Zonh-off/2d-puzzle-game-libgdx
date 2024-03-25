package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.utils.Assets;
import com.sun.jdi.Mirror;

import static com.mygdx.game.utils.Constants.PPM;

public class MirrorBox {
    private float posX = 0;
    private float posY = 0;
    private TextureRegion texture;

    MirrorBox(float posX, float posY, int rotation_id) {
        this.posX = posX;
        this.posY = posY;
        setState(rotation_id);
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

    public void setBatchTexture(SpriteBatch batch) {
        batch.draw(
                texture,
                posX * PPM - ((float) Assets.mirror_ne.getRegionWidth() / 2),
                posY * PPM - ((float) Assets.mirror_ne.getRegionHeight() / 2)
        );
    }
}
