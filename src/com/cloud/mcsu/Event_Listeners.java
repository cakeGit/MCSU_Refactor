package com.cloud.mcsu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class Event_Listeners implements Listener {

    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent evt) {
        evt.setJoinMessage(ChatColor.BLUE + evt.getPlayer().getName() + " has joined the pain :(");
    }
}
