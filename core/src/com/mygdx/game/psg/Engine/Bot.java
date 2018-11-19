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

        if(action >=0 && action <= 20) {

            switch (team) {
                case BOT1:
                    return actions[action - 1];
                case BOT2:
                    return actions[action + 19];
                case BOT3:
                    return actions[action + 39];
                case BOT4:
                    return actions[action + 59];
                case BOT5:
                    return actions[action + 79];
            }

        }

        return 0;

    }

    public int getAdjust(Cell.Team team, int adjust){

        //adjust values: 0, 5, 10.
        if(adjust == 0 || adjust == 5 || adjust == 10) {

            switch (team) {
                case BOT1:
                    return actions[100 + adjust];
                case BOT2:
                    return actions[101 + adjust];
                case BOT3:
                    return actions[102 + adjust];
                case BOT4:
                    return actions[103 + adjust];
                case BOT5:
                    return actions[104 + adjust];
            }

        }

        return 0;

    }

    public void adjustActions(ArrayList wheel, Cell.Team team){

        int index = 0;
        Attribute.AttributeType type;
        type = (Attribute.AttributeType) wheel.toArray()[random(0, wheel.toArray().length)];

        switch (type){
            case SIZE: index = 1;break;
            case PASSIVE1: index = 2;break;
            case PASSIVE2: index = 3;break;
            case PASSIVE3: index = 4;break;
            case ATTACK: index = 5;break;
            case OFFENSIVE1: index = 6;break;
            case OFFENSIVE2: index = 7;break;
            case OFFENSIVE3: index = 8;break;
            case DEFENSE: index = 9;break;
            case DEFENSIVE1: index = 10;break;
            case DEFENSIVE2: index = 11;break;
            case DEFENSIVE3: index = 12;break;
            case SPEED: index = 13;break;
            case MOVE1: index = 14;break;
            case MOVE2: index = 15;break;
            case MOVE3: index = 16;break;
            case REGEN: index = 17;break;
            case SELECT1: index = 18;break;
            case SELECT2: index = 19;break;
            case SELECT3: index = 20;break;
        }


        switch (team) {
            case BOT1: actions[index - 1] = random(0, 100);
            case BOT2: actions[index + 19] = random(0, 100);
            case BOT3: actions[index + 39] = random(0, 100);
            case BOT4: actions[index + 59] = random(0, 100);
            case BOT5: actions[index + 89] = random(0, 100);

        }
    }

    public void adjustParameters(int number, Cell.Team team){

        if(number == 0 || number == 5 || number == 10) {

            switch (team) {
                case BOT1:
                    actions[number - 100] = random(0, 100);
                case BOT2:
                    actions[number - 101] = random(0, 100);
                case BOT3:
                    actions[number - 102] = random(0, 100);
                case BOT4:
                    actions[number - 103] = random(0, 100);
                case BOT5:
                    actions[number - 104] = random(0, 100);

            }
        }
    }

}
