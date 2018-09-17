package com.mygdx.game.psg.Screens;

import com.badlogic.gdx.Screen;
import com.mygdx.game.psg.MainGame;

public abstract class BaseScreen implements Screen {

    protected MainGame game;

    public BaseScreen(MainGame game){

        this.game = game;

    }
}
