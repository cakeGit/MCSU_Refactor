package com.cloud.mcsu_rf.Definitions.Game;

import com.cloud.mcsu_rf.Definitions.GameFunctions.GameFunctionBase;

import java.util.ArrayList;

public class GameState {

    String name;
    boolean enabled;
    boolean enabledDefault;
    Runnable onEnable;
    Runnable onDisable;

    ArrayList<GameFunctionBase> gameFunctions = new ArrayList<>();

    public GameState(String name, boolean enabledDefault) { this.name = name; this.enabled = enabledDefault; this.enabledDefault = enabledDefault; }
    public GameState(String name) { this.name = name; this.enabled = false; this.enabledDefault = false; }

    public GameState addGameFunction(GameFunctionBase gameFunction, boolean isTemporary) {
        gameFunctions.add(gameFunction);
        if (isTemporary) { gameFunction.setEnabled(enabled); gameFunction.setIsTemporary(true); }
        return this;
    }

    public GameState addGameFunction(GameFunctionBase gameFunction) { gameFunctions.add(gameFunction); return this; }

    public ArrayList<GameFunctionBase> getGameFunctions() { return this.gameFunctions; }

    public boolean getEnabled() { return enabled; }

    public String getName() { return name; }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;

        for (GameFunctionBase gameFunction : gameFunctions) {

            gameFunction.setEnabled(enabled);

        }

        if ( enabled ) { if (onEnable != null) { onEnable.run(); } }
        else { if (onDisable != null) { onDisable.run(); } }

    }

    public void reset() {

        setEnabled(false);

        gameFunctions.removeIf(GameFunctionBase::getIsTemporary);

        this.enabled = enabledDefault;

    }

    public GameState onEnable(Runnable onEnable) { this.onEnable = onEnable; return this; }
    public GameState onDisable(Runnable onDisable) { this.onDisable = onDisable; return this; }

}
