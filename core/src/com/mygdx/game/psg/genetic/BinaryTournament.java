package com.mygdx.game.psg.genetic;

import java.util.ArrayList;
import java.util.Random;

public class BinaryTournament {

    private Random rng;

    public BinaryTournament() {
        this.rng = new Random(System.nanoTime());
    }

    public ArrayList<CellIndividual> doTournament(ArrayList<CellIndividual> population) {
        ArrayList<CellIndividual> winners = new ArrayList<CellIndividual>();
        while (winners.size() < population.size()) {
            int r1 = rng.nextInt(population.size());
            int r2 = r1;
            do {
                r2 = rng.nextInt(population.size());
            } while (r1 == r2);

            CellIndividual winner = match(population.get(r1), population.get(r2));
            winners.add(winner);
        }
        return winners;
    }

    public CellIndividual match(CellIndividual candidate1, CellIndividual candidate2) {
        if (candidate1.getFitness() <= candidate2.getFitness())
            return candidate1;
        else return candidate2;
    }

}
