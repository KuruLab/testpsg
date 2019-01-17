package com.mygdx.game.psg.Engine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.Sprites.Cell;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Bot {

    public int[] actions = new int[60];

    public void Bot(){

        setActions();
    }

    public void setActions(int[] actions){

        for(int i = 0; i < 10; i++){

            this.actions[i] = actions[i];

        }

    }

    public void setActions(){

        for(int i = 0; i < 6; i++){
            this.actions[0 + i * 10] = random(25, 75);
            this.actions[1+ i * 10] = random(25, 75);
            this.actions[2 + i * 10] = random(25, 75);
            this.actions[3 + i * 10] = random(25, 75);
            this.actions[4 + i * 10] = random(25, 75);
            this.actions[5 + i * 10] = random(25, 75);
            this.actions[6 + i * 10] = random(25, 75);
            this.actions[7 + i * 10] = random(25, 75);
            this.actions[8 + i * 10] = random(25, 75);
            this.actions[9 + i * 10] = random(25, 75);
        }
    }

    public int[] getActions() {
        return actions;
    }

    /*
    public Actions.actionCell getAction(Cell selected, Actor target){

        if(target.getClass() == Cell.class){
            if(((Cell)target).team == selected.team){



            }else{

                if(((Cell)target).team == Cell.Team.PLAYER){
                 //player


                }else{
                   //neutral or other bots


                }
            }

            if(random(0,100) > actions[getIndex(selected.team) + random(0,1)]){
                //aleatory move or attack

            }
        }

        return null;
    }

    private int getIndex(Cell.Team team){

        switch (team){
            case BOT1: return 0;
            case BOT2: return 10;
            case BOT3: return 20;
            case BOT4: return 30;
            case BOT5: return 40;
            case NEUTRAL: return 50;
        }

        return 100;
    }
    */
}
