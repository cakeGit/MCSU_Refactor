package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import org.bukkit.ChatColor;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Game_Cmds {

    public static MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    public static boolean listGames(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        Sender.sendMessage("All registered games");

        for (Game game : Game_Main.RegisteredGames) {
            Sender.sendMessage("  " + game.getName());
        }

        return true;
    }

    public static boolean spawnSled(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            World w = p.getWorld();
            Boat b = (Boat) w.spawnEntity(p.getLocation(), EntityType.BOAT);
            b.setWoodType(TreeSpecies.GENERIC);
            b.addPassenger(p);
        } else {
            sender.sendMessage(ChatColor.RED+"Only players can run this command.");
        }
        return true;
    }

    public static boolean playGame(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        Sender.sendMessage(ChatColor.YELLOW + "Warning: If possible use /queuegame instead");
        Sender.sendMessage("Loading game " + Args[0]);

        Game game = Game_Main.getRegisteredGame(Args[0]);

        if (game == null) {
            Sender.sendMessage(ChatColor.RED + "what game is " + Args[0] + " lol");

            for (Game gamase : Game_Main.RegisteredGames) {

                Sender.sendMessage(gamase.getName());

            }
        } else {
            game.initGameLoader( ((Player) Sender).getWorld() );
        }

        return true;

    }

}
