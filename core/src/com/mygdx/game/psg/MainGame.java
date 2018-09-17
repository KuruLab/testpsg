package com.mygdx.game.psg;

import com.badlogic.gdx.Game;
import com.mygdx.game.psg.Screens.GameScreen;

public class MainGame extends Game {

	@Override
	public void create() {

		setScreen(new GameScreen(this));

	}
}
