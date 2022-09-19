package com.cloud.mcsu_rf;

import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.McsuScoreboard;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements.FixedContent;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements.MapMetadataDisplay;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements.TeamTotalPoints;
import com.cloud.mcsu_rf.Definitions.McsuTeam;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class McsuEvent implements CommandExecutor {

    public static int taskID;
    public static int time;
    public static Player player;
    public static World world;
    public static boolean preevent;
    public static boolean eventStarted = false;
    public static int winnertime;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use that command!");
            return true;
        }
        player = (Player) sender;
        world = player.getWorld();
        if(cmd.getName().equalsIgnoreCase("stopglow") && player.isOp()) {
            for(Player players : Bukkit.getOnlinePlayers()) {
                players.setGlowing(false);
            }
        }
        if(cmd.getName().equalsIgnoreCase("mcsuevent") && player.isOp()) {
            if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
                stopTimer();
            }
            preevent = true;
            setTimer(30);
            startTimer();
            eventStarted = true;
        }
        if(cmd.getName().equalsIgnoreCase("winner") && player.isOp()) {
            McsuTeam first = TeamMain.getSortedTeams().get(0);
            McsuTeam second = TeamMain.getSortedTeams().get(1);
            McsuTeam third = TeamMain.getSortedTeams().get(2);
            player.getWorld().setTime(15000);
            winnertime = 0;
            for(Player players : Bukkit.getOnlinePlayers()) {
                players.getInventory().clear();
                players.setGameMode(GameMode.ADVENTURE);
                players.teleport(new Location(world,0.5, 6, -33.5, 180, 0));
            }
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(MCSU_Main.Mcsu_Plugin, new Runnable() {
                @Override
                public void run() {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(first.getStyledName()+" win MCSU!",second.getChatColour()+"2. "+second.getStyledName()+third.getChatColour()+" 3. "+third.getStyledName(),0,100,0);
                    }
                    winnertime = winnertime + 5;
                    Location loc1 = new Location(player.getWorld(),-9, 20, -50);
                    spawnFireworks(loc1,1,Color.AQUA);
                    spawnFireworks(loc1,1,Color.WHITE);
                    Location loc2 = new Location(player.getWorld(),0.5,20,-50);
                    spawnFireworks(loc2,1,Color.YELLOW);
                    spawnFireworks(loc2,1,Color.WHITE);
                    Location loc3 = new Location(player.getWorld(),9,20,-50);
                    spawnFireworks(loc3,1,Color.SILVER);
                    spawnFireworks(loc3,1,Color.WHITE);
                    if(winnertime == 200) {
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            players.teleport(new Location(player.getWorld(), 0.5, 1, 0.5, -180, 0));
                            players.sendTitle(ChatColor.AQUA+"MCSU Finished!",ChatColor.GRAY+"Thanks for playing!");
                        }
                        Bukkit.getScheduler().cancelTask(taskID);
                    }
                }
            }, 0L,20L);
            for(McsuPlayer player : first.getMembers()) {
                ItemStack trophy = new ItemStack(Material.END_ROD);
                ItemMeta trophyMeta = trophy.getItemMeta();
                trophyMeta.setDisplayName(ChatColor.GOLD+"Trophy");
                trophy.setItemMeta(trophyMeta);
                ItemStack crown = new ItemStack(Material.CARVED_PUMPKIN);
                ItemMeta crownMeta = crown.getItemMeta();
                crownMeta.setDisplayName(ChatColor.GOLD+"Crown");
                crown.setItemMeta(crownMeta);
                player.toBukkit().teleport(new Location(player.toBukkit().getWorld(),0.5,9,-56, 0, 0));
                player.toBukkit().getInventory().addItem(trophy);
                player.toBukkit().getInventory().setHelmet(crown);
            }
        }
        return true;
    }

    public static void setTimer(int amount) {
        time = amount;
    }

    public static void startTimer() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(MCSU_Main.Mcsu_Plugin, new Runnable() {
            @Override
            public void run() {
                // Pre Event
                if(time == 0) {
                    if(preevent) {
                        preevent = false;
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            players.sendTitle("§aMCSU Starting!", "§7Get Ready!");
                        }
                        stopTimer();
                        McsuScoreboard.defaultScoreboard.update();
                        for(Player players : Bukkit.getOnlinePlayers()) {
                            McsuScoreboard.defaultScoreboard.bindPlayer(McsuPlayer.fromBukkit(players),false);
                        }
                    }
                    return;
                }
                if(time % 1 == 0) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        createBoard(players);
                    }
                }
                if(time % 1 == 0 && time <= 10 && preevent) {
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        players.sendTitle(""+time, "", 1, 20, 1);
                        players.playSound(players.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT,1,1);
                    }
                }
                if(time % 5 == 0 && preevent) {
                    Location firework1 = new Location(world,-36,5,-52);
                    Location firework2 = new Location(world,-15, 13, -53);
                    Location firework3 = new Location(world,21, 13, -51);
                    Location firework4 = new Location(world,37, 1, -51);
                    spawnFireworks(firework1,1,Color.LIME);
                    spawnFireworks(firework2,2,Color.AQUA);
                    spawnFireworks(firework3,3,Color.BLUE);
                    spawnFireworks(firework4,4,Color.RED);
                    spawnFireworks(firework1,1,Color.PURPLE);
                    spawnFireworks(firework2,2,Color.GREEN);
                    spawnFireworks(firework3,3,Color.ORANGE);
                    spawnFireworks(firework4,4,Color.YELLOW);
                }
                if(time == 30 && preevent) {
                    world.setTime(15000);
                }
                time = time - 1;
            }
        }, 0L, 20L);
    }

    public static void spawnFireworks(Location location, int amount, Color color){
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public static void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public static void createBoard(Player player) {
        String fancytimer;
        int finish = time / 60;
        int remainder = time % 60;
        if(Integer.toString(remainder).length() == 1 && Integer.toString(finish).length() == 1) {
            String finishremainder = "0"+remainder;
            String finishtime = "0"+finish;
            fancytimer = finishtime+":"+finishremainder;
        } else if(Integer.toString(finish).length() == 1) {
            String finishtime = "0"+finish;
            fancytimer = finishtime+":"+remainder;
        } else if(Integer.toString(remainder).length() == 1) {
            String finishremainder = "0"+remainder;
            fancytimer = finish+":"+finishremainder;
        } else {
            fancytimer = finish+":"+remainder;
        }
        McsuScoreboard eventScoreboard = new McsuScoreboard(
                new MapMetadataDisplay(false),
                new FixedContent("", ChatColor.WHITE+" MCSU Starting in: "+fancytimer+"!", "")
        );
        eventScoreboard.bindPlayer(McsuPlayer.fromBukkit(player),true);
        eventScoreboard.generateScoreboard(McsuPlayer.fromBukkit(player));
        eventScoreboard.update();
    }

}
