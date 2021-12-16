package com.cloud.mcsu_rf.Objects.MCSU_Scoreboard;

import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Objects;

public class Scoreboard_Element {

    String Type;
    ArrayList<String> Content;
    Integer online;

    public Scoreboard_Element(String Type) {
 
        this.Type = Type;

    }

    public ArrayList<String> getContent() {
        return this.Content;
    }

    public String getType() {
        return this.Type;
    }

    public void update() {

        this.Content = new ArrayList<>(); // empty the array

        switch (this.Type) {

            case "Team_Totals":

                 for ( McsuTeam team : TeamMain.getSortedTeams() ) {

                     this.Content.add(" "+team.toScoreboardString());
                 }

                break;

            case "Online_Players":
                this.Content.add(ChatColor.RED+" Online: "+ChatColor.WHITE+Bukkit.getOnlinePlayers().size());
                break;

            case "Online_Players_Leave":
                int i = Bukkit.getOnlinePlayers().size()-1;
                this.Content.add(ChatColor.RED+" Online: "+ChatColor.WHITE+i);
                break;

            case "Line_Break":
                this.Content.add("");
                break;

            case "Top_Line_Break":
                this.Content.add(" ");
                break;

            case "Bottom_Line_Break":
                this.Content.add("  ");
                break;

            case "Team_Game_Totals":

                this.Content.add("ha ha not done yet");
                break;


            case "Custom":

                throw new IllegalArgumentException(
                        "Update called on a scoreboard element " +
                        "of type 'Custom' without arguments"
                );

        }



    }

    public void update(ArrayList<String> args) { // Used for fully custom elements

        if (Objects.equals(this.Type, "Custom")) {
            this.Content = args;
        } else {
            throw new IllegalArgumentException("Argument given to a Non-Custom Element");
        }

    }

}
