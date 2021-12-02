package com.cloud.mcsu_rf;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

import java.io.File;

public class Event_Listeners implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent evt) {
        evt.setJoinMessage(ChatColor.BLUE + evt.getPlayer().getName() + " has joined the pain :(");
    }
}
