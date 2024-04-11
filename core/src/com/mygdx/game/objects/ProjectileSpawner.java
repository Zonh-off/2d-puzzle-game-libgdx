package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.*;

public class ProjectileSpawner extends InteractableBaseComponent {
    private TextureRegion texture = Assets.projectileSpawner;

    public ProjectileSpawner(World world, float posX, float posY) {
        super(world, posX, posY);
        createBody(posX * PPM, posY * PPM, 48, 48, true, BIT_WALL, BIT_PLAYER, (short) 0);
    }

    @Override
    public void Interact() {
        System.out.println("Interact with spawner");
    }

    @Override
    public void setTexture(TextureRegion texture) {
        super.setTexture(this.texture);
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
