package com.mygdx.game.psg.Players;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {

    private Texture cell;
    private int size;

    public Player(Texture cell) {

        this.cell = cell;

    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setColor(0,1,1,1);
        batch.draw(cell, getX(), getY(), size, size);

    }

}