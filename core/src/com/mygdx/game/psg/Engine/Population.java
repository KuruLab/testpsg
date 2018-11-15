package com.mygdx.game.psg.Engine;


public class Population {

    public static Attribute[] population = new Attribute[350];


    public Population(){

        setPopulation();
    }

    public void setPopulation(){

        population = new Attribute[350];

        for(int i = 0; i < population.length; i++){

            population[i] = new Attribute();

        }
    }

    public void setPopulation(Attribute[] attributes){

        population = attributes;

    }

    public Attribute[] getPopulation(){

        return population;
    }
}
