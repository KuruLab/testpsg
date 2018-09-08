package com.mygdx.game.psg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.psg.Players.Attack;
import com.mygdx.game.psg.Players.Player;

public class MainGameScreen extends BaseScreen{

    private Stage stage;
    private Actor player, attack;
    private Texture cell, energy;

    public MainGameScreen(MainGame game){

        super(game);
        cell = new Texture("cell/00.png");
        energy = new Texture("attack/attack.png");

    }


    @Override
    public void show() {


        stage = new Stage();
        player = new Player(cell);
        attack = new Attack(energy);

        stage.addActor(player);
        stage.addActor(attack);

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.1f,0.2f,0.7f,0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

        stage.dispose();
        cell.dispose();

    }

    @Override
    public void dispose() {

    }
}
