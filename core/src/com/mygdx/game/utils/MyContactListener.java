package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.Player;
import com.mygdx.game.objects.Arrow;
import com.mygdx.game.objects.Destroyer;
import com.mygdx.game.objects.IInteractable;
import com.mygdx.game.objects.Projectile;

public class MyContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        System.out.println("FA: " + fixtureA.getBody().getUserData());
        System.out.println("FB: " + fixtureB.getBody().getUserData());

        int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            case Constants.BIT_PLAYER | Constants.BIT_ARROW:
            case Constants.BIT_PLAYER | Constants.BIT_SPAWNER:
                if (fixtureA.getFilterData().categoryBits == Constants.BIT_PLAYER) {
                    if (Player.Instance.getInteractableObject() == null) {
                        Player.Instance.setInteractableObject((IInteractable) fixtureB.getBody().getUserData());
                        Player.Instance.handleInteract();
                    }
                } else {
                    if (fixtureA.getFilterData().categoryBits == Constants.BIT_PLAYER) {
                        if (Player.Instance.getInteractableObject() == null) {
                            Player.Instance.setInteractableObject((IInteractable) fixtureB.getBody().getUserData());
                            Player.Instance.handleInteract();
                        }
                    }
                }
                break;
            case Constants.BIT_PROJECTILE | Constants.BIT_ARROW:
                Arrow arrow = (Arrow) fixtureA.getBody().getUserData();
                if (arrow != null) {
                    System.out.println("DIR: " + arrow.getDir());
                    Projectile.Instance.setArrow((Arrow) fixtureA.getBody().getUserData());
                    Projectile.Instance.setDir(Projectile.Instance.getArrow().getDir());
                }
                break;
            case Constants.BIT_PROJECTILE | Constants.BIT_WALL:
                if (Projectile.Instance != null)
                    Projectile.Instance.destroyProjectile();
                break;
            case Constants.BIT_PROJECTILE | Constants.BIT_DESTROYER:
                Destroyer destroyer = (Destroyer) fixtureA.getBody().getUserData();
                if (destroyer != null) {
                    Projectile.Instance.destroyProjectile();
//                    levelLoader.setLevel(1);
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Player.Instance.setInteractableObject(null);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Handle post-solve if needed
    }
}
