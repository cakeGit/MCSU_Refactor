package com.cloud.mcsu_rf.Score_Handlers;

import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

public class Scoreboard_Main {
    public static ScoreboardManager Scoreboard_Manager = Bukkit.getScoreboardManager();
    public static MCSU_Scoreboard Default = new MCSU_Scoreboard();
    public static Scoreboard Current_Scoreboard = Default.toBukkitScoreboard();

    public static void Init() {
        /*objective.setDisplayName("Custom Scoreboard");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Score first= objective.getScore("First Line");
        Score second = objective.getScore("Second Line");
        Score third = objective.getScore("Third Line");
        first.setScore(2);
        second.setScore(1);
        third.setScore(0);*/



    }

}
