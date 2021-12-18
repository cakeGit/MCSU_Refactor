package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.ShorthandClasses.Break;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.ChatColor;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Game_Cmds {

    public static MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    public static boolean listGames(CommandSender sender) {

        Break.line(sender);
        sender.sendMessage("All registered games:");
        for (Game game : Game_Main.RegisteredGames) {
            sender.sendMessage("  " + game.getName());
        }
        Break.line(sender);

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

    public static boolean playGame(CommandSender sender, String[] args) {

        Game game = Game_Main.getRegisteredGame(args[0]);

        if (game == null) {

            sender.sendMessage(ChatColor.RED + "[MCSU]: Couldn't find a game with name  " + args[0]);

            listGames(sender);

        } else {

            sender.sendMessage("Loading game " + args[0]);

            game.initGameLoader( ((Player) sender).getWorld() );

        }

        return true;

    }

}
