package com.mygdx.game.psg.Engine;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.psg.MainGame;
import com.mygdx.game.psg.Sprites.Attack;
import com.mygdx.game.psg.Sprites.Cell;

import static com.badlogic.gdx.math.MathUtils.random;

public class BotAction {

    public int[] botActions = new int[30];
    public int timeAttack;

    public BotAction(){
        setTimeAttack(100);
        setBotActions();
    }

    public void setBotActions(int[] botActions){

        for(int i = 0; i < 10; i++){

            this.botActions[i] = botActions[i];

        }

    }

    public void setBotActions(){

        // 0 = size, 1 = attack, 2 = defense, 3 = speed, 4 = regen
        for(int i = 0; i < 6; i++){
            this.botActions[0 + i * 5] = random(50, 100);
            this.botActions[1+ i * 5] = random(50, 100);
            this.botActions[2 + i * 5] = random(50, 100);
            this.botActions[3 + i * 5] = random(50, 100);
            this.botActions[4 + i * 5] = random(50, 100);
        }
    }

    public int[] getBotActions() {
        return botActions;
    }


    public Attribute.AttributeType getAction(Cell selected, Actor target){

            switch (random(1, 6)) {
                case 1:
                    if (random(0, 20000) < botActions[0 + getIndex(selected.team)]) {
                        return Attribute.AttributeType.SIZE;
                    }
                    break;
                case 2:
                    if (random(0, 20000) < botActions[1 + getIndex(selected.team)]) {
                        return Attribute.AttributeType.ATTACK;
                    }
                    break;
                case 3:
                    if (random(0, 20000) < botActions[2 + getIndex(selected.team)]) {
                        return Attribute.AttributeType.DEFENSE;
                    }
                    break;
                case 4:
                    if (random(0, 20000) < botActions[3 + getIndex(selected.team)]) {
                        return Attribute.AttributeType.SPEED;
                    }
                    break;
                case 5: if (random(0, 20000) < botActions[4 + getIndex(selected.team)]) {
                    if(((Cell)target).team == selected.team){
                    return Attribute.AttributeType.REGEN;
                    }
                    break;
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

    public int getTimeAttack() {
        return timeAttack;
    }

    public void setTimeAttack(int timeAttack) {
        this.timeAttack = timeAttack;
    }

    public void Adjust(){

        for(int i = 1; i <= MainGame.wins; i++){
            botActions[random(0,29)] += i;
        }

        for(int i = 1; i <= MainGame.loses; i++){
            botActions[random(0,29)] -= i;
        }

        timeAttack = timeAttack - MainGame.wins + MainGame.loses;
    }
}
