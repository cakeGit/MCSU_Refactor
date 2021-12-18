package com.cloud.mcsu_rf.ShorthandClasses;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Break {

    public static void line(CommandSender sender) { sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------"); }
    public static void line(Player player) { player.sendMessage(ChatColor.DARK_GRAY + "------------------------------"); }

    public static void empty(CommandSender sender) { sender.sendMessage(); }
    public static void empty(Player player) { player.sendMessage(); }


}
