package com.cloud.mcsu_rf.GamePlayers;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BlockSumoPlayer {

    public static ArrayList<BlockSumoPlayer> BlockSumoPlayers = new ArrayList<>();

    int maxLives = 5;
    int lives = 5;
    Player player;
    boolean spawnProt = false;

    public BlockSumoPlayer(Player player, int lives) {

        this.maxLives = lives;
        this.lives = lives;
        this.player = player;

        BlockSumoPlayers.add(this);

    }

    public static BlockSumoPlayer fromBukkit(Player player) {
        return BlockSumoPlayers.stream().filter(blockSumoPlayer -> blockSumoPlayer.toBukkit().equals(player)).findFirst().orElse(null);
    }

    public int getLives() { return lives; }
    public void removeLife() { this.lives -= 1; }
    public Player toBukkit() { return player; }

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
}
