package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SpleefInventory;
import com.cloud.mcsu_rf.Objects.Game_Functions.HeightActionZone;
import com.cloud.mcsu_rf.Objects.Game_Functions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.Game_Functions.PlayerInventoryManager;
import com.cloud.mcsu_rf.Objects.Game_Functions.PointAwarder;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class Spleef {

    Game game;

    public int killZoneY;

    public void init() {

        game = new Game("Spleef")
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {
                                            EventListenerMain.setActivityRule("TileDrops", false);
                                            EventListenerMain.setActivityRule("TileBreaking", false);
                                            EventListenerMain.setActivityRule("PVP", false);

                                            killZoneY = (int) game.getMapMetadata().get("GameData.KillZoneY");
                                            Bukkit.getLogger().info(String.valueOf(killZoneY));

                                            game.getGamestate("afterCountdown").addGameFunction(
                                                    new HeightActionZone(
                                                            killZoneY,
                                                            true
                                                    )
                                                            .setWhilePlayerInside(player -> {
                                                                if (
                                                                        game.getAlivePlayers().contains(
                                                                                McsuPlayer.getPlayerByBukkitPlayer(player)
                                                                        )
                                                                ) {
                                                                    player.setHealth(0);
                                                                }
                                                            })
                                            );

                                        }
                                )
                                .addGameFunction(new PointAwarder("Survival", 2))
                                .addGameFunction(new PlayerInventoryManager(new SpleefInventory()))
                                .addGameFunction(new CustomEventListener("PlayerDeathEvent", Event -> {

                                    game.eliminatePlayer(
                                            ( (PlayerDeathEvent) Event ).getEntity()
                                    );

                                    game.checkAliveTeams( true );

                                    checkIfEnded();

                                }))
                                .addGameFunction(new CustomEventListener("GameCountdownEndEvent", Event -> {
                                    game.getGamestate("afterCountdown").setEnabled(true);
                                    EventListenerMain.setActivityRule("TileBreaking", true);
                                }))
                )
                .addGameState(
                        new GameState("afterCountdown", false)
                                .onEnable( () -> {
                                    Bukkit.getLogger().info("Countdown End is now enabled!");
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
