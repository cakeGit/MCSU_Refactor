package com.cloud.mcsu_rf.GamePlayers;

import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class BlockSumoPlayer {

    public static ArrayList<BlockSumoPlayer> BlockSumoPlayers = new ArrayList<>();

    int lives = 3;
    Player player;

    public BlockSumoPlayer(Player player) {
        this.player = player;
        BlockSumoPlayers.add(this);
    }

    public static BlockSumoPlayer getPlayer(Player player) {
        return BlockSumoPlayers.stream().filter(blockSumoPlayer -> blockSumoPlayer.getPlayer().equals(player)).findFirst().orElse(null);
    }

    public int getLives() { return lives; }
    public void removeLife() { this.lives -= 1; }
    public Player getPlayer() { return player; }

}
