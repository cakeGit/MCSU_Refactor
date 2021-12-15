package com.cloud.mcsu_rf.Command_Handlers;

import com.cloud.mcsu_rf.Objects.MCSU_Player;
import com.cloud.mcsu_rf.Objects.MCSU_Team;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Objects;

import static java.lang.Integer.parseInt;

public class Score_Cmds {

    public static boolean giveScore(CommandSender sender, String[] args) {

        MCSU_Player Player = MCSU_Player.getPlayerByName(args[0]);

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

                MCSU_Player Player = MCSU_Player.getPlayerByName(args[1]);

                if ( Player != null) {

                    try {
                        try {
                            MCSU_Team oldTeam = Team_Main.getTeamById(Player.getTeamID());
                            oldTeam.removeMember(Player.getBukkitPlayer().getUniqueId().toString());
                        } catch (Exception e) {
                            Bukkit.getLogger().info(Player.getBukkitPlayer().getName() + " did not seem have a valid team prior to /team set, " +
                                    "could this be causing errors?");
                        }

                        MCSU_Team team = Team_Main.getTeamById(args[2]);
                        Player.setTeam(team);
                        team.addMember(Player.getBukkitPlayer().getUniqueId().toString());

                        sender.sendMessage(ChatColor.AQUA + "[MCSU]: Successfully set player " + Player.getName() + "'s team to " + team.getName());

                        Team_Main.refreshAllTeamPoints();
                    } catch (Exception err) {
                        sender.sendMessage(ChatColor.RED + "[MCSU]: Error in setting player team, are you sure that " +args[2] + " is a valid TeamID?");
                    }

                } else {
                    sender.sendMessage(ChatColor.RED + "[MCSU]: Player with name '" + args[1] + "' was not found");
                }
                break;
            case "list":
                sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
                sender.sendMessage("All loaded teams:");
                for (MCSU_Team team : Team_Main.Teams) {
                    sender.sendMessage("  " + team.getName() + ChatColor.GRAY + " (" + team.getTeamID() + ")");
                }
                sender.sendMessage(ChatColor.DARK_GRAY + "------------------------------");
        }

        return true;

    }
}
