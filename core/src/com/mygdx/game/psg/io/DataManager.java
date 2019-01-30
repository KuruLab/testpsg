package com.mygdx.game.psg.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.game.psg.engine.PlayerModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DataManager {

    private static DataManager ourInstance;

    static {
        try {
            ourInstance = new DataManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Json myJson = new Json();

    public DataManager() throws IOException {
    }

    public static DataManager getInstance() {
        return ourInstance;
    }

    public boolean savePlayerModel(PlayerModel playerModel) {
        myJson = new Json();
        FileHandle playerModelHandle = Gdx.files.local("Save/playerModel.json");

        File playerFile = new File("Save/playerModel.json");
        if (verify(playerFile, playerModel)) {
            playerModelHandle = Gdx.files.local("Save/playerModel.json");
            playerModelHandle.writeString(myJson.prettyPrint(playerModel), false);
            return true;
        } else return false;
    }

    public boolean savePlayerModels(ArrayList<PlayerModel> models) {
        File modelsFile = new File("Save/models_test.json");
        if (verify(modelsFile, models)) {
            myJson = new Json();
            myJson.setOutputType(JsonWriter.OutputType.json);
            myJson.addClassTag("PlayerModel", PlayerModel.class);
            myJson.toJson(models, Gdx.files.local("Save/player_models.json"));
            return true;
        } else return false;
    }

    public PlayerModel loadPlayerModel() {
        myJson = new Json();
        FileHandle playerModelHandle = Gdx.files.local("Save/playerModel.json");
        return myJson.fromJson(PlayerModel.class, playerModelHandle.readString());
    }

    public ArrayList<PlayerModel> loadPlayerModels() {
        myJson = new Json();
        myJson.addClassTag("PlayerModel", PlayerModel.class); //As above
        ArrayList<PlayerModel> list = myJson.fromJson(ArrayList.class, Gdx.files.local("Save/player_models.json"));
        return list;
    }

    public boolean verify(File file, Object data) {
        if (data == null) {
            Gdx.app.log("Warning", "NULL data.");
            return false;
        }
        if (!file.exists()) {
            try {
                Gdx.app.log("Warning", "Creating file " + file.getName() + ".");
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                return true;
            }
        } else {
            Gdx.app.log("Warning", "The file " + file.getName() + " already exists.");
            return true;
        }
    }

}
