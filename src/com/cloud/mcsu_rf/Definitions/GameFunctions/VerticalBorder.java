package com.cloud.mcsu_rf.Definitions.GameFunctions;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VerticalBorder extends GameFunctionBase {

    World world;
    BukkitRunnable borderTask;
    public double borderY;
    Integer[] corner1;
    Integer[] corner2;

    public VerticalBorder(int bY, Integer[] c1, Integer[] c2) {
        borderY = bY;
        corner1 = c1;
        corner2 = c2;
    }

    @Override
    public void onEnabledToggle(boolean enabled) {
        if (enabled) {
            borderTask = new BukkitRunnable() {
                @Override
                public void run() {

                    borderY -= 0.3;

                    for(Player players : Bukkit.getOnlinePlayers()) {

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
        } else {
            borderTask.cancel();
        }

        borderTask.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);

    }



}
