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

    double time = 0;
    Player player;
    BukkitRunnable stopwatchTimer;

    public SlimekourPlayer(Player player) {

        this.player = player;
        slimekourPlayers.add(this);

        stopwatchTimer = new BukkitRunnable() {
            @Override
            public void run() {
                time += 0.05;

                time = Math.round(time*100.0)/100.0;

                toBukkit().spigot().sendMessage(
                        ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(
                                getActionbarString()
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
            timer.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 1L);
        }
    }

    public void endTimer() {
        stopwatchTimer.cancel();
    }

    public String getFormattedTime() {
        return (time > 60 ? (int) Math.round(time/60-((time%60)/60)) + ":" : "") +
                (String.format("%.2f", time % 60).length() == 4 ? "0" : "") +
                String.format("%.2f", time % 60);
    }

    public String getActionbarString() {
        return ChatColor.BLUE + "Current Time: " + ChatColor.RESET + ChatColor.BOLD +
                getFormattedTime()
                +
                (Slimekour.countdownActive ? "Time left: none lol haha" : "");
    }

    public double getTime() { return time; }
    public Player toBukkit() { return player; }

}
