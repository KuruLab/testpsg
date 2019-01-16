package com.mygdx.game.psg.Engine;

import com.mygdx.game.psg.Sprites.Cell;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Bot {

    public int[] actions = new int[115];


    public void Bot(){

        for(int i = 0; i < actions.length; i++){

            actions[i] = random(0,100);

        }
    }

    public void setActions(int[] actions){

        this.actions = actions;

    }

    public int[] getActions() {
        return actions;
    }

    public int getChance(Cell.Team team, int action){

        return 0;
    }

    public int getAdjust(Cell.Team team, int adjust){


        return 0;
    }

    public void adjustActions(ArrayList wheel, Cell.Team team){

    }

    public void adjustParameters(int number, Cell.Team team){

    }

}
