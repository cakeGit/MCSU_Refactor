package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public class Game_Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender Sender, Command Command, String Label, String[] Args) {
        if (Sender.isOp()) {
            switch (Command.getName()) {
                case "playgame":
                    Sender.sendMessage(ChatColor.YELLOW + "Warning: If possible use /queuegame instead");
                    Sender.sendMessage("Loading game " + Args[0]);

                    Sender.sendMessage("Loading game thinge");
                    for (org.bukkit.World World : Sender.getServer().getWorlds()) {
                        Sender.sendMessage(World.getName());
                        Sender.sendMessage(((Player) Sender).getWorld().getName());
                    }
                    Sender.sendMessage("shown you the" + Args[0]);

                    Game_Main.Load_Game(
                            Args[0],
                            (Player) Sender,
                            Args
                    );
                    return true;
                case "listgames":
                    Sender.sendMessage("All registered games");

                    for (Game game : Game_Main.RegisteredGames) {
                        Sender.sendMessage("  " + game.getName());
                    }
                    return true;
                default:
                    Bukkit.getLogger().info(Command.getName());

            }
        }
        //Should not happen but so intellij shuts up
        return false;

    }

}
