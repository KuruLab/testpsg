package com.mygdx.game.psg.Engine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.Sprites.Cell;

import static com.badlogic.gdx.math.MathUtils.random;

public class BotAction {

    public int[] botActions = new int[30];

    public BotAction(){

        setBotActions();
    }

    public void setBotActions(int[] botActions){

        for(int i = 0; i < 10; i++){

            this.botActions[i] = botActions[i];

        }

    }

    public void setBotActions(){

        for(int i = 0; i < 6; i++){
            this.botActions[0 + i * 5] = random(25, 75);
            this.botActions[1+ i * 5] = random(25, 75);
            this.botActions[2 + i * 5] = random(25, 75);
            this.botActions[3 + i * 5] = random(25, 75);
            this.botActions[4 + i * 5] = random(25, 75);
        }
    }

    public int[] getBotActions() {
        return botActions;
    }


    public Attribute.AttributeType getAction(Cell selected, Actor target){

        // 0 = size, 1 = attack, 2 = defense, 3 = speed, 4 = regen
        if(target.getClass() == Cell.class){
            if(((Cell)target).team == selected.team){

                switch (random(1,3)){
                    case 1: if(random(0,100) < botActions[4]){
                    return Attribute.AttributeType.REGEN;
                    }break;

                    case 2: if(random(0,100) < botActions[1]){
                    return Attribute.AttributeType.ATTACK;
                    }break;

                    case 3:if(random(0,100) < botActions[0]){
                    return Attribute.AttributeType.SIZE;
                    }break;
                }
        }else{
                switch (random(1,2)){
                    case 1: if(random(0,100) < botActions[3]){
                        return Attribute.AttributeType.SPEED;
                    }break;

                    case 2: if(random(0,100) < botActions[2]){
                        return Attribute.AttributeType.DEFENSE;
                    }break;
                }
            }
        }

        return null;
    }

    private int getIndex(Cell.Team team){

        switch (team){
            case BOT1: return 0;
            case BOT2: return 5;
            case BOT3: return 10;
            case BOT4: return 15;
            case BOT5: return 20;
            case NEUTRAL: return 25;
        }

        return -1;
    }
}
