package com.mygdx.game.psg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.psg.Engine.Gesture;
import com.mygdx.game.psg.Engine.SaveGame;
import com.mygdx.game.psg.Screens.PlayScreen;

import java.io.IOException;

public class MainGame extends Game{

	public SpriteBatch batch;
	public static float V_Width = 1080, V_Height = 1920, PPM = 100;
	public  static float W_Width, W_Height;
	PlayScreen playScreen;

	@Override
	public void create() {

		W_Width = Gdx.graphics.getWidth();
		W_Height = Gdx.graphics.getHeight();

		batch = new SpriteBatch();

		try {
			playScreen = new PlayScreen(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setScreen(playScreen);



	}

	@Override
	public void render() {
		if(PlayScreen.restartCount > 600){
			if(PlayScreen.player == 0){
				V_Width -= 1080*0.01f;
				V_Height -= 1920*0.01f;
			}else{
				V_Width += 1080*0.01f;
				V_Height += 1920*0.01f;
			}
			this.screen.dispose();
			if(V_Width < 1080/2 || V_Height < 1920/2){
				V_Width = 1080/2;
				V_Height = 1920/2;
			}

			if(V_Width > 1080*2 || V_Height > 1920*2){
				V_Width = 1080*2;
				V_Height = 1920*2;
			}

			try {
				playScreen = new PlayScreen(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
			setScreen(playScreen);
		}

		super.render();

	}

}
