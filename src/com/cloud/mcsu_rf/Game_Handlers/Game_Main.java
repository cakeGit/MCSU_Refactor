package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.Games.testgame;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;

//Please note the actual Game object is in the objects package

public class Game_Main {
    public static ArrayList<Game> RegisteredGames = new ArrayList<>();

    public static void Init() {

        testgame.Init(); //If possible make it run every file in Games but this works

        Bukkit.getLogger().info(MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath());

    }

    public static void loadGame(String Game_Name, Player Player, String[] Args) { //TODO: fix pls
        org.bukkit.World World = Player.getWorld();

        Player.sendMessage("world is null");
        Player.sendMessage(Player.toString());
        Player.sendMessage(Player.getClass().toString());
        Player.sendMessage( ( (World) World).getName() );
        Player.sendMessage("playyer is not null????");

        MCSU_Main.Mcsu_Plugin.getLogger().info(MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath() + File.separator + "/schematics/" + Game_Name + ".schem");

        /*Schematic_Loader.Load_Schematic(
                new File( MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath() + File.separator + "/Game/" + Game_Name + ".schem"),
                BlockVector3.at(0, 70, 0),
                World
                );*/


    }
}
