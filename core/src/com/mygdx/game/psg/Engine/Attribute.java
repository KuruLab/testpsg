package com.mygdx.game.psg.Engine;

import static com.badlogic.gdx.math.MathUtils.random;

public class Attribute {

    public enum AttributeType {
        SIZE,
        ATTACK,
        DEFENSE,
        SPEED,
        REGEN

    }

    private AttributeType[] DNA = new AttributeType[25];
    private int[] resume = new int[5];

    public Attribute(){
        setDNA();
        setResume();
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
            DNA[i] = AttributeType.values()[random(0,4)];
        }
    }

    public void setDNA(AttributeType[] DNA){

        this.DNA =  DNA;

    }

    public void setResume(int[] resume) {
        this.resume = resume;
    }

    public int[] getResume() {
        return resume;
    }

    public AttributeType[] getDNA(){
        return DNA;
    }

    public void setResume() {

        for(int i = 0; i < 5; i ++) {

            resume[i] = this.AttributeCount(Attribute.AttributeType.values()[i]);

        }
    }
}
