package com.mygdx.game.psg.old_engine;

import static com.badlogic.gdx.math.MathUtils.randomBoolean;

public class History {

    public boolean[] history = new boolean[60];

    public History(){

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
