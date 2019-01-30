package com.mygdx.game.psg.old_engine;

public class Population {

    public OldAttribute[] population = new OldAttribute[175];

    public Population(){

        setPopulation();
    }

    public void setPopulation(){

        population = new OldAttribute[175];

        for(int i = 0; i < population.length; i++){

            population[i] = new OldAttribute();

        }
    }

    public OldAttribute[] getPopulation() {

        return population;
    }

    public void setPopulation(OldAttribute[] population) {
        this.population = population;
    }
}
