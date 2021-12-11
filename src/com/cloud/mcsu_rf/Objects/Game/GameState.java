package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function.GameFunction;

import java.util.ArrayList;

public class GameState {

    String name;
    ArrayList<GameFunction> GameFunctions = new ArrayList<>();

    public GameState(String name) {

        this.name = name;

    }

    public void addGameFunction(GameFunction gameFunction) { this.GameFunctions.add(gameFunction); }

}
