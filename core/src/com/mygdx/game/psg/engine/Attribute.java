package com.mygdx.game.psg.engine;

public class Attribute {
    private int id;
    private String name;
    private double value;

    public Attribute(int id, String name) {
        this.id = id;
        this.name = name;
        this.value = 0;
    }

    public Attribute(int id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}