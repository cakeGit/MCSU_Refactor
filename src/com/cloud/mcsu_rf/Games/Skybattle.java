package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SkybattleInventory;
import com.cloud.mcsu_rf.Objects.Game.*;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.HeightActionZone;
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
                                                                                McsuPlayer.getByBukkitPlayer(player)
                                                                        )
                                                                ) {
                                                                    player.setHealth(0);
                                                                }
                                                            })

                                            );

                                        }

                                )
                                .addGameFunction(new CustomEventListener("BlockPlaceEvent", Event -> {

                                    BlockPlaceEvent placeEvent = (BlockPlaceEvent) Event;
                                    skybattleInventory.reloadInventory(placeEvent.getHand(), placeEvent.getPlayer());

                                }))
                                .addGameFunction(new CustomEventListener("GameSpawnsActivatedEvent", Event -> {

                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        skybattleInventory.loadInventory(player);
                                    }

                                }))
                                .addGameFunction(new PointAwarder("Survival", 2))
                                .addGameFunction(new CustomEventListener("PlayerDeathEvent", Event -> {

                                    game.eliminatePlayer(
                                            ( (PlayerDeathEvent) Event ).getEntity()
                                    );

                                    game.checkAliveTeams( true );

                                    checkIfEnded();

                                }))
                                .addGameFunction(new CustomEventListener("GameCountdownEndEvent", Event -> {
                                    EventListenerMain.setActivityRule("TileDrops", true);
                                    EventListenerMain.setActivityRule("TileBreaking", true);
                                    EventListenerMain.setActivityRule("PVP", true);
                                    EventListenerMain.setActivityRule("ExplosionDamage", true);
                                    EventListenerMain.setActivityRule("Crafting", true);
                                    game.getGamestate("afterCountdown").setEnabled(true);
                                }))
                )
                .addGameState(new GameState("afterCountdown"));


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
