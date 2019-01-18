package com.mygdx.game.psg.Engine;

import com.mygdx.game.psg.Sprites.Cell;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Wheel {

    Attribute.AttributeType[][] par = new Attribute.AttributeType[25][2];
    private SaveGame saveGame = new SaveGame();
    private Attribute[] attribute = new Attribute[175];
    private Population newPopulation = new Population();
    private Population population;
    private  Actions actions;

    private ArrayList<Integer> integers = new ArrayList<Integer>();

    public Wheel(Population population, Actions actions) throws IOException {

        this.population = population;
        this.actions = actions;

    }

    public Population NewGeneration(Population population, Attribute.AttributeType attributeType){



        return null;
    }

    private void Crossover(Attribute.AttributeType[] A, Attribute.AttributeType[] B){

        Attribute.AttributeType a;
        Attribute.AttributeType b;

        int onePoint = random(0, 24);

        //vector crossover
        for(int i = 0; i <= onePoint; i++){
            a = A[i];
            b = B[i];

            A[i] = b;
            B[i] = a;
        }
    }

    private Attribute.AttributeType[] Mutation(Attribute.AttributeType[] DNA){
        //mutation based on wheel
        if(random(0,100) <= 5) {
            DNA[random(0, 24)] = Attribute.AttributeType.values()[random(0,4)];
        }
        return DNA;
    }

    public Population genetic(){

        for(int i=0; i < 175; i += 2){

            attribute[i].setDNA(Mutation(par[0]));
            attribute[i+1].setDNA(Mutation(par[1]));
        }
        newPopulation.setPopulation(attribute);
        return newPopulation;
    }
}
