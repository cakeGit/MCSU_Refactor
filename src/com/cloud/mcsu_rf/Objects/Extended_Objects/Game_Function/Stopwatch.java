package com.cloud.mcsu_rf.Objects.Extended_Objects.Game_Function;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class Stopwatch extends BukkitRunnable {

    public int time = 0;

    @Override
    public void run() {
        Bukkit.broadcastMessage("Stopwatch time: " + time);
        time++;
    }
}
