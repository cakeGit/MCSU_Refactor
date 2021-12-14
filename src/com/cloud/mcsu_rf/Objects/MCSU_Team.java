package com.cloud.mcsu_rf.Objects;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MCSU_Team {

    public String Name;
    public String TeamID;
    public String ChatColour;
    public int Points = 0;

    public ArrayList<String> memberUUIDs;

    public MCSU_Team(String Name, String TeamID, String Colour, ArrayList<String> memberUUIDs) {

        this.Name = Name;
        this.TeamID = TeamID;
        this.ChatColour = Colour;
        this.memberUUIDs = memberUUIDs;

    }

    public ArrayList<String> getMemberUUIDs() { return memberUUIDs;  }

    public String toScoreboardString() {

        return this.ChatColour + this.Name + ChatColor.WHITE + ": " + Points;

    }

    public String getColouredName() {

        return this.ChatColour + this.Name;

    }

}
