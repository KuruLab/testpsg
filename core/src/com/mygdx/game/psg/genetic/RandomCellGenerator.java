package com.mygdx.game.psg.genetic;

import java.util.Random;

public class RandomCellGenerator {

    private int size;
    private Random rng;

    public RandomCellGenerator(int size) {
        this.size = size;
        this.rng = new Random(System.nanoTime());
    }

    public CellIndividual generate() {
        double[] att = new double[size];

        double sum = 0;
        for (int i = 0; i < size; i++) {
            att[i] = rng.nextDouble();
            sum += att[i];
        }
        for (int i = 0; i < size; i++) {
            att[i] /= sum;
        }
        CellIndividual cell = new CellIndividual(att, 1);
        return cell;
    }
}