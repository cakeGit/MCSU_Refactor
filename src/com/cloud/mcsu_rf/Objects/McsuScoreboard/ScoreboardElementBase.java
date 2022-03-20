package com.cloud.mcsu_rf.Objects.McsuScoreboard;

import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class ScoreboardElementBase {

    public static ArrayList<ScoreboardElementBase> instances = new ArrayList<>();
    public McsuScoreboard boundScoreboard;

    public ScoreboardElementBase() {
        instances.add(this);
    }

    static public void update() {
        for (ScoreboardElementBase instance : instances ) {
            instance.boundScoreboard.update();
        }
    }

    public String[] generateContent() {
         return new String[]{ChatColor.RED + this.getClass().getSimpleName() + " is used without a created/functional generateContent"};
    }
}
