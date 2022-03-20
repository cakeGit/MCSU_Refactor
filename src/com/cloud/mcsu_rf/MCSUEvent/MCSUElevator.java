package com.cloud.mcsu_rf.MCSUEvent;

import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MCSUElevator {

    int elevatorY;

    Integer[] elevatorFrom;
    Integer[] elevatorTo;
    Material elevatorBlock;

    int floorHeight;
    int speed;
    int floor;

    BukkitRunnable elevatorTask;
    public FallingBlock fb;

    public void setBlock(Material elevatorBlock) {
        this.elevatorBlock = elevatorBlock;
    }

    public void setFrom(Integer[] elevatorFrom) {
        this.elevatorFrom = elevatorFrom;
    }

    public void setTo(Integer[] elevatorTo) {
        this.elevatorTo = elevatorTo;
    }

    public void setY(int elevatorY) {
        this.elevatorY = elevatorY;
    }

    public void setFloorHeight(int floorHeight) {
        this.floorHeight = floorHeight;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void spawnElevator() {
        int minx = Math.min(elevatorFrom[0], elevatorTo[0]);
        int maxx = Math.max(elevatorFrom[0], elevatorTo[0]);

        int miny = Math.min(elevatorFrom[1], elevatorTo[1]);
        int maxy = Math.max(elevatorFrom[1], elevatorTo[1]);

        int minz = Math.min(elevatorFrom[2], elevatorTo[2]);
        int maxz = Math.max(elevatorFrom[2], elevatorTo[2]);

        for (int x = minx; x <= maxx; x++) {
            for (int y = miny; y <= maxy; y++) {
                for (int z = minz; z <= maxz; z++) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        fb = players.getWorld().spawnFallingBlock(new Location(players.getWorld(),x,y,z), elevatorBlock.createBlockData());
                        players.sendMessage(ChatColor.GOLD+"Going to floor "+ChatColor.WHITE+"§l"+floor);
                        fb.setGravity(false);
                        elevatorY = elevatorY + (floorHeight*floor);
                        //fb.getLocation().setY(elevatorY);
                        if(fb.getLocation().getY()>=elevatorY) {
                            fb.setVelocity(new Vector(0,0,0));
                            fb.teleport(fb.getLocation());
                        } else {
                            fb.setVelocity(new Vector(0,1,0));
                        }
                    }
                }
            }
        }
        /*
        elevatorTask = new BukkitRunnable() {
            @Override
            public void run() {
                elevatorTask.cancel();
            }
        };
        elevatorTask.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L*speed);
         */
    }

}
