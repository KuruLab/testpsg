package com.mygdx.game.psg.old_engine;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Wheel {


    OldAttribute.AttributeType x;
    OldAttribute.AttributeType y;
    private OldAttribute.AttributeType[] X = new OldAttribute.AttributeType[25];
    private OldAttribute.AttributeType[] Y = new OldAttribute.AttributeType[25];


    private ArrayList<Integer> integers = new ArrayList<Integer>();
    private int[] fitness;
    private OldAttribute[] oldAttribute;
    private OldAttribute[] newOldAttribute = new OldAttribute[175];

    public Wheel(OldAttribute[] oldAttribute, int[] fitness) throws IOException {
        this.oldAttribute = oldAttribute;
        this.fitness = fitness;

        for(int i = 0; i < 175; i++){
            newOldAttribute[i] = new OldAttribute();
        }
    }

    public OldAttribute[] NewGeneration() {

        //crosover
        for(int a = 0; a < 175; a++){

            X = newOldAttribute[a].getDNA();

            if((a + 1) % 25 == 0){
                Y = newOldAttribute[a - 24].getDNA();
            }else{
                Y = newOldAttribute[a + 1].getDNA();
            }

            int onePoint = random(0, 24);

            //vector crossover
            for(int b = 0; b < onePoint; b++){
                x = X[b];
                y = Y[b];

                X[b] = y;
                Y[b] = x;
            }

            newOldAttribute[a].setDNA(X);

            if((a + 1) % 25 == 0){
                newOldAttribute[a - 24].setDNA(Mutation(Y));
            }else{
                newOldAttribute[a + 1].setDNA(Mutation(Y));
            }
        }

        return newOldAttribute;

    }

    private OldAttribute.AttributeType[] Mutation(OldAttribute.AttributeType[] DNA) {
        //mutation
        if(random(0,100) <= 5) {
            DNA[random(0, 24)] = OldAttribute.AttributeType.values()[random(0, 4)];
        }
        return DNA;
    }

    public void generateWheel(int a){

        for(int b = 0; b < 25; b++){

            for(int c = 0; c < 5; c++){

                int difference = fitness[c] - oldAttribute[b + a * 25].getResume()[c];

                if(difference > -5 && difference < 5){
                    integers.add(a * 5 + b);
                }

                if(difference > -10 && difference < 10){
                    integers.add(a * 5 + b);
                }

                if(difference > -15 && difference < 15){
                    integers.add(a * 5 + b);
                }

                if(difference > -20 && difference < 20){
                    integers.add(a * 5 + b);
                }
            }
        }
    }

    public void PreCrossover(int a){
        for(int i = 0; i < 25; i++){
            newOldAttribute[i + a * 5] = oldAttribute[integers.get(random(0, integers.size() - 1))];
            integers.trimToSize();
        }
        integers.clear();
        integers.trimToSize();
    }

    public void Crossover(int index){
        //crosover
        for(int a = 0 + 25 * index; a < 25 + 25 * index; a++){

            X = newOldAttribute[a].getDNA();
            Y = newOldAttribute[a + 1].getDNA();

            int onePoint = random(0, 24);

            //vector crossover
            for(int b = 0; b < onePoint; b++){
                x = X[b];
                y = Y[b];

                X[b] = y;
                Y[b] = x;
            }

            newOldAttribute[a].setDNA(X);
            newOldAttribute[a + 1].setDNA(Y);
        }
    }
}
