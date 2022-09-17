package com.cloud.mcsu_rf.GamePlayers;

import com.cloud.mcsu_rf.Definitions.Game.Game;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SnowballFightPlayer {


    public static ArrayList<SnowballFightPlayer> SnowballFightPlayers = new ArrayList<>();

    Player player;
    int lives;
    int kills = 0;
    int killstreak = 0;
    String weaponId;

    public SnowballFightPlayer(Player player) {

        this.player = player;

        String livesChoiceName = Game.currentGame.getGamemodeOptionBlockChoice("Lives").getGamemodeOption().getName();

        lives = livesChoiceName.equals("Unlimited") ? -1 : Integer.parseInt(livesChoiceName);

        SnowballFightPlayers.add(this);

    }

    public Player toBukkit() { return player; }
    public static SnowballFightPlayer fromBukkit(Player player) {
        return SnowballFightPlayers.stream().filter(snowballFightPlayer -> snowballFightPlayer.toBukkit().equals(player)).findFirst().orElse(null);
    }

    public String getLivesTabString() {
        return (lives == 0 ? ChatColor.GRAY + "[☠]": ChatColor.RED + "["+lives+"❤]");
    }

    public void removeLife() { lives--; killstreak = 0; }
    public int getLives() { return lives; }
    public boolean hasLives() { return !(lives == 0); }

    public void awardKill() { kills++; killstreak++; }
    public int getKills() { return kills; }

}
