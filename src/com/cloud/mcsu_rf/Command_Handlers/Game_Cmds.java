package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

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
            WanderingTrader santa = (WanderingTrader) w.spawnEntity(p.getLocation(), EntityType.WANDERING_TRADER);
            String wh = "§f";
            String r = "§c";
            santa.setCustomName(ChatColor.BOLD+r+"S"+wh+"a"+r+"n"+wh+"t"+r+"a");
            santa.setCustomNameVisible(true);
            b.addPassenger(santa);
            String[] reindeerNames = {
                    "Dasher",
                    "Dancer",
                    "Prancer",
                    "Vixen",
                    "Comet",
                    "Cupid",
                    "Donner",
                    "Blitzen",
                    "Rudolph"
            };
            //String direction;
            int distance = 2;
            for(int i = 0; i < 9; i++) {
                Location spawnLoc = p.getLocation();
                /*
                if(p.getLocation().getYaw() == -180) {
                    direction = "North";
                } else if(p.getLocation().getYaw() == -90) {
                    direction = "East";
                } else if(p.getLocation().getYaw() == 0) {
                    direction = "South";
                } else if(p.getLocation().getYaw() == 90) {
                    direction = "West";
                }
                 */
                spawnLoc.setZ(p.getLocation().getZ()-distance);
                TraderLlama reindeer = (TraderLlama) w.spawnEntity(spawnLoc, EntityType.TRADER_LLAMA);
                distance += 2;
                //reindeer.setLeashHolder(santa);
                reindeer.setAI(false);
                reindeer.setCustomName(ChatColor.GOLD+reindeerNames[i]);
                reindeer.setCustomNameVisible(true);
            }
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
