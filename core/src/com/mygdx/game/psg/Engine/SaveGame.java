package com.mygdx.game.psg.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.IOException;
import java.io.Writer;

public class SaveGame {

    public Attribute attribute = new Attribute();
    private String text;

    Json myjson;
    JsonWriter jsonWriter;
    JsonReader jsonReader;
    JsonValue jsonValue;
    Writer writer;
    FileHandle fileHandle;

    public SaveGame() throws IOException {

        myjson = new Json();
        jsonWriter = new JsonWriter(writer);
        jsonReader = new JsonReader();

        Gdx.files.local("Save/save.json").writeString(myjson.toJson(attribute), false);



        
        //jsonWriter.write(Myjson.toJson(attribute),);
        //jsonWriter.flush();
        //jsonWriter.close();




    }

    public void Print(){
        text = myjson.toJson(attribute);
        System.out.println(text);
    }

}
