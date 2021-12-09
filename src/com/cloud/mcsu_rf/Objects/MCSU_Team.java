package com.cloud.mcsu_rf.Objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MCSU_Team {

    public String Name;
    public String TeamID;
    public ChatColor ChatColour;
    public int Points = 0;

    ArrayList<Player> Members;

    public MCSU_Team(String Name, String TeamID, ChatColor Colour) {

        this.Name = Name;
        this.TeamID = TeamID;
        this.ChatColour = Colour;

    }

    public String toScoreboardString() {

        return this.ChatColour + this.Name + ChatColor.WHITE + ": " + Points;

    }

    public String getColouredName() {

        return this.ChatColour + this.Name;

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
