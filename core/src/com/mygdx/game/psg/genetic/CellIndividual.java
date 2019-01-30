package com.mygdx.game.psg.genetic;

public class CellIndividual {

    private double[] attributes;
    private double fitness;

    public CellIndividual(double[] attributes, double fitness) {
        this.attributes = attributes;
        this.fitness = fitness;
    }

    public CellIndividual() {
        this.fitness = 1;
    }

    public double[] getAttributes() {
        return attributes;
    }

    public void setAttributes(double[] attributes) {
        this.attributes = attributes;
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public CellIndividual copy() {
        CellIndividual copy = new CellIndividual();
        double[] att = new double[attributes.length];
        double fit = fitness;
        for (int i = 0; i < att.length; i++) {
            att[i] = attributes[i];
        }
        copy.setAttributes(att);
        copy.setFitness(fit);
        return copy;
    }

    @Override
    public String toString() {
        String result = "[";
        for (int i = 0; i < attributes.length; i++) {
            result += String.format("%.3f", attributes[i]);
            if (i != attributes.length - 1) {
                result += ", ";
            }
        }
        result += "] f: " + String.format("%.9f", fitness);
        return result;
    }
}
