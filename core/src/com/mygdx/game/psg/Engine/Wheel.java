package com.mygdx.game.psg.Engine;

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

    public void NewGeneration(int index){



        switch (index){

            //fitness
            case 1: break;
            case 20: break;
            case 40: break;
            case 60: break;
            case 80: break;
            case 100: break;
            case 120: break;

            //distribuição proporcional
            case 140: break;
            case 160: break;
            case 180: break;
            case 200: break;
            case 220: break;
            case 240: break;

            //new generation
            case 260: break;
            case 280: break;
            case 300: break;
            case 320: break;
            case 340: break;
            case 360: break;

            //crossover
            case 380: break;
            case 400: break;
            case 420: break;
            case 440: break;
            case 460: break;
            case 480: break;

            //mutation
            case 500: break;
            case 520: break;
            case 540: break;
            case 560: break;
            case 580: break;
            case 600: break;

        }
    }

    private void Crossover(Attribute.AttributeType[] A, Attribute.AttributeType[] B){

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
    }

    private Attribute.AttributeType[] Mutation(Attribute.AttributeType[] DNA){
        //mutation based on wheel
        if(random(0,100) <= 5) {
            DNA[random(0, 24)] = Attribute.AttributeType.values()[random(0,9)];
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
