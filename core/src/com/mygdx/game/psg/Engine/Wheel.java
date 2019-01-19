package com.mygdx.game.psg.Engine;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Wheel {


    private Attribute.AttributeType[] X = new Attribute.AttributeType[25];
    private Attribute.AttributeType[] Y = new Attribute.AttributeType[25];
    private Attribute[] attribute = new Attribute[175];
    private Attribute[] newAttribute = new Attribute[175];
    private Population newPopulation = new Population();

    private ArrayList<Integer> integers = new ArrayList<Integer>();
    private int[] fitness = new int[35];

    public Wheel(Attribute[] attribute, int[] fitness) throws IOException {

        this.attribute = attribute;
        this.fitness = fitness;

    }

    public Attribute[] NewGeneration(){


        for(int a = 0; a < 7; a++){

            for(int b = 0; b < 25; b++){

                for(int c = 0; c < 5; c++){

                    int diference = fitness[c] - attribute[b + a * 25].getResume()[c];

                    if(diference > -5 && diference < 5){
                        integers.add(a * 5 + b);
                    }

                    if(diference > -10 && diference < 10){
                        integers.add(a * 5 + b);
                    }

                    if(diference > -15 && diference < 15){
                        integers.add(a * 5 + b);
                    }

                    if(diference > -20 && diference < 20){
                        integers.add(a * 5 + b);
                    }
                }
            }

            for(int i = 0; i < 25; i++){
                newAttribute[i = a * 5] = attribute[integers.get(random(0, integers.size()))];
                integers.trimToSize();
            }
        }

        //crosover
        for(int a = 0; a < 175; a += 2){

            Attribute.AttributeType x;
            Attribute.AttributeType y;

            X = newAttribute[a].getDNA();
            Y = newAttribute[a + 1].getDNA();

            int onePoint = random(0, 24);

            //vector crossover
            for(int b = 0; b <= onePoint; b++){
                x = X[b];
                y = Y[b];

                X[b] = y;
                Y[b] = x;
            }

            newAttribute[a].setDNA(Mutation(X));
            newAttribute[a].setDNA(Mutation(Y));
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
}
