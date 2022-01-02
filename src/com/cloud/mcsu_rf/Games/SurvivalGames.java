package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SurvivalGamesInventory;
import com.cloud.mcsu_rf.LootTables.SurvivalGamesLoot;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Enums.PointGoal;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.PointAwarder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class SurvivalGames {

    Game game;
    BukkitRunnable gracePeriodCountdown;
    int gracePeriodTime = 30;
    MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    public void init() {

        SurvivalGamesLoot.init();

        game = new Game("SurvivalGames")
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new PointAwarder(PointGoal.Survival, 5))
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
                                    EventListenerMain.setActivityRule("PVP", false);
                                    EventListenerMain.setActivityRule("ExplosionDamage", true);
                                    EventListenerMain.setActivityRule("Crafting", true);
                                    EventListenerMain.setActivityRule("FallDamage", true);
                                    game.getGamestate("afterCountdown").setEnabled(true);
                                }, "GameCountdownEndEvent"))
                )
                .addGameState(
                        new GameState("afterCountdown")
                                .onEnable(() -> {
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "PVP will be enabled in "+gracePeriodTime+" seconds!");
                                    game.getGamestate("gracePeriod").setEnabled(true);
                                })
                                .addGameFunction(new CustomEventListener(Event -> {
                                    PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) Event;
                                    Block b = playerInteractEvent.getClickedBlock();
                                    if(b.getType().equals(Material.CHEST)) {
                                        Bukkit.broadcastMessage(String.valueOf(b.hasMetadata("opened")));
                                        if(!b.hasMetadata("opened")) {
                                            SurvivalGamesInventory inv = new SurvivalGamesInventory();
                                            ItemStack[] contents = inv.getInventory().getContents();
                                            Chest c = (Chest) b.getState();
                                            c.getBlockInventory().setContents(contents);
                                        }
                                        b.setMetadata("opened", new FixedMetadataValue(plugin,true));
                                    }
                                }, "PlayerInteractEvent"))
                ).addGameState(
                        new GameState("gracePeriod")
                                .onEnable(() -> {
                                    gracePeriodCountdown = new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            game.getGamestate("postGracePeriod").setEnabled(true);
                                            this.cancel();
                                        }
                                    };
                                    gracePeriodCountdown.runTaskLater(MCSU_Main.Mcsu_Plugin, 20L*gracePeriodTime);
                                })
                ).addGameState(
                        new GameState("postGracePeriod")
                                .onEnable(() -> {
                                    EventListenerMain.setActivityRule("PVP",true);
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "Grace Period is Over. PVP is Enabled!");
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
