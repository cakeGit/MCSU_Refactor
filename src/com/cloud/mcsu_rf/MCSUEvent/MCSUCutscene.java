package com.cloud.mcsu_rf.MCSUEvent;

import com.cloud.mcsu_rf.MCSU_Main;
import com.sk89q.worldedit.math.BlockVector3;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;

public class MCSUCutscene {

    ArrayList<String> queuedText;
    ArrayList<PotionEffectType> queuedEffects;
    ArrayList<Player> cutscenePlayers;
    ArrayList<Integer> queuedTime;
    ArrayList<Location> queuedLocs;
    BukkitRunnable timeSection;
    int currentIndex;

    public MCSUCutscene() {
        Bukkit.broadcastMessage("Starting cutscene..");
        queuedText = new ArrayList<>();
        queuedEffects = new ArrayList<>();
        queuedTime = new ArrayList<>();
        cutscenePlayers = new ArrayList<>();
        queuedLocs = new ArrayList<>();
        cutscenePlayers.addAll(Bukkit.getOnlinePlayers());
    }

    public void addSection(String text, PotionEffectType effect, int time, Location loc) { queuedText.add(text); queuedEffects.add(effect); queuedTime.add(time); queuedLocs.add(loc); }

    public void startCutscene() {
        currentIndex = 0;
        timeSection();
    }

    public void timeSection() {
        timeSection = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player players : cutscenePlayers) {
                    players.sendTitle(queuedText.get(currentIndex),"",0,20*queuedTime.get(currentIndex),0);
                    if(queuedEffects.get(currentIndex).equals(PotionEffectType.CONFUSION)) {
                        players.addPotionEffect(new PotionEffect(queuedEffects.get(currentIndex),20*(queuedTime.get(currentIndex)+3),0));
                    } else {
                        players.addPotionEffect(new PotionEffect(queuedEffects.get(currentIndex),20*queuedTime.get(currentIndex),0));
                    }
                    players.setInvisible(false);
                    players.setGameMode(GameMode.SPECTATOR);
                    ArmorStand as = (ArmorStand) players.getWorld().spawnEntity(queuedLocs.get(currentIndex), EntityType.ARMOR_STAND);
                    as.setInvisible(true);
                    as.setGravity(false);
                    as.setInvulnerable(true);
                    as.addPassenger(players);
                }
                currentIndex++;
                timeSection();
                timeSection.cancel();
            }
        };
        timeSection.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L*queuedTime.get(currentIndex));
    }

}
