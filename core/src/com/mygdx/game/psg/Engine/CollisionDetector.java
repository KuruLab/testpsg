package com.mygdx.game.psg.Engine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionDetector implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getBody().isBullet()!= contact.getFixtureB().getBody().isBullet()){



        }

        if(contact.getFixtureA().getBody().isBullet() == true) {}

        if(contact.getFixtureB().getBody().isBullet() == true) {}

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
