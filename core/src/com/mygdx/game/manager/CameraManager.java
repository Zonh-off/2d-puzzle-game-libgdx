package com.mygdx.game.manager;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Player;
import com.mygdx.game.utils.Assets;

import static com.mygdx.game.utils.Constants.PPM;

public class CameraManager {
    public static CameraManager Instance;

    private final OrthographicCamera camera;
    private float speed = 2.5f;
    private boolean debugMode = false;

    public CameraManager() {
        super();
        Instance = this;
        camera = new OrthographicCamera();
    }

    public void update(float delta) {
        handleCamera(delta);
    }

    public void handleCamera(float delta) {
        Vector3 position = camera.position;
        if (debugMode) {
            position.x = camera.position.x + (Player.Instance.getPosition().x * PPM - camera.position.x) * .2f;
            position.y = camera.position.y + (Player.Instance.getPosition().y * PPM - camera.position.y) * .2f;
        } else {
            position.x = Assets.level0.getProperties().get("width", Integer.class) * 32 / 2;
            position.y = Assets.level0.getProperties().get("height", Integer.class) * 32 / 2;
        }
        camera.position.set(new Vector3(position.x / PPM - 0.5f, position.y / PPM - 0.5f, camera.position.z));
        camera.update();
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}