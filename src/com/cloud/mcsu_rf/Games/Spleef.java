package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.Definitions.Game.*;
import com.cloud.mcsu_rf.Definitions.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.GameFunctions.PointAwarders.SurvivalAwarder;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SpleefInventory;
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
    float explosionPower = 7F;
    SpleefInventory spleefInventory;
    String toolGamemodeChoice;

    public void init() {

        game = new Game("spleef", "Spleef")
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
                                            EventListenerMain.setActivityRule("PlaceFireworks", false);
                                            EventListenerMain.setActivityRule("Crafting", true);
                                            EventListenerMain.setActivityRule("LockInvItems", true);
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
                                                    .addGameFunction(new CustomEventListener(Event -> {

                                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                                            spleefInventory.loadInventory(player);
                                                        }

                                                    }, "GameSpawnsActivatedEvent"), true);

                                            if (Objects.equals(toolGamemodeChoice, "Fireworks")) {

                                                game.getGamestate("base")
                                                        .addGameFunction(new CustomEventListener(Event -> {

                                                            EntityShootBowEvent shootEvent = (EntityShootBowEvent) Event;

                                                            Player player = (Player) shootEvent.getEntity();

                                                            spleefInventory.reloadInventory(player);

                                                        }, "EntityShootBowEvent"), true);

                                            }

                                            game.getGamestate("afterCountdown").addGameFunction(
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
                                .addGameFunction(new SurvivalAwarder(20))
                                .addGameFunction(new CustomEventListener(Event -> {

                                    game.eliminatePlayer(
                                            ( (PlayerDeathEvent) Event ).getEntity()
                                    );

                                    game.checkAliveTeams( true );

                                    checkIfEnded();

                                }, "PlayerDeathEvent"))
                                .addGameFunction(new CustomEventListener(Event -> {
                                    game.getGamestate("afterCountdown").setEnabled(true);
                                    EventListenerMain.setActivityRule("TileBreaking", true);
                                    EventListenerMain.setActivityRule("Crafting", false);
                                }, "GameCountdownEndEvent"))
                                .addGameFunction(new CustomEventListener(Event -> {

                                    ProjectileHitEvent hitEvent = (ProjectileHitEvent) Event;

                                    if (hitEvent.getEntity() instanceof Snowball) {
                                        hitEvent.getHitBlock().setType(Material.AIR);
                                    } else if (hitEvent.getEntity() instanceof Firework) {
                                        Location hitLocation;
                                        if (hitEvent.getHitBlock() != null) {
                                            hitLocation = hitEvent.getHitBlock().getLocation();
                                        } else {
                                            hitLocation = hitEvent.getEntity().getLocation();
                                        }

                                        hitEvent.getEntity().getWorld().createExplosion(hitLocation, 2.2F, false);
                                        hitEvent.getEntity().getWorld().createExplosion(hitLocation, explosionPower, false, false);
                                    }

                                }, "ProjectileHitEvent"))
                )
                .addGameState(new GameState("afterCountdown"));


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            game.endGame(game.getAliveTeams().get(0));

        } else  {

            Bukkit.getLogger().info(game.getName() + " has not ended because " + game.getAliveTeams().size()
                    + " teams are still alive");

        }

    }

}
