package com.cloud.mcsu_rf.Objects.MCSU_Scoreboard;

public class Scoreboard_Element {

    String Type;
    String[] Content;

    public Scoreboard_Element(String Type) {

        this.Type = Type;

    }

    public String[] getContent() {
        return this.Content;
    }

    public String getType() {
        return this.Type;
    }

    public void update() {

        switch (this.Type) {
            case "Team_Totals":
                this.Content = new String[] {"todo - implement teams lol"};
                break;
            case "Team_Game_Totals":
                this.Content = new String[] {"todo - implement teams and current games lol"};
                break;
            case "Custom":
                throw new IllegalArgumentException("Update called on a scoreboard element of type 'Custom' without arguments");
        }

    }

    public void update(String[] Args) { // Used for fully custom elements

        if (this.Type == "Custom") {
            this.Content = Args;
        } else {
            throw new IllegalArgumentException("Argument given to a Non-Custom Element");
        }

    }

}
