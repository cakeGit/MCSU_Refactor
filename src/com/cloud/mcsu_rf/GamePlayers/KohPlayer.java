package com.cloud.mcsu_rf.GamePlayers;

import com.cak.what.Util.ChCol;
import com.cloud.mcsu_rf.Games.KOH;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class KohPlayer {

    public static ArrayList<KohPlayer> KohPlayers = new ArrayList<>();

    Player player;
    boolean spawnProt = false;
    int hillTime = 0;

    public KohPlayer(Player player) {

        this.player = player;

        KohPlayers.add(this);

    }

    public static KohPlayer fromBukkit(Player player) {
        return KohPlayers.stream().filter(blockSumoPlayer -> blockSumoPlayer.toBukkit().equals(player)).findFirst().orElse(null);
    }

    public Player toBukkit() { return player; }

    public void setSpawnProt(boolean spawnProt) { this.spawnProt = spawnProt; }
    public boolean hasSpawnProt() { return this.spawnProt; }

    public String getTimeString() {
        return ChCol.BLUE + "- Hill Control: " +
                "|".repeat(KOH.ownerTime) +
                ".".repeat(KOH.captureTime-KOH.ownerTime)
                + ChCol.BLUE + " -";
    }

    public String getLivesTabString() {
        return "not yet bozo";
    }
}
