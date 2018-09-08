package com.mygdx.game.psg;

import com.badlogic.gdx.Screen;

public abstract class BaseScreen implements Screen {

    protected MainGame game;

    public BaseScreen(MainGame game){

        this.game = game;

    }
}
