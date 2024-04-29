package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.*;

public class Destroyer extends InteractableBaseComponent {
    private final World world;
    private final TextureRegion texture = Assets.destroyer;
    private float posX = 0;
    private float posY = 0;

    public Destroyer(World world, float posX, float posY) {
        super(world, posX, posY);
        this.posX = posX;
        this.posY = posY;
        this.world = world;
        this.createBody(posX * PPM, posY * PPM, 48, 48, true, BIT_DESTROYER, BIT_PROJECTILE, (short) 0);
        setTexture(texture);
    }

    @Override
    public void Interact() {

    }

    @Override
    public void setTexture(TextureRegion texture) {
        super.setTexture(this.texture);
    }

    @Override
    public void setBatchTexture(SpriteBatch batch) {
        if (texture != null) {
            batch.draw(texture, posX * PPM - ((float) Assets.arrow_right.getRegionWidth() / 2), posY * PPM - ((float) Assets.arrow_right.getRegionHeight() / 2));
        }
    }

    @Override
    public void createBody(float x, float y, int width, int height, boolean isStatic, short cBits, short mBits, short gIndex) {
        super.createBody(x, y, width, height, isStatic, cBits, mBits, gIndex);
    }

    @Override
    public Body getBody() {
        return super.getBody();
    }
}
