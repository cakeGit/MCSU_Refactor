package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

import java.io.File;

public class Event_Listeners implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        Player Player = e.getPlayer();
        e.setJoinMessage(ChatColor.BLUE + e.getPlayer().getName() + ChatColor.WHITE + " has joined the pain :(");
        Player.setScoreboard(Scoreboard_Main.Current_Scoreboard);
    }
}
