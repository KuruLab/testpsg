package com.mygdx.game.psg.Engine;

import static com.badlogic.gdx.math.MathUtils.randomBoolean;

public class History {

    public boolean[] history = new boolean[10];

    public void History(){

        setHistory();

    }

    public void setHistory() {

        for(int i = 0; i < history.length; i++){

            history[i] = randomBoolean();

        }
    }

    public void setHistory(boolean[] history) {
        this.history = history;
    }

    public boolean[] getHistory() {
        return history;
    }

}
