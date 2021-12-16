package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.cloud.mcsu_rf.Score_Handlers.Scoreboard_Main.reloadScoreboard;

public class McsuPlayer {

    //Static Elements

    public static ArrayList<McsuPlayer> McsuPlayers = new ArrayList<>();

    public static McsuPlayer getPlayerByBukkitPlayer(Player bukkitPlayer) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (mcsuPlayer.getBukkitPlayer() == bukkitPlayer) {
                return mcsuPlayer;
            }
        }

        return null;
    }
    public static McsuPlayer getPlayerByUUID(String UUID) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (Objects.equals(mcsuPlayer.getBukkitPlayer().getUniqueId().toString(), UUID)) {
                return mcsuPlayer;
            }
        }

        Bukkit.getLogger().warning("Could not find any player with UUID of " + UUID);
        Bukkit.getLogger().warning("All player UUIDs");
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            Bukkit.getLogger().warning(String.valueOf(mcsuPlayer.getBukkitPlayer().getUniqueId()));
        }
        return null;
    }
    public static McsuPlayer getPlayerByName(String Name) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (Objects.equals(mcsuPlayer.getBukkitPlayer().getName(), Name)) {
                return mcsuPlayer;
            }
        }

        return null;
    }

    public static void registerPlayer(Player p) {

        Bukkit.getLogger().info("Registering player " + p.getName());

        AtomicBoolean playerExists = new AtomicBoolean(false);

        McsuPlayers.forEach(mcsuPlayer -> {
            if (mcsuPlayer.getBukkitPlayer() == p) {
                playerExists.set(true);
            }
        });

        if (!playerExists.get()) {
            McsuPlayer.McsuPlayers.add(new McsuPlayer(p));

            for ( McsuTeam team : TeamMain.Teams ) {
                if (team.getMemberUUIDs() != null) {
                    if (team.getMemberUUIDs().contains(p.getUniqueId().toString())) {
                        assert McsuPlayer.getPlayerByBukkitPlayer(p) != null;
                        McsuPlayer.getPlayerByBukkitPlayer(p).setTeam(team);
                        TeamMain.refreshAllTeamPoints();
                    }
                }
            }
        } else {
            Bukkit.getLogger().info("Player " + p.getName() + " was attempted to be registered but already exists");
        }

        reloadScoreboard();

    }

    //Object defenition

    Player bukkitPlayer;
    String teamID = null;
    String Colour = String.valueOf(ChatColor.WHITE);
    int points = 0;

    public McsuPlayer(Player BukkitPlayer) {

        this.bukkitPlayer = BukkitPlayer;
        McsuPlayers.add(this);

    }

    public String getTeamID() { return this.teamID; }
    public Player getBukkitPlayer() { return this.bukkitPlayer; }
    public int getPoints() { return this.points; }
    public String getColouredName() { return this.Colour + this.bukkitPlayer.getName() + ChatColor.RESET; }
    public String getName() { return this.bukkitPlayer.getName(); }

    public void setTeam(McsuTeam newTeam) {

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
        TeamMain.refreshAllTeamPoints();
        reloadScoreboard();


        return this.points;
    }



}
