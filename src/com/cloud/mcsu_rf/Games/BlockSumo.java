package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.GamePlayers.BlockSumoPlayer;
import com.cloud.mcsu_rf.Inventories.SpleefInventory;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.*;
import com.cloud.mcsu_rf.Objects.Game_Functions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.Game_Functions.HeightActionZone;
import com.cloud.mcsu_rf.Objects.Game_Functions.PointAwarder;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class BlockSumo {

    Game game;



    public void init() {

        game = new Game("BlockSumo")
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        new BlockSumoPlayer(player);

                                    }


                                })
                                .addGameFunction(
                                        new CustomEventListener("BlockPlaceEvent", event -> {
                                            BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
                                            ItemStack itemStack = new ItemStack(Material.BLACK_WOOL);
                                            placeEvent.getPlayer().getInventory().setItem(placeEvent.getHand(), itemStack);

                                            /*new BukkitRunnable() { Unused block despawning
                                                @Override
                                                public void run() {
                                                    ((BlockPlaceEvent) event).getBlock().breakNaturally();
                                                }
                                            }.runTaskLater(MCSU_Main.Mcsu_Plugin, 10L);*/
                                        })
                                )
                )
                .addGameState(
                        new GameState("afterCountdown")
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            game.getAliveTeams().get(0).awardTeamPoints(100);

            game.endGame(game.getAliveTeams().get(0));

        } else  {

            Bukkit.getLogger().info(game.getName() + " has not ended because " + game.getAliveTeams().size()
                    + " teams are still alive");

        }

    }

}
