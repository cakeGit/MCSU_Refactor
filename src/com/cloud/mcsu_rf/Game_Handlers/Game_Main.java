package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.Games.*;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

//Please note the actual Game object is in the objects package

//Game contains the data of the game
//Basegame contains the handlers

public class Game_Main {

    public static ArrayList<Game> RegisteredGames = new ArrayList<>();

    public static void init() {

        registerGames();

        Bukkit.getLogger().info(MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath());

    }

    public static void registerGames() {

        new Spleef().init();
        new Skybattle().init();
        new BlockSumo().init();
        new SurvivalGames().init();
        new Slimekour().init();
        new MCTriathlon().init();
        new SnowballFight().init();
        new KOH().init();

    }

    public static Game getRegisteredGame(String id) {

        for (Game game : RegisteredGames) {

            if (Objects.equals(game.getId().toLowerCase(Locale.ROOT), id.toLowerCase(Locale.ROOT))) {

                return game;

            }

        }

        return null;
    }

}
