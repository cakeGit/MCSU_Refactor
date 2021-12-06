package com.cloud.mcsu_rf.Game_Handlers;

import org.bukkit.scheduler.BukkitRunnable;

public class Game_Countdown_Task extends BukkitRunnable {

    private int timeLeft = 30;

    @Override
    public void run() {
        timeLeft--;
        if(timeLeft <= 0) {
            cancel();
        }
    }

}
