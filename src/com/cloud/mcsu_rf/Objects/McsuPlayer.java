package com.cloud.mcsu_rf.Objects;

import com.cloud.mcsu_rf.Objects.McsuScoreboard.McsuScoreboard;
import com.cloud.mcsu_rf.Objects.McsuScoreboard.ScoreboardElements.TeamTotalPoints;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class McsuPlayer {

    //Static Elements

    public static ArrayList<McsuPlayer> McsuPlayers = new ArrayList<>();

    public static McsuPlayer fromBukkit(Player bukkitPlayer) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (mcsuPlayer.toBukkit() == bukkitPlayer) {
                return mcsuPlayer;
            }
        }

        return null;
    }
    public static McsuPlayer getPlayerByUUID(String UUID) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (Objects.equals(mcsuPlayer.toBukkit().getUniqueId().toString(), UUID)) {
                return mcsuPlayer;
            }
        }

        Bukkit.getLogger().warning("Could not find any player with UUID of " + UUID + " (are they offline?)");
        /*Bukkit.getLogger().warning("All player UUIDs:");
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            Bukkit.getLogger().warning(String.valueOf(mcsuPlayer.getBukkitPlayer().getUniqueId()));
        }*/
        return null;
    }
    public static McsuPlayer getPlayerByName(String Name) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (Objects.equals(mcsuPlayer.toBukkit().getName(), Name)) {
                return mcsuPlayer;
            }
        }

        return null;
    }

    public static void registerPlayer(Player p) {

        Bukkit.getLogger().info("Registering player " + p.getName());

        AtomicBoolean playerExists = new AtomicBoolean(false);

        McsuPlayers.forEach(mcsuPlayer -> {
            if (mcsuPlayer.toBukkit().getUniqueId() == p.getUniqueId()) {
                Bukkit.getLogger().info("Player " + p.getName() + " is already registered!");
                playerExists.set(true);
            }
        });

        if (!playerExists.get()) {
            McsuPlayer.McsuPlayers.add(new McsuPlayer(p));

            for ( McsuTeam team : TeamMain.Teams ) {
                if (team.getMemberUUIDs() != null) {
                    if (team.getMemberUUIDs().contains(p.getUniqueId().toString())) {
                        assert McsuPlayer.fromBukkit(p) != null;
                        McsuPlayer.fromBukkit(p).setTeam(team);
                        TeamMain.refreshTeamsCalculatedPoints();
                    }
                }
            }
        } else {
            Bukkit.getLogger().info("Player " + p.getName() + " was attempted to be registered but already exists");
        }

        McsuScoreboard.defaultScoreboard.bindPlayer(McsuPlayer.fromBukkit(p));

    }

    //Object defenition

    Player bukkitPlayer;
    String teamID = null;
    String Colour = String.valueOf(ChatColor.WHITE);
    int points = 0;

    public McsuPlayer(Player BukkitPlayer) {

        this.bukkitPlayer = BukkitPlayer;

    }

    public String getTeamID() { return this.teamID; }
    public Player toBukkit() { return this.bukkitPlayer; }
    public int getPoints() { return this.points; }
    public String getColouredName() { return this.Colour + this.bukkitPlayer.getName() + ChatColor.RESET; }
    public String getColouredName(boolean bold) { return this.Colour + (bold ? ChatColor.BOLD : "") + this.bukkitPlayer.getName(); }
    public String getName() { return this.bukkitPlayer.getName(); }
    public String getColour() { return Colour; }

    public void setTeam(McsuTeam newTeam) {

        this.teamID = newTeam.TeamID;
        this.Colour = newTeam.getChatColour();
        bukkitPlayer.setDisplayName(getColouredName());
        bukkitPlayer.setPlayerListName(getColouredName());

    }

    public int awardPoints(int points) {

        this.points += points;
        Bukkit.broadcastMessage("(Debug) " + getColouredName()  + " has recived " + points + " points!");
        TeamMain.refreshTeamsCalculatedPoints();
        TeamTotalPoints.update();

        return this.points;

    }



}
