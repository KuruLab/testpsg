package com.mygdx.game.psg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.psg.Engine.Playability;
import com.mygdx.game.psg.Screens.GameScreen;

public class MainGame extends Game {

	@Override
	public void create() {
		setScreen(new GameScreen(this));
		Playability playability = new Playability();
        Gdx.input.setInputProcessor(playability);
	}
}
