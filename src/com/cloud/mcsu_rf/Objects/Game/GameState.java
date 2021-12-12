package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.GameFunction;

import java.util.ArrayList;

public class GameState {

    String name;
    boolean enabled;
    ArrayList<GameFunction> GameFunctions = new ArrayList<>();

    public GameState(String name, boolean enabled) { this.name = name; this.enabled = enabled; }
    public GameState(String name) { this.name = name;}

    public GameState addGameFunction(GameFunction gameFunction) { this.GameFunctions.add(gameFunction); return this; }

    public ArrayList<GameFunction> getGameFunctions() { return this.GameFunctions; }

}
