package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main.reloadScoreboard;

public class McsuTeam {

    String Name;
    String TeamID;
    String ChatColour;
    int teamPoints = 0;
    int calculatedPoints = 0;

    ArrayList<String> memberUUIDs;

    public McsuTeam(String Name, String TeamID, String Colour, ArrayList<String> memberUUIDs, int teamPoints) {

        this.Name = Name;
        this.TeamID = TeamID;
        this.ChatColour = Colour;
        this.memberUUIDs = memberUUIDs;
        this.teamPoints = teamPoints;

    }

    public void saveYaml() {
        for (HashMap<String, Object> teamHash : TeamMain.teamList) {
            if (Objects.equals(teamHash.get("Name"), Name)) {

                teamHash.put("TeamID", TeamID);
                teamHash.put("Points", teamPoints);
                teamHash.put("ChatColour", ChatColour);
                teamHash.put("memberUUIDs", memberUUIDs);

            }
        }
        TeamMain.saveTeamList();
    }

    public ArrayList<String> getMemberUUIDs() { return memberUUIDs;  }
    public String getChatColour() { return ChatColour; }
    public int getCalculatedPoints() { return calculatedPoints; }
    public String getTeamID() { return TeamID; }
    public String getStyledName() { return this.ChatColour + this.Name + ChatColor.RESET; }
    public String getRawName() { return this.Name; }
    public int getTeamPoints() { return teamPoints; }
    public String toScoreboardString() { return this.ChatColour + this.Name + ChatColor.WHITE + ": " + calculatedPoints; }

    public void setCalculatedPoints(int Points) { this.calculatedPoints = Points; saveYaml(); }
    public void addMember(String memberUUID) { this.memberUUIDs.add(memberUUID); saveYaml(); }
    public void removeMember(String memberUUID) { this.memberUUIDs.remove(memberUUID); saveYaml(); }
    public void awardTeamPoints(int teamPoints) {
        this.teamPoints += teamPoints;
        saveYaml();
        TeamMain.refreshTeamsCalculatedPoints();
        reloadScoreboard();
    }

}
