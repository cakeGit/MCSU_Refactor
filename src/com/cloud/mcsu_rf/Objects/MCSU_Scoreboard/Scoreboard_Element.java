package com.cloud.mcsu_rf.Objects.MCSU_Scoreboard;

import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;

import java.util.ArrayList;

public class Scoreboard_Element {

    String Type;
    ArrayList<String> Content;

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

        this.Content = new ArrayList(); // empty the array

        switch (this.Type) {

            case "Team_Totals":

                 for ( MCSU_Team team : Team_Main.getSortedTeams() ) {

                     this.Content.add(team.toScoreboardString());

                 }

                break;


            case "Team_Game_Totals":

                this.Content.add("ha ha not done yet - do when current game is in");
                break;


            case "Custom":

                throw new IllegalArgumentException(
                        "Update called on a scoreboard element " +
                        "of type 'Custom' without arguments"
                );

        }



    }

    public void update(ArrayList<String> args) { // Used for fully custom elements

        if (this.Type == "Custom") {
            this.Content = args;
        } else {
            throw new IllegalArgumentException("Argument given to a Non-Custom Element");
        }

    }

}
