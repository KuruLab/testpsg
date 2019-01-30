package com.mygdx.game.psg.old_engine;


import com.mygdx.game.psg.sprites.Cell;

import static com.badlogic.gdx.math.MathUtils.random;

public class Actions {


    public static OldAttribute.AttributeType[] actions = new OldAttribute.AttributeType[175];
    public int fitness[] = new int[35];

    public static OldAttribute.AttributeType[] getActions() {
        return actions;
    }

    public static void setActions(OldAttribute.AttributeType[] actions) {
        Actions.actions = actions;
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

    public void AddAction(Cell.Team team, OldAttribute.AttributeType attributeType) {

        actions[random(0, 24) + getIndex(team)] = attributeType;

    }

    public void SetFitness() {

        for (int i = 0; i < 7; i++) {

            for (int a = 0; a < 5; a++) {

                int counter = 0;

                for (int b = 0; b < 25; b++) {

                    if (actions[b] == OldAttribute.AttributeType.values()[a]) {

                        counter++;
                    }
                }

                fitness[i + a * 5] = counter;
            }
        }
    }
}
