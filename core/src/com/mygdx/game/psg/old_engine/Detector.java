package com.mygdx.game.psg.old_engine;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.psg.screens.PlayScreen;

public class Detector implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getBody().isBullet()!= contact.getFixtureB().getBody().isBullet()){
            PlayScreen.contact.add(contact.getFixtureA().getBody());
            PlayScreen.contact.add(contact.getFixtureB().getBody());
        }
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
