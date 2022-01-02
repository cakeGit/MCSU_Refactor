package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SkybattleInventory;
import com.cloud.mcsu_rf.Objects.Enums.PointGoal;
import com.cloud.mcsu_rf.Objects.Game.*;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Objects.GameFunctions.PointAwarder;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Skybattle {

    Game game;
    int killZoneY;
    SkybattleInventory skybattleInventory;

    public void init() {

        game = new Game("Skybattle")
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {

                                    killZoneY = (int) game.getMapMetadata().get("GameData.KillZoneY");
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
                                    skybattleInventory.reloadInventory(placeEvent.getHand(), placeEvent.getPlayer());

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
                        })
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
