package com.mygdx.game.psg.engine;

import com.mygdx.game.psg.old_engine.OldAttribute;

import java.util.ArrayList;

/**
 * @author Kurumin
 */
public class GameData {
    ArrayList<Action> actions;
    ArrayList<OldAttribute> oldAttributes;
    ArrayList<PlayerModel> playerModels;

    public GameData(ArrayList<Action> actions,
                    ArrayList<OldAttribute> oldAttributes,
                    ArrayList<PlayerModel> playerModels) {
        this.actions = actions;
        this.oldAttributes = oldAttributes;
        this.playerModels = playerModels;
    }

    public GameData() {
        // not implemented yet
        // loadGameData();
    }

    public ArrayList<Action> getActions() {
        return actions;
    }

    public void setActions(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public ArrayList<OldAttribute> getOldAttributes() {
        return oldAttributes;
    }

    public void setOldAttributes(ArrayList<OldAttribute> oldAttributes) {
        this.oldAttributes = oldAttributes;
    }

    public ArrayList<PlayerModel> getPlayerModels() {
        return playerModels;
    }

    public void setPlayerModels(ArrayList<PlayerModel> playerModels) {
        this.playerModels = playerModels;
    }

    /*private void loadGameData(){
        AttributesReader attReader = new AttributesReader("data", "oldAttributes.json");
        ActionsReader actReader = new ActionsReader("data", "actions.json");
        PlayerModelsReader playerReader = new PlayerModelsReader("data", "player_models.json");

        actions = actReader.loadActions();
        oldAttributes = attReader.loadAttributes();
        playerModels = playerReader.loadPlayerModels();
    }*/
}
