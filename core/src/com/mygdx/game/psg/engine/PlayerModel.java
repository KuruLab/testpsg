package com.mygdx.game.psg.engine;

import java.util.Arrays;

/**
 * @author Kurumin
 */
public class PlayerModel {

    private int id;
    private String name;
    private long coolDown;
    private double[] rates;

    public PlayerModel() {
        this.id = -1;
        this.name = "null";
        this.coolDown = -1;
        this.rates = null;
    }

    public PlayerModel(int id, String name, long coolDown, double[] rates) {
        this.id = id;
        this.name = name;
        this.coolDown = coolDown;
        this.rates = rates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(long coolDown) {
        this.coolDown = coolDown;
    }

    public double[] getRates() {
        return rates;
    }

    public void setRates(double[] rates) {
        this.rates = rates;
    }

    @Override
    public String toString() {
        return "PlayerModel{" +
                "id=" + id +
                ", name=\"" + name + '\"' +
                ", coolDown=" + coolDown +
                ", rates=" + Arrays.toString(rates) +
                '}';
    }
}
