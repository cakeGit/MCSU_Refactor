package com.cloud.mcsu_rf.GamePlayers;

import com.cloud.mcsu_rf.Games.Slimekour;
import com.cloud.mcsu_rf.MCSU_Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
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

        stopwatchTimer = new BukkitRunnable() {
            @Override
            public void run() {
                time += 0.01;
                toBukkit().spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(
                                getTimeString()
                        )
                );
            }
        };
        stopwatchTimers.add(stopwatchTimer);

    }

    public static SlimekourPlayer fromBukkit(Player player) {
        return slimekourPlayers.stream().filter(slimekourPlayer -> slimekourPlayer.toBukkit().equals(player)).findFirst().orElse(null);
    }

    public static void startAllTimers() {
        for (BukkitRunnable timer : stopwatchTimers) {
            timer.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 2L);
        }
    }
    public static void resetAllTimers() {
        for (SlimekourPlayer slimekourPlayer : slimekourPlayers) {
            slimekourPlayer.time = 0;
        }
    }

    public void endTimer() {
        stopwatchTimer.cancel();
    }

    public String getTimeString() {
        return ChatColor.BLUE + "Time: " + ChatColor.RESET + ChatColor.BOLD + time +
                (Slimekour.countdownActive ? "Time left: none lol haha" : "");
    }

    public float getTime() { return time; }
    public Player toBukkit() { return player; }

}
