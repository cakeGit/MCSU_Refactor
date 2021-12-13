package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class MCSU_Player {

    public static ArrayList<MCSU_Player> MCSU_Players = new ArrayList<>();
    public static MCSU_Player getPlayerByBukkitPlayer(Player bukkitPlayer) {
        for (MCSU_Player mcsuPlayer: MCSU_Players) {
            if (mcsuPlayer.bukkitPlayer == bukkitPlayer) {
                return mcsuPlayer;
            }
        }

        return null;
    }

    Player bukkitPlayer;
    String teamID = null;
    ChatColor Colour = ChatColor.WHITE;
    int points = 0;

    public MCSU_Player(Player BukkitPlayer) {

        this.bukkitPlayer = BukkitPlayer;
        MCSU_Players.add(this);

    }

    public String getTeamID() { return this.teamID; }

    public void setTeam(MCSU_Team newTeam) {
        this.teamID = newTeam.TeamID;
    }

    public int awardPoints(int points) {
        Bukkit.getServer().broadcastMessage("spak ]" + this.points);
        this.points += points;
        return this.points;
    }

    public void setSetScoreboard(MCSU_Scoreboard scoreboard) {
        this.bukkitPlayer.setScoreboard(scoreboard.toBukkitScoreboard());
    }

}
