package com.cloud.mcsu_rf.GamePlayers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BlockSumoPlayer {

    public static ArrayList<BlockSumoPlayer> BlockSumoPlayers = new ArrayList<>();

    int maxLives = 5;
    int lives = 5;
    int lifePowerups = 0;
    Player player;
    boolean spawnProt = false;

    public BlockSumoPlayer(Player player) {

        this.player = player;

        BlockSumoPlayers.add(this);

    }

    public static BlockSumoPlayer fromBukkit(Player player) {
        return BlockSumoPlayers.stream().filter(blockSumoPlayer -> blockSumoPlayer.toBukkit().equals(player)).findFirst().orElse(null);
    }
    public Player toBukkit() { return player; }

    public int getLives() { return lives; }
    public void removeLife() { if (lives > 0) { this.lives -= 1; } }

    public int getLifePowerups() { return lifePowerups; }
    public void resetLifePowerups() { lifePowerups = 0; }
    public void addLifePowerup() { lifePowerups +=1; }

    public void setSpawnProt(boolean spawnProt) { this.spawnProt = spawnProt; }
    public boolean hasSpawnProt() { return this.spawnProt; }

    public String getLivesString() {
        return  (lives == 0 ? ChatColor.GRAY + " -- DEAD -- ": ChatColor.RED + "Lives: ") +
                ChatColor.RED +""+
                "❤ ".repeat(lives)+
                ChatColor.GRAY +
                "☠ ".repeat(maxLives-lives) +
                (lives == 0 ? " -- DEAD -- " : "");
    }

    public String getLivesTabString() {
        return (lives == 0 ? ChatColor.GRAY + "[☠]": ChatColor.RED + "["+lives+"❤]");
    }

    public void setLives(int i) {
        lives = i;
    }
}
