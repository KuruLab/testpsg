package com.mygdx.game.psg.Players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.System.*;

public class Player extends Actor {

    private Texture cell;
    private int[] atributos = new int[100];
    private int ID, team, size, energy, finalx, finaly, time = 0, base = 50, factor = 5;
    private boolean select, sleep;

    public Player(Texture cell, int[] atributos) {

        arraycopy(atributos, 0, this.atributos, 0, 100);
        this.cell = cell;
    }

    public Player(Texture cell) {

        for(int i = 0; i < 100; i ++) {
            this.atributos[i] = ((int)Math.round(Math.random()*20));
        }

        this.size= ContaAtributo(this.atributos, 1);
        this.cell = cell;
        setHeight(base+factor*this.size);
    }

    @Override
    public void act(float delta) {

        System.out.println(select);
        if (this.time == 10) {
            //select
            if (Gdx.input.isTouched()) {

                if (Gdx.input.getX() >= getX() &&
                    Gdx.input.getX() <= getX() + base + factor * this.size &&
                    Gdx.graphics.getHeight() - Gdx.input.getY() >= getY() &&
                    Gdx.graphics.getHeight() - Gdx.input.getY() <= getY() + base + factor * this.size) {

                    if (this.select == true) {this.select = false;} else {this.select = true;}

                } else {if (this.select == true){
                    this.finalx = Gdx.input.getX()-(base + factor * this.size)/2;
                    this.finaly = Gdx.graphics.getHeight() - Gdx.input.getY()-(base + factor * this.size)/2;
                    select = false;}
                }
            }

            this.time = 0;} else {this.time = this.time + 1;
        }

        setX(getX() + (finalx - getX())*0.1f);
        setY(getY() + (finaly - getY())*0.1f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

        if(this.select == false){batch.setColor(0,1,1,1);}else{ batch.setColor(1,1,0,1);}

        batch.draw(cell, getX(), getY(), base+factor*this.size, base+factor*this.size);

    }

    private int ContaAtributo(int[] atributos, int tipo){
        int contador = 0;

        for (int i = 0; i < 100; i ++) {
            if (atributos[i] == tipo){
                contador++;
                out.println(contador);
            }
        }
        return contador;
    }

}