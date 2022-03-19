package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;
import com.cloud.mcsu_rf.Inventories.SkybattleInventory;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Enums.PointGoal;
import com.cloud.mcsu_rf.Objects.Game.*;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Objects.GameFunctions.PointAwarder;
import com.cloud.mcsu_rf.Objects.GameFunctions.VerticleBorder;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Skybattle {

    Game game;
    int killZoneY;
    SkybattleInventory skybattleInventory;
    BukkitRunnable preBorder;
    int borderY;
    World world;

    Integer[] corner1;
    Integer[] corner2;
    Integer[] mapCenter;

    public void init() {

        game = new Game("Skybattle")
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {
                                    for(Player player : Bukkit.getOnlinePlayers()) {
                                        world = player.getWorld();
                                    }

                                    killZoneY = (int) game.getMapMetadata().get("GameData.KillZoneY");
                                    borderY = (int) game.getMapMetadata().get("GameData.BorderY");

                                    corner1 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.BorderCorner1")).split(" "));
                                    corner2 = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.BorderCorner2")).split(" "));

                                    mapCenter = ParseArr.toInteger(((String) game.getMapMetadata().get("GameData.MapCenter")).split(" "));

                                    world.getWorldBorder().setCenter(mapCenter[0],mapCenter[1]);
                                    world.getWorldBorder().setSize(160,1);

                                            Bukkit.getLogger().info(String.valueOf(killZoneY));

                                            skybattleInventory = new SkybattleInventory();

                                            game.getGamestate("afterCountdown").addGameFunction(

                                                    //Done in here because it has to load off config
                                                    new HeightActionZone(
                                                            killZoneY,
                                                            true
                                                    )
                                                            .setWhilePlayerInside(player -> {
                                                                if (
                                                                        game.getAlivePlayers().contains(
                                                                                McsuPlayer.fromBukkit(player)
                                                                        )
                                                                ) {
                                                                    player.setHealth(0);
                                                                }
                                                            })

                                            );

                                        }

                                )
                                .addGameFunction(new CustomEventListener(Event -> {

                                    BlockPlaceEvent placeEvent = (BlockPlaceEvent) Event;
                                    skybattleInventory.reloadInventory(placeEvent.getPlayer());

                                }, "BlockPlaceEvent"))
                                .addGameFunction(new CustomEventListener(Event -> {

                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        skybattleInventory.loadInventory(player);
                                    }

                                }, "GameSpawnsActivatedEvent"))
                                .addGameFunction(new PointAwarder(PointGoal.Survival, 5))
                                .addGameFunction(new CustomEventListener(Event -> {

                                    game.eliminatePlayer(
                                            ( (PlayerDeathEvent) Event ).getEntity()
                                    );

                                    game.checkAliveTeams( true );

                                    checkIfEnded();

                                }, "PlayerDeathEvent"))
                                .addGameFunction(new CustomEventListener(Event -> {
                                    game.getGamestate("afterCountdown").setEnabled(true);
                                }, "GameCountdownEndEvent"))
                )
                .addGameState(new GameState("afterCountdown")
                        .onEnable(() -> {
                            EventListenerMain.setActivityRule("TileDrops", true);
                            EventListenerMain.setActivityRule("TileBreaking", true);
                            EventListenerMain.setActivityRule("PVP", true);
                            EventListenerMain.setActivityRule("ExplosionDamage", true);
                            EventListenerMain.setActivityRule("Crafting", true);
                            EventListenerMain.setActivityRule("FallDamage", true);
                            EventListenerMain.setActivityRule("EntityDamage", true);
                            preBorder = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    game.getGamestate("borderStart").setEnabled(true);
                                    this.cancel();
                                }
                            };
                            preBorder.runTaskLater(MCSU_Main.Mcsu_Plugin, 20L*150); // Delays for 2 min 30 secs
                        })
                ).addGameState( new GameState("borderStart")
                        .onEnable(() -> {
                            VerticleBorder border = new VerticleBorder(borderY,corner1,corner2);
                            border.startBorder();
                            Bukkit.broadcastMessage(ChatColor.RED+"Border shrinking!");
                            for(Player players : Bukkit.getOnlinePlayers()) {
                                world = players.getWorld();
                                world.getWorldBorder().setSize(10,90);
                            }
                        }));

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
