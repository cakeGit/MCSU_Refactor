package com.cloud.mcsu_rf.TeamHandlers;

import com.cak.what.ConfigApi.ConfigFile;
import com.cak.what.Util.ChCol;
import com.cloud.mcsu_rf.Definitions.McsuTeam;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TeamMain {

    public static ArrayList<McsuTeam> Teams = new ArrayList<>();

    public static ConfigFile teamRegister = new ConfigFile("teams.yml");
    public static List<HashMap> teamList = (List<HashMap>) teamRegister.getConfig().getList("Teams");

    static void initTeams() {
        assert teamList != null;

        Teams.add(new McsuTeam(
                "No Team",
                "None",
                "nt",
                ChCol.DARK_GREEN,
                new ArrayList<>(),
                0
        ));

        for (HashMap<String, Object> teamHash : teamList) {
            Bukkit.getLogger().info("Loading team from config: " + teamHash.get("Name"));
            Teams.add(new McsuTeam(
                    (String) teamHash.get("Name"),
                    (String) teamHash.get("Shorthand"),
                    (String) teamHash.get("TeamID"),
                    (String) teamHash.get("ChatColour"),
                    (ArrayList<String>) teamHash.get("memberUUIDs"),
                    (int) teamHash.get("Points"))
            );
        }

    }

    public static void saveTeamList() {
        teamRegister.getConfig().set("Teams", teamList);
        teamRegister.saveDat();
    }

    public static void init() {
        initTeams();
    }

    public static McsuTeam getTeamById(String ID) {
        return Teams.stream().filter(Team -> Objects.equals(Team.getTeamID(), ID)).findFirst().orElse(null);
    }
    public static int getTeamRanking(McsuTeam team) {
        return Teams.indexOf(team)+1;
    }


    public static ArrayList<McsuTeam> getSortedTeams() {
        ArrayList<McsuTeam> sortedTeams = Teams;
        sortedTeams.sort((t1, t2) -> {
            return Integer.compare(t2.getTeamPoints(), t1.getTeamPoints());
        });

        return sortedTeams;
    }


}
