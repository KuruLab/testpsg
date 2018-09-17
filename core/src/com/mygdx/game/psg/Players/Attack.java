package com.mygdx.game.psg.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Attack extends Actor{

    private Texture attack;
    private float energy;


    public Attack(Texture attack, float energy, float modify, float bonus){
        this.attack = attack;
        this.energy = energy*modify + bonus;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(1,0,1,1);
        batch.draw(attack, getX(), getY(), energy, energy);
    }
}
