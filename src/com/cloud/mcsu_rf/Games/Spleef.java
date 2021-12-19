package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SpleefInventory;
import com.cloud.mcsu_rf.Objects.Game.*;
import com.cloud.mcsu_rf.Objects.Game_Functions.HeightActionZone;
import com.cloud.mcsu_rf.Objects.Game_Functions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.Game_Functions.PointAwarder;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.Objects;

public class Spleef {

    Game game;

    int killZoneY;
    int explosionPower = 3;
    SpleefInventory spleefInventory;
    String toolGamemodeChoice;

    public void init() {

        game = new Game("Spleef")
                .setGamemodeManager(
                        new GamemodeManager(
                                new GamemodeOptionBlock(
                                        "Tool",
                                        new GamemodeOption(0, "Shovels"),
                                        new GamemodeOption(5, "Fireworks")
                                        )
                        )
                )
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {

                                            EventListenerMain.setActivityRule("TileDrops", false);
                                            EventListenerMain.setActivityRule("TileBreaking", false);
                                            EventListenerMain.setActivityRule("PVP", false);
                                            EventListenerMain.setActivityRule("ExplosionDamage", false);
                                            EventListenerMain.setActivityRule("Crafting", true);
                                            killZoneY = (int) game.getMapMetadata().get("GameData.KillZoneY");
                                            Bukkit.getLogger().info(String.valueOf(killZoneY));

                                            toolGamemodeChoice =
                                                    game.getGamemodeOptionBlockChoice("Tool")
                                                    .getGamemodeOption()
                                                    .getName();

                                            spleefInventory = new SpleefInventory(
                                                    toolGamemodeChoice
                                            );

                                            game.getGamestate("base")
                                                    .addGameFunction(new CustomEventListener("GameSpawnsActivatedEvent", Event -> {

                                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                                            spleefInventory.loadInventory(player);
                                                        }

                                                    }), true);

                                            if (Objects.equals(toolGamemodeChoice, "Fireworks")) {

                                                game.getGamestate("base")
                                                        .addGameFunction(new CustomEventListener("EntityShootBowEvent", Event -> {

                                                            EntityShootBowEvent shootEvent = (EntityShootBowEvent) Event;

                                                            Player player = (Player) shootEvent.getEntity();

                                                            spleefInventory.reloadInventory(player);

                                                        }), true);

                                            }


                                            Bukkit.broadcastMessage(
                                                    game.getGamemodeOptionBlockChoice("Tool")
                                                            .getGamemodeOption()
                                                            .getName() + " a the rapist"
                                            );

                                            game.getGamestate("afterCountdown").addGameFunction(
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
                                .addGameFunction(new PointAwarder("Survival", 2))
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
                                    EventListenerMain.setActivityRule("Crafting", false);
                                }))
                                .addGameFunction(new CustomEventListener("ProjectileHitEvent", Event -> {

                                    ProjectileHitEvent hitEvent = (ProjectileHitEvent) Event;

                                    if (hitEvent.getEntity() instanceof Snowball) {
                                        hitEvent.getHitBlock().setType(Material.AIR);
                                    } else if (hitEvent.getEntity() instanceof Firework) {
                                        Location hitLocation = hitEvent.getHitBlock().getLocation();

                                        hitEvent.getEntity().getWorld().createExplosion(hitLocation, 2.2F, false);
                                        for (int i = 0; i < explosionPower; i++) {
                                            hitEvent.getEntity().getWorld().createExplosion(hitLocation, 1F, false);
                                        }
                                    }

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
