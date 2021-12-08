package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.GameManager;
import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Command_Main {

    private static GameManager gameManager;

    public static void registerCommands() {

        MCSU_Main.Mcsu_Plugin.getCommand("listgames").setExecutor(new Game_Commands(gameManager));
        MCSU_Main.Mcsu_Plugin.getCommand("playgame").setExecutor(new Game_Commands(gameManager));
        //MCSU_Main.Mcsu_Plugin.getCommand("setspawn").setExecutor(new Player_Commands());
        //MCSU_Main.Mcsu_Plugin.getCommand("hub").setExecutor(new Player_Commands());
    }

    public static void Init() {
        registerCommands();
    }

    public static boolean notOpError(CommandSender sender) {
        sender.sendMessage( ChatColor.RED + "oi mate bugger off you need op to do that");
        return true;
    }

}
