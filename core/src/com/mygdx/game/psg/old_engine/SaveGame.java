package com.mygdx.game.psg.old_engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;

import java.io.IOException;

public class SaveGame {

    Json myjson;
    JsonReader jsonReader;


    public SaveGame() throws IOException {

        myjson = new Json();
        jsonReader = new JsonReader();

    }

    public Population GetPopulation(){

        return myjson.readValue(Population.class, jsonReader.parse(Gdx.files.local("Save/population.json").readString()));

    }

    public boolean SavePopulation(Population population){

        Gdx.files.local("Save/population.json").writeString(myjson.prettyPrint(population), false);

        return true;
    }

    public BotAction GetBot(){

        return myjson.readValue(BotAction.class, jsonReader.parse(Gdx.files.local("Save/bot.json").readString()));

    }

    public boolean SaveBot(BotAction bot){

        Gdx.files.local("Save/bot.json").writeString(myjson.prettyPrint(bot), false);

        return true;
    }

    public History GetHistory(){

        return myjson.readValue(History.class, jsonReader.parse(Gdx.files.local("Save/history.json").readString()));

    }

    public boolean SaveHistory(History history){

        Gdx.files.local("Save/history.json").writeString(myjson.prettyPrint(history), false);

        return true;
    }
}