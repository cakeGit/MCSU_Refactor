package com.cloud.mcsu_rf.Team_Handlers;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Objects.ConfigFile;
import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.fastasyncworldedit.core.configuration.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
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
        for (HashMap<String, Object> teamHash: teamList) {
            Bukkit.getLogger().info((String) teamHash.get("Name"));
            Teams.add(new MCSU_Team((String) teamHash.get("Name"), (String) teamHash.get("TeamID"), ChatColor.LIGHT_PURPLE.toString(), (ArrayList<String>) teamHash.get("memberUUIDs") ));
        }
        teamRegister.config.set("Teams", teamList);
        teamRegister.saveDat();



        //Teams = (ArrayList<MCSU_Team>) teamList;
    }

    public static void init() {
        initTeams();
        Scoreboard_Main.onTeamsLoaded();
    }

    public static MCSU_Team getTeamById(String ID) {
        return Teams.stream().filter(Team -> Objects.equals(Team.TeamID, ID)).findFirst().orElse(null);
    }

    public static ArrayList<MCSU_Team> getSortedTeams() {
        ArrayList<MCSU_Team> sortedTeams = Teams;
        sortedTeams.sort((t1, t2) -> {
            if (t1.Points > t2.Points)
                return 1;
            if (t1.Points < t2.Points)
                return -1;
            return 0;
        });

        return sortedTeams;
    }

}
