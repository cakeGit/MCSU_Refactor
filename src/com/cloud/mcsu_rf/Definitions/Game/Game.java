package com.cloud.mcsu_rf.Definitions.Game;

import com.cloud.mcsu_rf.Definitions.CustomEvents.GameCountdownEndEvent;
import com.cloud.mcsu_rf.Definitions.CustomEvents.GameInitEvent;
import com.cloud.mcsu_rf.Definitions.CustomEvents.GameSpawnsActivatedEvent;
import com.cloud.mcsu_rf.Definitions.Lambdas.StringReturnMcsuPlayerLambda;
import com.cloud.mcsu_rf.Definitions.Map.MapLoader;
import com.cloud.mcsu_rf.Definitions.Map.MapMetadata;
import com.cloud.mcsu_rf.Definitions.Map.SpawnManager;
import com.cloud.mcsu_rf.Definitions.McsuPlayer;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.McsuScoreboard;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements.MapMetadataDisplay;
import com.cloud.mcsu_rf.Definitions.McsuScoreboard.ScoreboardElements.TimedEventDisplay;
import com.cloud.mcsu_rf.Definitions.McsuTeam;
import com.cloud.mcsu_rf.Definitions.Timer;
import com.cloud.mcsu_rf.EventListenerMain;
import com.cloud.mcsu_rf.Game_Handlers.Game_Main;
import com.cloud.mcsu_rf.Game_Handlers.ShorthandClasses.Pick;
import com.cloud.mcsu_rf.MCSU_Main;
import com.cloud.mcsu_rf.TeamHandlers.TeamMain;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Game {

    public static String generateEndSplash() {

        return Pick.Random(
                "wow what a sweat lol",
                "*clap* *clap* *clap* *clap*",
                "mcsu????",
                "pog????/?/???",
                ":D",
                "gg",
                "very enterprising",
                "very christian values",
                "Mr Ebenezer approves",
                "Mr Tice approves",
                "i think that deserves a housepoint",
                "my wife has filed for a divorce",
                "very mcsu",
                "sus like impostor"
                );

    }

    public static String generateNoWinnerEndSplash() {
        return  "";
        //return Pick.Random("");
    }

    public static String getGameSetPrefix(McsuPlayer mcsuPlayer) {
        try {
            return getGamePrefix.exec(mcsuPlayer)+" ";
        } catch (NullPointerException ignored) {}
        return "";
    }

    public static void setGameSetPrefixFunc(StringReturnMcsuPlayerLambda func) {
        getGamePrefix = func;
    }

    //defaults
    static final int DefaultStartLength = 15;
    public static final Sound DefaultGamemodePickSound = Sound.BLOCK_NOTE_BLOCK_PLING;
    public static final Sound DefaultStartTimerTickSound = Sound.BLOCK_NOTE_BLOCK_SNARE;
    public static final Sound DefaultStartTimerEndSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;
    public static final Sound DefaultTeamDeathSound = Sound.ENTITY_WOLF_HOWL;
    public static final Sound DefaultRespawnSound = Sound.ENTITY_FIREWORK_ROCKET_LAUNCH;

    public static boolean gameActive = false;
    public static Game currentGame = null;
    public static MapMetadata mapMetadata;

    //static
    static ArrayList<McsuPlayer> alivePlayers = new ArrayList<>();
    static StringReturnMcsuPlayerLambda getGamePrefix;
    public static HashMap<String, Integer> QueuedTimedEvents = new HashMap<>();

    //Other vars
    String Id;
    String Name;
    World world;
    GamemodeManager gamemodeManager;
    ArrayList<GamemodeOptionChoice> gamemodeChoices;
    MapLoader mapLoader;
    boolean startIntervalEnabled = true;
    boolean freezeOnGameCountdown = true;
    GameMode playerGameMode = GameMode.SURVIVAL;

    ArrayList<GameState> gameStates = new ArrayList<>();
    ArrayList<McsuTeam> aliveTeams = new ArrayList<>();

    public Game(String id, String Name) {

        this.Id = id;
        this.Name = Name;

        Bukkit.getLogger().info("Registering game " + Name);

        Game_Main.RegisteredGames.add(this);

    }

    public void initGameLoader(World world, MapMetadata map) { //Can be passed a map to load

        currentGame = this;

        this.world = world;

        if (gamemodeManager != null) {
            gamemodeChoices = (ArrayList<GamemodeOptionChoice>) gamemodeManager.pickOptions().clone();

            for (GamemodeOptionChoice gamemodeOptionChoice : gamemodeChoices) {
                try {
                    gameStates.stream().filter(gameState -> gameState.getName().equals(gamemodeOptionChoice.getGamemodeOption().getName()))
                            .findFirst().orElse(null).setEnabled(true);
                }catch(NullPointerException ignored){}
            }

            //send title to all players of the gamemodes and their choices


            final int[] i = {gamemodeChoices.size()}; // done because weird java quirks
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (i[0] > 0) {
                        GamemodeOptionChoice gamemodeOptionChoice = gamemodeChoices.get(i[0] - 1);

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle( ChatColor.RED + gamemodeOptionChoice.getOptionBlockName() +": "+ ChatColor.RESET +""+ ChatColor.MAGIC +""+ ChatColor.BOLD + "MCSU=EPIC",
                                    "", 10, 20, 10);

                            // plays a lower pitched pling sound
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.8f);

                        }

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    player.sendTitle(ChatColor.RED + gamemodeOptionChoice.getOptionBlockName() + ": " + ChatColor.RESET + "" + ChatColor.BOLD + gamemodeOptionChoice.getGamemodeOption().getStyledName(),
                                            "", 0, 60, 10);

                                    Sound selectSound = gamemodeOptionChoice.getGamemodeOption().getSelectSound();
                                    if (selectSound != null) {
                                        player.playSound(player.getLocation(), selectSound, 1, 1);
                                    } else {
                                        player.playSound(player.getLocation(), DefaultGamemodePickSound, 1, 1);
                                    }
                                }
                            }
                        }.runTaskLater(MCSU_Main.Mcsu_Plugin, 20);

                        i[0]--;
                    }

                }
            }.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0, 70);




        } else {
            Bukkit.getLogger().info("GamemodeManager is unset");
        }

        for (Player player : Bukkit.getOnlinePlayers()) { player.sendTitle(ChatColor.BOLD+this.getName(), ""); }

        if (mapLoader == null) {
            mapLoader = new MapLoader().setSpawnManager(new SpawnManager()); // defaults to generic map + spawn loaders
        }

        EventListenerMain.setActivityRule("PlayerMovement", false);

        mapLoader.MapInit(this, world, map);
        mapLoader.getSpawnManager().lobbySpawns(mapLoader);

        alivePlayers = (ArrayList<McsuPlayer>) McsuPlayer.McsuPlayers.clone();
        aliveTeams = (ArrayList<McsuTeam>) TeamMain.Teams.clone();
        checkAliveTeams(false);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
        }

       for (GameState gameState : gameStates) { // trust me this has a purpose - gamestates are written down as enabled (for defaults) but are not initalized
            if ( gameState.getEnabled() ) {
                gameState.setEnabled(true);
           }
       }

        GameInitEvent initEvent = new GameInitEvent(this);
        Bukkit.getPluginManager().callEvent(initEvent);

        gameActive = true;
        List<Entity> entList = world.getEntities();//get all entities in the world
        for(Entity current : entList) {//loop through the list
            if (current instanceof Item) {//make sure we aren't deleting mobs/players
                current.remove();//remove it
            }
            if( current instanceof Arrow) {
                current.remove();
            }
            if( current instanceof Boat) {
                current.remove();
            }
        }
        MapMetadataDisplay.update();

        if (startIntervalEnabled) {
            Timer startTimer = new Timer(-1, DefaultStartLength)
                    .setOnTickIncrease(timer -> {

                        if (timer.getTimeLeft() == 5) {

                            mapLoader.getSpawnManager().gameSpawns(mapLoader);
                            if (freezeOnGameCountdown) EventListenerMain.setActivityRule("PlayerMovement", false);

                            GameSpawnsActivatedEvent spawnsActivatedEvent = new GameSpawnsActivatedEvent(this);
                            Bukkit.getPluginManager().callEvent(spawnsActivatedEvent);

                        }

                        for (Player player : Bukkit.getOnlinePlayers()) {

                            String message = ChatColor.WHITE + "" + ChatColor.BOLD + Name +
                                    ChatColor.RESET + "" + ChatColor.RED + " starting in " + timer.getTimeLeft();

                            if (timer.getTimeLeft() > 0) {

                                if (timer.getTimeLeft() <= 5) {

                                    player.sendTitle(message, "Get ready!");
                                    player.playSound(player.getLocation(), DefaultStartTimerTickSound, 1, 1);

                                } else {
                                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
                                }

                            }

                        }

                    })
                    .setOnTimerEnd(timer -> {

                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.playSound(player.getLocation(), DefaultStartTimerEndSound, 1, 1);
                        }

                        gameCountdownEnd();

                    });

        }


    }

    public void gameCountdownEnd() {

        if (freezeOnGameCountdown) EventListenerMain.setActivityRule("PlayerMovement", true);

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(playerGameMode);
            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "GO!", "");
        }

        GameCountdownEndEvent event = new GameCountdownEndEvent(this);
        Bukkit.getPluginManager().callEvent(event);

    }

    public void eliminatePlayer(Player bukkitPlayer) {
        bukkitPlayer.setGameMode(GameMode.SPECTATOR);
        this.removeFromAlivePlayers(
                McsuPlayer.fromBukkit(bukkitPlayer)
        );

    }

    public void spawnFireworks(Location location, int amount, Color color){
        Location loc = location;
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(color).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public void checkAliveTeams( boolean announceElimination ) {

        ArrayList<McsuTeam> newDeadTeams = new ArrayList<>();

        for ( McsuTeam team : aliveTeams ) {

            boolean teamAlive = false;

            for ( McsuPlayer player : team.getMembers() ) {

                if ( alivePlayers.contains( player ) ) {
                    teamAlive = true;
                    break;
                }

            }

            if ( !teamAlive ) {

                newDeadTeams.add(team);

                if (announceElimination) {
                    new BukkitRunnable()
                    {
                        public void run()
                        {

                            Bukkit.broadcastMessage( team.getStyledName() + " has been eliminated!" );
                            for(Player players : Bukkit.getOnlinePlayers()) {
                                players.playSound(players.getLocation(),DefaultTeamDeathSound,100,1);
                            }
                        }
                    }.runTaskLater(MCSU_Main.Mcsu_Plugin, 1L);
                } else {
                    Bukkit.getLogger().info( team.getStyledName() + " has been eliminated silently as announceElimination is false" );
                }

            }

        }

        for ( McsuTeam team : newDeadTeams ) { // Somewhat weird solution to a ConcurrentModificationException
            this.removeFromAliveTeams(team);
        }

    }

    public void endGame(McsuTeam winner){endGame(winner, 100);}
    public void endGame(McsuTeam winner, int points) {
        String style = ChatColor.BOLD + "" + ChatColor.WHITE;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(winner.getStyledName() + style + " win " + ChatColor.RESET + getName() + style + "!", generateEndSplash());
        }
        winner.awardPoints(points, "winning "+getName());
        gameReset();
        world.getWorldBorder().setSize(100000,1);
        world.setTime(1000);
    }

    public void endGame() {
        String style = ChatColor.BOLD + "" + ChatColor.WHITE;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(getName() + style + " has finished!", generateNoWinnerEndSplash());
        }
        gameReset();
    }
    void gameReset() {

        currentGame = null;

        for (GameState gameState : gameStates) {
            gameState.reset();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.setGlowing(false);
            player.setHealth(20);
            player.setSaturation(20f);
            player.setFireTicks(0);
        }

        getGamePrefix = null;
        McsuScoreboard.defaultScoreboard.update();
        world.getWorldBorder().setSize(60000000);
        world.getWorldBorder().setCenter(0, 0);

        EventListenerMain.resetActivityRules();
    }

    public void mapLoadingFinished() {
        EventListenerMain.setActivityRule("PlayerMovement", true);
    }

    public Game addGameState(GameState gameState) { gameStates.add(gameState); return this; }
    public Game setMapLoader(MapLoader mapLoader) { this.mapLoader = mapLoader; return this; }

    public Game setMapMetadata(MapMetadata mapMetadata) { this.mapMetadata = mapMetadata; return this; }
    public static MapMetadata getMapMetadata() { return mapMetadata; }


    public Game setGamemodeManager(GamemodeManager gamemodeManager) { this.gamemodeManager = gamemodeManager; return this; }
    public Game setPlayerGamemode(GameMode gameMode) { playerGameMode = gameMode; return this; }

    public Game setFreezeOnGameCountdown(boolean freezeOnGameCountdown) { this.freezeOnGameCountdown = freezeOnGameCountdown; return this; }

    public static ArrayList<McsuPlayer> getAlivePlayers() { return alivePlayers; }
    public void removeFromAlivePlayers(McsuPlayer player) { alivePlayers.remove(player); }

    public ArrayList<McsuTeam> getAliveTeams() { return aliveTeams; }
    public void removeFromAliveTeams(McsuTeam mcsuTeam) { aliveTeams.remove(mcsuTeam); }

    public World getWorld() { return world; }
    public String getId() { return Id; }
    public String getName() { return Name; }
    public MapLoader getMapLoader() {  return mapLoader; }

    public GameState getGamestate(String Name) {

        return gameStates.stream().filter(gameState -> Objects.equals(gameState.getName(), Name)).findFirst().orElse(null);

    }

    public GamemodeOptionChoice getGamemodeOptionBlockChoice(String Name) {

        return gamemodeChoices.stream().filter(gamemodeChoice -> Objects.equals(gamemodeChoice.getOptionBlockName(), Name)).findFirst().orElse(null);

    }

    public ArrayList<GameState> getEnabledGameStates() {

        ArrayList<GameState> enabled = new ArrayList<>();
        gameStates.stream().filter(gameState -> gameState.enabled).forEach(enabled::add);
        return enabled;

    }

    public Game addTimedEvent(String name, Integer delay, GameTimedEventLambda onActive) {

        QueuedTimedEvents.put(name, delay);

        BukkitRunnable timedEvent = new BukkitRunnable() {

            @Override
            public void run() {
                QueuedTimedEvents.put(name, QueuedTimedEvents.get(name)-1);
                TimedEventDisplay.update();
            }

        };
        timedEvent.runTaskTimer(MCSU_Main.Mcsu_Plugin, 0L, 20L);

        new BukkitRunnable() {

            @Override
            public void run() {
                onActive.exec();
                QueuedTimedEvents.remove(name);
                TimedEventDisplay.update();
                timedEvent.cancel();
            }

        }.runTaskLater(MCSU_Main.Mcsu_Plugin, delay * 20L);

        return this;
    }
}
