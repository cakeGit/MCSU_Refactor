package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.GamePlayers.MCTriathlonPlayer;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;
import com.cloud.mcsu_rf.Inventories.MCTriathlonInventory;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Game.GameState;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;

public class MCTriathlon {

    Game game;

    public static float timeLeft;
    public static boolean countdownActive;
    BukkitRunnable stopwatchDisplayTimer;
    BlockVector[] area1portal = new BlockVector[2];
    Integer[] GlassWallFrom;
    Integer[] GlassWallTo;
    Double[] area2start = new Double[3];
    Float[] area2startrot = new Float[2];
    BlockVector[] area3clear = new BlockVector[2];
    BlockVector[] area3boat = new BlockVector[2];
    int resetZoneY = 0;
    BlockVector[] checkpoint1area = new BlockVector[2];
    BlockVector[] checkpoint2area = new BlockVector[2];
    BlockVector[] checkpoint3area = new BlockVector[2];
    BlockVector[] crasharea = new BlockVector[2];
    Double[] checkpoint0 = new Double[3];
    Float[] checkpoint0rot = new Float[2];
    Double[] checkpoint1 = new Double[3];
    Float[] checkpoint1rot = new Float[2];
    Double[] checkpoint2 = new Double[3];
    Float[] checkpoint2rot = new Float[2];
    Double[] checkpoint3 = new Double[3];
    Float[] checkpoint3rot = new Float[2];

    int currentPos = 1;

    MCTriathlonInventory mcTriathlonInventory = new MCTriathlonInventory();

    public void init() {

        game = new Game("mcTriathlon", "MC Triathlon")
                .setPlayerGamemode(GameMode.ADVENTURE)
                .setFreezeOnGameCountdown(false)
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new CustomEventListener(
                                        event -> game.getGamestate("afterCountdown").setEnabled(true),
                                        "GameCountdownEndEvent"
                                ))
                                .onEnable(() -> {
                                    GlassWallFrom = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.GlassWallFrom")).split(" "));
                                    GlassWallTo = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.GlassWallTo")).split(" "));
                                    Integer[] area1portal1 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Area1Portal1")).split(" "));
                                    Integer[] area1portal2 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Area1Portal2")).split(" "));
                                    area1portal[0] = new BlockVector(area1portal1[0],area1portal1[1],area1portal1[2]);
                                    area1portal[1] = new BlockVector(area1portal2[0],area1portal2[1],area1portal2[2]);

                                    Integer[] area3clear1 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Area3Clear1")).split(" "));
                                    Integer[] area3clear2 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Area3Clear2")).split(" "));
                                    area3clear[0] = new BlockVector(area3clear1[0],area3clear1[1],area3clear1[2]);
                                    area3clear[1] = new BlockVector(area3clear2[0],area3clear2[1],area3clear2[2]);

                                    Integer[] area3boat1 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Area3Boat1")).split(" "));
                                    Integer[] area3boat2 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Area3Boat2")).split(" "));
                                    area3boat[0] = new BlockVector(area3boat1[0],area3boat1[1],area3boat1[2]);
                                    area3boat[1] = new BlockVector(area3boat2[0],area3boat2[1],area3boat2[2]);

                                    area2start = ParseArr.toDouble(((String) game.getMapMetadata().get("GameData.Area2Start")).split(" "));
                                    area2startrot = ParseArr.toFloat(((String) game.getMapMetadata().get("GameData.Area2StartRotation")).split(" "));
                                    resetZoneY = (int) game.getMapMetadata().get("GameData.ResetZoneY");

                                    Integer[] checkpoint1From = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Checkpoint1From")).split(" "));
                                    Integer[] checkpoint1To = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Checkpoint1To")).split(" "));
                                    checkpoint1area[0] = new BlockVector(checkpoint1From[0],checkpoint1From[1],checkpoint1From[2]);
                                    checkpoint1area[1] = new BlockVector(checkpoint1To[0],checkpoint1To[1],checkpoint1To[2]);

                                    Integer[] checkpoint2From = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Checkpoint2From")).split(" "));
                                    Integer[] checkpoint2To = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Checkpoint2To")).split(" "));
                                    checkpoint2area[0] = new BlockVector(checkpoint2From[0],checkpoint2From[1],checkpoint2From[2]);
                                    checkpoint2area[1] = new BlockVector(checkpoint2To[0],checkpoint2To[1],checkpoint2To[2]);

                                    Integer[] checkpoint3From = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Checkpoint3From")).split(" "));
                                    Integer[] checkpoint3To = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.Checkpoint3To")).split(" "));
                                    checkpoint3area[0] = new BlockVector(checkpoint3From[0],checkpoint3From[1],checkpoint3From[2]);
                                    checkpoint3area[1] = new BlockVector(checkpoint3To[0],checkpoint3To[1],checkpoint3To[2]);

                                    checkpoint0 = ParseArr.toDouble(((String) game.getMapMetadata().get("GameData.Checkpoint0")).split(" "));
                                    checkpoint0rot = ParseArr.toFloat(((String) game.getMapMetadata().get("GameData.Checkpoint0Rotation")).split(" "));
                                    checkpoint1 = ParseArr.toDouble(((String) game.getMapMetadata().get("GameData.Checkpoint1")).split(" "));
                                    checkpoint1rot = ParseArr.toFloat(((String) game.getMapMetadata().get("GameData.Checkpoint1Rotation")).split(" "));
                                    checkpoint2 = ParseArr.toDouble(((String) game.getMapMetadata().get("GameData.Checkpoint2")).split(" "));
                                    checkpoint2rot = ParseArr.toFloat(((String) game.getMapMetadata().get("GameData.Checkpoint2Rotation")).split(" "));
                                    checkpoint3 = ParseArr.toDouble(((String) game.getMapMetadata().get("GameData.Checkpoint3")).split(" "));
                                    checkpoint3rot = ParseArr.toFloat(((String) game.getMapMetadata().get("GameData.Checkpoint3Rotation")).split(" "));

                                    Integer[] crashFrom = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.CrashFrom")).split(" "));
                                    Integer[] crashTo = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.CrashTo")).split(" "));
                                    crasharea[0] = new BlockVector(crashFrom[0],crashFrom[1],crashFrom[2]);
                                    crasharea[1] = new BlockVector(crashTo[0],crashTo[1],crashTo[2]);

                                    int minx = Math.min(GlassWallFrom[0], GlassWallTo[0]);
                                    int maxx = Math.max(GlassWallFrom[0], GlassWallTo[0]);

                                    int miny = Math.min(GlassWallFrom[1], GlassWallTo[1]);
                                    int maxy = Math.max(GlassWallFrom[1], GlassWallTo[1]);

                                    int minz = Math.min(GlassWallFrom[2], GlassWallTo[2]);
                                    int maxz = Math.max(GlassWallFrom[2], GlassWallTo[2]);

                                    for (int x = minx; x <= maxx; x++) {
                                        for (int y = miny; y <= maxy; y++) {
                                            for (int z = minz; z <= maxz; z++) {
                                                game.getWorld().getBlockAt(x, y, z).setType(Material.LIGHT_BLUE_STAINED_GLASS);
                                            }}
                                    }
                                    timeLeft = 120;
                                    countdownActive = false;

                                    for (McsuPlayer player : Game.getAlivePlayers()) {
                                        mcTriathlonInventory.loadArea1(player);
                                    }

                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        new MCTriathlonPlayer(player,0);
                                        player.setCollidable(false);
                                    }


                                    game.getGamestate("afterCountdown").addGameFunction(
                                            new ActionZone(
                                                    area1portal[0],
                                                    area1portal[1]
                                            )
                                                    .setWhilePlayerInside(player -> {
                                                        if (
                                                                game.getAlivePlayers().contains(
                                                                        McsuPlayer.fromBukkit(player)
                                                                )
                                                        ) {
                                                            mcTriathlonInventory.loadArea2(McsuPlayer.fromBukkit(player));
                                                            Location area2startloc = new Location(player.getWorld(),area2start[0],area2start[1],area2start[2],area2startrot[0],area2startrot[1]);
                                                            player.teleport(area2startloc);
                                                        }
                                                    })
                                    )
                                            .addGameFunction(
                                            new ActionZone(
                                                    area3clear[0],
                                                    area3clear[1]
                                            )
                                                    .setWhilePlayerInside(player -> {
                                                        if(
                                                                game.getAlivePlayers().contains(
                                                                        McsuPlayer.fromBukkit(player)
                                                                )
                                                        ) {
                                                            player.getInventory().clear();
                                                        }
                                                    })
                                    ).addGameFunction(
                                            new ActionZone(
                                                    area3boat[0],
                                                    area3boat[1]
                                            )
                                                    .setWhilePlayerInside(player -> {
                                                        if(
                                                                game.getAlivePlayers().contains(
                                                                        McsuPlayer.fromBukkit(player)
                                                                )
                                                        ) {
                                                            mcTriathlonInventory.loadArea3(McsuPlayer.fromBukkit(player));
                                                        }
                                                    })
                                    ).addGameFunction(
                                            new ActionZone(
                                                    checkpoint1area[0],
                                                    checkpoint1area[1]
                                            )
                                                    .setWhilePlayerInside(player -> {
                                                        if(
                                                                game.getAlivePlayers().contains(
                                                                        McsuPlayer.fromBukkit(player)
                                                                )
                                                        ) {
                                                            if(MCTriathlonPlayer.fromBukkit(player).getCheckpoint() != 1) {
                                                                game.spawnFireworks(player.getLocation(),1,Color.LIME);
                                                                player.sendMessage(ChatColor.AQUA+"You have reached the first checkpoint.\nIf you mess up you will be teleported back to this checkpoint.");
                                                                MCTriathlonPlayer.fromBukkit(player).setCheckpoint(1);
                                                            }

                                                        }
                                                    })
                                    ).addGameFunction(
                                            new ActionZone(
                                                    crasharea[0],
                                                    crasharea[1]
                                            ).setWhilePlayerInside(player -> {
                                                Location loc = player.getLocation();
                                                loc.add(0,-1,0);
                                                if(!loc.getBlock().getType().equals(Material.AIR)) {
                                                    player.sendMessage(ChatColor.RED+"You crashed!");
                                                    player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_HURT,100,1);
                                                    switch(MCTriathlonPlayer.fromBukkit(player).getCheckpoint()) {
                                                        case 0:
                                                            player.teleport(new Location(player.getWorld(),checkpoint0[0],checkpoint0[1],checkpoint0[2],checkpoint0rot[0],checkpoint0rot[1]));
                                                            player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to the start");
                                                            break;
                                                        case 1:
                                                            player.teleport(new Location(player.getWorld(),checkpoint1[0],checkpoint1[1],checkpoint1[2],checkpoint1rot[0],checkpoint1rot[1]));
                                                            player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                            break;
                                                        case 2:
                                                            player.teleport(new Location(player.getWorld(),checkpoint2[0],checkpoint2[1],checkpoint2[2],checkpoint2rot[0],checkpoint2rot[1]));
                                                            player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                            break;
                                                    }
                                                }
                                                player.setFireTicks(1);
                                                player.setHealth(20);
                                            })
                                    ).addGameFunction(
                                            new ActionZone(
                                                    checkpoint2area[0],
                                                    checkpoint2area[1]
                                            )
                                                    .setWhilePlayerInside(player -> {
                                                        if(
                                                                game.getAlivePlayers().contains(
                                                                        McsuPlayer.fromBukkit(player)
                                                                )
                                                        ) {
                                                            if(MCTriathlonPlayer.fromBukkit(player).getCheckpoint() != 2) {
                                                                game.spawnFireworks(player.getLocation(),1,Color.LIME);
                                                                MCTriathlonPlayer.fromBukkit(player).setCheckpoint(2);
                                                                player.sendMessage(ChatColor.AQUA+"You have reached the second checkpoint.\nIf you mess up you will be teleported back to this checkpoint.");
                                                            }
                                                        }
                                                    })
                                    ).addGameFunction(
                                            new ActionZone(
                                                    checkpoint3area[0],
                                                    checkpoint3area[1]
                                            )
                                                    .setWhilePlayerInside(player -> {
                                                        if(
                                                                game.getAlivePlayers().contains(
                                                                        McsuPlayer.fromBukkit(player)
                                                                )
                                                        ) {
                                                            if(MCTriathlonPlayer.fromBukkit(player).getCheckpoint() != 3) {
                                                                game.spawnFireworks(player.getLocation(),1,Color.LIME);
                                                                MCTriathlonPlayer.fromBukkit(player).setCheckpoint(3);
                                                                player.sendMessage(ChatColor.AQUA+"You have reached the third checkpoint.\nIf you mess up you will be teleported back to this checkpoint.");
                                                            } else {
                                                                mcTriathlonInventory.loadArea3(McsuPlayer.fromBukkit(player));
                                                            }
                                                        }
                                                    })
                                    ).addGameFunction(
                                            new HeightActionZone(
                                                    resetZoneY,
                                                    true
                                            ).setWhilePlayerInside(player -> {
                                                switch(MCTriathlonPlayer.fromBukkit(player).getCheckpoint()) {
                                                    case 0:
                                                        player.teleport(new Location(player.getWorld(),checkpoint0[0],checkpoint0[1],checkpoint0[2],checkpoint0rot[0],checkpoint0rot[1]));
                                                        player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to the start");
                                                        break;
                                                    case 1:
                                                        player.teleport(new Location(player.getWorld(),checkpoint1[0],checkpoint1[1],checkpoint1[2],checkpoint1rot[0],checkpoint1rot[1]));
                                                        player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                        break;
                                                    case 2:
                                                        player.teleport(new Location(player.getWorld(),checkpoint2[0],checkpoint2[1],checkpoint2[2],checkpoint2rot[0],checkpoint2rot[1]));
                                                        player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                        break;
                                                    case 3:
                                                        player.teleport(new Location(player.getWorld(),checkpoint3[0],checkpoint3[1],checkpoint3[2],checkpoint3rot[0],checkpoint3rot[1]));
                                                        player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                        break;
                                                }
                                                MCTriathlonPlayer.fromBukkit(player).getBoat().remove();
                                            }));

                                }).addGameFunction(new CustomEventListener(Event -> {
                                    EntityDamageEvent playerdmgEvent = (EntityDamageEvent) Event;
                                    if(playerdmgEvent.getEntity() instanceof Player) {
                                        Player player = (Player) playerdmgEvent.getEntity();
                                        if(playerdmgEvent.getCause().equals(EntityDamageEvent.DamageCause.LAVA)
                                                || playerdmgEvent.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)
                                                || playerdmgEvent.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                                                || playerdmgEvent.getCause().equals(EntityDamageEvent.DamageCause.FIRE)) {
                                            player.sendMessage(ChatColor.RED+"You crashed!");
                                            player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_HURT,100,1);
                                            switch(MCTriathlonPlayer.fromBukkit(player).getCheckpoint()) {
                                                case 0:
                                                    player.teleport(new Location(player.getWorld(),checkpoint0[0],checkpoint0[1],checkpoint0[2],checkpoint0rot[0],checkpoint0rot[1]));
                                                    player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to the start");
                                                    break;
                                                case 1:
                                                    player.teleport(new Location(player.getWorld(),checkpoint1[0],checkpoint1[1],checkpoint1[2],checkpoint1rot[0],checkpoint1rot[1]));
                                                    player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                    break;
                                                case 2:
                                                    player.teleport(new Location(player.getWorld(),checkpoint2[0],checkpoint2[1],checkpoint2[2],checkpoint2rot[0],checkpoint2rot[1]));
                                                    player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                    break;
                                                case 3:
                                                    player.teleport(new Location(player.getWorld(),checkpoint3[0],checkpoint3[1],checkpoint3[2],checkpoint3rot[0],checkpoint3rot[1]));
                                                    player.sendMessage(ChatColor.DARK_AQUA+"Teleporting you to checkpoint #"+MCTriathlonPlayer.fromBukkit(player).getCheckpoint());
                                                    break;
                                            }
                                            player.setFireTicks(1);
                                            player.setHealth(20);
                                        }
                                    }

                                }, "EntityDamageEvent"))
                                .addGameFunction(new CustomEventListener(Event -> {
                                    VehicleEnterEvent vehicleEnterEvent = (VehicleEnterEvent) Event;
                                    if(vehicleEnterEvent instanceof Player) {
                                        Player player = (Player) vehicleEnterEvent.getEntered();
                                        if(vehicleEnterEvent.getVehicle() instanceof Boat) {
                                            Boat boat = (Boat) vehicleEnterEvent.getVehicle();
                                            MCTriathlonPlayer.fromBukkit(player).setBoat(boat);
                                        }
                                    }
                                }, "VehicleEnterEvent"))
                )
                .addGameState(
                        new GameState("afterCountdown")
                                .addGameFunction(new CustomEventListener(
                                        event -> {
                                            PlayerMoveEvent playerMove = (PlayerMoveEvent) event;
                                            Player player = playerMove.getPlayer();
                                            Location blockLoc = playerMove.getPlayer().getLocation().add(0, -1, 0);

                                            if (blockLoc.getBlock().getType() == Material.GOLD_BLOCK && Game.getAlivePlayers().contains(McsuPlayer.fromBukkit(player))) {
                                                Bukkit.broadcastMessage(
                                                        McsuPlayer.fromBukkit(player).getColouredName(true) +
                                                                McsuPlayer.fromBukkit(player).getColour() +
                                                                " has finished MCTriathlon in " + ChatColor.WHITE + ChatColor.BOLD +
                                                                MCTriathlonPlayer.fromBukkit(player).getFormattedTime() + " (#"+currentPos+")"
                                                );

                                                McsuPlayer.fromBukkit(player).awardPoints((int) (
                                                                ( ((float) (Bukkit.getOnlinePlayers().size() - (currentPos-1)) )
                                                                        / Bukkit.getOnlinePlayers().size() ) * 100
                                                        )
                                                );

                                                currentPos+=1;

                                                game.spawnFireworks(player.getLocation(),3,Color.YELLOW);
                                                game.eliminatePlayer(player);
                                                MCTriathlonPlayer.fromBukkit(player).endTimer();
                                                player.spigot().sendMessage(
                                                        ChatMessageType.ACTION_BAR,
                                                        TextComponent.fromLegacyText(
                                                                ChatColor.GOLD +""+ ChatColor.BOLD + "Finished!"
                                                        )
                                                );
                                                checkIfEnded();
                                                new EventListenerMain().onPlayerDeath(new PlayerDeathEvent(player, new ArrayList<ItemStack>().stream().toList(), 0, ""));
                                            }
                                        },
                                        "PlayerMoveEvent"
                                ))
                                .onEnable(()-> {
                                    GlassWallFrom = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.GlassWallFrom")).split(" "));
                                    GlassWallTo = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.GlassWallTo")).split(" "));

                                    int minx = Math.min(GlassWallFrom[0], GlassWallTo[0]);
                                    int maxx = Math.max(GlassWallFrom[0], GlassWallTo[0]);

                                    int miny = Math.min(GlassWallFrom[1], GlassWallTo[1]);
                                    int maxy = Math.max(GlassWallFrom[1], GlassWallTo[1]);

                                    int minz = Math.min(GlassWallFrom[2], GlassWallTo[2]);
                                    int maxz = Math.max(GlassWallFrom[2], GlassWallTo[2]);

                                    for (int x = minx; x <= maxx; x++) {
                                        for (int y = miny; y <= maxy; y++) {
                                            for (int z = minz; z <= maxz; z++) {
                                                game.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                                            }
                                        }
                                    }

                                    MCTriathlonPlayer.startAllTimers();
                                })
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (Game.getAlivePlayers().size() == 0) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            MCTriathlonPlayer.mctriathlonPlayers = new ArrayList<>();

            game.endGame();

        }

    }

}
