package com.mygdx.game.psg.genetic;

import java.util.Random;

/**
 * @author Kurumin
 * <p>
 * ->  K. Deb and R. B. Agrawal.
 * Simulated Binary Crossover for Continuous Search Space.
 * Complex Systems, 9(2):115–148, 1995.
 */
public class SBXCrossover {

    /**
     * EPS defines the minimum difference allowed between real values
     */
    private static final double EPS = 1.0e-14;

    private double distributionIndex;
    private double crossoverProbability;
    private Random rng;

    public SBXCrossover(double distributionIndex, double crossoverProbability) {
        this.distributionIndex = distributionIndex;
        this.crossoverProbability = crossoverProbability;

        if (crossoverProbability < 0) {
            System.err.println("Crossover probability is negative: " + crossoverProbability);
        } else if (distributionIndex < 0) {
            System.err.println("Distribution index is negative: " + distributionIndex);
        }

        this.rng = new Random(System.nanoTime());
    }

    public CellIndividual[] doCrossover(CellIndividual father, CellIndividual mother) {
        CellIndividual[] offspring = new CellIndividual[2];

        offspring[0] = father.copy();
        offspring[1] = mother.copy();

        int i;
        double rand;
        double y1, y2, lowerBound = 0, upperBound = 1.0;
        double c1, c2;
        double alpha, beta, betaq;
        double valueX1, valueX2;

        if (rng.nextDouble() <= crossoverProbability) {
            double sum0 = 0, sum1 = 0;
            for (i = 0; i < father.getAttributes().length; i++) {
                valueX1 = father.getAttributes()[i];
                valueX2 = mother.getAttributes()[i];
                if (rng.nextDouble() <= 0.5) {
                    if (Math.abs(valueX1 - valueX2) > EPS) {
                        if (valueX1 < valueX2) {
                            y1 = valueX1;
                            y2 = valueX2;
                        } else {
                            y1 = valueX2;
                            y2 = valueX1;
                        }
                        rand = rng.nextDouble();
                        beta = 1.0 + (2.0 * (y1 - lowerBound) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow(rand * alpha, (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math.pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }
                        c1 = 0.5 * (y1 + y2 - betaq * (y2 - y1));

                        beta = 1.0 + (2.0 * (upperBound - y2) / (y2 - y1));
                        alpha = 2.0 - Math.pow(beta, -(distributionIndex + 1.0));

                        if (rand <= (1.0 / alpha)) {
                            betaq = Math.pow((rand * alpha), (1.0 / (distributionIndex + 1.0)));
                        } else {
                            betaq = Math
                                    .pow(1.0 / (2.0 - rand * alpha), 1.0 / (distributionIndex + 1.0));
                        }
                        c2 = 0.5 * (y1 + y2 + betaq * (y2 - y1));

                        if (rng.nextDouble() <= 0.5) {
                            offspring[0].getAttributes()[i] = c2;
                            offspring[1].getAttributes()[i] = c1;
                            sum0 += c2;
                            sum1 += c1;
                        } else {
                            offspring[0].getAttributes()[i] = c1;
                            offspring[1].getAttributes()[i] = c2;
                            sum0 += c1;
                            sum1 += c2;
                        }
                    } else {
                        offspring[0].getAttributes()[i] = valueX2;
                        offspring[1].getAttributes()[i] = valueX1;
                        sum0 += valueX2;
                        sum1 += valueX1;
                    }

                } else {
                    offspring[0].getAttributes()[i] = valueX1;
                    offspring[1].getAttributes()[i] = valueX2;
                    sum0 += valueX1;
                    sum1 += valueX2;
                }
            }

            for (i = 0; i < father.getAttributes().length; i++) {
                offspring[0].getAttributes()[i] /= sum0;
                offspring[1].getAttributes()[i] /= sum1;
            }
        }
        return offspring;
    }
}
