package com.cloud.mcsu_rf.Game_Handlers;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Game_Countdown_Task extends BukkitRunnable {

    private int timeLeft = 10;
    private GameManager gameManager;
    public Game_Countdown_Task(GameManager gameManager) {this.gameManager = gameManager;}

    @Override
    public void run() {
        timeLeft--;
        if(timeLeft <= 0) {
            cancel();
            gameManager.setGameState(GameState.ACTIVE);
            return;
        }
        for(Player players : Bukkit.getOnlinePlayers()) {
            players.sendTitle(timeLeft + " until game starts!","Get Ready!");
            playCountdownSound(players,Sound.BLOCK_NOTE_BLOCK_SNARE);
        }
    }

    private void playCountdownSound(Player p, Sound s) {
        p.playSound(p.getLocation(),s,1,1);
    }

}
