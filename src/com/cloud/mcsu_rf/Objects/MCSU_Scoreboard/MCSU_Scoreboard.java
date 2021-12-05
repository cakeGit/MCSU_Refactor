package com.cloud.mcsu_rf.Objects.MCSU_Scoreboard;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Locale;

public class MCSU_Scoreboard {

    String Name;
    String Default_Name = "MCSU";

    ArrayList<Scoreboard_Element> Elements = new ArrayList<>();

    public MCSU_Scoreboard() {

        this.Name = Default_Name;

    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void addElement(Scoreboard_Element Element) {
        Elements.add(Element);
    }

    public Scoreboard toBukkitScoreboard() {
        Scoreboard Scoreboard = Scoreboard_Main.Scoreboard_Manager.getNewScoreboard();
        Objective Objective = Scoreboard.registerNewObjective(Name.toLowerCase(), "dummy", Name);

        for (Scoreboard_Element Element : Elements) {

        }

        return Scoreboard;
    }
    
}
