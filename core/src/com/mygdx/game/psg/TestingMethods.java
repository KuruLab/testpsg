package com.mygdx.game.psg;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.psg.engine.PlayerModel;
import com.mygdx.game.psg.io.DataManager;

import java.io.IOException;
import java.util.ArrayList;

public class TestingMethods {

    public void testJsonSave() {
        Gdx.app.log("Log", "Testando Save...");
        PlayerModel playerModel = new PlayerModel(0, "teste", 500, new double[]{0.2, 0.2, 0.2, 0.2, 0.2});
        Gdx.app.log("Log", playerModel.toString());

        DataManager dw = null;
        try {
            dw = new DataManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dw.savePlayerModel(playerModel);
    }

    public void generateModels() {
        Gdx.app.log("Log", "Generating Models...");

        PlayerModel passiveBalanced = new PlayerModel(0, "PassiveBalanced", 500, new double[]{0.5, 0.125, 0.125, 0.125, 0.125});
        PlayerModel pureBalanced = new PlayerModel(1, "PurelyBalanced", 500, new double[]{0.2, 0.2, 0.2, 0.2, 0.2});
        PlayerModel offensive = new PlayerModel(2, "Offensive", 500, new double[]{0.2, 0.5, 0.0, 0.1, 0.2});
        PlayerModel defensive = new PlayerModel(3, "Defensive", 500, new double[]{0.2, 0.0, 0.5, 0.1, 0.2});
        PlayerModel tactical = new PlayerModel(4, "Tactical", 500, new double[]{0.2, 0.1, 0.1, 0.5, 0.1});
        PlayerModel support = new PlayerModel(5, "Support", 500, new double[]{0.3, 0.0, 0.1, 0.1, 0.5});

        ArrayList<PlayerModel> models = new ArrayList<PlayerModel>();
        models.add(passiveBalanced);
        models.add(pureBalanced);
        models.add(offensive);
        models.add(defensive);
        models.add(tactical);
        models.add(support);

        DataManager dw = null;
        try {
            dw = new DataManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dw.savePlayerModels(models);
    }

    public void loadModels() {
        Gdx.app.log("Log", "Loading Models...");
        DataManager dw = null;
        try {
            dw = new DataManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<PlayerModel> models = dw.loadPlayerModels();
        for (PlayerModel model : models) {
            Gdx.app.log("Log", model.toString());
        }
    }

    public void testJsonLoad() {
        Gdx.app.log("Log", "Testando Load...");

        DataManager dw = null;
        try {
            dw = new DataManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PlayerModel player = dw.loadPlayerModel();
        Gdx.app.log("Log", player.toString());
    }

}
