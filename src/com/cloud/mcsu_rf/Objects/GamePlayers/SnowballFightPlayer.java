package com.cloud.mcsu_rf.Objects.GamePlayers;

import org.bukkit.entity.Player;

public class SnowballFightPlayer {

    Player player;
    int lives;
    String ownerUuid;

    public SnowballFightPlayer(Player player, int lives) {

        this.player = player;
        this.lives = lives;

    }

}
