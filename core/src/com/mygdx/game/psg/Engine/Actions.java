package com.mygdx.game.psg.Engine;


import com.mygdx.game.psg.Sprites.Cell;

import static com.badlogic.gdx.math.MathUtils.random;

public class Actions {

    enum actionCell{

        MOVE,
        ATTACK,
        ENEMY,
        NEUTRAL,
        TRANSFER,
        FIRE,
        SELECT,
        TARGET,
        AVOID,
        TOUCH

    }


    public static Attribute.AttributeType[] actions = new Attribute.AttributeType[25];
    public int fitness[] = new int[70];

    public void AddAction(Cell.Team team, Attribute.AttributeType attributeType){

        actions[random(0,24) + getIndex(team)] = attributeType;

    }

    public void SetFitness(){

        for(int i = 0; i < 7; i++) {

            for (int a = 0; a < 10; a++) {

                int counter = 0;

                for (int b = 0; b < 25; b++) {

                    if (actions[b] == Attribute.AttributeType.values()[a]) {

                        counter++;
                    }
                }

                fitness[i + a * 10] = counter;
            }
        }
    }

    public int[] getFitness(){

        return this.fitness;

    }

    private int getIndex(Cell.Team team){

        switch (team){
            case PLAYER: return 0;
            case BOT1: return 10;
            case BOT2: return 20;
            case BOT3: return 30;
            case BOT4: return 40;
            case BOT5: return 50;
            case NEUTRAL: return 60;
        }

        return 100;
    }

    public static void setActions(Attribute.AttributeType[] actions) {
        Actions.actions = actions;
    }

    public static Attribute.AttributeType[] getActions() {
        return actions;
    }
}
