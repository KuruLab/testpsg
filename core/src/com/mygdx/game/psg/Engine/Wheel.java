package com.mygdx.game.psg.Engine;

import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.mygdx.game.psg.Screens.PlayScreen;
import com.mygdx.game.psg.Sprites.Cell;

import java.io.IOException;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

public class Wheel {

    public SaveGame saveGame = new SaveGame();
    private ArrayList<Attribute> attribute;
    private Population population;
    private  Actions actions;

    private ArrayList<Integer> integers = new ArrayList<Integer>();

    public Wheel() throws IOException {

        attribute = new ArrayList<Attribute>();

    }


    private void GetSave() throws IOException {

        this.population = saveGame.GetPopulation();
        this.actions = saveGame.GetActions();

    }

    private void NewGeration(){

        int i = 0;

        while(i < population.getPopulation().length){

                if(i<50){
                    for(int aux = 0;aux < actions.FitnessCell(population.getPopulation()[i], Cell.Team.NEUTRAL); aux++){
                        integers.add(i);
                    }
                }else{
                    if(i<100){
                        for(int aux = 0;aux < actions.FitnessCell(population.getPopulation()[i], Cell.Team.PLAYER); aux++){
                            integers.add(i);
                        }
                    }else{
                        if(i<150){
                            for(int aux = 0;aux < actions.FitnessCell(population.getPopulation()[i], Cell.Team.BOT1); aux++){
                                integers.add(i);
                            }
                        }else{
                            if(i<200){
                                for(int aux = 0;aux < actions.FitnessCell(population.getPopulation()[i], Cell.Team.BOT2); aux++){
                                    integers.add(i);
                                }

                            }else{
                                if(i<250){
                                    for(int aux = 0;aux < actions.FitnessCell(population.getPopulation()[i], Cell.Team.BOT3); aux++){
                                        integers.add(i);
                                    }

                                }else{
                                    if(i<300){
                                        for(int aux = 0;aux < actions.FitnessCell(population.getPopulation()[i], Cell.Team.BOT4); aux++){
                                            integers.add(i);
                                        }

                                    }else{
                                        for(int aux = 0;aux < actions.FitnessCell(population.getPopulation()[i], Cell.Team.BOT5); aux++){
                                            integers.add(i);
                                        }

                                    }
                                }
                            }
                        }
                    }
                }


               i++;

               if(i%50 == 0){
                   for(int aux = i; aux - i > -50; aux++){
                       attribute.add(i-1,population.getPopulation()[random(0,integers.toArray().length)]);
                   }
               }

        }

        population.setPopulation((Attribute[]) attribute.toArray());
        saveGame.SavePopulation(population);
    }

    private Attribute Crossover(Attribute A, Attribute B){

        
        return  A;
    }
}
