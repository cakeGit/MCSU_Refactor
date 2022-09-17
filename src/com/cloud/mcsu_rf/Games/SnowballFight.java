package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.GamePlayers.SnowballFightPlayer;
import com.cloud.mcsu_rf.Inventories.SnowballFightInventory;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Definitions.Game.*;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.GameFunctions.GamePrefixText;
import com.cloud.mcsu_rf.Definitions.GameFunctions.InventoryManager;
import com.cloud.mcsu_rf.Definitions.GameFunctions.LethalSnowballs;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class SnowballFight {

    Game game;

    public void init() {

        game = new Game("snowballFight","Snowball Fight")
                .setGamemodeManager(
                        new GamemodeManager(
                                new GamemodeOptionBlock(
                                        "Lives",
                                        new GamemodeOption(1, "1", ChatColor.RED),
                                        new GamemodeOption(2, "2", ChatColor.GOLD),
                                        new GamemodeOption(3, "3", ChatColor.GREEN),
                                        new GamemodeOption(1, "Unlimited", ChatColor.DARK_PURPLE)
                                ),
                                new GamemodeOptionBlock(
                                        "Weapon",
                                        new GamemodeOption(0, "Snowballs", Sound.ENTITY_SNOWBALL_THROW),
                                        new GamemodeOption(1, "Launchers", Sound.ENTITY_ARROW_SHOOT),
                                        new GamemodeOption(0, "Guns", Sound.ITEM_CROSSBOW_SHOOT)
                                )
                        )
                )
                .addGameState(
                        new GameState("base", true)
                                .onEnable(
                                        () -> {
                                            for (Player player : Bukkit.getOnlinePlayers()) {
                                                new SnowballFightPlayer(player);
                                            }
                                        }
                                )
                                .addGameFunction(new InventoryManager(new SnowballFightInventory()))
                                .addGameFunction(new LethalSnowballs(5))
                                .addGameFunction(new GamePrefixText(
                                        mcsuPlayer -> SnowballFightPlayer.fromBukkit(mcsuPlayer.toBukkit()).getLivesTabString()
                                ))
                                .addGameFunction(new CustomEventListener(event -> { //Turn bow / crossbow projectiles into snowballs

                                    EntityShootBowEvent shootBow = (EntityShootBowEvent) event;
                                    Player player = (Player) shootBow.getEntity();

                                    Projectile projectile = (Projectile) shootBow.getProjectile();
                                    Snowball ball = shootBow.getEntity().launchProjectile(Snowball.class);

                                    ball.setVelocity(projectile.getVelocity());
                                    ball.setShooter(shootBow.getEntity());

                                    player.playSound(shootBow.getEntity().getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1, 1);

                                    //look for arrows in player inventory and remove 1 from stack
                                    for (int i = 0; i < player.getInventory().getSize(); i++) {
                                        ItemStack item = player.getInventory().getItem(i);
                                        if (item != null && item.getType() == Material.ARROW) {
                                            if (item.getAmount() > 1) {
                                                item.setAmount(item.getAmount() - 1);
                                            } else {
                                                player.getInventory().setItem(i, null);
                                            }
                                            break;
                                        }
                                    }

                                    projectile.remove();

                                }, "EntityShootBowEvent"))
                                .addGameFunction(new CustomEventListener(event -> { //Give a player a snowball 3 seconds after they shoot

                                    //check weapon GameMode isn't guns
                                    if (!Objects.equals(game.getGamemodeOptionBlockChoice("Weapon").getGamemodeOption().getName(), "Guns")) {
                                        ProjectileLaunchEvent launchEvent = (ProjectileLaunchEvent) event;

                                        if (launchEvent.getEntity() instanceof Snowball && !launchEvent.getEntity().hasMetadata("processed")) {
                                            //Gets fired twice so metadata is checked to make sure it's not already been processed
                                            launchEvent.getEntity().setMetadata("processed", new FixedMetadataValue(MCSU_Main.Mcsu_Plugin, true));

                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    ItemStack itemStack;
                                                    if (Objects.equals(game.getGamemodeOptionBlockChoice("Weapon").getGamemodeOption().getName(),
                                                            "Snowballs")) {
                                                        itemStack = new ItemStack(Material.SNOWBALL);
                                                    } else {
                                                        itemStack = SnowballFightInventory.arrow;
                                                    }

                                                    ((Player) launchEvent.getEntity().getShooter()).getInventory().addItem(itemStack);
                                                }
                                            }.runTaskLater(MCSU_Main.Mcsu_Plugin, 20 * 2);
                                        }
                                    }
                                }, "ProjectileLaunchEvent"))
                                .addGameFunction(new CustomEventListener(event -> {
                                    // Remove a player's life in SnowballFightPlayer if they die and give the killer a kill

                                    PlayerDeathEvent deathEvent = (PlayerDeathEvent) event;

                                    if (deathEvent.getEntity().getKiller() instanceof Player) {
                                        SnowballFightPlayer killer = SnowballFightPlayer.fromBukkit(deathEvent.getEntity().getKiller());
                                        killer.awardKill();
                                    }

                                    SnowballFightPlayer player = SnowballFightPlayer.fromBukkit(deathEvent.getEntity());
                                    player.removeLife();

                                    //If the player has no lives left, remove them from the game
                                    if (player.hasLives()) {
                                        game.eliminatePlayer(player.toBukkit());
                                    } else {//If the player DOES have lives left, respawn them after 5 seconds

                                    }


                                }, "PlayerDeathEvent"))
                )
                .addGameState(
                    new GameState("Snowballs", false)


                );

    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            game.endGame(game.getAliveTeams().get(0));

        }

    }

}
