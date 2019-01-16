package com.mygdx.game.psg.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class SaveGame {

    //public Attribute attribute = new Attribute();
    //public Population population = new Population();

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
}