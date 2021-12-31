package com.cloud.mcsu_rf.GamePlayers;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SlimekourPlayer {

    public static ArrayList<SlimekourPlayer> slimekourPlayers = new ArrayList<>();
    public static ArrayList<BukkitRunnable> stopwatchTimers = new ArrayList<>();

    float time = 0;
    Player player;
    BukkitRunnable stopwatchTimer;

    public SlimekourPlayer(Player player) {

        this.player = player;
        slimekourPlayers.add(this);

        /*stopwatchTimer = new BukkitRunnable() {
            @Override
            public void run() {

            }
        }*/

    }

    public static SlimekourPlayer fromBukkit(Player player) {
        return slimekourPlayers.stream().filter(slimekourPlayer -> slimekourPlayer.toBukkit().equals(player)).findFirst().orElse(null);
    }

    public float getTime() { return time; }
    public Player toBukkit() { return player; }

}
