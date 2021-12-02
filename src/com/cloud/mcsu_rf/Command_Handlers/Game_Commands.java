package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Classes.Game_Main;
import com.cloud.mcsu_rf.Objects.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Game_Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            switch (command.getName()) {
                case "playgame":
                    Game_Main.Load_Game(command.getName(), sender, args);
                    return true;
                case "listgames":
                    Bukkit.broadcastMessage("All created games");

                    for (Game game : Game_Main.RegisteredGames) {
                        Bukkit.broadcastMessage("  " + ChatColor.BLUE + game.getName());
                    }
                    return true;
                default:
                    Bukkit.getLogger().info(command.getName());

            }
        }
        //Should not happen but so intellij shuts up
        return false;

    }

}
