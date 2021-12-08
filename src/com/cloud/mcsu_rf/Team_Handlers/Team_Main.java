package com.cloud.mcsu_rf.Team_Handlers;

import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Team_Main {

    public static ArrayList<MCSU_Team> Teams = new ArrayList<MCSU_Team>();

    static void initTeams() {
        Teams.add(new MCSU_Team("Blue Smurfs", "b", ChatColor.BLUE));
        Teams.add(new MCSU_Team("Red Reindeers", "r", ChatColor.RED));
        Teams.add(new MCSU_Team("Green Grinches", "gn", ChatColor.GREEN));
        Teams.add(new MCSU_Team("Yellow Yodellers", "y", ChatColor.YELLOW));
        Teams.add(new MCSU_Team("Aqua Angels", "a", ChatColor.AQUA));
        Teams.add(new MCSU_Team("Pink Pipers", "p", ChatColor.LIGHT_PURPLE));
        Teams.add(new MCSU_Team("White Wise Men", "w", ChatColor.WHITE));
        Teams.add(new MCSU_Team("Gray Gingerbreads", "gy", ChatColor.GRAY));
    }

    public static void init() {
        initTeams();
        Scoreboard_Main.onTeamsLoaded();
    }

    public static ArrayList<MCSU_Team> getSortedTeams() {
        ArrayList<MCSU_Team> sortedTeams = Teams;

        sortedTeams.sort((t1, t2) -> {
            if (t1.getPoints() > t2.getPoints())
                return 1;
            if (t1.getPoints() < t2.getPoints())
                return -1;
            return 0;
        });

        return sortedTeams;
    }

}
