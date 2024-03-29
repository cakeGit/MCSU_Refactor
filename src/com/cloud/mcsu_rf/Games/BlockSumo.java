package com.cloud.mcsu_rf.Games;

import com.cak.what.Util.ChCol;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Game.GameState;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZones.HeightKillZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.BuildLimits.BuildMaxDistance;
import com.cloud.mcsu_rf.Definitions.GameFunctions.BuildLimits.BuildMaxHeight;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomExplosionVelocities;
import com.cloud.mcsu_rf.Definitions.GameFunctions.GamePrefixText;
import com.cloud.mcsu_rf.Definitions.GameFunctions.InventoryManager;
import com.cloud.mcsu_rf.Definitions.GameFunctions.PointAwarders.KillsAwarder;
import com.cloud.mcsu_rf.Definitions.Map.MapPoint;
import com.cloud.mcsu_rf.Definitions.Map.SpawnManager;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.McsuScoreboard;
import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.GamePlayers.BlockSumoPlayer;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;
import com.cloud.mcsu_rf.Inventories.BlockSumoInventory;
import com.cloud.mcsu_rf.LootTables.BlockSumoLoot;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class BlockSumo {

    Game game;

    int killZoneY;
    int buildHeightLimit;
    int buildDistanceLimit;
    BukkitRunnable powerupTimer;
    BukkitRunnable displayLivesTimer;
    InventoryManager inventoryManager = new InventoryManager(new BlockSumoInventory());
    Integer[] buildDistanceOrigin;

    public static int maxLives = 5;
    public static int maxLifePowerups = 5;

    ArrayList<Location> placedBlockPositions = new ArrayList<>();

    public void init() {

        BlockSumoLoot.init();

        game = new Game("blockSumo", "Block Sumo")
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new CustomEventListener(event -> {game.checkAliveTeams(false);checkIfEnded();}, "PlayerLeaveEvent"))
                                .addGameFunction(new GamePrefixText(
                                        mcsuPlayer -> BlockSumoPlayer.fromBukkit(mcsuPlayer.toBukkit()).getLivesTabString()
                                ))
                                .addGameFunction(new CustomEventListener(
                                        event -> game.getGamestate("afterCountdown").setEnabled(true),
                                        "GameCountdownEndEvent"
                                ))
                                .addGameFunction(new KillsAwarder(20))
                                .onEnable(() -> {

                                    EventListenerMain.setActivityRule("TileDrops", false);
                                    EventListenerMain.setActivityRule("TileBreaking", true);
                                    EventListenerMain.setActivityRule("ExplosionDamage", false);
                                    EventListenerMain.setActivityRule("PearlDamage", false);
                                    EventListenerMain.setActivityRule("EntityDamage",false);
                                    EventListenerMain.setActivityRule("PlayerInteract",true);
                                    EventListenerMain.setActivityRule("Hunger",false);
                                    EventListenerMain.setActivityRule("Suffocation",false);

                                    killZoneY = (int) game.getMapMetadata().get("GameData.KillZoneY");
                                    buildHeightLimit = (int) game.getMapMetadata().get("GameData.BuildHeightLimit");
                                    buildDistanceLimit = (int) game.getMapMetadata().get("GameData.BuildDistanceLimit");
                                    buildDistanceOrigin = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.BuildDistanceOrigin")).split(" "));

                                    game.getGamestate("afterCountdown")
                                            .addGameFunction(new HeightKillZone(
                                                    killZoneY,
                                                    true
                                            ), true)
                                            .addGameFunction(new BuildMaxHeight(buildHeightLimit), true)
                                            .addGameFunction(new BuildMaxDistance(buildDistanceOrigin[0], buildDistanceOrigin[1], buildDistanceLimit), true);

                                    game.getGamestate("lobby")
                                            .addGameFunction(
                                                    new HeightActionZone(killZoneY, true)
                                                            .setWhilePlayerInside(player -> SpawnManager.teleportToMapPoint(
                                                                    player,
                                                                    game.getMapMetadata().getLobbyPoints().get(0),
                                                                    game.getWorld()
                                                            )), true);


                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        new BlockSumoPlayer(player);
                                    }

                                    game.getGamestate("lobby").setEnabled(true);

                                })
                                .addGameFunction(inventoryManager)
                                .addGameFunction(new CustomExplosionVelocities())
                                .addGameFunction(new CustomEventListener(event -> {
                                    EntityExplodeEvent explodeEvent = (EntityExplodeEvent) event;
                                    explodeEvent.setCancelled(true);
                                    if(explodeEvent.getEntity() instanceof TNTPrimed) {
                                        game.getWorld().createExplosion(explodeEvent.getLocation(),0,false,false);
                                    } else {
                                        game.getWorld().createExplosion(explodeEvent.getLocation(),0,false,false);
                                    }
                                }, "EntityExplodeEvent"))
                                .addGameFunction(new CustomEventListener(event -> {

                                    Player player;
                                    if (event.getEventName().equals("InventoryClickEvent")) {
                                        player = (Player) ((InventoryClickEvent) event).getWhoClicked();
                                    } else /*if (event.getEventName().equals("PlayerInteractEvent"))*/ {
                                        player = ((PlayerInteractEvent) event).getPlayer();
                                    }

                                    PlayerInventory playerInv = player.getInventory();

                                    try {
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

                                    } catch (NullPointerException ignored) { }

                                }, "InventoryClickEvent", "PlayerInteractEvent"))
                                .addGameFunction(new CustomEventListener(event -> {
                                    EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
                                    if (BlockSumoPlayer.fromBukkit((Player) damageByEntityEvent.getEntity()).hasSpawnProt()) {
                                        damageByEntityEvent.setCancelled(true);
                                    }
                                }, "EntityDamageByEntityEvent"))
                                .addGameFunction(new CustomEventListener(event -> {
                                    try {
                                        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
                                        Player player = playerInteractEvent.getPlayer();
                                        if(playerInteractEvent.getItem().getType().equals(Material.FIRE_CHARGE)) {
                                            Location fireballLoc = player.getEyeLocation();
                                            player.getWorld().spawnEntity(fireballLoc, EntityType.FIREBALL);
                                            playerInteractEvent.getItem().setAmount(playerInteractEvent.getItem().getAmount()-1);
                                        }
                                    } catch (NullPointerException ignored){}
                                }, "PlayerInteractEvent"))

                )
                .addGameState(
                        new GameState("lobby")
                )
                .addGameState(
                        new GameState("afterCountdown")
                                .onEnable(()-> {
                                    game.addTimedEvent("Powerups I", 60, () -> {
                                        powerupTimer.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L*30L); //Happens every 30s
                                    });
                                    game.addTimedEvent("Powerups II", 60*4, () -> {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.sendMessage("Powerups upgrading!");
                                        }
                                    });
                                    game.addTimedEvent("Powerups III", 60*8, () -> {
                                        Bukkit.broadcastMessage("Powerups upgrading!");
                                    });

                                    game.addTimedEvent(ChCol.BOLD + ChCol.RED + "Sudden Death", 60*10, () -> {
                                        Bukkit.broadcastMessage(ChCol.BOLD + ChCol.RED + "Sudden Death has begun!");
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
                                            if (BlockSumoPlayer.fromBukkit(player).getLives() != 0) {
                                                BlockSumoPlayer.fromBukkit(player).setLives(1);
                                            }
                                        }

                                        for (McsuPlayer player : Game.getAlivePlayers()) {
                                            player.toBukkit().sendTitle(ChCol.BOLD + ChCol.RED + "Sudden Death",  ChCol.GRAY + "Everyone has one life!");
                                        }
                                    });

                                    EventListenerMain.setActivityRule("PVP", true);
                                    game.getGamestate("lobby").setEnabled(false);
                                    for(Player players : Bukkit.getOnlinePlayers()) {
                                        players.setGlowing(true);
                                    }

                                    powerupTimer = new BukkitRunnable() {
                                        @Override
                                        public void run() {

                                            for (McsuPlayer player : Game.getAlivePlayers()) {
                                                if (BlockSumoPlayer.fromBukkit(player.toBukkit()).getLifePowerups() != maxLifePowerups) {
                                                    BlockSumoPlayer blockSumoPlayer = BlockSumoPlayer.fromBukkit(player.toBukkit());
                                                    blockSumoPlayer.addLifePowerup();
                                                    player.toBukkit().sendMessage(ChCol.BLUE + "You have received a powerup! [" +
                                                            blockSumoPlayer.getLifePowerups()
                                                            + "/" + maxLifePowerups + "]"
                                                    );
                                                    BlockSumoInventory.givePowerUp(player.toBukkit());
                                                }
                                            }

                                        }
                                    };

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
                                })

                                .addGameFunction(new CustomEventListener(
                                        event -> {
                                            BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
                                            Location blockLoc = placeEvent.getBlockPlaced().getLocation();

                                            placedBlockPositions.add(blockLoc);
                                        }, "BlockPlaceEvent"))

                                .addGameFunction(new CustomEventListener(
                                        event -> {
                                            BlockBreakEvent breakEvent = (BlockBreakEvent) event;
                                            if (placedBlockPositions.contains(breakEvent.getBlock().getLocation())) {
                                                placedBlockPositions.remove(breakEvent.getBlock().getLocation());
                                            } else {
                                                breakEvent.setCancelled(true);
                                                breakEvent.getPlayer().sendMessage(ChCol.RED + "Hey! World edit works very hard to load these maps!");
                                            }
                                        } , "BlockBreakEvent"))
                                .addGameFunction(new CustomEventListener(
                                        event -> {

                                            PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;
                                            Player deathEventPlayer = deathEvent.getEntity().getPlayer();
                                            BlockSumoPlayer sumoPlayer = BlockSumoPlayer.fromBukkit(deathEventPlayer);

                                            displayLivesTimer.run(); // Runs an off-beat display to update immediately
                                            sumoPlayer.removeLife();
                                            McsuScoreboard.defaultScoreboard.update();

                                            Player killer = deathEvent.getEntity().getKiller();
                                            if (killer != null) {
                                                BlockSumoInventory.givePowerUp(killer);
                                                assert deathEventPlayer != null;
                                                killer.sendMessage(ChCol.BLUE + "You have received a powerup for killing " + deathEventPlayer.getDisplayName() + ChCol.BLUE + "!");
                                            }

                                            if (sumoPlayer.getLives() <= 0) {
                                                game.eliminatePlayer(sumoPlayer.toBukkit());
                                                game.checkAliveTeams(true);
                                                checkIfEnded();
                                            } else {
                                                new BukkitRunnable() {
                                                    int timeLeft = 3;

                                                    @Override
                                                    public void run() {

                                                        if (timeLeft == 0) {

                                                            MapPoint pickedPoint = SpawnManager.tpPlayerToGameSpawn(game.getMapLoader(), deathEventPlayer);
                                                            Double[] pointCoords = pickedPoint.getCoordinates();
                                                            Location blockLoc = new Location(
                                                                    game.getWorld(),
                                                                    pointCoords[0],
                                                                    pointCoords[1],
                                                                    pointCoords[2]
                                                            );

                                                            game.getWorld().getBlockAt(blockLoc).setType(Material.AIR);
                                                            game.getWorld().getBlockAt(blockLoc.add(0, 1, 0)).setType(Material.AIR);

                                                            deathEventPlayer.sendTitle(ChatColor.RED +""+ ChatColor.BOLD + "Go!", "", 0, 20, 10);

                                                            deathEventPlayer.playSound(deathEventPlayer.getLocation(), Game.DefaultRespawnSound, 1, 1);
                                                            deathEventPlayer.setGameMode(GameMode.SURVIVAL);

                                                            BlockSumoInventory inventory = new BlockSumoInventory();
                                                            inventory.load(deathEvent.getEntity());

                                                            BlockSumoInventory.givePowerUp(deathEventPlayer);
                                                            deathEventPlayer.sendMessage(ChCol.BLUE + "You have received a powerup!");

                                                            sumoPlayer.setSpawnProt(true);

                                                            new BukkitRunnable() {
                                                                @Override
                                                                public void run() {
                                                                    //Bukkit.broadcastMessage("Spwan prot off");
                                                                    sumoPlayer.setSpawnProt(false);
                                                                }
                                                            }.runTaskLater(MCSU_Main.Mcsu_Plugin, 60L);

                                                            BlockSumoPlayer.fromBukkit(deathEventPlayer).resetLifePowerups();

                                                            cancel();
                                                        } else {
                                                            deathEventPlayer.sendTitle(
                                                                    ChatColor.RED + "Respawning in " + ChatColor.WHITE +""+ ChatColor.BOLD +""+  timeLeft,
                                                                    ChatColor.RED +""+ sumoPlayer.getLives() + " lives left!"
                                                            );
                                                            deathEventPlayer.playSound(deathEventPlayer.getLocation(), Game.DefaultStartTimerTickSound, 1, 1);
                                                        }

                                                        timeLeft -= 1;
                                                    }
                                                }.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);
                                            }
                                        },
                                        "PlayerDeathEvent"))
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            powerupTimer.cancel();
            displayLivesTimer.cancel();

            BlockSumoPlayer.BlockSumoPlayers = new ArrayList<>();

            game.endGame(game.getAliveTeams().get(0));

        }

    }

}
