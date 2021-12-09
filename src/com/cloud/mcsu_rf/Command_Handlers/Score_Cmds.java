package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import static java.lang.Integer.parseInt;

public class Score_Cmds {

    public static boolean giveScore(CommandSender Sender, Command Cmd, String Label, String[] Args) {

        MCSU_Team Team = Team_Main.getTeamById( Args[0] );

        if ( Team != null) {

            try {
                Team.Points = parseInt( Args[1] );
            } catch (Exception err) {
                Sender.sendMessage(Args[1] + " isnt an int im quite sure");
            }

        } else {
            Sender.sendMessage("Team with Id '" + Args[0] + "' is not recognised");
        }

        return true;

    }

}
