package com.cloud.mcsu_rf.Objects.MCSU_Scoreboard;

import com.cloud.mcsu_rf.Config_Main;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class MCSU_Scoreboard {

    String Name;
    String Default_Name = ChatColor.RED+"§lMCSU";

    ArrayList<Scoreboard_Element> Elements = new ArrayList<>();

    public MCSU_Scoreboard() {
        this.Name = Default_Name;
    }

    public MCSU_Scoreboard(Scoreboard_Element[] Elements) {
        this.Elements.addAll(Arrays.asList(Elements));
        this.Name = Default_Name;
    }

    public void setName(String Name) { this.Name = Name; }

    public void addElement(Scoreboard_Element Element) { this.Elements.add(Element); }

    public void update() {

        for (Scoreboard_Element Element : this.Elements) {
            Element.update();
        }

    }

    public void update(ArrayList<ArrayList<String>> args) { //Arrayception
        int Custom_Arg_Index = 0;

        for (Scoreboard_Element Element : this.Elements) {
            if (!Objects.equals(Element.getType(), "Custom")) {
                Element.update();
            } else {
                Element.update(args.get(Custom_Arg_Index));
                Custom_Arg_Index += 1;
            }
        }
    }

    public Scoreboard toBukkitScoreboard() {

        this.update();
        Scoreboard Scoreboard = Scoreboard_Main.Scoreboard_Manager.getNewScoreboard();
        Objective Objective = Scoreboard.registerNewObjective(this.Name, "dummy", this.Name);

        for(McsuTeam mcsuTeam : TeamMain.Teams) {
            Team team = Scoreboard.registerNewTeam(mcsuTeam.getTeamID());
            team.setDisplayName(mcsuTeam.getStyledName());
            //team.setColor(TeamSwitchStatements.toColour(mcsuTeam.getTeamID()));
            team.setColor(ChatColor.translateAlternateColorCodes('§', mcsuTeam.getChatColour()));
            team.setPrefix(mcsuTeam.getChatColour()+"["+mcsuTeam.getTeamID().toUpperCase()+" ]");
            for(String uuids : mcsuTeam.getMemberUUIDs()) {
                team.addEntry(Bukkit.getPlayer(uuids).getName());
            }
        }

        Objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        int Current_Score = 0;

        for (Scoreboard_Element Element : this.Elements) {

            for (String String : Element.getContent()) {
                Score Score = Objective.getScore(String);
                Score.setScore(Current_Score);
                Current_Score += 1;
            }

        }

        return Scoreboard;
    }
    
}
