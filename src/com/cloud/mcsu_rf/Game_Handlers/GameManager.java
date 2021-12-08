package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.Bukkit;

public class GameManager {

    private final MCSU_Main MCSU_Plugin;
    public static GameState gameState = GameState.LOBBY;
    private Game_Countdown_Task gameCountdownTask;

    public GameManager(MCSU_Main mcsu_plugin) {
        this.MCSU_Plugin = mcsu_plugin;
    }

    public static void setGameState(GameState state) {
        if(gameState == GameState.ACTIVE && state == GameState.STARTING) return;
        if(gameState == state) return;
        gameState = state;
        switch (state) {
            case ACTIVE:
                if(this.gameCountdownTask != null) this.gameCountdownTask.cancel();
                Bukkit.broadcastMessage("Active!");
                break;
            case STARTING:
                Bukkit.broadcastMessage("Starting");
                this.gameCountdownTask = new Game_Countdown_Task(this);
                this.gameCountdownTask.runTaskTimer(MCSU_Plugin, 0, 20);
                break;
        }
    }
}
