package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.GamePlayers.BlockSumoPlayer;
import com.cloud.mcsu_rf.Inventories.BlockSumoInventory;
import com.cloud.mcsu_rf.LootTables.BlockSumoLoot;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.*;
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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockSumo {

    Game game;

    int killZoneY = 0;
    int buildHeightLimit = 0;
    int buildDistanceLimit = 0;
    BukkitRunnable powerupTimer;
    BukkitRunnable displayLivesTimer;
    GameFunction lobbyHeightActionZone;
    InventoryManager inventoryManager = new InventoryManager(new BlockSumoInventory());

    public void init() {

        BlockSumoLoot.init();

        game = new Game("BlockSumo")
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new CustomEventListener(
                                        event -> game.getGamestate("afterCountdown").setEnabled(true),
                                        "GameCountdownEndEvent"
                                ))
                                .onEnable(() -> {

                                    killZoneY = (int) game.getMapMetadata().get("GameData.KillZoneY");
                                    buildHeightLimit = (int) game.getMapMetadata().get("GameData.BuildHeightLimit");
                                    buildDistanceLimit = (int) game.getMapMetadata().get("GameData.BuildDistanceLimit");


                                    game.getGamestate("afterCountdown")
                                            .addGameFunction(new HeightKillZone(
                                                    killZoneY,
                                                    true
                                            ), true)
                                            .addGameFunction(new BuildMaxHeight(buildHeightLimit), true)
                                            .addGameFunction(new BuildMaxDistance(2000, 0, buildDistanceLimit), true);

                                    game.getGamestate("lobby")
                                            .addGameFunction(
                                                    new HeightActionZone(killZoneY, true)
                                                            .setWhilePlayerInside(player -> SpawnManager.teleportToMapPoint(
                                                                    player,
                                                                    game.getMapMetadata().getLobbyPoints().get(0),
                                                                    game.getWorld()
                                                            )), true);


                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        new BlockSumoPlayer(player, 3);
                                    }


                                    game.getGamestate("lobby").setEnabled(true);

                                })
                                .addGameFunction(inventoryManager)
                                .addGameFunction(new CustomEventListener(event -> {

                                    Player player;
                                    if (event.getEventName().equals("InventoryClickEvent")) {
                                        player = (Player) ((InventoryClickEvent) event).getWhoClicked();
                                    } else /*if (event.getEventName().equals("PlayerInteractEvent"))*/ {
                                        player = ((PlayerInteractEvent) event).getPlayer();
                                    }

                                    PlayerInventory playerInv = player.getInventory();

                                    if (playerInv.getChestplate().getData().getItemType() == Material.LEGACY_LEATHER_CHESTPLATE) {

                                        playerInv.setHelmet(TeamSwitchStatements.toColouredLeatherArmour(
                                                McsuPlayer.fromBukkit(player).getTeamID(),
                                                new ItemStack(Material.LEATHER_HELMET)
                                        ));

                                        playerInv.setChestplate(TeamSwitchStatements.toColouredLeatherArmour(
                                                McsuPlayer.fromBukkit(player).getTeamID(),
                                                new ItemStack(Material.LEATHER_CHESTPLATE)
                                        ));

                                        playerInv.setLeggings(TeamSwitchStatements.toColouredLeatherArmour(
                                                McsuPlayer.fromBukkit(player).getTeamID(),
                                                new ItemStack(Material.LEATHER_LEGGINGS)
                                        ));

                                        playerInv.setBoots(TeamSwitchStatements.toColouredLeatherArmour(
                                                McsuPlayer.fromBukkit(player).getTeamID(),
                                                new ItemStack(Material.LEATHER_BOOTS)
                                        ));

                                    }

                                }, "InventoryClickEvent", "PlayerInteractEvent"))

                )
                .addGameState(
                        new GameState("lobby")
                )
                .addGameState(
                        new GameState("afterCountdown")
                                .onEnable(()-> {

                                    game.getGamestate("lobby").setEnabled(false);

                                    powerupTimer = new BukkitRunnable() {
                                        @Override
                                        public void run() {

                                            Bukkit.broadcastMessage(ChatColor.BLUE + "Every player has received a powerup!");
                                            inventoryManager.emitInventoryEvent("Powerup");

                                        }
                                    };
                                    powerupTimer.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L*30L); //Happens every 30s

                                    displayLivesTimer = new BukkitRunnable() {
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
                                    displayLivesTimer.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);

                                    lobbyHeightActionZone.setEnabled(false);


                                })
                                .addGameFunction(new CustomEventListener(
                                        event -> {

                                            PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;
                                            BlockSumoPlayer sumoPlayer = BlockSumoPlayer.fromBukkit(deathEvent.getEntity().getPlayer());

                                            displayLivesTimer.run(); // Runs an off-beat display to update immediately
                                            sumoPlayer.removeLife();

                                            if (sumoPlayer.getLives() == 0) {
                                                game.eliminatePlayer(sumoPlayer.toBukkit());
                                                checkIfEnded();
                                            }
                                        },
                                        "PlayerDeathEvent"
                                ))
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            powerupTimer.cancel();
            displayLivesTimer.cancel();

            game.getAliveTeams().get(0).awardTeamPoints(100);
            game.endGame(game.getAliveTeams().get(0));

        }

    }

}
