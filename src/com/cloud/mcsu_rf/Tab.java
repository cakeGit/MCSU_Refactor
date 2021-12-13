package com.cloud.mcsu_rf;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Tab {

    public static MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);
    public static String defaultHeader = ChatColor.WHITE+"MCSU"+"\n";
    public static String header;
    public static String footer;
    public static String r = "§c";
    public static String w = "§f";
    public static String n = "\n";
    public static ChatColor b = ChatColor.BOLD;

    public static void showTab(Player player, int online) {
        setHeader(defaultHeader);
        String f = n+ChatColor.YELLOW+"Players Online: "+online;
        setFooter(f);
        player.setPlayerListHeaderFooter(header,f);
        animateTab(player);
    }

    public static void animateTab(Player player) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            int count = 0;

            @Override
            public void run() {
                if(count == 5)
                    count = 0;
                switch(count) {
                    case 0:
                        setHeader(b+w+"MCSU"+n);
                        break;
                    case 1:
                        setHeader(b+r+"M"+w+"CSU"+n);
                        break;
                    case 2:
                        setHeader(b+w+"M"+r+"C"+w+"SU"+n);
                        break;
                    case 3:
                        setHeader(b+w+"MC"+r+"S"+w+"U"+n);
                        break;
                    case 4:
                        setHeader(b+w+"MCS"+r+"U"+n);
                        break;
                }
                player.setPlayerListHeaderFooter(header,footer);
                count++;
            }
        },0,10);
    }

    public static void setHeader(String h) { header = h; }
    public static void setFooter(String f) { footer = f; }

}
