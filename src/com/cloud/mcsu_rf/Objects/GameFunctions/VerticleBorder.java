package com.cloud.mcsu_rf.Objects.GameFunctions;

import com.cloud.mcsu_rf.GamePlayers.BlockSumoPlayer;
import com.cloud.mcsu_rf.MCSU_Main;
import com.sk89q.worldedit.math.BlockVector3;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BlockVector;

public class VerticleBorder {

    World world;
    BukkitRunnable borderTask;
    public double borderY;
    Integer[] corner1;
    Integer[] corner2;

    public VerticleBorder(int bY, Integer[] c1, Integer[] c2) {
        borderY = bY;
        corner1 = c1;
        corner2 = c2;
    }

    public void startBorder() {
        borderTask = new BukkitRunnable() {
            @Override
            public void run() {
                borderY -= 0.3;
                for(Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage("Border Y: "+borderY);
                    world = players.getWorld();
                    if(players.getLocation().getY() >= borderY) {
                        players.damage(2.5F);
                    }
                    int minX = Math.min(corner1[0], corner2[0]);
                    int maxX = Math.max(corner1[0], corner2[0]);
                    int minZ = Math.min(corner1[1], corner2[1]);
                    int maxZ = Math.max(corner1[1], corner2[1]);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.ORANGE, 10);
                    for (int x = minX; x <= maxX; x = x + 6) {
                        for (int z = minZ; z <= maxZ; z = z + 6) {
                            world.spawnParticle(Particle.REDSTONE, new Location(world, x, borderY, z),1, dustOptions);
                        }
                    }
                }
            }
        };
        borderTask.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);
    }



}
