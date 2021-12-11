package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Game_Cmds {

    public static boolean listGames(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        Sender.sendMessage("All registered games");

        for (Game game : Game_Main.RegisteredGames) {
            Sender.sendMessage("  " + game.getName());
        }

        return true;

    }

    public static boolean playGame(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        Sender.sendMessage(ChatColor.YELLOW + "Warning: If possible use /queuegame instead");
        Sender.sendMessage("Loading game " + Args[0]);

        Game game = Game_Main.getRegisteredGameByName(Args[0]);

        if (game == null) {
            Sender.sendMessage(ChatColor.RED + "what game is " + Args[0] + " lol");

            for (Game gamase : Game_Main.RegisteredGames) {

                Sender.sendMessage(gamase.getName());

            }
        } else {
            com.sk89q.worldedit.world.World adaptedWorld = BukkitAdapter.adapt(((Player) Sender).getWorld());

            //game.startGame( ((Player) Sender).getWorld() );
        }


        /*Game_Main.loadGame(
                Args[0],
                (Player) Sender,
                Args
        );            */

        return true;

    }

}
