package com.mygdx.game.psg.engine;

public class Action {
    private int id;
    private String name;
    //private PlayerModel player;
    private long time;

    public Action(int id, String name) {
        this.id = id;
        this.name = name;
        //this.player = null;
        this.time = 0;
    }

    public Action() {
        this.id = -1;
        this.name = "";
        //this.player = null;
        this.time = 0;
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

    /*public PlayerModel getPlayer() {
        return player;
    }

    public void setPlayer(PlayerModel player) {
        this.player = player;
    }*/

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Action copy(Action action) {
        Action copy = new Action();
        copy.setId(action.getId());
        copy.setName(action.getName());
        //copy.setPlayer(action.getPlayer());
        copy.setTime(action.getTime());
        return copy;
    }
}
