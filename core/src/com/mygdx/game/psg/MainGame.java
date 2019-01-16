package com.mygdx.game.psg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.psg.Engine.Attribute;
import com.mygdx.game.psg.Engine.Population;
import com.mygdx.game.psg.Engine.SaveGame;
import com.mygdx.game.psg.Screens.MenuScreen;
import com.mygdx.game.psg.Screens.PlayScreen;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainGame extends Game{

	public SpriteBatch batch;
	public static float V_Width = 1080, V_Height = 1920, PPM = 100;
	public  static float W_Width, W_Height;
	private PlayScreen playScreen;
	private MenuScreen menuScreen;
	public static boolean load, exists;
    public Population loadgame = new Population();
    public Population newGame = new Population();
    public static Attribute[] attributes = new Attribute[350];
    public static Attribute[] attributesLoad = new Attribute[350];
    public static Attribute[] attributesNew = new Attribute[350];

	public static ArrayList<Color> colors = new ArrayList<Color>();

	public static float M_Width = 3*1080, M_Height = 1920;

	public static boolean alterated = true;

    public enum Controler{
        MENU,
        COLOR,
        START,
        RESTART
    }

    public static Controler controler = Controler.MENU;
    File file = new File("Save/population.json");

	public static boolean win, lose, restart, menu, color, start;

    @Override
	public void create() {

        colors.add(0, Color.WHITE);
        colors.add(0, Color.ROYAL);
        colors.add(0, Color.RED);
        colors.add(0, Color.GREEN);
        colors.add(0, Color.YELLOW);
        colors.add(0, Color.ORANGE);
        colors.add(0, Color.PURPLE);

        win = false;
        lose = false;
        restart = false;
        color = false;
        start = false;
        menu = true;

        W_Width = Gdx.graphics.getWidth();
        W_Height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        if(file.exists()){
            load = true;
            exists = true;
        }

        try {
            SaveGame saveGame = new SaveGame();
            if (!exists) {
                load = false;
                loadgame = new Population();
                saveGame.SavePopulation(loadgame);
                attributesLoad = loadgame.getPopulation();

            } else {
                newGame = saveGame.GetPopulation();
                attributesLoad = newGame.getPopulation();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Override
	public void render() {

        if(alterated) {

            if (win) {
                V_Width += 1080 * 0.01f;
                V_Height += 1920 * 0.01f;
            }

            if (lose) {
                V_Width -= 1080 * 0.01f;
                V_Height -= 1920 * 0.01f;
            }

            if (V_Width < 1080 / 2 || V_Height < 1920 / 2) {
                V_Width = 1080 / 2;
                V_Height = 1920 / 2;
            }

            if (V_Width > 1080 * 2 || V_Height > 1920 * 2) {
                V_Width = 1080 * 2;
                V_Height = 1920 * 2;
            }

        }


        if(alterated) {

            if (controler != Controler.START) {
                try {
                    menuScreen = new MenuScreen(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setScreen(menuScreen);
                alterated = false;

            } else {

                if(load){
                    attributes = attributesLoad;
                }else{
                    attributes = attributesNew;
                }

                try {
                    playScreen = new PlayScreen(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setScreen(playScreen);
                alterated = false;
            }
        }

		super.render();

	}

}
