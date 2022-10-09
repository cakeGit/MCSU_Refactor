package com.cloud.mcsu_rf.Games;

import com.cak.what.Util.ChCol;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Game.GameState;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZones.HeightKillZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.BuildLimits.BuildMaxDistance;
import com.cloud.mcsu_rf.Definitions.GameFunctions.BuildLimits.BuildMaxHeight;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.GameFunctions.GamePrefixText;
import com.cloud.mcsu_rf.Definitions.GameFunctions.InventoryManager;
import com.cloud.mcsu_rf.Definitions.GameFunctions.PointAwarders.KillsAwarder;
import com.cloud.mcsu_rf.Definitions.Map.MapPoint;
import com.cloud.mcsu_rf.Definitions.Map.SpawnManager;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.McsuScoreboard;
import com.cloud.mcsu_rf.Definitions.McsuTeam;
import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.GamePlayers.KohPlayer;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;
import com.cloud.mcsu_rf.Inventories.KohInventory;
import com.cloud.mcsu_rf.MCSU_Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;

public class KOH {

    Game game;

    BukkitRunnable hillUpdate;
    InventoryManager inventoryManager = new InventoryManager(new KohInventory());

    int killZoneY;
    int buildHeightLimit;
    int buildDistanceLimit;
    Integer[] buildDistanceOrigin;
    Integer[] hillZoneTo;
    Integer[] hillZoneFrom;
    BoundingBox hillBoundingBox;

    public static int captureTime = 30;
    public static McsuTeam owner = null;
    public static ArrayList<McsuTeam> hillTeams = new ArrayList<>();
    public static int ownerTime = 0;

    ArrayList<Location> placedBlockPositions = new ArrayList<>();

    public void init() {

        game = new Game("KOH", "King Of The Hill") // placeholder name
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new CustomEventListener(event -> {game.checkAliveTeams(false);checkIfEnded();}, "PlayerLeaveEvent"))
                                .addGameFunction(new GamePrefixText(
                                        mcsuPlayer -> KohPlayer.fromBukkit(mcsuPlayer.toBukkit()).getLivesTabString()
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

                                    hillZoneTo = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.HillZoneTo")).split(" "));
                                    hillZoneFrom = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.HillZoneFrom")).split(" "));

                                    hillBoundingBox = new BoundingBox(
                                            hillZoneTo[0],hillZoneTo[1],hillZoneTo[2],
                                            hillZoneFrom[0],hillZoneFrom[1],hillZoneFrom[2]
                                    );

                                    game.getGamestate("afterCountdown")
                                            .addGameFunction(new HeightKillZone(
                                                    killZoneY,
                                                    true
                                            ), true)
                                            .addGameFunction(new BuildMaxHeight(buildHeightLimit), true)
                                            .addGameFunction(new BuildMaxDistance(
                                                    buildDistanceOrigin[0],
                                                    buildDistanceOrigin[1],
                                                    buildDistanceLimit
                                            ), true)
                                            .addGameFunction(
                                                    new ActionZone(
                                                            new BlockVector(hillZoneTo[0],hillZoneTo[1],hillZoneTo[2]),
                                                            new BlockVector(hillZoneFrom[0],hillZoneFrom[1],hillZoneFrom[2])
                                                    ).setOnEnterEvent(player -> {
                                                        Bukkit.broadcastMessage("a");

                                                        McsuTeam team = McsuPlayer.fromBukkit(player).getTeam();

                                                        if (!hillTeams.contains(team)) {
                                                            hillTeams.add(team);
                                                        }

                                                    }).setOnExitEvent(player -> {

                                                        McsuTeam team = McsuPlayer.fromBukkit(player).getTeam();

                                                        hillTeams.remove(team);

                                                    })
                                            ,true);

                                    game.getGamestate("lobby")
                                            .addGameFunction(
                                                    new HeightActionZone(killZoneY, true)
                                                            .setWhilePlayerInside(player -> SpawnManager.teleportToMapPoint(
                                                                    player,
                                                                    game.getMapMetadata().getLobbyPoints().get(0),
                                                                    game.getWorld()
                                                            )), true);

                                    hillUpdate = new BukkitRunnable() {
                                        @Override
                                        public void run() {

                                            if (hillTeams.contains(owner)) {

                                            } else if (hillTeams.size() != 0) {
                                                if (ownerTime == 0) {
                                                    if (hillTeams.size() == 1) {
                                                        Bukkit.broadcastMessage("Switchowner");
                                                        owner = hillTeams.get(0);
                                                    }
                                                }

                                                ownerTime-=1;
                                            }

                                            for (KohPlayer kohPlayer : KohPlayer.KohPlayers) {
                                                kohPlayer.toBukkit().spigot().sendMessage(
                                                    ChatMessageType.ACTION_BAR,
                                                    TextComponent.fromLegacyText(kohPlayer.getTimeString())
                                                );
                                            }

                                        }
                                    };
                                    hillUpdate.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);


                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        new KohPlayer(player);
                                    }


                                    game.getGamestate("lobby").setEnabled(true);

                                })
                                .addGameFunction(inventoryManager)
                                /*.addGameFunction(new CustomExplosionVelocities())
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
                                    } else /*if (event.getEventName().equals("PlayerInteractEvent")) {
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

                                }, "InventoryClickEvent", "PlayerInteractEvent"))*/
                                .addGameFunction(new CustomEventListener(event -> {
                                    EntityDamageByEntityEvent damageByEntityEvent = (EntityDamageByEntityEvent) event;
                                    if (KohPlayer.fromBukkit((Player) damageByEntityEvent.getEntity()).hasSpawnProt()) {
                                        damageByEntityEvent.setCancelled(true);
                                    }
                                }, "EntityDamageByEntityEvent"))
                                /*.addGameFunction(new CustomEventListener(event -> {
                                    try {
                                        PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) event;
                                        Player player = playerInteractEvent.getPlayer();
                                        if(playerInteractEvent.getItem().getType().equals(Material.FIRE_CHARGE)) {
                                            Location fireballLoc = player.getEyeLocation();
                                            player.getWorld().spawnEntity(fireballLoc, EntityType.FIREBALL);
                                            playerInteractEvent.getItem().setAmount(playerInteractEvent.getItem().getAmount()-1);
                                        }
                                    } catch (NullPointerException ignored){}
                                }, "PlayerInteractEvent"))*/

                )
                .addGameState(
                        new GameState("lobby")
                )
                .addGameState(
                        new GameState("afterCountdown")
                                .onEnable(()-> {

                                    EventListenerMain.setActivityRule("PVP", true);
                                    game.getGamestate("lobby").setEnabled(false);
                                    for(Player players : Bukkit.getOnlinePlayers()) {
                                        players.setGlowing(true);
                                    }

                                })
                                .addGameFunction(new CustomEventListener(
                                        event -> {
                                            BlockPlaceEvent placeEvent = (BlockPlaceEvent) event;
                                            Location blockLoc = placeEvent.getBlockPlaced().getLocation();

                                            placedBlockPositions.add(blockLoc);

                                            if (hillBoundingBox.contains(blockLoc.toVector())) {
                                                placeEvent.setCancelled(true);
                                                placeEvent.getPlayer().sendMessage(ChCol.RED + "You cant build on the hill!");
                                            }
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
                                            KohPlayer sumoPlayer = KohPlayer.fromBukkit(deathEventPlayer);

                                            McsuScoreboard.defaultScoreboard.update();

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

                                                            KohInventory inventory = new KohInventory();
                                                            inventory.load(deathEvent.getEntity());

                                                            sumoPlayer.setSpawnProt(true);

                                                            new BukkitRunnable() {
                                                                @Override
                                                                public void run() {
                                                                    //Bukkit.broadcastMessage("Spwan prot off");
                                                                    sumoPlayer.setSpawnProt(false);
                                                                }
                                                            }.runTaskLater(MCSU_Main.Mcsu_Plugin, 60L);

                                                            cancel();
                                                        } else {
                                                            deathEventPlayer.sendTitle(
                                                                    ChatColor.RED + "Respawning in " + ChatColor.WHITE +""+ ChatColor.BOLD +""+  timeLeft,
                                                                    "");
                                                            deathEventPlayer.playSound(deathEventPlayer.getLocation(), Game.DefaultStartTimerTickSound, 1, 1);
                                                        }

                                                        timeLeft -= 1;
                                                    }
                                                }.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);
                                        }, "PlayerDeathEvent"))
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            KohPlayer.KohPlayers = new ArrayList<>();

            game.endGame(game.getAliveTeams().get(0));

        }

    }

}
