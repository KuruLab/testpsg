package com.mygdx.game.psg.genetic;

import java.util.Random;

/**
 * @author Kurumin
 */
public class PolinomialMutation {

    /**
     * EPS defines the minimum difference allowed between real values
     */
    private static final double EPS = 1.0e-14;

    private double distributionIndex;
    private double mutationProbability;

    private Random rng;

    public PolinomialMutation(double distributionIndex, double mutationProbability) {
        this.distributionIndex = distributionIndex;
        this.mutationProbability = mutationProbability;

        if (mutationProbability < 0) {
            System.err.println("Crossover probability is negative: " + mutationProbability);
        } else if (distributionIndex < 0) {
            System.err.println("Distribution index is negative: " + distributionIndex);
        }

        this.rng = new Random(System.nanoTime());
    }

    /**
     * Perform the mutation operation
     */
    public void doMutation(CellIndividual solution) {
        double rnd, delta1, delta2, mutPow, deltaq;
        double y, yl, yu, val, xy, sum = 0;
        boolean mutation = false;
        for (int i = 0; i < solution.getAttributes().length; i++) {
            if (rng.nextDouble() <= mutationProbability) {
                mutation = true; // a mutation
                y = solution.getAttributes()[i];
                yl = 0.0; // lower bound
                yu = 1.0; // upper bound
                if (yl == yu) {
                    y = yl;
                } else {
                    delta1 = (y - yl) / (yu - yl);
                    delta2 = (yu - y) / (yu - yl);
                    rnd = rng.nextDouble();
                    mutPow = 1.0 / (distributionIndex + 1.0);
                    if (rnd <= 0.5) {
                        xy = 1.0 - delta1;
                        val = 2.0 * rnd + (1.0 - 2.0 * rnd) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = Math.pow(val, mutPow) - 1.0;
                    } else {
                        xy = 1.0 - delta2;
                        val = 2.0 * (1.0 - rnd) + 2.0 * (rnd - 0.5) * (Math.pow(xy, distributionIndex + 1.0));
                        deltaq = 1.0 - Math.pow(val, mutPow);
                    }
                    y = y + deltaq * (yu - yl);

                }
                solution.getAttributes()[i] = y;
            }
            sum += solution.getAttributes()[i];
        }
        if (mutation) {
            for (int i = 0; i < solution.getAttributes().length; i++) {
                solution.getAttributes()[i] /= sum;
            }
            //System.out.println("Mutation");
        }
    }
}
