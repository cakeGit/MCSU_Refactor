package com.cloud.mcsu_rf.Team_Handlers;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Objects.ConfigFile;
import com.cloud.mcsu_rf.Objects.MCSU_Player;
import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import org.bukkit.Bukkit;

import java.util.*;

public class Team_Main {

    public static ArrayList<MCSU_Team> Teams = new ArrayList<>();

    public static ConfigFile teamRegister = Config_Main.getByID("t");
    public static List<HashMap> teamList = (List<HashMap>) teamRegister.config.getList("Teams");

    static void initTeams() {

        /*if (teamList == null) {
            teamList = new ArrayList<MCSU_Team>();
        }

        teamList.add(new MCSU_Team("Pink Pandasas", "sp", ChatColor.LIGHT_PURPLE.toString(), new String[] {} ));

        teamRegister.config.set("Teams", teamList);
        teamRegister.saveDat();*/

        assert teamList != null;
        for (HashMap<String, Object> teamHash : teamList) {
            Bukkit.getLogger().info("Loading team from config: " + teamHash.get("Name"));
            Teams.add(new MCSU_Team((String) teamHash.get("Name"), (String) teamHash.get("TeamID"), (String) teamHash.get("ChatColour"), (ArrayList<String>) teamHash.get("memberUUIDs") ));
        }

        //teamRegister.config.set("Teams", teamList);
        //teamRegister.saveDat();
        //Teams = (ArrayList<MCSU_Team>) teamList;
    }

    public static void saveTeamList() {
        teamRegister.config.set("Teams", teamList);
        teamRegister.saveDat();
    }

    public static void init() {
        initTeams();
        Scoreboard_Main.onTeamsLoaded();
    }

    public static MCSU_Team getTeamById(String ID) {
        return Teams.stream().filter(Team -> Objects.equals(Team.getTeamID(), ID)).findFirst().orElse(null);
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

    public static void refreshAllTeamPoints() {
        for (MCSU_Team team : Teams) {
            int teamPoints = 0;

            for ( String playerUUID : team.getMemberUUIDs() ) {

                teamPoints += Objects.requireNonNull(MCSU_Player.getPlayerByUUID(playerUUID)).getPoints();

            }

            team.setPoints(teamPoints);
        }
    }

}
