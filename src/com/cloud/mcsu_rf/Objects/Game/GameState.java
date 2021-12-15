package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.GameFunctionBase;

import java.util.ArrayList;

public class GameState {

    String name;
    boolean enabled;

    ArrayList<GameFunctionBase> gameFunctions = new ArrayList<>();

    public GameState(String name, boolean enabled) { this.name = name; this.enabled = enabled; }
    public GameState(String name) { this.name = name; this.enabled = false; }

    public GameState addGameFunction(GameFunctionBase gameFunction) { this.gameFunctions.add(gameFunction); return this; }
    public ArrayList<GameFunctionBase> getGameFunctions() { return this.gameFunctions; }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;

        for (GameFunctionBase gameFunction: gameFunctions) {
            gameFunction.setEnabled(enabled);
        }

    }

}
