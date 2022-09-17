package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SurvivalGamesInventory;
import com.cloud.mcsu_rf.LootTables.SurvivalGamesLoot;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Game.GameState;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.GameFunctions.PointAwarders.SurvivalAwarder;
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

import java.util.UUID;

public class SurvivalGames {

    Game game;
    BukkitRunnable gracePeriodCountdown;
    int gracePeriodTime = 30;
    MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);

    UUID gameId;

    public void init() {

        SurvivalGamesLoot.init();

        game = new Game("survivalGames", "Survival Games")
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {
                                    //Set this rounds "id" (used to ensure chests aren't infinitely regenerated)
                                    gameId = UUID.randomUUID();
                                })
                                .addGameFunction(new SurvivalAwarder(5))
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
                                    EventListenerMain.setActivityRule("EntityDamage",true);
                                    EventListenerMain.setActivityRule("PearlDamage",true);
                                    EventListenerMain.setActivityRule("Hunger",true);
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
                                        Bukkit.broadcastMessage(String.valueOf(b.hasMetadata(gameId.toString())));
                                        if(!b.hasMetadata(gameId.toString())) {
                                            SurvivalGamesInventory inv = new SurvivalGamesInventory();
                                            ItemStack[] contents = inv.getInventory().getContents();
                                            Chest c = (Chest) b.getState();
                                            c.getBlockInventory().setContents(contents);
                                        }
                                        b.setMetadata(gameId.toString(), new FixedMetadataValue(plugin,true));
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

    public void getChests() {
        /*
        // Mid Chests
        Location midlower1 = new Location(world,1162,4,249);
        Location midlower2 = new Location(world,1162,4,247);
        Location midlower3 = new Location(world,1163,4,246);
        Location midlower4 = new Location(world,1165,4,246);
        Location midlower5 = new Location(world,1166,4,247);
        Location midlower6 = new Location(world,1166,4,249);
        Location midlower7 = new Location(world,1165,4,250);
        Location midlower8 = new Location(world,1163,4,250);
        Location midmid1 = new Location(world,1163,5,248);
        Location midmid2 = new Location(world,1164,5,247);
        Location midmid3 = new Location(world,1165,5,248);
        Location midmid4 = new Location(world,1164,5,249);
        Location midtop1 = new Location(world,1164,6,248);

        //Forest Chests
        Location forest1 = new Location(world,1106,4,289);
        Location forest2 = new Location(world,1100,7,274);
        Location forest3 = new Location(world,1119,4,237);
        Location forest4 = new Location(world,1104,4,221);

        // Lake and Beach Chests
        Location lake = new Location(world,1212,2,292);
        Location beach1 = new Location(world,1209,4,324);
        Location beach2 = new Location(world,1185,3,314);
        Location nearlake = new Location(world,1186,5,275);

        // Playground Chest
        Location playground = new Location(world,1237,4,232);

        // Farm Chest
        Location farm = new Location(world,1231,4,180);

        // Mountain Chests
        Location icemountain1 = new Location(world,1241,5,265);
        Location icemountain2 = new Location(world,1132,4,177);
        Location mesamountain1 = new Location(world,1099,5,316);
        Location mesamountain2 = new Location(world,1157,4,326);

        // Tent Chests
        Location tent1 = new Location(world,1197,4,203);
        Location tent2 = new Location(world,1178,4,210);
        Location campfire = new Location(world,1167,4,214);

        // PogGamers Shack Chest
        Location shack = new Location(world,1150,7,204);

        // Clouds Manor Chests
        Location manor1 = new Location(world,1156,4,297);
        Location manor2 = new Location(world,1144,4,290);
        Location manor3 = new Location(world,1135,11,287);
        Location manor4 = new Location(world,1154,13,295);

        // House Chest
        Location house = new Location(world,1237,4,242);

         */
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
