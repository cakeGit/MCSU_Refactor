package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Map.MapMetadata;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Break;
import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.TreeSpecies;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class Game_Cmds {

    public static MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    public static boolean listGames(CommandSender sender) {

        Break.line(sender);
        sender.sendMessage("All registered games:");
        for (Game game : Game_Main.RegisteredGames) {
            sender.sendMessage("  " + game.getName() + " (ID: "+game.getId()+")");
        }
        Break.line(sender);

        return true;

    }

    private static void listMaps(CommandSender sender, String game, String mapName) {

        Break.line(sender);
        sender.sendMessage("All registered maps for " + game + " ("+mapName+" isn't valid): ");
        for (MapMetadata meta : MapMetadata.RegisteredMapMetadata) {
            if (meta.getGame().toLowerCase(Locale.ROOT).equals(game.toLowerCase(Locale.ROOT))) {
                sender.sendMessage("  " + meta.getName());
            }
        }
        Break.line(sender);

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

    public static boolean playGame(CommandSender sender, String[] args) {

        Game game = Game_Main.getRegisteredGame(args[0]);

        if (game == null) {

            sender.sendMessage(ChatColor.RED + "[MCSU]: Couldn't find a game with name  " + args[0]);

            listGames(sender);

        } else {

            if (args.length > 1) {
                String mapName = String.join(" ", Arrays.copyOfRange(args, 1, args.length));

                //Check if valid map
                boolean found = false;
                MapMetadata map = null;

                for (MapMetadata meta : MapMetadata.RegisteredMapMetadata) {
                    if (meta.getName().toLowerCase(Locale.ROOT).equals(mapName.toLowerCase(Locale.ROOT)) &&
                            Objects.equals(meta.getGame(), args[0])
                            ) {
                        found = true;
                        map = meta;
                    }
                }

                if (!found) {
                    listMaps(sender, args[0], mapName);
                } else {

                    sender.sendMessage(ChatColor.BLUE+"Loading "+ args[0] +" with map "+ mapName);

                    game.initGameLoader( ((Player) sender).getWorld(), map);
                }
            } else {

                sender.sendMessage(ChatColor.BLUE+"Loading "+ args[0]);

                game.initGameLoader( ((Player) sender).getWorld(), null);
            }


        }

        return true;

    }

    public static boolean forceEndGame(CommandSender sender, String[] args) {
        Game.currentGame.endGame();
        return true;
    }
}
