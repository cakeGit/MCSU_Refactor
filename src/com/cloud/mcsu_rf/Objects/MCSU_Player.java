package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main;
import com.cloud.mcsu_rf.Team_Handlers.Team_Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

import static com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main.reloadScoreboard;

public class MCSU_Player {

    //Static Elements

    public static ArrayList<MCSU_Player> MCSU_Players = new ArrayList<>();
    public static MCSU_Player getPlayerByBukkitPlayer(Player bukkitPlayer) {
        for (MCSU_Player mcsuPlayer: MCSU_Players) {
            if (mcsuPlayer.bukkitPlayer == bukkitPlayer) {
                return mcsuPlayer;
            }
        }

        return null;
    }
    public static MCSU_Player getPlayerByUUID(String UUID) {
        for (MCSU_Player mcsuPlayer: MCSU_Players) {
            if (Objects.equals(mcsuPlayer.bukkitPlayer.getUniqueId().toString(), UUID)) {
                return mcsuPlayer;
            }
        }

        return null;
    }
    public static MCSU_Player getPlayerByName(String Name) {
        for (MCSU_Player mcsuPlayer: MCSU_Players) {
            if (Objects.equals(mcsuPlayer.bukkitPlayer.getName(), Name)) {
                return mcsuPlayer;
            }
        }

        return null;
    }

    //Object defenition

    Player bukkitPlayer;
    String teamID = null;
    String Colour = String.valueOf(ChatColor.WHITE);
    int points = 0;

    public MCSU_Player(Player BukkitPlayer) {

        this.bukkitPlayer = BukkitPlayer;
        MCSU_Players.add(this);

    }

    public String getTeamID() { return this.teamID; }
    public Player getBukkitPlayer() { return this.bukkitPlayer; }
    public int getPoints() { return this.points; }
    public String getColouredName() { return this.Colour + this.bukkitPlayer.getName() + ChatColor.RESET; }
    public String getName() { return this.bukkitPlayer.getName(); }

    public void setTeam(MCSU_Team newTeam) {

        this.teamID = newTeam.TeamID;
        this.Colour = newTeam.getChatColour();
        bukkitPlayer.setDisplayName(getColouredName());
        bukkitPlayer.setPlayerListName(getColouredName());

    }

    public void setScoreboard(MCSU_Scoreboard scoreboard) {
        this.bukkitPlayer.setScoreboard(scoreboard.toBukkitScoreboard());
    }

    public int awardPoints(int points) {
        this.points += points;
        Bukkit.broadcastMessage("(Debug) " + getColouredName()  + " has recived " + points + " points!");
        Team_Main.refreshAllTeamPoints();
        reloadScoreboard();


        return this.points;
    }



}
