package com.mygdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Player;
import com.mygdx.game.Projectile;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.*;

public class Spawner extends InteractableBaseComponent {
    private float posX = 0;
    private float posY = 0;
    private World world;

    private TextureRegion texture = Assets.spawner;

    public Spawner(World world, float posX, float posY) {
        super(world, posX, posY);
        this.posX = posX;
        this.posY = posY;
        this.world = world;
        this.createBody(posX * PPM, posY * PPM, 48, 48, true, BIT_SPAWNER, BIT_PLAYER, (short) 0);
        setTexture(texture);
    }

    @Override
    public void Interact() {
        if (Projectile.Instance == null) {
            System.out.println("Interact with spawner");
            new Projectile(world, posX * 32, posY * 32);
//            world.setContactListener(Projectile.Instance);

            if (Projectile.Instance != null)
                Projectile.Instance.setDir(Player.Instance.getLookingDir());

        }
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
