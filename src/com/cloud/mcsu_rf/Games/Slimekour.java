package com.cloud.mcsu_rf.Games;

import com.cak.what.ItemAPI.WhItemStack;
import com.cak.what.Util.ChCol;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Game.GameState;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.GamePlayers.SlimekourPlayer;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.ParseArr;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Slimekour {

    Game game;

    public static float timeLeft;
    public static boolean countdownActive;

    int currentPos = 1;

    Integer[] GlassWallFrom;
    Integer[] GlassWallTo;

    //InventoryManager inventoryManager = new InventoryManager(new BlockSumoInventory());

    public void init() {

        game = new Game("slimekour", "Slimekour")
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

                                            if (Game.getAlivePlayers().contains(McsuPlayer.fromBukkit(player))) {
                                                if (blockLoc.getBlock().getType() == Material.LIGHT_BLUE_STAINED_GLASS) {
                                                    player.addPotionEffect((new PotionEffect(PotionEffectType.SPEED, 5, 2)));
                                                } else if (blockLoc.getBlock().getType() == Material.GOLD_BLOCK ||
                                                        player.getLocation().getBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE) {
                                                    Bukkit.broadcastMessage(
                                                            McsuPlayer.fromBukkit(player).getColouredName(true) +
                                                                    McsuPlayer.fromBukkit(player).getColour() +
                                                                    " has finished Slimekour in " + ChatColor.WHITE + ChatColor.BOLD +
                                                                    SlimekourPlayer.fromBukkit(player).getFormattedTime() + " (#"+currentPos+")"
                                                    );

                                                    McsuPlayer.fromBukkit(player).awardPoints((int) (
                                                                    ( ((float) (Bukkit.getOnlinePlayers().size() - (currentPos-1)) )
                                                                            / Bukkit.getOnlinePlayers().size() ) * 100
                                                            )
                                                    );

                                                    currentPos+=1;


                                                    game.eliminatePlayer(player);
                                                    SlimekourPlayer.fromBukkit(player).endTimer();
                                                    player.spigot().sendMessage(
                                                            ChatMessageType.ACTION_BAR,
                                                            TextComponent.fromLegacyText(
                                                                    ChatColor.GOLD +""+ ChatColor.BOLD + "Finished!"
                                                            )
                                                    );
                                                    new EventListenerMain().onPlayerDeath(new PlayerDeathEvent(player, new ArrayList<ItemStack>().stream().toList(), 0, ""));

                                                    checkIfEnded();

                                                }
                                            }

                                        },
                                        "PlayerMoveEvent"
                                ))
                                .onEnable(()-> {

                                    EventListenerMain.setActivityRule("PVP", true);

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

                                    EventListenerMain.setActivityRule("PVP", true);

                                    WhItemStack item = new WhItemStack(Material.STICK, ChCol.BOLD + ChCol.DARK_PURPLE + "Annoying stick");
                                    ItemMeta meta = item.getItemMeta();
                                    meta.setCustomModelData(1);
                                    item.setItemMeta(meta);
                                    item.setLore("§7Use this stick to annoy enemies");
                                    item.addUnsafeEnchantment(Enchantment.KNOCKBACK,1);

                                    for(Player players : Bukkit.getOnlinePlayers()) {
                                        players.getInventory().addItem(item);
                                        players.playSound(players.getLocation(),Sound.MUSIC_DISC_FAR,SoundCategory.AMBIENT,1,1);
                                    }

                                    SlimekourPlayer.startAllTimers();
                                })
                );


    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (Game.getAlivePlayers().size() == 0) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            SlimekourPlayer.slimekourPlayers = new ArrayList<>();

            game.endGame();

            for(Player players : Bukkit.getOnlinePlayers()) {
                players.stopSound(Sound.MUSIC_DISC_FAR,SoundCategory.AMBIENT);
            }

        }

    }

}
