package com.cloud.mcsu_rf.Definitions;

import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class McsuTeam {

    String Name;
    String Shorthand;
    String TeamID;
    String ChatColour;
    int teamPoints;

    ArrayList<String> memberUUIDs;
    ArrayList<Player> members;
    ArrayList<McsuPlayer> team;

    public McsuTeam(String Name, String Shorthand, String TeamID, String Colour, ArrayList<String> memberUUIDs, int teamPoints) {

        this.Name = Name;
        this.Shorthand = Shorthand;
        this.TeamID = TeamID;
        this.ChatColour = Colour;
        this.memberUUIDs = memberUUIDs;
        this.teamPoints = teamPoints;

        members = new ArrayList<>();
        for (String memberUuid : memberUUIDs) {
            members.add(Bukkit.getPlayer(memberUuid));
        }

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
    public McsuPlayer[] getMembers() {
        return McsuPlayer.McsuPlayers.stream().filter(player -> memberUUIDs.contains(player.bukkitPlayer.getUniqueId().toString())).toArray(McsuPlayer[]::new);
    }
    public String getChatColour() { return ChatColour; }
    public String getTeamID() { return TeamID; }
    public String getStyledName() { return ChatColour + Name + ChatColor.RESET; }
    public String getRawName() { return Name; }
    public int getTeamPoints() { return teamPoints; }
    public String toScoreboardString() { return toScoreboardString(true); }
    public String toScoreboardString(boolean indent) {
        return (indent ? "  " : "") + ChatColor.GRAY + TeamMain.getTeamRanking(this)+ ". " + ChatColour + Name + ChatColor.WHITE + ": " + teamPoints;
    }
    public String toScoreboardString(McsuPlayer player) { return (Objects.equals(player.getTeamID(), TeamID) ? "> " : "  ") +
            toScoreboardString(false); }

    public void addMember(McsuPlayer player) { memberUUIDs.add(player.toBukkit().getUniqueId().toString()); members.add(player.toBukkit()); saveYaml(); }
    public void removeMember(McsuPlayer player) { memberUUIDs.remove(player.toBukkit().getUniqueId().toString()); members.remove(player); saveYaml(); }

    public int awardPoints(int teamPoints) { return awardPoints(teamPoints, null, false); }
    public int awardPoints(int teamPoints, String reason) { return awardPoints(teamPoints, reason, true); }

    public int awardPoints(int teamPoints, String reason, boolean announce) {

        this.teamPoints += teamPoints;
        saveYaml();
        if (announce) {
            for (McsuPlayer player : getMembers()) {
                player.notifyTeamPoints(teamPoints, reason);
            }
        }

        return teamPoints;

    }

}
