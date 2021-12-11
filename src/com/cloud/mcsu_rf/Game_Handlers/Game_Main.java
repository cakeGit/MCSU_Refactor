package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.Games.TestGame;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

//Please note the actual Game object is in the objects package

//Game contains the data of the game
//Basegame contains the handlers

public class Game_Main {

    public static ArrayList<Game> RegisteredGames = new ArrayList<>();

    public static void registerGames() {

        TestGame testGame = new TestGame(); testGame.init();

    }

    public static void init() {

        registerGames();

        Bukkit.getLogger().info(MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath());

    }

    public static Game getRegisteredGameByName(String Name) {

        for (Game game : RegisteredGames) {

            if (Objects.equals(game.getName(), Name)) {

                Bukkit.getLogger().info("found thinge");
                return game;

            }

        }

        Bukkit.getLogger().info("cannot find thing called " + Name);
        return null;
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
