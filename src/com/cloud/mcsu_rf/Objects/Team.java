package com.cloud.mcsu_rf.Objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    String Name;
    ChatColor ChatColour;
    int Points = 0;

    ArrayList<Player> Members;

    public Team(String Name, ChatColor Colour) {

        this.Name = Name;
        this.ChatColour = Colour;

    }


    //String functions

    public String toScoreboardString() {
        return this.ChatColour + this.Name + ChatColour.WHITE + ": " + Points;
    }


    //Member Functions

    //Both return the updated member list for debugging
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
