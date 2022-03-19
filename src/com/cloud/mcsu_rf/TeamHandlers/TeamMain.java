package com.cloud.mcsu_rf.TeamHandlers;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Objects.ConfigFile;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class TeamMain {

    public static ArrayList<McsuTeam> Teams = new ArrayList<>();

    public static ConfigFile teamRegister = Config_Main.getByID("t");
    public static List<HashMap> teamList = (List<HashMap>) teamRegister.config.getList("Teams");

    static void initTeams() {
        assert teamList != null;

        for (HashMap<String, Object> teamHash : teamList) {
            Bukkit.getLogger().info("Loading team from config: " + teamHash.get("Name"));
            Teams.add(new McsuTeam(
                    (String) teamHash.get("Name"),
                    (String) teamHash.get("TeamID"),
                    (String) teamHash.get("ChatColour"),
                    (ArrayList<String>) teamHash.get("memberUUIDs"),
                    (int) teamHash.get("Points"))
            );
        }

        new TeamMain().scoborardteaminit();

    }

    void scoborardteaminit() {


        for(McsuTeam mcsuTeam : TeamMain.Teams) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();

            Scoreboard board = manager.getNewScoreboard();

            Team team = board.registerNewTeam(mcsuTeam.getTeamID());

            Bukkit.broadcastMessage("pp");

            for (String playerUUID : mcsuTeam.getMemberUUIDs()) {
                if (Bukkit.getPlayer(playerUUID) != null) {
                    team.addPlayer(Objects.requireNonNull(Bukkit.getPlayer(playerUUID)));
                } else {
                    Bukkit.broadcastMessage("Player with UUID "+playerUUID+" wasn't registered,");
                }
            }
            //Adding players

            //Adding prefixes (shows up in player list before the player's name, supports ChatColors)
            team.setPrefix("prefix");

            //Adding suffixes (shows up in player list after the player's name, supports ChatColors)
            team.setSuffix("suffix");

            //Setting the display name
            team.setDisplayName("display name");

            //Making invisible players on the same team have a transparent body
            team.setCanSeeFriendlyInvisibles(true);

            //Making it so players can't hurt others on the same team
            team.setAllowFriendlyFire(false);
        }
    }

    public static void saveTeamList() {
        teamRegister.config.set("Teams", teamList);
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
            return Integer.compare(t2.getCalculatedPoints(), t1.getCalculatedPoints());
        });

        return sortedTeams;
    }

    public static void refreshTeamsCalculatedPoints() {

        for (McsuTeam team : Teams) {

            int teamPoints = team.getTeamPoints(); // gets the teams's points from winning games and things

            if (team.getMemberUUIDs() == null) {

                Bukkit.getLogger().info("[MCSU]: Team with name" +team.getRawName() + " has null member uuids");

            } else {

                for ( String playerUUID : team.getMemberUUIDs() ) {

                    try {
                        teamPoints += Objects.requireNonNull(McsuPlayer.getPlayerByUUID(playerUUID)).getPoints();
                    } catch (NullPointerException ignored) { }

                }

                team.setCalculatedPoints(teamPoints);

            }

        }

    }

}
