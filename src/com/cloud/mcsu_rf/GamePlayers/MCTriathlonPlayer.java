package com.cloud.mcsu_rf.GamePlayers;

import com.cloud.mcsu_rf.Games.MCTriathlon;
import com.cloud.mcsu_rf.MCSU_Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MCTriathlonPlayer {

    public static ArrayList<MCTriathlonPlayer> mctriathlonPlayers = new ArrayList<>();
    public static ArrayList<BukkitRunnable> stopwatchTimers = new ArrayList<>();
    int checkpoint;
    Boat boat;

    double time = 0;
    Player player;
    BukkitRunnable stopwatchTimer;

    public MCTriathlonPlayer(Player player, int checkpoint) {

        this.player = player;
        this.checkpoint = checkpoint;
        mctriathlonPlayers.add(this);

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

    public static MCTriathlonPlayer fromBukkit(Player player) {
        return mctriathlonPlayers.stream().filter(mctriathlonPlayers -> mctriathlonPlayers.toBukkit().equals(player)).findFirst().orElse(null);
    }

    public static void startAllTimers() {
        resetAllTimers();
        for (BukkitRunnable timer : stopwatchTimers) {
            timer.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 1L);
        }
    }
    public static void resetAllTimers() {
        for (MCTriathlonPlayer mctriathlonPlayer : mctriathlonPlayers) {
            mctriathlonPlayer.time = 0;
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
                (MCTriathlon.countdownActive ? "Time left: none lol haha" : "");
    }

    public double getTime() { return time; }
    public int getCheckpoint() { return checkpoint; }
    public int setCheckpoint(int checkpoint) { this.checkpoint = checkpoint; return checkpoint; }
    public Boat getBoat() { return boat; }
    public Boat setBoat(Boat boat) { this.boat = boat; return boat; }
    public Player toBukkit() { return player; }

}
