package com.mygdx.game.psg.Engine;


public class Population {

    public Attribute[] population = new Attribute[700];


    public Population(){

        setPopulation();
    }

    public void setPopulation(){

        population = new Attribute[700];

        for(int i = 0; i < population.length; i++){

            population[i] = new Attribute();

        }
    }

    public Attribute[] getPopulation(){

        return population;
    }
}
