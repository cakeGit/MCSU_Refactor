package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.Bukkit;

public class GameManager {

    private final MCSU_Main MCSU_Plugin;
    public GameState gameState = GameState.LOBBY;
    private Game_Countdown_Task gameCountdownTask;

    public GameManager(MCSU_Main mcsu_plugin) {
        this.MCSU_Plugin = mcsu_plugin;
    }

    public void setGameState(GameState gameState) {
        if(this.gameState == GameState.ACTIVE && gameState == GameState.STARTING) return;
        if(this.gameState == gameState) return;
        this.gameState = gameState;
        switch (gameState) {
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
