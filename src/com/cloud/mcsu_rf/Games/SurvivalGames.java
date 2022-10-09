package com.cloud.mcsu_rf.Games;

import com.cloud.mcsu_rf.Cars.CarEvents;
import com.cloud.mcsu_rf.Definitions.Game.Game;
import com.cloud.mcsu_rf.Definitions.Game.GameState;
import com.cloud.mcsu_rf.Definitions.GameFunctions.CustomEventListener;
import com.cloud.mcsu_rf.Definitions.GameFunctions.PointAwarders.SurvivalAwarder;
import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Inventories.SurvivalGamesInventory;
import com.cloud.mcsu_rf.LootTables.SurvivalGamesLoot;
import com.cloud.mcsu_rf.MCSU_Main;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class SurvivalGames implements Listener {

    Game game;
    BukkitRunnable gracePeriodCountdown;
    int gracePeriodTime = 120;
    MCSU_Main plugin = MCSU_Main.getPlugin(MCSU_Main.class);
    public static World gameWorld;
    public static int waterTaskID;

    public static HashMap<Player, Boolean> sandbeezMusicPlaying = new HashMap<>();
    public static HashMap<Player, Boolean> darkForestPlaying = new HashMap<>();
    public static HashMap<Player, Boolean> spruceForestPlaying = new HashMap<>();
    public static HashMap<Player, Boolean> smallForestPlaying = new HashMap<>();
    public static HashMap<Player, Boolean> mountainPlaying1 = new HashMap<>();
    public static HashMap<Player, Boolean> mountainPlaying2 = new HashMap<>();
    public static HashMap<Player, Boolean> restaurantPlaying = new HashMap<>();
    public static HashMap<Player, Boolean> gasStationPlaying = new HashMap<>();

    UUID gameId;

    public void init() {

        SurvivalGamesLoot.init();

        game = new Game("survivalGames", "Survival Games")
                .setPlayerGamemode(GameMode.ADVENTURE)
                .addGameState(
                        new GameState("base", true)
                                .onEnable(() -> {
                                    gameWorld = game.getWorld();
                                    //Set this rounds "id" (used to ensure chests aren't infinitely regenerated)
                                    gameId = UUID.randomUUID();
                                    gameWorld.setTime(18000);
                                    gameWorld.getWorldBorder().setCenter(2000, 2000);
                                    gameWorld.getWorldBorder().setSize(450);
                                    waterDamage();
                                    // Sound effects
                                    for(Player p : Bukkit.getOnlinePlayers()) {
                                        sandbeezMusicPlaying.put(p,false);
                                        darkForestPlaying.put(p,false);
                                        restaurantPlaying.put(p,false);
                                        gasStationPlaying.put(p,false);
                                        spruceForestPlaying.put(p,false);
                                        smallForestPlaying.put(p,false);
                                        mountainPlaying1.put(p,false);
                                        mountainPlaying2.put(p,false);
                                    }
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
                                    EventListenerMain.setActivityRule("Cars",true);
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
                                    spawnCars();
                                })
                                .addGameFunction(new CustomEventListener(Event -> {
                                    PlayerInteractEvent playerInteractEvent = (PlayerInteractEvent) Event;
                                    Block b = playerInteractEvent.getClickedBlock();
                                    if(b.getType().equals(Material.CHEST)) {
                                        //Bukkit.broadcastMessage(String.valueOf(b.hasMetadata(gameId.toString())));
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
                                    gameWorld.getWorldBorder().setSize(10,450);
                                })
                );

    }

    public void spawnCars() {
        // Gas Station
        String[] cars = {
                "Ford",
                "Rover",
                "Small",
                "Taxi",
                "Boogatti",
                "Mercedes"
        };
        Location[] gasSpawnLocs = {
                new Location(gameWorld,1976.5, 21, 2126, -90, 0),
                new Location(gameWorld,1976.5, 21, 2119, -90, 0),
                new Location(gameWorld,1985.5, 21, 2119, -90, 0),
                new Location(gameWorld,1985.5, 21, 2126, -90, 0),
        };
        for(int i = 0; i < 4; i++) {
            CarEvents.spawnCar(gasSpawnLocs[i],cars[new Random().nextInt(6)],gameWorld);
        }

        // Spawn
        Location[] spawnCars = {
                new Location(gameWorld, 2005, 21, 1996, getRandomNumber(140,-50), 0),
                new Location(gameWorld, 2005, 21, 2005, getRandomNumber(-130,40), 0),
                new Location(gameWorld, 1996, 21, 2005, getRandomNumber(-40, 130), 0),
                new Location(gameWorld, 1196, 21, 1996, getRandomNumber(50, -140), 0)
        };
        int rand1 = getRandomNumber(0,1);
        int rand2 = getRandomNumber(2,3);
        CarEvents.spawnCar(spawnCars[rand1],cars[new Random().nextInt(6)],gameWorld);
        CarEvents.spawnCar(spawnCars[rand2],cars[new Random().nextInt(6)],gameWorld);

        spawnSanbeezCars();
        spawnAlienBaseCars();
    }

    public static void spawnAlienBaseCars() {
        String[] cars = {
                "Ford",
                "Rover",
                "Small",
                "Taxi",
                "Boogatti",
                "Mercedes"
        };
        /*
        Location[] alienBaseLocs = {
                new Location(gameWorld, -1350.5, 4, 1225.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1350.5, 4, 1231.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1350.5, 4, 1237.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1350.5, 4, 1243.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1350.5, 4, 1249.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1356.5, 4, 1225.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1356.5, 4, 1231.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1356.5, 4, 1237.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1356.5, 4, 1243.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1356.5, 4, 1249.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1366.5, 4, 1225.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1366.5, 4, 1231.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1366.5, 4, 1237.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1366.5, 4, 1243.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1366.5, 4, 1249.5, getRandomNumber(40,140), 0),
                new Location(gameWorld, -1372.5, 4, 1225.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1372.5, 4, 1231.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1372.5, 4, 1237.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1372.5, 4, 1243.5, getRandomNumber(-50,-135), 0),
                new Location(gameWorld, -1372.5, 4, 1249.5, getRandomNumber(-50,-135), 0),
        };
        int[] alienBaseCars = {
                0,
                0,
        };
        for(int i = 0; i < 2; i++) {
            int random = new Random().nextInt(alienBaseLocs.length);
            alienBaseCars[i] = random;
            if(i == 1) {
                if(!duplicates(alienBaseCars)) {
                    for(int j = 0; j < 2; j++) {
                        CarEvents.spawnCar(alienBaseLocs[alienBaseCars[j]],cars[new Random().nextInt(6)],gameWorld);
                    }
                } else {
                    spawnAlienBaseCars();
                }
            }
        }
         */
        Location[] alienbaseLocs = {
                new Location(gameWorld,1839.5, 21, 2189.5, -90, 0),
                new Location(gameWorld,1844.5, 21, 2177.5, 90, 0),
                new Location(gameWorld,1855.5, 21, 2183.5, -90, 0),
                new Location(gameWorld,1861.5, 21, 2195.5, 90, 0),
        };
        for(int i = 0; i < 2; i++) {
            CarEvents.spawnCar(alienbaseLocs[new Random().nextInt(4)],cars[new Random().nextInt(6)],gameWorld);
        }
    }

    public static void spawnSanbeezCars() {
        String[] cars = {
                "Ford",
                "Rover",
                "Small",
                "Taxi",
                "Boogatti",
                "Mercedes"
        };
        /*
        Location[] sandbeezLocs = {
                new Location(gameWorld, -1194.5, 4, 979.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1194.5, 4, 973.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1194.5, 4, 967.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1194.5, 4, 961.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1194.5, 4, 955.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1200.5, 4, 979.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1200.5, 4, 973.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1200.5, 4, 967.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1200.5, 4, 961.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1200.5, 4, 955.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1210.5, 4, 979.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1210.5, 4, 973.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1210.5, 4, 967.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1210.5, 4, 961.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1210.5, 4, 955.5,getRandomNumber(60,140),0),
                new Location(gameWorld, -1216.5, 4, 979.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1216.5, 4, 973.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1216.5, 4, 967.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1216.5, 4, 961.5,getRandomNumber(-40,-120),0),
                new Location(gameWorld, -1216.5, 4, 955.5,getRandomNumber(-40,-120),0),
        };
        int[] sandbeezCars = {
                0,
                0,
                0,
                0
        };
        for(int i = 0; i < 4; i++) {
            int random = new Random().nextInt(sandbeezLocs.length);
            sandbeezCars[i] = random;
            if(i == 3) {
                if(!duplicates(sandbeezCars)) {
                    for(int j = 0; j < 4; j++) {
                        CarEvents.spawnCar(sandbeezLocs[sandbeezCars[j]],cars[new Random().nextInt(6)],gameWorld);
                    }
                } else {
                    spawnSanbeezCars();
                }
            }
        } */
        Location[] sandbeezLocs = {
                new Location(gameWorld,2017.5, 21, 1907.5, 90, 0),
                new Location(gameWorld,2011.5, 21, 1919.5, -90, 0),
                new Location(gameWorld,2001.5, 21, 1919.5, 90, 0),
                new Location(gameWorld,1995.5, 21, 1907.5, -90, 0),
        };
        for(int i = 0; i < sandbeezLocs.length; i++) {
            CarEvents.spawnCar(sandbeezLocs[i],cars[new Random().nextInt(6)],gameWorld);
        }
    }

    static boolean duplicates(final int[] zipcodelist)
    {
        Set<Integer> lump = new HashSet<Integer>();
        for (int i : zipcodelist)
        {
            if (lump.contains(i)) return true;
            lump.add(i);
        }
        return false;
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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

    public static void waterDamage() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        waterTaskID = scheduler.scheduleSyncRepeatingTask(MCSU_Main.Mcsu_Plugin, new Runnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()) {
                    Location blockBelow = p.getLocation().subtract(0,1,0);
                    if(p.isInWater() || p.getLocation().getBlock().getType().equals(Material.WATER) || blockBelow.getBlock().getType().equals(Material.WATER)) {
                        p.damage(4F);
                    }
                }
            }
        }, 20L, 20L);
    }

    //@EventHandler
    public static void enterSoundRegion(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        int[] sanbeez1 = {
                2025, 21, 1890
        };
        int[] sandbeez2 = {
                1986, 28, 1823
        };
        int[] darkforest1 = {
                1959, 20, 2029
        };
        int[] darkforest2 = {
                1849, 29, 2155
        };
        int[] spruceforest1 = {
                2077, 21, 2119
        };
        int[] spruceforest2 = {
                2125, 40, 2022
        };
        int[] smallforest1 = {
                1958, 20, 1954
        };
        int[] smallforest2 = {
                1919, 40, 2029
        };
        int[] mountain1 = {
                1922, 20, 2062
        };
        int[] mountain2 = {
                2004, 160, 2108
        };
        int[] restaurant1 = {
                1981, 20, 1932
        };
        int[] restaurant2 = {
                1967, 25, 1919
        };
        int[] gasStation1 = {
                1968, 21, 2129
        };
        int[] gasStation2 = {
                1955, 24, 2115
        };
        if(!sandbeezMusicPlaying.get(player) && isPlayerInRegion(player,sanbeez1,sandbeez2)) {
            player.playSound(new Location(gameWorld,2003, 25, 1855),Sound.MUSIC_DISC_11,SoundCategory.AMBIENT,2.2F,1);
            sandbeezMusicPlaying.put(player,true);
        }
        if(sandbeezMusicPlaying.get(player) && !isPlayerInRegion(player,sanbeez1,sandbeez2)) {
            player.stopSound(Sound.MUSIC_DISC_11,SoundCategory.AMBIENT);
            sandbeezMusicPlaying.put(player,false);
        }
        if(!darkForestPlaying.get(player) && isPlayerInRegion(player,darkforest1,darkforest2)) {
            player.playSound(new Location(gameWorld,1890, 23.5, 2094),Sound.MUSIC_DISC_13,SoundCategory.AMBIENT,4,1);
            darkForestPlaying.put(player,true);
        }
        if(darkForestPlaying.get(player) && !isPlayerInRegion(player,darkforest1,darkforest2)) {
            player.stopSound(Sound.MUSIC_DISC_13,SoundCategory.AMBIENT);
            darkForestPlaying.put(player,false);
        }
        if(!spruceForestPlaying.get(player) && isPlayerInRegion(player,spruceforest1,spruceforest2)) {
            player.playSound(new Location(gameWorld,2101, 26, 2082),Sound.MUSIC_DISC_MALL,SoundCategory.AMBIENT,3.5F,1);
            spruceForestPlaying.put(player,true);
        }
        if(spruceForestPlaying.get(player) && !isPlayerInRegion(player,spruceforest1,spruceforest2)) {
            player.stopSound(Sound.MUSIC_DISC_MALL,SoundCategory.AMBIENT);
            spruceForestPlaying.put(player,false);
        }
        if(!smallForestPlaying.get(player) && isPlayerInRegion(player,smallforest1,smallforest2)) {
            player.playSound(new Location(gameWorld,1943, 24, 1998),Sound.MUSIC_DISC_MALL,SoundCategory.AMBIENT,3,1);
            smallForestPlaying.put(player,true);
        }
        if(smallForestPlaying.get(player) && !isPlayerInRegion(player,smallforest1,smallforest2)) {
            player.stopSound(Sound.MUSIC_DISC_MALL,SoundCategory.AMBIENT);
            smallForestPlaying.put(player,false);
        }
        if(!mountainPlaying1.get(player) && isPlayerInRegion(player,mountain1,mountain2)) {
            player.playSound(new Location(gameWorld,1972, 140, 2092),Sound.MUSIC_DISC_WAIT,SoundCategory.AMBIENT,7.5F,1);
            mountainPlaying1.put(player,true);
        }
        if(mountainPlaying1.get(player) && !isPlayerInRegion(player,mountain1,mountain2)) {
            player.stopSound(Sound.MUSIC_DISC_WAIT,SoundCategory.AMBIENT);
            mountainPlaying1.put(player,false);
        }
        if(!restaurantPlaying.get(player) && isPlayerInRegion(player,restaurant1,restaurant2)) {
            player.playSound(new Location(gameWorld, 1973, 25, 1926),Sound.MUSIC_DISC_BLOCKS,SoundCategory.AMBIENT,0.6F,1);
            restaurantPlaying.put(player,true);
        }
        if(restaurantPlaying.get(player) && !isPlayerInRegion(player,restaurant1,restaurant2)) {
            player.stopSound(Sound.MUSIC_DISC_BLOCKS,SoundCategory.AMBIENT);
            restaurantPlaying.put(player,false);
        }
        if(!gasStationPlaying.get(player) && isPlayerInRegion(player,gasStation1,gasStation2)) {
            player.playSound(new Location(gameWorld,1962, 24, 2122),Sound.MUSIC_DISC_STRAD,SoundCategory.AMBIENT,0.6F,1);
            gasStationPlaying.put(player,true);
        }
        if(gasStationPlaying.get(player) && !isPlayerInRegion(player,gasStation1,gasStation2)) {
            player.stopSound(Sound.MUSIC_DISC_BLOCKS,SoundCategory.AMBIENT);
            gasStationPlaying.put(player,false);
        }
    }

    public static boolean isPlayerInRegion(Player player, int[] coords1, int[] coords2) {

        Location pLoc = player.getLocation();
        int[] pCoords = { pLoc.getBlockX(), pLoc.getBlockY(), pLoc.getBlockZ() };

        for(int index = 0; index < pCoords.length; index++) {
            if(!isNumBetween(pCoords[index], coords1[index], coords2[index])) return false;
        }

        return true;
    }

    public static boolean isNumBetween(int targetNum, int min, int max) {
        if(min > max) {
            int i = min;
            min = max;
            max = i;
        }
        return ( (targetNum >= min) && (targetNum <= max) );
    }

    /*
    @EventHandler
    public static void onFallInVoidTP(PlayerMoveEvent e) {
        if(e.getPlayer().getLocation().getY() < -20) {
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*3, 255));
        }
        if(e.getPlayer().getLocation().getY() < -50) {
            e.getPlayer().teleport(new Location(gameWorld,-1368.5, 76, 1058.5));
        }
    }
     */

    public static void stopWaterDamage() {
        Bukkit.getScheduler().cancelTask(waterTaskID);
    }

    public void checkIfEnded() {

        Bukkit.getLogger().info("Checking if game " + game.getName() + " has ended");

        if (game.getAliveTeams().size() == 1) {

            Bukkit.getLogger().info("Game " + game.getName() + " has ended!");

            stopWaterDamage();

            game.endGame(game.getAliveTeams().get(0));

        } else  {

            Bukkit.getLogger().info(game.getName() + " has not ended because " + game.getAliveTeams().size()
                    + " teams are still alive");

        }

    }

}
