package com.cloud.mcsu_rf.Objects.Game;

import com.cloud.mcsu_rf.Objects.Game_Functions.GameFunctionBase;

import java.util.ArrayList;

public class GameState {

    String name;
    boolean enabled;
    Runnable onEnable;
    Runnable onDisable;

    ArrayList<GameFunctionBase> gameFunctions = new ArrayList<>();

    public GameState(String name, boolean enabled) { this.name = name; this.enabled = enabled; }
    public GameState(String name) { this.name = name; this.enabled = false; }

    public GameState addGameFunction(GameFunctionBase gameFunction) { this.gameFunctions.add(gameFunction); return this; }
    public ArrayList<GameFunctionBase> getGameFunctions() { return this.gameFunctions; }

    public boolean getEnabled() { return enabled; }

    public String getName() { return name; }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;

        for (GameFunctionBase gameFunction : gameFunctions) {

            gameFunction.setEnabled(enabled);

        }

        if ( enabled ) { onEnable.run(); }
        else { onDisable.run(); }

    }

    public GameState onEnable(Runnable onEnable) { this.onEnable = onEnable; return this; }
    public GameState onDisable(Runnable onDisable) { this.onDisable = onDisable; return this; }

}
