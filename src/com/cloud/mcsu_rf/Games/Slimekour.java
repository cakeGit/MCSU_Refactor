package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.GamePlayers.BlockSumoPlayer;
import com.cloud.mcsu_rf.Inventories.BlockSumoInventory;
import com.cloud.mcsu_rf.LootTables.BlockSumoLoot;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones.HeightKillZone;
import com.cloud.mcsu_rf.Objects.GameFunctions.BuildLimits.BuildMaxDistance;
import com.cloud.mcsu_rf.Objects.GameFunctions.BuildLimits.BuildMaxHeight;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.GameFunction;
import com.cloud.mcsu_rf.Objects.GameFunctions.InventoryManager;
import com.cloud.mcsu_rf.Objects.Map.SpawnManager;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Slimekour {

    Game game;

    int killZoneY = 0;
    int buildHeightLimit = 0;
    int buildDistanceLimit = 0;
    BukkitRunnable stopwatchDisplayTimer;
    InventoryManager inventoryManager = new InventoryManager(new BlockSumoInventory());

    public void init() {

        BlockSumoLoot.init();

        game = new Game("Slimekour")
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new CustomEventListener(
                                        event -> game.getGamestate("afterCountdown").setEnabled(true),
                                        "GameCountdownEndEvent"
                                ))
                                .onEnable(() -> {

                                })
                                .addGameFunction(inventoryManager)
                )
                .addGameState(
                        new GameState("afterCountdown")
                                .onEnable(()-> {
                                    stopwatchDisplayTimer = new BukkitRunnable() {
                                        @Override
                                        public void run() {

                                            for (BlockSumoPlayer blockSumoPlayer : BlockSumoPlayer.BlockSumoPlayers) {
                                                blockSumoPlayer.toBukkit().spigot().sendMessage(
                                                        ChatMessageType.ACTION_BAR,
                                                        TextComponent.fromLegacyText(blockSumoPlayer.getLivesString())
                                                );
                                            }

                                        }
                                    };
                                    stopwatchDisplayTimer.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);
                                })
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            stopwatchDisplayTimer.cancel();

            BlockSumoPlayer.BlockSumoPlayers = new ArrayList<>();

            game.getAliveTeams().get(0).awardTeamPoints(100);
            game.endGame(game.getAliveTeams().get(0));

        }

    }

}
