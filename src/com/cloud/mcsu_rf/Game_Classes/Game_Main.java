package com.cloud.mcsu_rf.Game_Classes;

import com.cloud.mcsu_rf.Games.testgame;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;


//Please note the actual Game object is in the objects package

public class Game_Main {
    public static ArrayList<Game> RegisteredGames = new ArrayList<>();

    public static void Games_Init() {

        testgame.Init(); //If possible make it run every file in Games but this works


        Bukkit.getLogger().info(MCSU_Main.Mcsu_Plugin.getDataFolder() + File.separator + "/schematics/hideandseek.schem");

    }

    public static void Load_Game(String Name, World World, String[] args) {

        Schematic_Loader.Load_Schematic(
                new File("/schematics/" + Name + ".schem"),
                BlockVector3.at(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])),
                World
                );


    }
}
