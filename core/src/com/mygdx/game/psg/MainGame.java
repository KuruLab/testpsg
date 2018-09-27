package com.mygdx.game.psg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.psg.Screens.PlayScreen;

public class MainGame extends Game {

	public SpriteBatch batch;
	public static final int V_Width = 1080*2, V_Height = 1920*2, PPM = 100;

	@Override
	public void create() {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {

		super.render();

	}
}
