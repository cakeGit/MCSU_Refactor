package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MCSU_Team {

    String Name;
    String TeamID;
    String ChatColour;
    int Points = 0;

    ArrayList<String> memberUUIDs;

    public MCSU_Team(String Name, String TeamID, String Colour, ArrayList<String> memberUUIDs) {

        this.Name = Name;
        this.TeamID = TeamID;
        this.ChatColour = Colour;
        this.memberUUIDs = memberUUIDs;

    }

    public void saveYaml() {
        for (HashMap<String, Object> teamHash : Team_Main.teamList) {
            if (Objects.equals(teamHash.get("Name"), Name)) {

                teamHash.put("TeamID", TeamID);
                teamHash.put("Points", Points);
                teamHash.put("ChatColour", ChatColour);
                teamHash.put("memberUUIDs", memberUUIDs);

            }
        }
        Team_Main.saveTeamList();
    }

    public ArrayList<String> getMemberUUIDs() { return memberUUIDs;  }
    public String getChatColour() { return ChatColour; }
    public int getPoints() { return Points; }
    public String getTeamID() { return TeamID; }
    public String getName() { return this.ChatColour + this.Name + ChatColor.RESET; }
    public String getRawName() { return this.Name; }
    public String toScoreboardString() { return this.ChatColour + this.Name + ChatColor.WHITE + ": " + Points; }

    public void setPoints(int Points) { this.Points = Points; saveYaml(); }
    public void addMember(String memberUUID) { this.memberUUIDs.add(memberUUID); saveYaml(); }
    public void removeMember(String memberUUID) { this.memberUUIDs.remove(memberUUID); saveYaml(); }

}
