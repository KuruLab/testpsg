package com.mygdx.game.psg.Engine;

import com.mygdx.game.psg.Sprites.Cell;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Wheel {

    Attribute.AttributeType[][] par = new Attribute.AttributeType[100][2];
    private SaveGame saveGame = new SaveGame();
    private Attribute[] attribute = new Attribute[350];
    private Population newPopulation = new Population();
    private Population population;
    private  Actions actions;

    private ArrayList<Integer> integers = new ArrayList<Integer>();

    public Wheel(Population population, Actions actions) throws IOException {

        this.population = population;
        this.actions = actions;

    }

    public void NewGeneration(){






    }

    private Attribute.AttributeType[][] Crossover(Attribute.AttributeType[] A, Attribute.AttributeType[] B){

        Attribute.AttributeType a;
        Attribute.AttributeType b;

        int onePoint = random(0, 99);

        //vector crossover
        for(int i = 0; i <= onePoint; i++){
            a = A[i];
            b = B[i];

            A[i] = b;
            B[i] = a;
        }

        par[0] = A;
        par[1] = B;

        return  par;
    }

    private Attribute.AttributeType[] Mutation(Attribute.AttributeType[] DNA){
        //mutation based on wheel
        if(random(0,100) <= 5) {
            DNA[random(0, 99)] = Attribute.AttributeType.values()[random(0,9)];
        }
        return DNA;
    }

    public Population genetic(){


        for(int i=0; i < 350; i += 2){

            par = Crossover(attribute[i].getDNA(), attribute[i+1].getDNA());

            attribute[i].setDNA(Mutation(par[0]));
            attribute[i+1].setDNA(Mutation(par[1]));

        }

        newPopulation.setPopulation(attribute);

        return newPopulation;
    }
}
