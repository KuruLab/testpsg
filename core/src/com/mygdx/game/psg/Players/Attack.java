package com.mygdx.game.psg.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Attack extends Actor{

    private Texture attack;

    public Attack(Texture attack){

        this.attack = attack;

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(attack, getX(), getY());

    }
}
