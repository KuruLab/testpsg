package com.mygdx.game.psg.Engine;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Attribute {

    public enum AttributeType {

        SIZE,
        ATTACK,
        DEFENSE,
        SPEED,
        REGEN,

        PASSIVE1,
        PASSIVE2,
        PASSIVE3,

        OFFENSIVE1,
        OFFENSIVE2,
        OFFENSIVE3,

        DEFENSIVE1,
        DEFENSIVE2,
        DEFENSIVE3,

        MOVE1,
        MOVE2,
        MOVE3,

        SELECT1,
        SELECT2,
        SELECT3
    }

    private AttributeType[] DNA = new AttributeType[100];

    public Attribute(){
        setDNA();
    }

    public int AttributeCount(AttributeType type){
        int count = 0;
        for (int i = 0; i < 100; i ++) {
            if (DNA[i] == type) {
                count++;
            }
        }
        return count;
    }

    public int AttributeCount(AttributeType type, ArrayList<Attribute.AttributeType> DNA){
        int count = 0;
        for (int i = 0; i < DNA.toArray().length; i ++) {
            if (DNA.toArray()[i] == type) {
                count++;
            }
        }
        return count;
    }

    public void setDNA(){

        for(int i = 0; i < 100; i ++) {
            switch (random(1,20)){
                case 1 : DNA[i] = AttributeType.SIZE; break;
                case 2 : DNA[i] = AttributeType.PASSIVE1; break;
                case 3 : DNA[i] = AttributeType.PASSIVE2; break;
                case 4 : DNA[i] = AttributeType.PASSIVE3; break;
                case 5 : DNA[i] = AttributeType.ATTACK; break;
                case 6 : DNA[i] = AttributeType.OFFENSIVE1; break;
                case 7 : DNA[i] = AttributeType.OFFENSIVE2; break;
                case 8 : DNA[i] = AttributeType.OFFENSIVE3; break;
                case 9 : DNA[i] = AttributeType.DEFENSE; break;
                case 10 : DNA[i] = AttributeType.DEFENSIVE1; break;
                case 11 : DNA[i] = AttributeType.DEFENSIVE2; break;
                case 12 : DNA[i] = AttributeType.DEFENSIVE3; break;
                case 13 : DNA[i] = AttributeType.SPEED; break;
                case 14 : DNA[i] = AttributeType.MOVE1; break;
                case 15 : DNA[i] = AttributeType.MOVE2; break;
                case 16 : DNA[i] = AttributeType.MOVE3; break;
                case 17 : DNA[i] = AttributeType.REGEN; break;
                case 18 : DNA[i] = AttributeType.SELECT1; break;
                case 19 : DNA[i] = AttributeType.SELECT2; break;
                case 20 : DNA[i] = AttributeType.SELECT3; break;
            }
        }
    }

    public void setDNA(AttributeType[] DNA){

        this.DNA =  DNA;

    }

    public AttributeType[] getDNA(){
        return DNA;
    }
}
