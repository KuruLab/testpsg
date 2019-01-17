package com.mygdx.game.psg.Engine;


import static com.badlogic.gdx.math.MathUtils.random;

public class Attribute {

    public enum AttributeType {

        SIZE,
        ATTACK,
        DEFENSE,
        SPEED,
        REGEN,
        PASSIVE,
        OFFENSIVE,
        DEFENSIVE,
        MOVE,
        SELECT
    }

    private AttributeType[] DNA = new AttributeType[25];

    public Attribute(){
        setDNA();
    }

    public int AttributeCount(AttributeType type){
        int count = 0;
        for (int i = 0; i < 25; i ++) {
            if (DNA[i] == type) {
                count++;
            }
        }
        return count;
    }

    public void setDNA(){

        for(int i = 0; i < 25; i ++) {
            DNA[i] = AttributeType.values()[random(0,9)];
        }
    }

    public void setDNA(AttributeType[] DNA){

        this.DNA =  DNA;

    }

    public AttributeType[] getDNA(){
        return DNA;
    }
}
