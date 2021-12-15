package com.cloud.mcsu_rf.Score_Handlers;

import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.Scoreboard_Element;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard_Main {
    public static ScoreboardManager Scoreboard_Manager = Bukkit.getScoreboardManager();

    public static MCSU_Scoreboard Default = new MCSU_Scoreboard(new Scoreboard_Element[] { new Scoreboard_Element("Bottom_Line_Break"), new Scoreboard_Element("Team_Totals"), new Scoreboard_Element("Line_Break"), new Scoreboard_Element("Online_Players"), new Scoreboard_Element("Top_Line_Break") });

    public static MCSU_Scoreboard Current_Scoreboard;

    public static void init() { }

    public static void onTeamsLoaded() {

        Current_Scoreboard = Default;

    }
    public static void reloadScoreboard() {

        for (Player player:
             Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Current_Scoreboard.toBukkitScoreboard());

        }

    }

}
