package com.cloud.mcsu_rf.Score_Handlers;

import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.MCSU_Scoreboard;
import com.cloud.mcsu_rf.Objects.MCSU_Scoreboard.Scoreboard_Element;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Scoreboard_Main {
    public static ScoreboardManager Scoreboard_Manager = Bukkit.getScoreboardManager();

    public static MCSU_Scoreboard Default = new MCSU_Scoreboard(new Scoreboard_Element[] { new Scoreboard_Element("Bottom_Line_Break"), new Scoreboard_Element("Team_Totals"), new Scoreboard_Element("Line_Break"), new Scoreboard_Element("Online_Players"), new Scoreboard_Element("Top_Line_Break") });

    public static MCSU_Scoreboard Current_Scoreboard;

    public static MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);
    public static String r = "§c";
    public static String w = "§f";
    public static ChatColor b = ChatColor.BOLD;
    public static ChatColor m = ChatColor.MAGIC;

    public static void init() { }

    public static void onTeamsLoaded() {

        Current_Scoreboard = Default;

    }

    /*
    public static void animateScoreboard(Player p) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            int count = 0;

            @Override
            public void run() {
                if(count == 5)
                    count = 0;
                switch(count) {
                    case 0:
                        setHeader(p,m+"s"+b+w+"MCSU"+m+"s");
                        break;
                    case 1:
                        setHeader(p,m+"s"+b+r+"M"+w+"CSU"+m+"s");
                        break;
                    case 2:
                        setHeader(p,m+"s"+b+w+"M"+r+"C"+w+"SU"+m+"s");
                        break;
                    case 3:
                        setHeader(p,m+"s"+b+w+"MC"+r+"S"+w+"U"+m+"s");
                        break;
                    case 4:
                        setHeader(p,m+"s"+b+w+"MCS"+r+"U"+m+"s");
                        break;
                }
                count++;
            }
        },0,10);
    }

    public static void setHeader(Player p, String s) {
        MCSU_Scoreboard.Name = s;
        p.setScoreboard(Current_Scoreboard.toBukkitScoreboard());
    }

     */

    public static void reloadScoreboard() {

        for (Player player:
             Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Current_Scoreboard.toBukkitScoreboard());

        }

    }

}
