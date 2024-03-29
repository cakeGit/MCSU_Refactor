package com.cloud.mcsu_rf.Definitions;

import com.cloud.mcsu_rf.Definitions.McsuScoreboard.McsuScoreboard;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements.TeamTotalPoints;
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

        throw new IllegalArgumentException("Player '"+bukkitPlayer.getName()+"' not found in McsuPlayers");
    }

    public static McsuPlayer getPlayerByUUID(String UUID) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (Objects.equals(mcsuPlayer.toBukkit().getUniqueId().toString(), UUID)) {
                return mcsuPlayer;
            }
        }

        throw new IllegalArgumentException("Player '"+UUID+"' not found in McsuPlayers");

        /*Bukkit.getLogger().warning("All player UUIDs:");
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            Bukkit.getLogger().warning(String.valueOf(mcsuPlayer.getBukkitPlayer().getUniqueId()));
        }*/
    }

    @Deprecated //Use getPlayerByUUID() instead
    public static McsuPlayer getPlayerByName(String Name) {
        for (McsuPlayer mcsuPlayer: McsuPlayers) {
            if (Objects.equals(mcsuPlayer.toBukkit().getName(), Name)) {
                return mcsuPlayer;
            }
        }

        throw new IllegalArgumentException("Player '"+Name+"' not found in McsuPlayers");
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
                    }
                }
            }

            if (McsuPlayer.fromBukkit(p).getTeam() == null) {
                McsuPlayer.fromBukkit(p).setTeam(TeamMain.Teams.get(0));

            }
        } else {
            Bukkit.getLogger().info("Player " + p.getName() + " was attempted to be registered but already exists");
        }

        McsuScoreboard.defaultScoreboard.bindPlayer(McsuPlayer.fromBukkit(p), false);

    }

    //Object defenition

    Player bukkitPlayer;
    McsuTeam team = null;
    String Colour = String.valueOf(ChatColor.WHITE);

    public McsuPlayer(Player BukkitPlayer) {

        this.bukkitPlayer = BukkitPlayer;

    }

    public String getTeamID() { return team.getTeamID(); }
    public McsuTeam getTeam() { return team; }
    public Player toBukkit() { return this.bukkitPlayer; }
    public String getColouredName() { return this.Colour + this.bukkitPlayer.getName() + ChatColor.RESET; }
    public String getColouredName(boolean bold) { return this.Colour + (bold ? ChatColor.BOLD : "") + this.bukkitPlayer.getName(); }
    public String getName() { return this.bukkitPlayer.getName(); }
    public String getColour() { return Colour; }

    public void setTeam(McsuTeam newTeam) {

        this.team = newTeam;
        this.Colour = newTeam.getChatColour();
        bukkitPlayer.setDisplayName(getColouredName());
        //bukkitPlayer.setPlayerListName(getColouredName());

    }

    public int awardPoints(int points) { return awardPoints(points, null, true); }
    public int awardPoints(int points, String reason) { return awardPoints(points, reason, true); }

    public int awardPoints(int points, String reason, Boolean announce) {

        team.awardPoints(points, reason, false);


        Bukkit.getLogger().info("(Debug) " + getColouredName()  + " has received " + points + " points!");
        boolean hasReason = (reason!=null);

        if (announce) {
            bukkitPlayer.sendMessage(ChatColor.BLUE +""+ ChatColor.ITALIC +
                    "You have received " + points + " points " + (hasReason ? " for " + reason : "" ) + "!");
        }

        TeamTotalPoints.update();

        return team.teamPoints;

    }

    public void notifyTeamPoints(int points, String reason) {
        bukkitPlayer.sendMessage(ChatColor.BLUE +""+ ChatColor.ITALIC +
                "Your team has received " + points + " points for " + reason + "!");
    }

    public int getWins() { return 0; }

    public ChatColor getColourRaw() {
        return ChatColor.getByChar(this.Colour.substring(1));
    }

}
