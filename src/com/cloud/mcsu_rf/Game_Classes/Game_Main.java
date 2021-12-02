package com.cloud.mcsu_rf.Game_Classes;

import com.cloud.mcsu_rf.Games.testgame;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.ArrayList;


//Please note the actual Game object is in the objects package

public class Game_Main {
    public static ArrayList<Game> RegisteredGames = new ArrayList<>();

    public static void Games_Init() {

        testgame.Init(); //If possible make it run every file in Games but this works

    }

    public static void Load_Game(String Name, CommandSender Sender, String[] args) {

        Schematic_Loader.Load_Schematic(
                new File(MCSU_Main.Data_Folder + File.separator + "/schematics/" + Name + ".schem"),
                BlockVector3.at(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])),
                ((Player) Sender).getWorld()
                );


    }
}
