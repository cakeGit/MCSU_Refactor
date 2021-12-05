package com.cloud.mcsu_rf.Team_Handlers;

import com.cloud.mcsu_rf.Objects.Team;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class Team_Main {

    public static ArrayList<Team> Teams;

    public static void initTeams() {
        Teams.add(new Team("Blue smurfs haha", ChatColor.BLUE));
    }

}
