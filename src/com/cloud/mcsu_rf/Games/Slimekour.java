package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.GamePlayers.BlockSumoPlayer;
import com.cloud.mcsu_rf.GamePlayers.SlimekourPlayer;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;
import com.cloud.mcsu_rf.Inventories.BlockSumoInventory;
import com.cloud.mcsu_rf.LootTables.BlockSumoLoot;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Objects.Game.Game;
import com.cloud.mcsu_rf.Objects.Game.GameState;
import com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones.HeightActionZone;
import com.cloud.mcsu_rf.Objects.GameFunctions.ActionZones.HeightKillZone;
import com.cloud.mcsu_rf.Objects.GameFunctions.BuildLimits.BuildMaxDistance;
import com.cloud.mcsu_rf.Objects.GameFunctions.BuildLimits.BuildMaxHeight;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.GameFunction;
import com.cloud.mcsu_rf.Objects.GameFunctions.InventoryManager;
import com.cloud.mcsu_rf.Objects.Map.SpawnManager;
import com.cloud.mcsu_rf.Objects.McsuPlayer;
import com.cloud.mcsu_rf.TeamSwitchStatements;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Slimekour {

    Game game;

    public static float timeLeft;
    public static boolean countdownActive;
    BukkitRunnable stopwatchDisplayTimer;

    Integer[] GlassWallFrom;
    Integer[] GlassWallTo;

    //InventoryManager inventoryManager = new InventoryManager(new BlockSumoInventory());

    public void init() {

        game = new Game("Slimekour")
                .setPlayerGamemode(GameMode.ADVENTURE)
                .setFreezeOnGameCountdown(false)
                .addGameState(
                        new GameState("base", true)
                                .addGameFunction(new CustomEventListener(
                                        event -> game.getGamestate("afterCountdown").setEnabled(true),
                                        "GameCountdownEndEvent"
                                ))
                                .onEnable(() -> {
                                    timeLeft = 120;
                                    countdownActive = false;

                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        new SlimekourPlayer(player);
                                    }
                                })
                                //.addGameFunction(inventoryManager)
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
                                                                " has finished slimekour in " + ChatColor.WHITE + ChatColor.BOLD +
                                                                SlimekourPlayer.fromBukkit(player).getFormattedTime()
                                                );

                                                game.eliminatePlayer(player);
                                                SlimekourPlayer.fromBukkit(player).endTimer();
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

                                    SlimekourPlayer.startAllTimers();
                                })
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            stopwatchDisplayTimer.cancel();

            SlimekourPlayer.slimekourPlayers = new ArrayList<>();

            game.getAliveTeams().get(0).awardTeamPoints(100);
            game.endGame(game.getAliveTeams().get(0));

        }

    }

}
