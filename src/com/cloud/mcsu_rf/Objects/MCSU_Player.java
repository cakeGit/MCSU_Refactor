package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MCSU_Player {

    Player bukkitPlayer;
    String teamID = null;
    ChatColor Colour = ChatColor.WHITE;

    public MCSU_Player(Player BukkitPlayer) {

        this.bukkitPlayer = BukkitPlayer;

    }

    public String getTeamID() {
        return this.teamID;
    }

    public void setTeam(MCSU_Team newTeam) {
        this.teamID = newTeam.getID();
    }

    public void setSetScoreboard(MCSU_Scoreboard scoreboard) {
        this.bukkitPlayer.setScoreboard(scoreboard.toBukkitScoreboard());
    }

}
