package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.ShorthandClasses.Break;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import static java.lang.Integer.parseInt;

public class Score_Cmds {

    public static boolean giveScore(CommandSender sender, String[] args) {

        McsuPlayer Player = McsuPlayer.getPlayerByName(args[0]);

        if ( Player != null) {

            try {
                Player.awardPoints( parseInt( args[1] ) );
            } catch (Exception err) {
                sender.sendMessage(args[1] + " isnt an int im quite sure");
            }

        } else {
            sender.sendMessage("Player with name '" + args[0] + "' was not found");
        }

        return true;

    }

    public static boolean Team(CommandSender sender, String[] args) {

        switch (args[0]) {
            case "set":

                McsuPlayer Player = McsuPlayer.getPlayerByName(args[1]);

                if ( Player != null) {

                    try {
                        try {
                            McsuTeam oldTeam = TeamMain.getTeamById(Player.getTeamID());
                            oldTeam.removeMember(Player.getBukkitPlayer().getUniqueId().toString());
                        } catch (Exception e) {
                            Bukkit.getLogger().info(Player.getBukkitPlayer().getName() + " did not seem have a valid team prior to /team set, " +
                                    "could this be causing errors?");
                        }

                        McsuTeam team = TeamMain.getTeamById(args[2]);
                        Player.setTeam(team);
                        team.addMember(Player.getBukkitPlayer().getUniqueId().toString());

                        sender.sendMessage(ChatColor.AQUA + "[MCSU]: Successfully set player " + Player.getName() + "'s team to " + team.getStyledName());

                        TeamMain.refreshTeamsCalculatedPoints();
                    } catch (Exception err) {
                        sender.sendMessage(ChatColor.RED + "[MCSU]: Error in setting player team, are you sure that " +args[2] + " is a valid TeamID?");
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "[MCSU]: Player with name '" + args[1] + "' was not found");
                }
                break;
            case "list":
                Break.line(sender);
                sender.sendMessage("All loaded teams:");
                for (McsuTeam team : TeamMain.Teams) {
                    sender.sendMessage("  " + team.getStyledName() + ChatColor.GRAY + " (" + team.getTeamID() + ")");
                }
                Break.line(sender);
        }

        return true;

    }
}
