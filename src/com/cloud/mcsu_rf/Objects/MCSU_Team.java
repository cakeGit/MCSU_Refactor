package com.cloud.mcsu_rf.Objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MCSU_Team {

    String Name;
    String TeamID;
    ChatColor ChatColour;
    int Points = 0;

    ArrayList<Player> Members;

    public MCSU_Team(String Name, String TeamID, ChatColor Colour) {

        this.Name = Name;
        this.TeamID = TeamID;
        this.ChatColour = Colour;

    }

    public String getID() {
        return this.TeamID;
    }

    public String toScoreboardString() {
        return this.ChatColour + this.Name + ChatColor.WHITE + ": " + Points;
    }

    public String getName() {
        return this.ChatColour + this.Name;
    }

    public String getPlainTextName() {
        return this.Name;
    }

    public int getPoints() {
        return this.Points;
    }

    public ArrayList<Player> addMember(Player Member) {
        this.Members.add(Member);
        return this.Members;
    }

    public ArrayList<Player> removeMember(Player Member) {
        this.Members.remove(Member);
        return this.Members;
    }


    public ArrayList<String> getMemberNames() {
        ArrayList<String> Member_Names = new ArrayList();

        for (Player Member : Members) {
            Member_Names.add(Member.getName());
        }

        return Member_Names;
    }

}
