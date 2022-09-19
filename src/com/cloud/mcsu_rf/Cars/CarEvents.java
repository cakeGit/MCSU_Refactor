package com.cloud.mcsu_rf.Cars;

import com.cloud.mcsu_rf.MCSU_Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Boat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class CarEvents implements Listener {

    public static HashMap<Player, Float> honkCooldown;
    public static HashMap<Player, Double> speed;

    @EventHandler
    public static void onDriveCar(VehicleMoveEvent e) {
        if(e.getVehicle() instanceof Boat) {
            Boat car = (Boat) e.getVehicle();
            Player player = (Player) car.getPassengers().get(0);
            Location floor = car.getLocation().subtract(0,1,0);
            if(car.getWorld().getBlockAt(floor).getType().equals(Material.BLACK_CONCRETE) || car.getWorld().getBlockAt(floor).getType().equals(Material.WHITE_CONCRETE)) {
                double carSpeed = speed.get(player)/125;
                car.setVelocity(new Vector(player.getLocation().getDirection().multiply(carSpeed).getX(),0,player.getLocation().getDirection().multiply(carSpeed).getZ()));
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.RED+"Current Car Speed: "+Math.round(speed.get(player))+" mph"));
                /*
                BlockFace facing = player.getFacing();
                Vector addLoc = facing.getDirection();
                addLoc.add(new Vector(0,1,0));
                Block facingBlock = player.getWorld().getBlockAt(player.getLocation().add(addLoc));
                if(facingBlock.getType().isSolid()) {
                    car.setVelocity(new Vector(0,0.5F,0));
                }
                 */
            } else {
                if(car.getName().equals(ChatColor.GOLD+"Off-roader")) {
                    double carSpeed = speed.get(player) / 110;
                    car.setVelocity(new Vector(player.getLocation().getDirection().multiply(carSpeed).getX(),0,player.getLocation().getDirection().multiply(carSpeed).getZ()));
                } else {
                    double carSpeed = speed.get(player) / 750;
                    car.setVelocity(new Vector(player.getLocation().getDirection().multiply(carSpeed).getX(),0,player.getLocation().getDirection().multiply(carSpeed).getZ()));
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.RED+"Current Car Speed: "+Math.round(speed.get(player))+" mph"));
            }
        }
    }

    @EventHandler
    public static void onSwitchOffhand(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if(player.isInsideVehicle()) {
            if(player.getVehicle() instanceof Boat) {
                if(player.getVehicle().getName().equals(ChatColor.YELLOW+"Old York Taxi")) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(honkCooldown.get(player) == 0) {
                            players.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_YES, SoundCategory.AMBIENT,4,1);
                            honkCooldown.put(player,0.5F);
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    honkCooldown.put(player,0F);
                                }
                            };
                            runnable.runTaskLater(MCSU_Main.Mcsu_Plugin,10L);
                        }
                    }
                } else if(player.getVehicle().getName().equals(ChatColor.GOLD+"Off-roader")) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(honkCooldown.get(player) == 0) {
                            players.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_NO, SoundCategory.AMBIENT,4,1);
                            honkCooldown.put(player,0.5F);
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    honkCooldown.put(player,0F);
                                }
                            };
                            runnable.runTaskLater(MCSU_Main.Mcsu_Plugin,10L);
                        }
                    }
                } else if(player.getVehicle().getName().equals(ChatColor.RED+"Ford Concentrate") || player.getVehicle().getName().equals(ChatColor.GREEN+"Small")) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(honkCooldown.get(player) == 0) {
                            players.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_DEATH, SoundCategory.AMBIENT,4,1);
                            honkCooldown.put(player,0.5F);
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    honkCooldown.put(player,0F);
                                }
                            };
                            runnable.runTaskLater(MCSU_Main.Mcsu_Plugin,10L);
                        }
                    }
                } else if(player.getVehicle().getName().equals(ChatColor.DARK_PURPLE+"Boogatti Veyron") || player.getVehicle().getName().equals(ChatColor.BLUE+"Mercedes-Beanz")) {
                    for(Player players : Bukkit.getOnlinePlayers()) {
                        if(honkCooldown.get(player) == 0) {
                            players.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_HURT, SoundCategory.AMBIENT,4,1);
                            honkCooldown.put(player,0.5F);
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    honkCooldown.put(player,0F);
                                }
                            };
                            runnable.runTaskLater(MCSU_Main.Mcsu_Plugin,10L);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public static void onEnterCar(VehicleEnterEvent e) {
        if(e.getEntered() instanceof Player) {
            Player player = (Player) e.getEntered();
            honkCooldown.put(player,0F);
            if(e.getVehicle() instanceof Boat) {
                Boat car = (Boat) e.getVehicle();
                if(car.getCustomName().equals(ChatColor.RED+"Ford Concentrate")) {
                    speed.put(player,100D);
                } else if(car.getCustomName().equals(ChatColor.GOLD+"Off-roader")) {
                    speed.put(player,90D);
                } else if(car.getCustomName().equals(ChatColor.GREEN+"Small")) {
                    speed.put(player,110D);
                } else if(car.getCustomName().equals(ChatColor.YELLOW+"Old York Taxi")) {
                    speed.put(player,115D);
                } else if(car.getCustomName().equals(ChatColor.BLUE+"Mercedes-Beanz")) {
                    speed.put(player,125D);
                } else if(car.getCustomName().equals(ChatColor.DARK_PURPLE+"Boogatti Veyron")) {
                    speed.put(player,150D);
                }
            }
        }
    }

    public static void spawnCar(Location spawnLoc, String car, World world) {
        Boat boat = (Boat) world.spawnEntity(spawnLoc, EntityType.BOAT);
        switch(car) {
            case "Ford":
                boat.setCustomName(ChatColor.RED+"Ford Concentrate");
                boat.setWoodType(TreeSpecies.JUNGLE);
                break;
            case "Rover":
                boat.setCustomName(ChatColor.GOLD+"Off-roader");
                boat.setWoodType(TreeSpecies.ACACIA);
                break;
            case "Small":
                boat.setCustomName(ChatColor.GREEN+"Small");
                boat.setWoodType(TreeSpecies.DARK_OAK);
                break;
            case "Taxi":
                boat.setCustomName(ChatColor.YELLOW+"Old York Taxi");
                boat.setWoodType(TreeSpecies.GENERIC);
                break;
            case "Boogatti":
                boat.setCustomName(ChatColor.DARK_PURPLE+"Boogatti Veyron");
                boat.setWoodType(TreeSpecies.REDWOOD);
                break;
            case "Mercedes":
                boat.setCustomName(ChatColor.BLUE+"Mercedes-Beanz");
                boat.setWoodType(TreeSpecies.BIRCH);
                break;
        }
        //boat.addPassenger(player);
        boat.setCustomNameVisible(true);
    }

    /*
    @EventHandler
    public static void onJoin(PlayerJoinEvent e) {
        TextComponent message = new TextComponent(ChatColor.AQUA+"It is recommended when using the cars plugin to also have the custom resource pack installed.\n"+ChatColor.WHITE+"§lClick Here"+ChatColor.AQUA+" to download the pack (optifine required). "+ChatColor.DARK_PURPLE+"Credit to GreyDesh");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pack "+e.getPlayer().getName()));
        e.getPlayer().spigot().sendMessage(message);
    }
     */

}
