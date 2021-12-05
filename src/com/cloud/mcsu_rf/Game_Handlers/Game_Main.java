package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.Games.testgame;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.World;
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

    public static void loadGame(String Game_Name, Player player, String[] args) { //TODO: fix pls
        World world = player.getWorld();

        player.sendMessage("world is null");
        player.sendMessage(player.toString());
        player.sendMessage(player.getClass().toString());
        player.sendMessage(world.getName());
        player.sendMessage("playyer is not null????");

        //MCSU_Main.Mcsu_Plugin.getLogger().info(MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath() + File.separator + "/schematics/" + Game_Name + ".schem");

        Schematic_Loader.loadSchematic("testgame", BlockVector3.at(0, 70, 0), world);


    }
}
