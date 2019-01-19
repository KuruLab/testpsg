package com.mygdx.game.psg.Engine;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Wheel {


    private Attribute.AttributeType[] X = new Attribute.AttributeType[25];
    private Attribute.AttributeType[] Y = new Attribute.AttributeType[25];
    private Attribute[] attribute;
    private Attribute[] newAttribute = new Attribute[175];


    private ArrayList<Integer> integers = new ArrayList<Integer>();
    private int[] fitness;

    Attribute.AttributeType x;
    Attribute.AttributeType y;

    public Wheel(Attribute[] attribute, int[] fitness) throws IOException {
        this.attribute = attribute;
        this.fitness = fitness;

        for(int i = 0; i < 175; i++){
            newAttribute[i] = new Attribute();
        }
    }

    public Attribute[] NewGeneration(){

        //crosover
        for(int a = 0; a < 175; a++){

            X = newAttribute[a].getDNA();

            if((a + 1) % 25 == 0){
                Y = newAttribute[a - 24].getDNA();
            }else{
                Y = newAttribute[a + 1].getDNA();
            }

            int onePoint = random(0, 24);

            //vector crossover
            for(int b = 0; b < onePoint; b++){
                x = X[b];
                y = Y[b];

                X[b] = y;
                Y[b] = x;
            }

            newAttribute[a].setDNA(X);

            if((a + 1) % 25 == 0){
                newAttribute[a - 24].setDNA(Mutation(Y));
            }else{
                newAttribute[a + 1].setDNA(Mutation(Y));
            }
        }

        return newAttribute;

    }

    private Attribute.AttributeType[] Mutation(Attribute.AttributeType[] DNA){
        //mutation
        if(random(0,100) <= 5) {
            DNA[random(0, 24)] = Attribute.AttributeType.values()[random(0,4)];
        }
        return DNA;
    }

    public void generateWheel(int a){

        for(int b = 0; b < 25; b++){

            for(int c = 0; c < 5; c++){

                int difference = fitness[c] - attribute[b + a * 25].getResume()[c];

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
            newAttribute[i + a * 5] = attribute[integers.get(random(0, integers.size() - 1))];
            integers.trimToSize();
        }
        integers.clear();
        integers.trimToSize();
    }

    public void Crossover(int index){
        //crosover
        for(int a = 0 + 25 * index; a < 25 + 25 * index; a++){

            X = newAttribute[a].getDNA();
            Y = newAttribute[a + 1].getDNA();

            int onePoint = random(0, 24);

            //vector crossover
            for(int b = 0; b < onePoint; b++){
                x = X[b];
                y = Y[b];

                X[b] = y;
                Y[b] = x;
            }

            newAttribute[a].setDNA(X);
            newAttribute[a + 1].setDNA(Y);
        }
    }
}
