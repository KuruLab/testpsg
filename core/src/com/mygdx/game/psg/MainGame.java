package com.mygdx.game.psg;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.psg.Engine.Attribute;
import com.mygdx.game.psg.Engine.BotAction;
import com.mygdx.game.psg.Engine.History;
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

	//save and load
	public static boolean load, exists;
    public Population loadPopulation = new Population();
    public Population newPopulation = new Population();
    public History loadHistory = new History();
    public History newHistory = new History();
    public BotAction loadBot = new BotAction();
    public BotAction newBot = new BotAction();

    public SaveGame saveGame = new SaveGame();

    public static Attribute[] attributes = new Attribute[175];
    public static Attribute[] attributesLoad = new Attribute[175];
    public static Attribute[] attributesNew = new Attribute[175];

    public static boolean[] histories = new boolean[10];
    public static boolean[] historiesLoad = new boolean[10];
    public static boolean[] historiesNew = new boolean[10];
    public static int wins, loses;

    public static int[] actions = new int[30];
    public static int[] actionsLoad = new int[30];
    public static int[] actionsNew = new int[30];
    public static int timeAttack;

	public static ArrayList<Color> colors = new ArrayList<Color>();

	public static float M_Width = 3*1080, M_Height = 1920;

	public static boolean alterated = true;

    public MainGame() throws IOException {

    }

    public enum Controler{
        MENU,
        COLOR,
        START,
        RESTART
    }

    public static Controler controler = Controler.MENU;
    File population = new File("Save/population.json");
    File bot = new File("Save/bot.json");
    File history = new File("Save/history.json");

	public static boolean win, lose, restart, menu, color, start;

    @Override
	public void create() {

        colors.add(0, Color.WHITE);
        colors.add(0, Color.GREEN);
        colors.add(0, Color.ROYAL);
        colors.add(0, Color.PURPLE);
        colors.add(0, Color.RED);
        colors.add(0, Color.ORANGE);
        colors.add(0, Color.YELLOW);

        win = false;
        lose = false;
        restart = false;
        color = false;
        start = false;
        menu = true;

        W_Width = Gdx.graphics.getWidth();
        W_Height = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        if(population.exists() && bot.exists() && history.exists()){
            load = true;
            exists = true;
        }

        if (exists) {
            loadPopulation = saveGame.GetPopulation();
            attributesLoad = loadPopulation.getPopulation();

            loadBot = saveGame.GetBot();
            actionsLoad = loadBot.getBotActions();
            timeAttack = loadBot.timeAttack;

            loadHistory = saveGame.GetHistory();
            historiesLoad = loadHistory.getHistory();

        } else {
            load = false;

            loadPopulation = newPopulation;
            saveGame.SavePopulation(loadPopulation);
            attributesLoad = loadPopulation.getPopulation();

            loadBot = newBot;
            saveGame.SaveBot(loadBot);
            actionsLoad = loadBot.getBotActions();
            timeAttack = loadBot.timeAttack;

            loadHistory = newHistory;
            saveGame.SaveHistory(loadHistory);
            historiesLoad = loadHistory.getHistory();
        }

    }

	@Override
	public void render() {

        if(alterated) {

            loadHistory.setHistory(historiesLoad);
            saveGame.SaveHistory(loadHistory);

            if (win) {
                V_Width += 1080 * 0.001f * wins;
                V_Height += 1920 * 0.001f * wins;
            }

            if (lose) {
                V_Width -= 1080 * 0.001f * loses;
                V_Height -= 1920 * 0.001f * loses;
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
