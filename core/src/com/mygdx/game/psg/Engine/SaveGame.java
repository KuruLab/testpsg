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

public class SaveGame {

    public Attribute attribute = new Attribute();
    public Population population = new Population();
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

        //Gdx.files.local("Save/population.json").writeString(myjson.prettyPrint(population), false);



        //attribute = myjson.readValue(Attribute.class, jsonReader.Gdx.files.local("Save/save.json").readString()));





        //jsonWriter.write(Gdx.files.local("Save/test.json").path(),);
        //jsonWriter.flush();
        //jsonWriter.close();




    }

    public void Print(){
        //text = myjson.toJson(myjson.readValue(Attribute.class, jsonReader.parse(Gdx.files.local("Save/save.json").readString())));
        //System.out.println(myjson.prettyPrint(population));
    }

    public void SaveStage(Stage stage){

        //Gdx.files.local("Save/stage.json").writeString(myjson.prettyPrint(stage), false);

    }

}
