package com.mygdx.game.psg.Engine;


import com.mygdx.game.psg.Sprites.Cell;

import static com.badlogic.gdx.math.MathUtils.random;

public class Actions {


    public static Attribute.AttributeType[] actions = new Attribute.AttributeType[175];
    public int fitness[] = new int[35];

    public void AddAction(Cell.Team team, Attribute.AttributeType attributeType){

        actions[random(0,24) + getIndex(team)] = attributeType;

    }

    public void SetFitness(){

        for(int i = 0; i < 7; i++) {

            for (int a = 0; a < 5; a++) {

                int counter = 0;

                for (int b = 0; b < 25; b++) {

                    if (actions[b] == Attribute.AttributeType.values()[a]) {

                        counter++;
                    }
                }

                fitness[i + a * 5] = counter;
            }
        }
    }

    public int[] getFitness(){

        SetFitness();

        return this.fitness;

    }

    private int getIndex(Cell.Team team){

        switch (team){
            case PLAYER: return 0;
            case BOT1: return 24;
            case BOT2: return 49;
            case BOT3: return 74;
            case BOT4: return 99;
            case BOT5: return 124;
            case NEUTRAL: return 149;
        }

        return -1;
    }

    public static void setActions(Attribute.AttributeType[] actions) {
        Actions.actions = actions;
    }

    public static Attribute.AttributeType[] getActions() {
        return actions;
    }
}
