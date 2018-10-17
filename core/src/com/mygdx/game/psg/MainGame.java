package com.mygdx.game.psg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.psg.Engine.Gesture;
import com.mygdx.game.psg.Screens.PlayScreen;

public class MainGame extends Game{

	public SpriteBatch batch;
	public static final float V_Width = 1080, V_Height = 1920, PPM = 100;
	public  static float W_Width, W_Height;

	@Override
	public void create() {
		W_Width = Gdx.graphics.getWidth();
		W_Height = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render() {

		super.render();

	}

}
