package com.cloud.mcsu_rf.Objects.McsuScoreboard;


import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements.FixedContent;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements.MapMetadataDisplay;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements.TeamTotalPoints;
import com.cloud.mcsu_rf.Objects.McsuTeam;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class McsuScoreboard {

    public static McsuScoreboard defaultScoreboard;

    public static void init() {

        defaultScoreboard = new McsuScoreboard(
                new MapMetadataDisplay(false),
                new FixedContent("",
                        ChatColor.RED+""+ChatColor.BOLD +"Team Scores"+ ChatColor.RESET +":"),
                new TeamTotalPoints(),
                new FixedContent("",
                        ChatColor.GRAY+""+ChatColor.ITALIC+"  Mcsu v"+
                                MCSU_Main.Mcsu_Plugin.getDescription().getVersion()
                )
        );

    }

    ArrayList<ScoreboardElementBase> scoreboardElements;
    ArrayList<McsuPlayer> boundPlayers = new ArrayList<>();
    String title = ChatColor.RED+""+ChatColor.BOLD +"   MCSU   "; // spaces for a minimum scoreboard width

    public McsuScoreboard(ScoreboardElementBase... Elements) {
        scoreboardElements = new ArrayList<>(Arrays.stream(Elements).toList());

        for (ScoreboardElementBase element : Elements) {
            element.boundScoreboard = this;
        }

    }

    public McsuScoreboard setTitle(String title) {
        this.title = title;
        return this;
    }

    public ArrayList<McsuPlayer> bindPlayer(McsuPlayer player) {
        return bindPlayer(player, true);
    }
    public ArrayList<McsuPlayer> bindPlayer(McsuPlayer player, boolean update) {
        boundPlayers.add(player);
        if(update){update();}
        return boundPlayers;
    }

    public Scoreboard generateScoreboard(McsuPlayer mcsuPlayer) {

        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(title, "dummy", title);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ArrayList<String> generatedContents = new ArrayList<>();

        // register teams

        for(McsuTeam mcsuTeam : TeamMain.getSortedTeams()) {
            Team team = scoreboard.registerNewTeam(mcsuTeam.getTeamID());
            team.setPrefix(mcsuTeam.getChatColour()+"["+mcsuTeam.getStyledName()+"] ");
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            for (McsuPlayer mcdsuPlayer : McsuPlayer.McsuPlayers) {
                Bukkit.getLogger().info("MCSU PLATYER : " + mcdsuPlayer.getName());
            }/*
            Bukkit.getLogger().info(McsuPlayer.fromBukkit(player).getName());*/
            /*Team team = scoreboard.getTeam(McsuPlayer.fromBukkit(player).getTeamID());
            team.addEntry(player.getName());*/
        }

        for (ScoreboardElementBase element : scoreboardElements) {
            generatedContents.addAll(Arrays.asList(element.generateContent(mcsuPlayer)));
        }

        for (int i = generatedContents.size(); i > 0; i--) {
            String content = generatedContents.get(i-1);
            int score = generatedContents.size()-i;

            if  (Objects.equals(content, "")) {
                content = " ".repeat(score);
            }

            if (content.length() <= 40) { // failsafe in case line is longer than 40 chars
                objective.getScore(content).setScore(score);
            } else {
                String uncutText = content;
                while (uncutText.length() > 40) {
                    objective.getScore(uncutText.substring(0, 40)).setScore(score);
                    score-=1;
                    uncutText = uncutText.substring(40);
                }
                objective.getScore(uncutText).setScore(score);
            }

        }


        return scoreboard;

    }


    public McsuScoreboard update() {

        for (McsuPlayer player : boundPlayers) {
            player.toBukkit().setScoreboard(generateScoreboard(player));
        }
        return this;

    }

    @Deprecated //not finished
    public McsuScoreboard playerSpecificUpdate(McsuPlayer player) {
        if (!boundPlayers.contains(player))
            throw new RuntimeException("[MCSU - McsuScoreboard]: Told to update a player which isnt bound to the scoreboard (replace this line for a warn if this is an issue)");
        return this;
    }



}