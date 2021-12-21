package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SkybattleInventory;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.PointAwarder;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SurvivalGames {

    Game game;
    BukkitRunnable gracePeriodCountdown;

    public void init() {

        game = new Game("Survival Games")
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new PointAwarder("Survival", 2))
                                .addGameFunction(new CustomEventListener(Event -> {
                                    game.eliminatePlayer(
                                            ( (PlayerDeathEvent) Event ).getEntity()
                                    );

                                    game.checkAliveTeams( true );
                                    checkIfEnded();

                                }, "PlayerDeathEvent"))
                                .addGameFunction(new CustomEventListener(Event -> {
                                    EventListenerMain.setActivityRule("TileDrops", true);
                                    EventListenerMain.setActivityRule("TileBreaking", true);
                                    EventListenerMain.setActivityRule("PVP", true);
                                    EventListenerMain.setActivityRule("ExplosionDamage", true);
                                    EventListenerMain.setActivityRule("Crafting", true);
                                    EventListenerMain.setActivityRule("FallDamage", true);
                                    game.getGamestate("afterCountdown").setEnabled(true);
                                }, "GameCountdownEndEvent"))
                )
                .addGameState(
                        new GameState("afterCountdown")
                                .onEnable(()-> {

                                    game.getGamestate("graceperiod").setEnabled(true);

                                    gracePeriodCountdown = new BukkitRunnable() {
                                        @Override
                                        public void run() {

                                            Bukkit.broadcastMessage(ChatColor.BLUE + "Every player has received a powerup!");
                                            //inventoryManager.emitInventoryEvent("Powerup");

                                        }
                                    };
                                    gracePeriodCountdown.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L * 30L); //Happens every 30s
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
