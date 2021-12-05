package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Objects.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Game_Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp()) {
            switch (cmd.getName()) {
                case "playgame":
                    sender.sendMessage(ChatColor.YELLOW + "Warning: If possible use /queuegame instead");
                    sender.sendMessage("Loading game " + args[0]);

                    sender.sendMessage("Loading game thinge");
                    for (org.bukkit.World World : sender.getServer().getWorlds()) {
                        sender.sendMessage(World.getName());
                        sender.sendMessage(((Player) sender).getWorld().getName());
                    }
                    sender.sendMessage("shown you the" + args[0]);

                    Game_Main.loadGame(
                            args[0],
                            (Player) sender,
                            args
                    );
                    return true;
                case "listgames":
                    sender.sendMessage("All registered games");

                    for (Game game : Game_Main.RegisteredGames) {
                        sender.sendMessage("  " + game.getName());
                    }
                    return true;
                default:
                    Bukkit.getLogger().info(cmd.getName());

            }
        }
        //Should not happen but so intellij shuts up
        return false;

    }

}
