package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.GamePlayers.BlockSumoPlayer;
import com.cloud.mcsu_rf.Inventories.BlockSumoInventory;
import com.cloud.mcsu_rf.LootTables.BlockSumoLoot;
import com.cloud.mcsu_rf.Objects.Game.*;
import com.cloud.mcsu_rf.Objects.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Objects.GameFunctions.InventoryManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockSumo {

    Game game;

    BlockSumoInventory blockSumoInventory;

    public void init() {

        BlockSumoLoot.init();

        game = new Game("BlockSumo")
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        new BlockSumoPlayer(player);
                                    }
                                })
                                .addGameFunction(new InventoryManager(new BlockSumoInventory()))
                                .addGameFunction(new CustomEventListener("BlockPlaceEvent", Event -> {

                                }))
                )
                .addGameState(
                        new GameState("afterCountdown")
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
