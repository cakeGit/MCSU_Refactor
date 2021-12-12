package com.cloud.mcsu_rf.Game_Handlers;

import com.cloud.mcsu_rf.Games.TestGame;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.ActivityRule;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Objects;

//Please note the actual Game object is in the objects package

//Game contains the data of the game
//Basegame contains the handlers

public class Game_Main {

    public static ArrayList<Game> RegisteredGames = new ArrayList<>();
    public static ArrayList<ActivityRule> activityRules = new ArrayList<>();

    public static void init() {

        registerGames();
        registerActivityRules();

        Bukkit.getLogger().info(MCSU_Main.Mcsu_Plugin.getDataFolder().getAbsolutePath());

    }


    public static void registerActivityRules() {

        new ActivityRule("TileDrops", true);
        new ActivityRule("PVP", true);

    }

    public static void registerGames() {

        TestGame testGame = new TestGame(); testGame.init();

    }

    public static Game getRegisteredGame(String Name) {

        for (Game game : RegisteredGames) {

            if (Objects.equals(game.getName(), Name)) {

                return game;

            }

        }

        return null;
    }

    public static ActivityRule getActivityRule(String name) {

        for (ActivityRule activityRule : activityRules) {

            if (Objects.equals(activityRule.getName(), name)) {

                return activityRule;

            }

        }

        return null;
    }

}
